package com.qgd.yfb.assist.biz;

import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.qgd.yfb.assist.bean.DownloadInfo;
import com.qgd.yfb.assist.bean.DownloadState;
import com.qgd.yfb.assist.util.ObjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangke on 2017/5/23.
 */

public class DownloadHelper {
    public static final String TAG = "Download";
    private static final String DOWNLOAD_DIR = "qgd/download/";
    private DownloadManager downloadManager;

    public DownloadHelper(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    public static File getDownloadFile(String fileName) {
        return new File(Environment.getExternalStorageDirectory(), DOWNLOAD_DIR + fileName);
    }

    public static boolean checkFileExist(File file) {
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 筛选出正在下载的任务
     * @param downloadList
     * @return
     */
    @NonNull
    public static List<DownloadInfo> filterDownloading(List<DownloadInfo> downloadList) {
        List<DownloadInfo> result = new ArrayList<DownloadInfo>();
        for (DownloadInfo dinfo : downloadList) {
            if (dinfo.getDownloadStatus() != DownloadManager.STATUS_SUCCESSFUL
                    && dinfo.getDownloadStatus() != DownloadManager.STATUS_FAILED) {
                result.add(dinfo);
            }
        }
        return result;
    }

    /**
     * 从下载任务列表提取下载id数组
     * @param downloadList
     * @return
     */
    public static long[] toIdList(List<DownloadInfo> downloadList) {
        long[] ids = new long[downloadList.size()];
        int idx = 0;
        for (DownloadInfo dinfo : downloadList) {
            if (dinfo.getEnqueueID() > 0) {
                ids[idx++] = dinfo.getEnqueueID();
            }
        }
        return ids;
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public void addRequest(List<DownloadInfo> downloadList) {
        for (DownloadInfo dinfo : downloadList) {
            if (dinfo.getDownloadStatus() != DownloadManager.STATUS_PENDING) {
                continue;
            }

            File file = DownloadHelper.getDownloadFile(dinfo.getFileName());
            if (DownloadHelper.checkFileExist(file)) {
                Log.i(TAG, "file exist, url=" + dinfo.getDownloadURL());
                dinfo.setFilePath(file.getAbsolutePath());
                dinfo.setDownloadStatus(DownloadManager.STATUS_SUCCESSFUL);
            } else {
                long id = addRequest(dinfo);
                Log.i(TAG, "add download success, url=" + dinfo.getDownloadURL() + ", id=" + id);
                dinfo.setEnqueueID(id);
            }
        }
    }

    public long addRequest(DownloadInfo downloadInfo) {
        return addRequest(downloadInfo.getDownloadURL(), downloadInfo.getFileName());
    }

    public long addRequest(String url, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir(DOWNLOAD_DIR, fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setVisibleInDownloadsUi(false);
        return downloadManager.enqueue(request);
    }

    public List<DownloadState> queryState(Collection<Long> ids) {
        return queryState(ObjectUtils.transformLongCollection(ids));
    }

    @NonNull
    public int checkDownloadStatus(List<DownloadInfo> downloadList) {
        List<DownloadInfo> downloadingList = DownloadHelper.filterDownloading(downloadList);
        Map<Long, DownloadInfo> downloadingMap = new HashMap<Long, DownloadInfo>();
        for (DownloadInfo dinfo : downloadingList) {
            downloadingMap.put(dinfo.getEnqueueID(), dinfo);
        }
        if (!downloadingMap.isEmpty()) {
            Log.i(TAG, "checkDownload: " + downloadingMap.keySet());
            List<DownloadState> stateList = queryState(ObjectUtils.transformLongCollection(downloadingMap.keySet()));
            for (DownloadState ds : stateList) {
                DownloadInfo dinfo = downloadingMap.get(ds.getId());
                dinfo.setDownloadStatus(ds.getStatus());
                if (ds.getTotalBytes() > 0) {
                    dinfo.setDownloadProcess((int)((ds.getDownloadedBytes() * 100) / ds.getTotalBytes()));
                    Log.d(TAG,"setDownloadProcess " +(ds.getDownloadedBytes() * 100) + "  "+ ds.getTotalBytes() );
                }
                if (ds.getStatus() == DownloadManager.STATUS_SUCCESSFUL) {
                    downloadingMap.remove(ds.getId());

                    Log.i(TAG, "download success: " + dinfo.getDownloadURL() + ", id=" + ds.getId() + ", localFile=" + ds.getLocalFilename());
                    File file = new File(ds.getLocalFilename());
                    /*String md5 = DigestUtils.md5sum(file);
                    Log.i(TAG, "check md5 downloadInfo=" + dinfo.getMd5() + ", localFile=" + md5);
                    if (dinfo.getMd5().equals(md5)) {
                        File downloadFile = DownloadHelper.getDownloadFile(dinfo.getFileName());
                        if (!file.getName().equals(dinfo.getFileName())) {
                            Log.i(TAG, "rename " + file.getName() + " to " + dinfo.getFileName());
                            file.renameTo(downloadFile);
                        }
                        dinfo.setFilePath(downloadFile.getAbsolutePath());
                    } else {
                        Log.w(TAG, "md5 invalid, delete file");
                        file.delete();
                        dinfo.setDownloadStatus(DownloadManager.STATUS_FAILED);
                    }*/

                    File downloadFile = DownloadHelper.getDownloadFile(dinfo.getFileName());
                    if (!file.getName().equals(dinfo.getFileName())) {
                        Log.i(TAG, "rename " + file.getName() + " to " + dinfo.getFileName());
                        file.renameTo(downloadFile);
                    }
                    dinfo.setFilePath(downloadFile.getAbsolutePath());
                } else if (ds.getStatus() == DownloadManager.STATUS_FAILED) {
                    downloadingMap.remove(ds.getId());
                }
            }
        }
        return downloadingMap.size();
    }

    public List<DownloadState> queryState(long... ids) {
        List<DownloadState> result = new ArrayList<DownloadState>();
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(ids);
        Cursor cursor = downloadManager.query(query);
        try {
            while (cursor.moveToNext()) {
                DownloadState state = new DownloadState();
                state.setId(cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID)));
                state.setStatus(cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)));
                state.setLocalFilename(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME)));
                state.setDownloadedBytes(cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
                state.setTotalBytes(cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)));
                result.add(state);
            }
            return result;
        } finally {
            cursor.close();
        }
    }

    public void remove(long... ids) {
        downloadManager.remove(ids);
    }
}
