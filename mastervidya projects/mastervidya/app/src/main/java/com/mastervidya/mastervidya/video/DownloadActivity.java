package com.mastervidya.mastervidya.video;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.exoplayer2.offline.Download;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.adapter.DownloadedVideoAdapter;
import com.mastervidya.mastervidya.localdatabase.Dbconnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DownloadActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    private List<Download> downloadedVideoList;
    private DownloadedVideoAdapter downloadedVideoAdapter;
    private Runnable runnableCode;
    private Handler handler;
    ImageView back;
    SearchView searchView;
    Dbconnector dbconnector;
    List<VideoModel> videoModelList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        dbconnector=new Dbconnector(this);


        searchView=findViewById(R.id.sv);
        Dbconnector dbconnector;
        dbconnector=new Dbconnector(this);

//        videoModels.add(new VideoModel("1","Resignation","https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",15,"STANDARD 1","chapter 1","English"));
//        videoModels.add(new VideoModel("2","Introduction","https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8",128,"STANDARD 1","chapter 1","English"));
//        videoModels.add(new VideoModel("3","example","http://masterlibrary.s3.amazonaws.com/Video/c1b871b7543f4edc8deffdede0cf0580.m3u8",30,"STANDARD 1","chapter 1","English"));

         videoModelList = dbconnector.getallvideo();
        for (int i = 0; i<videoModelList.size(); i++)
        {

        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        recyclerView = findViewById(R.id.recycler_view_downloaded_video);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DownloadActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        back=findViewById(R.id.back);

        loadVideos();

        handler = new Handler();
        runnableCode = new Runnable() {
            @Override
            public void run() {
                List<Download> exoVideoList = new ArrayList<>();
                for(Map.Entry<Uri, Download> entry : AdaptiveExoplayer.getInstance().getDownloadTracker().downloads.entrySet()) {
                    Uri keyUri = entry.getKey();
                    Download download = entry.getValue();
                    exoVideoList.add(download);
                }
                downloadedVideoAdapter.onNewData(exoVideoList);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnableCode);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void loadVideos()
    {
        downloadedVideoList = new ArrayList<>();

        for(Map.Entry<Uri, Download> entry : AdaptiveExoplayer.getInstance().getDownloadTracker().downloads.entrySet())
        {
            Uri keyUri = entry.getKey();
            Download download = entry.getValue();
            downloadedVideoList.add(download);
        }


        downloadedVideoAdapter = new DownloadedVideoAdapter(DownloadActivity.this, DownloadActivity.this);
        recyclerView.setAdapter(downloadedVideoAdapter);
        downloadedVideoAdapter.addItems(downloadedVideoList);


    }

    public void openBottomSheet(Download download){

        VideoModel videoModel = AppUtil.getVideoDetail(download.request.id);

        String statusTitle = videoModel.getVideoName();

        View dialogView = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(DownloadActivity.this);
        dialog.setContentView(dialogView);

        TextView tvVideoTitle =  dialog.findViewById(R.id.tv_video_title);
        LinearLayout llDownloadStart = dialog.findViewById(R.id.ll_start_download);
        LinearLayout llDownloadResume = dialog.findViewById(R.id.ll_resume_download);
        LinearLayout llDownloadPause = dialog.findViewById(R.id.ll_pause_download);
        LinearLayout llDownloadDelete = dialog.findViewById(R.id.ll_delete_download);

        llDownloadStart.setVisibility(View.GONE);


        if(download.state == Download.STATE_DOWNLOADING){
            llDownloadPause.setVisibility(View.VISIBLE);
            llDownloadResume.setVisibility(View.GONE);

        }else if(download.state == Download.STATE_STOPPED){
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.VISIBLE);

        } else if(download.state == Download.STATE_QUEUED){
            llDownloadStart.setVisibility(View.VISIBLE);
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.GONE);
        }else {
            llDownloadStart.setVisibility(View.GONE);
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.GONE);
        }

        tvVideoTitle.setText(statusTitle);
        llDownloadStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdaptiveExoplayer.getInstance().getDownloadManager().addDownload(download.request);
                dialog.dismiss();
            }
        });
        llDownloadResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdaptiveExoplayer.getInstance().getDownloadManager().addDownload(download.request, Download.STOP_REASON_NONE);

                dialog.dismiss();
            }
        });

        llDownloadPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdaptiveExoplayer.getInstance().getDownloadManager().addDownload(download.request, Download.STATE_STOPPED);
                dialog.dismiss();
            }
        });

        llDownloadDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdaptiveExoplayer.getInstance().getDownloadManager().removeDownload(download.request.id);
//
//                Dbconnector dbconnector=new Dbconnector(DownloadActivity.this);
//                dbconnector.Delete(download.request.id);

                dialog.dismiss();
            }
        });

        dialog.show();




    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnableCode);
    }


    private  void filter(String text)
    {
        ArrayList<VideoModel> arrayList=new ArrayList<>();
        for(VideoModel item: videoModelList)
        {
            if (item.getVideoName().toLowerCase().contains(text.toLowerCase()))
            {
                arrayList.add(item);
            }
        }

        if(arrayList.isEmpty())
        {
            Toast.makeText(this, "No Video Found", Toast.LENGTH_SHORT).show();

            downloadedVideoAdapter.filterlist(arrayList);
        }
        else
        {
            downloadedVideoAdapter.filterlist(arrayList);

        }
    }
}
