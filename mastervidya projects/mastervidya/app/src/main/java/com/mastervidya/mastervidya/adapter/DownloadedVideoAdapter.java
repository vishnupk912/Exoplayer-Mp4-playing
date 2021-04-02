package com.mastervidya.mastervidya.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.video.AdaptiveExoplayer;
import com.mastervidya.mastervidya.video.AppUtil;
import com.mastervidya.mastervidya.video.DownloadActivity;
import com.mastervidya.mastervidya.video.MyDiffUtilCallback;
import com.mastervidya.mastervidya.video.OfflinePlayerActivity;
import com.mastervidya.mastervidya.video.VideoModel;

import java.util.ArrayList;
import java.util.List;


public class DownloadedVideoAdapter extends RecyclerView.Adapter<DownloadedVideoAdapter.MyViewHolder> { //implements Filterable

    List<Download> videosList;
    List<VideoModel> videoModelList=new ArrayList<>();
    //    private ValueFilter mFilter = new ValueFilter();
    Context context;
    DownloadActivity downloadActivity;

    SessionHandler sessionHandler;

    VideoModel videoModel;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rlContainer;
        ImageView imageView;
        TextView tvDownloadVideoTitle;
        TextView tvDownloadVideoPercentage;
        TextView tvDownloadVideoStatus;
        ImageView imgMenuOverFlow;
        ProgressBar progressBarPercentage;
        TextView chapternametvtv;



        public MyViewHolder(View view) {
            super(view);
            rlContainer = view.findViewById(R.id.rl_container);
            imageView = view.findViewById(R.id.img_download_banner);
            tvDownloadVideoTitle = view.findViewById(R.id.tv_download_vid_title);
            tvDownloadVideoPercentage = view.findViewById(R.id.tv_downloaded_percentage);
            tvDownloadVideoStatus = view.findViewById(R.id.tv_downloaded_status);
            imgMenuOverFlow = view.findViewById(R.id.img_overflow);
            progressBarPercentage = view.findViewById(R.id.progress_horizontal_percentage);
            chapternametvtv=view.findViewById(R.id.chapternametvtv);
//            imgDownloadDelete = view.findViewById(R.id.img_delete_download);
//            imgDownloadPlayPause = view.findViewById(R.id.img_download_play_pause);



        }
    }


    public DownloadedVideoAdapter(Context context, DownloadActivity downloadActivity) {
        this.context = context;
        this.videosList = new ArrayList<>();
        this.downloadActivity = downloadActivity;
        sessionHandler=new SessionHandler(context);



    }

    @Override
    public DownloadedVideoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_downloaded_video, parent, false);

        return new DownloadedVideoAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DownloadedVideoAdapter.MyViewHolder holder, int position, List<Object> payloads) {

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals("percentDownloaded")) {

                    Download download = (Download) payloads.get(position);

                    VideoModel videoModel = AppUtil.getVideoDetail(download.request.id);
                    videoModelList.add(videoModel);

                    if (!videoModelList.get(position).getVideoName().isEmpty()) {
                        holder.tvDownloadVideoTitle.setText(videoModel.getVideoName());
                    }
                    holder.chapternametvtv.setText(videoModelList.get(position).getClassname()+"|"+videoModelList.get(position).getChaptername()+" | "+ videoModelList.get(position).getSubjectname());

                    Toast.makeText(context, "Log : "+videoModel.getVideoName(), Toast.LENGTH_SHORT).show();

                    DownloadRequest downloadRequest = AdaptiveExoplayer.getInstance().getDownloadTracker().getDownloadRequest(download.request.uri);

                    if (download.state == Download.STATE_COMPLETED) {
                        holder.progressBarPercentage.setVisibility(View.GONE);
                    } else {
                        holder.progressBarPercentage.setVisibility(View.VISIBLE);
                        holder.progressBarPercentage.setProgress((int) download.getPercentDownloaded());
                    }
                    String percentage = AppUtil.floatToPercentage(download.getPercentDownloaded());
//                    String downloadInMb = AppUtil.getProgressDisplayLine(download.getBytesDownloaded(), downloadRequest.data.length);


                    if (download.state == Download.STATE_DOWNLOADING || download.state == Download.STATE_COMPLETED) {
                        holder.tvDownloadVideoPercentage.setVisibility(View.VISIBLE);
                        holder.tvDownloadVideoPercentage.setText("Size: " + AppUtil.formatFileSize(download.getBytesDownloaded()) + " | Progress: " + percentage);

                    } else {
                        holder.tvDownloadVideoPercentage.setVisibility(View.INVISIBLE);

                    }
                    holder.tvDownloadVideoStatus.setText(AppUtil.downloadStatusFromId(download));


                }
            }
        }
    }

    @Override
    public void onBindViewHolder(final DownloadedVideoAdapter.MyViewHolder holder, final int position) {

        Download download = videosList.get(position);


         videoModel =   AppUtil.getVideoDetail(download.request.id);
        videoModelList.add(videoModel);

        try {
            if (!videoModelList.get(position).getVideoName().isEmpty()) {
                holder.tvDownloadVideoTitle.setText(videoModel.getVideoName());
            }
        }
        catch (Exception e)
        {

        }

        holder.chapternametvtv.setText(videoModelList.get(position).getClassname()+"|"+videoModelList.get(position).getChaptername()+" | "+ videoModelList.get(position).getSubjectname());

////
        if (download.state == Download.STATE_COMPLETED) {
            holder.progressBarPercentage.setVisibility(View.GONE);
        } else {
            holder.progressBarPercentage.setVisibility(View.VISIBLE);
            holder.progressBarPercentage.setProgress((int) download.getPercentDownloaded());
        }
        String percentage = AppUtil.floatToPercentage(download.getPercentDownloaded());
//        String downloadInMb = AppUtil.getProgressDisplayLine(download.getBytesDownloaded(), downloadRequest.data.length);

        if (download.state == Download.STATE_DOWNLOADING || download.state == Download.STATE_COMPLETED) {
            holder.tvDownloadVideoPercentage.setVisibility(View.VISIBLE);
            holder.tvDownloadVideoPercentage.setText("Size: " + AppUtil.formatFileSize(download.getBytesDownloaded()) + " | Progress: " + percentage);

        } else {
            holder.tvDownloadVideoPercentage.setVisibility(View.INVISIBLE);

        }


        holder.tvDownloadVideoStatus.setText(AppUtil.downloadStatusFromId(download));


        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(download.state == Download.STATE_COMPLETED)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("video_url", download.request.id);
                    Intent intent = new Intent(context, OfflinePlayerActivity.class);
                    intent.putExtra("video_title", download.request.id);
                    intent.putExtra("video_url","testing");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                else {
                    downloadActivity.openBottomSheet(download);
                }
            }
        });

        holder.imgMenuOverFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadActivity.openBottomSheet(download);
            }

        });

    }



    @Override
    public int getItemCount() {
        return videosList.size();
    }


//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//
//    public Download getItem(int position) {
//        return videosList.get(position);
//    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//

    public void addItems(List<Download> lst) {
        this.videosList = lst;
    }


    public void onNewData(List<Download> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtilCallback(newData, videosList));
        diffResult.dispatchUpdatesTo(this);
        this.videosList.clear();
        this.videosList.addAll(newData);
    }

    public void filterlist(ArrayList<VideoModel> filterlist)
    {
        videoModelList=filterlist;

        notifyDataSetChanged();
        Toast.makeText(context, videoModelList.toString(), Toast.LENGTH_SHORT).show();
    }


}




