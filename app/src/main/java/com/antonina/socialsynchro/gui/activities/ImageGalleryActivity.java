package com.antonina.socialsynchro.gui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.attachments.ImageAttachment;
import com.antonina.socialsynchro.databinding.ActivityImageGalleryBinding;
import com.antonina.socialsynchro.gui.adapters.ImageGalleryAdapter;
import com.antonina.socialsynchro.gui.helpers.SerializableList;

import java.util.List;

public class ImageGalleryActivity extends AppCompatActivity {
    private ActivityImageGalleryBinding binding;
    private ImageGalleryAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_gallery);
        imageAdapter = new ImageGalleryAdapter(this);
        RecyclerView imageRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_images);
        imageRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        binding.setImageAdapter(imageAdapter);
        binding.executePendingBindings();

        Runnable loadImages = new Runnable() {
            @Override
            public void run() {
                imageAdapter.loadData();
            }
        };
        Thread thread = new Thread(loadImages);
        thread.run();
    }

    public void buttonConfirm_onClick(View view) {
        List<ImageAttachment> images = imageAdapter.getSelectedItems();
        SerializableList<ImageAttachment> serializableImages = new SerializableList<ImageAttachment>(images);
        Intent editActivity = new Intent();
        editActivity.putExtra("images", serializableImages);
        setResult(RESULT_OK, editActivity);
        finish();
    }
}
