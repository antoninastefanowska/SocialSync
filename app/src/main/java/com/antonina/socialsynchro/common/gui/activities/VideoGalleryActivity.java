package com.antonina.socialsynchro.common.gui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.content.attachments.VideoAttachment;
import com.antonina.socialsynchro.common.gui.adapters.VideoGalleryAdapter;
import com.antonina.socialsynchro.common.gui.other.SerializableList;
import com.antonina.socialsynchro.databinding.ActivityVideoGalleryBinding;

import java.util.ArrayList;
import java.util.List;

public class VideoGalleryActivity extends AppCompatActivity {
    private VideoGalleryAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);

        ActivityVideoGalleryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_video_gallery);
        videoAdapter = new VideoGalleryAdapter(this);
        RecyclerView imageRecyclerView = findViewById(R.id.recyclerview_videos);
        imageRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        binding.setVideoAdapter(videoAdapter);
        binding.executePendingBindings();
    }

    public void confirm(View view) {
        List<VideoAttachment> videos = videoAdapter.getSelectedItems();
        List<Attachment> attachments = new ArrayList<>();
        attachments.addAll(videos);

        SerializableList<Attachment> serializableAttachments = new SerializableList<>(attachments);
        Intent editActivity = new Intent();
        editActivity.putExtra("attachments", serializableAttachments);
        setResult(RESULT_OK, editActivity);
        finish();
    }
}
