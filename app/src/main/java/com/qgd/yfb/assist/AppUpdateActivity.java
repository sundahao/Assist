package com.qgd.yfb.assist;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qgd.yfb.assist.bean.AppUpdateInfo2;
import com.qgd.yfb.assist.bean.DownloadInfo;
import com.qgd.yfb.assist.biz.AppUpdateTask;
import com.qgd.yfb.assist.biz.DownloadHelper;
import com.qgd.yfb.assist.biz.UpdateManager;
import com.qgd.yfb.assist.util.PackageInfoUtil;
import com.qgd.yfb.assist.util.PackageUtils;

import java.util.ArrayList;
import java.util.List;

import static com.qgd.yfb.assist.R.id.tv_update_message;


public class AppUpdateActivity extends Activity  {
    public static final String TAG = "AppUpdate";
    //文件保存地址
    private List<DownloadInfo> mDownloadInfos = new ArrayList<DownloadInfo>();


    private TextView     mTv_update_message;
    private RecyclerView mRv_update_message;

    private DownloadAdapter     mDownloadAdapter;
    private ImageView           mIv_more_guide;
    private LinearLayoutManager mLinearLayoutManager;
    private TextView            mTv_ok_continue;
    public int successCount ;
    public int failCount  ;
    private List<AppUpdateInfo2> appUpdateInfos;
    private UpdateManager updateManager;
    private DownloadHelper downloadHelper;
    private AsyncTask installTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_progress);
        successCount = 0;
        failCount    = 0;

        downloadHelper = new DownloadHelper((DownloadManager) getSystemService(DOWNLOAD_SERVICE));
        updateManager = AssistApplication.getInstance().getUpdateManager();
        appUpdateInfos = ((AppUpdateTask) updateManager.getCurrentTask()).getTestData();
        mDownloadInfos = updateManager.getCurrentTask().getDownloadList();

        initView();
        initAdapter();
        initListner();

        startInstall();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (installTask != null && installTask.getStatus() == AsyncTask.Status.RUNNING) {
            installTask.cancel(false);
            installTask = null;
        }

        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            updateManager.onInstallFinish();
        } catch (Throwable e) {
            Log.e(TAG, "", e);
        }

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (!isFinishing()) {
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        mTv_update_message = (TextView) findViewById(tv_update_message);
        mRv_update_message = (RecyclerView) findViewById(R.id.rv_update_message);
        mIv_more_guide = (ImageView) findViewById(R.id.iv_more_guide);
        mTv_ok_continue = (TextView) findViewById(R.id.tv_ok_continue);

        for (int i = 0; i < this.appUpdateInfos.size(); i++) {
            AppUpdateInfo2 updateInfo = appUpdateInfos.get(i);
            DownloadInfo downloadInfo = this.mDownloadInfos.get(i);
            downloadInfo.setIcon(PackageInfoUtil.getAppIcon(this, updateInfo.getPackageName()));
        }
    }

    private void initListner() {
        mRv_update_message.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItem < mDownloadInfos.size() - 1) {
                        mIv_more_guide.setVisibility(View.VISIBLE);
                    } else {
                        mIv_more_guide.setVisibility(View.GONE);
                    }
                }
            }

        });
    }

    private void initAdapter() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRv_update_message.setLayoutManager(mLinearLayoutManager);
        mDownloadAdapter = new DownloadAdapter(mDownloadInfos,appUpdateInfos, this);
        mRv_update_message.setAdapter(mDownloadAdapter);
    }

    private void startInstall() {
        installTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                Log.i(TAG, "download begin");
                downloadHelper.addRequest(mDownloadInfos);
                publishProgress();

                while (!isCancelled()) {
                    int remain = downloadHelper.checkDownloadStatus(mDownloadInfos);
                    if (remain == 0) {
                        break;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "interrupted", e);
                    }
                    Log.d(TAG,"publishProgress  " + mDownloadInfos.get(0).getDownloadProcess() );
                    publishProgress();
                }

                if (isCancelled()) {
                    Log.i(TAG, "download canceled");
                    return null;
                } else {
                    Log.i(TAG, "download end");
                    Log.d(TAG,""+mDownloadInfos.get(0).getDownloadProcess());
                    publishProgress();
                }

                for (DownloadInfo downloadInfo : mDownloadInfos) {
                    Log.i(TAG, "install " + downloadInfo.getFilePath());
                    downloadInfo.setInstallStatus(1);
                    publishProgress();
                    int ret = PackageUtils.installSilent(AppUpdateActivity.this, downloadInfo.getFilePath());
                    Log.i(TAG, "install result: " + ret);
                    if (ret == PackageUtils.INSTALL_SUCCEEDED) {
                        downloadInfo.setInstallStatus(2);
                        successCount++;
                    } else {
                        downloadInfo.setInstallStatus(3);
                        failCount++;
                    }

                    if (isCancelled()) {
                        Log.i(TAG, "install canceled");
                        break;
                    } else {
                        publishProgress();
                    }
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Object... values) {
                mDownloadAdapter.notifyDataSetChanged();
                mTv_update_message.setText(mDownloadInfos.size() + "个应用安装，成功" + successCount + "个，失败" + failCount + "个");
            }

            @Override
            protected void onPostExecute(Object o) {
                mDownloadAdapter.notifyDataSetChanged();
                mTv_ok_continue.setVisibility(View.VISIBLE);
            }
        };

        installTask.execute();
    }
}
