package com.qgd.yfb.assist;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qgd.yfb.assist.bean.AppUpdateInfo2;
import com.qgd.yfb.assist.bean.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {
    private static final String TAG = "DownloadAdapter";

    private List<DownloadInfo> downloadInfos = new ArrayList<DownloadInfo>();
    private Context mContext;
    private List<AppUpdateInfo2> appUpdateInfos;

    public DownloadAdapter(List<DownloadInfo> downloadInfos, List<AppUpdateInfo2> appUpdateInfos, AppUpdateActivity appUpdateActivity) {
        this.downloadInfos = downloadInfos;
        this.mContext = appUpdateActivity;
        this.appUpdateInfos = appUpdateInfos;
    }


    @Override
    public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_download_show, parent, false);
        DownloadViewHolder downloadViewHolder = new DownloadViewHolder(mContext, view);
        return downloadViewHolder;
    }


    @Override
    public void onBindViewHolder(DownloadViewHolder holder, int position) {
        AppUpdateInfo2 updateInfo = appUpdateInfos.get(position);
        DownloadInfo downloadInfo = null;
        for (DownloadInfo temp : downloadInfos) {
            if (temp.getPackageName().equals(updateInfo.getPackageName())) {
                downloadInfo = temp;
            }
        }

        holder.tv_update_item_verson.setText(updateInfo.getName() + " " + updateInfo.getVersion());
        holder.mIv_update_item_icon.setImageDrawable(downloadInfo.getIcon());

        updateStatus(holder, downloadInfo);
    }

    private void updateStatus(DownloadViewHolder holder, DownloadInfo downloadInfo) {
        ProgressBar progressBar = holder.mPb_update_item;
        TextView statusText = holder.mTv_update_item_status;

        switch (downloadInfo.getDownloadStatus()){
            case DownloadManager.STATUS_PENDING:
                statusText.setText("等待下载");
                progressBar.setProgress((int) downloadInfo.getDownloadProcess());
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText.setText("正在下载");
                progressBar.setProgress((int)downloadInfo.getDownloadProcess());
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                updateInstallStatus(holder, downloadInfo);
                break;
            case DownloadManager.STATUS_FAILED:
                statusText.setText("下载失败");
                statusText.setTextColor(mContext.getResources().getColor(R.color.install_orange));
                progressBar.setProgress((int)downloadInfo.getDownloadProcess());
                break;
        }
    }

    private void updateInstallStatus(DownloadViewHolder viewHolder, DownloadInfo downloadInfo) {
        ProgressBar progressBar = viewHolder.mPb_update_item;
        TextView statusText = viewHolder.mTv_update_item_status;

        //给progressbar更换样式
        if (progressBar.getProgressDrawable() != viewHolder.mPbInstall) {
            progressBar.setProgressDrawable(viewHolder.mPbInstall);
        }

        if (downloadInfo.getInstallStatus() == 0){
            statusText.setText("等待安装");
            statusText.setTextColor(Color.WHITE);
            progressBar.setProgress(20);
        }else if(downloadInfo.getInstallStatus() == 1){
            statusText.setText("正在安装");
            statusText.setTextColor(mContext.getResources().getColor(R.color.install_green));
            //模拟安装进度
            progressBar.setProgress(60);
        }else if(downloadInfo.getInstallStatus() == 2){
            statusText.setText("安装完成");
            statusText.setTextColor(mContext.getResources().getColor(R.color.install_green));
            progressBar.setProgress(100);
        }else{
            statusText.setText("安装失败");
            statusText.setTextColor(mContext.getResources().getColor(R.color.install_orange));
            progressBar.setProgress(100);
        }
    }

    @Override
    public int getItemCount() {
        return appUpdateInfos.size();
    }

    public static class DownloadViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_update_item_verson;
        private final TextView mTv_update_item_status;
        private final ImageView mIv_update_item_icon;
        private final ProgressBar mPb_update_item;
        private final Drawable mPbInstall;
        private final Drawable mPbDownload;

        private DownloadViewHolder(Context context, View itemView) {
            super(itemView);
            tv_update_item_verson = (TextView) itemView.findViewById(R.id.tv_update_item_verson);
            mTv_update_item_status = (TextView) itemView.findViewById(R.id.tv_update_item_status);
            mIv_update_item_icon = (ImageView) itemView.findViewById(R.id.iv_update_item_icon);
            mPb_update_item = (ProgressBar) itemView.findViewById(R.id.pb_update_item);
            mPbInstall = context.getResources().getDrawable(R.drawable.progressbar_install);
            mPbDownload = context.getResources().getDrawable(R.drawable.progressbar_load);
            mPb_update_item.setProgressDrawable(mPbDownload);
        }
    }
}
