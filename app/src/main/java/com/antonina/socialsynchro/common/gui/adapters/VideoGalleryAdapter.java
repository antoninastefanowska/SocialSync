package com.antonina.socialsynchro.common.gui.adapters;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.model.attachments.VideoAttachment;
import com.antonina.socialsynchro.databinding.VideoGalleryItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class VideoGalleryAdapter extends BaseAdapter<VideoAttachment, VideoGalleryAdapter.VideoViewHolder> {
    private int imageSize;

    protected static class VideoViewHolder extends BaseAdapter.BaseViewHolder<VideoGalleryItemBinding> {
        public final ImageView imageView;

        public VideoViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.imageview_video);
        }

        @Override
        protected VideoGalleryItemBinding getBinding(View view) {
            return VideoGalleryItemBinding.bind(view);
        }
    }

    public VideoGalleryAdapter(AppCompatActivity context) {
        super(context);
        imageSize = context.getResources().getDimensionPixelSize(R.dimen.image_gallery_item_size);
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.video_gallery_item;
    }

    @Override
    protected void setItemBinding(VideoViewHolder viewHolder, VideoAttachment item) {
        viewHolder.binding.setVideo(item);
        RequestOptions options = new RequestOptions().override(imageSize).fitCenter();
        Glide.with(context)
                .load(item.getThumbnail())
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewHolder.imageView);
    }

    @Override
    protected VideoViewHolder createViewHolder(View view) {
        return new VideoViewHolder(view);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        VideoViewHolder viewHolder = super.onCreateViewHolder(parent, position);
        setSelectable(viewHolder);
        return viewHolder;
    }

    @Override
    public void loadData() {
        Runnable loadVideos = new Runnable() {
            @Override
            public void run() {
                loadVideos();
            }
        };
        Thread thread = new Thread(loadVideos);
        thread.run();
    }

    private void loadVideos() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        String sortOrder = MediaStore.Video.VideoColumns.DATE_MODIFIED + " DESC";
        Cursor cursor = null;

        String[] directories = null;
        if (uri != null)
            cursor = context.managedQuery(uri, projection, null, null, sortOrder);
        if (cursor != null && cursor.moveToFirst()) {
            SortedSet<String> sortedDirectories = new TreeSet<>();
            do {
                String directory = cursor.getString(0);
                directory = directory.substring(0, directory.lastIndexOf("/"));
                sortedDirectories.add(directory);
            } while (cursor.moveToNext());
            directories = new String[sortedDirectories.size()];
            sortedDirectories.toArray(directories);
        }

        SortedSet<VideoAttachment> sortedVideos = new TreeSet<>(new Comparator<VideoAttachment>() {
            @Override
            public int compare(VideoAttachment o1, VideoAttachment o2) {
                if (o1.getFile().lastModified() > o2.getFile().lastModified())
                    return -1;
                else if (o1.getFile().lastModified() < o2.getFile().lastModified())
                    return 1;
                else
                    return 0;
            }
        });
        if (directories != null) {
            for (String directoryPath : directories) {
                File directory = new File(directoryPath);
                File[] files = directory.listFiles();
                if (files == null)
                    continue;
                for (File file : files) {
                    if (file.isDirectory())
                        files = file.listFiles();
                    if (isVideo(file)) {
                        VideoAttachment video = new VideoAttachment(file);
                        sortedVideos.add(video);
                    }
                }
            }
            items = new ArrayList<>(sortedVideos);
            notifyDataSetChanged();
        }
    }

    private static boolean isVideo(File file) {
        final String[] extensions = {".avi", ".mp4", ".mpeg", ".flv"};
        String filename = file.getName().toLowerCase();
        for (String extension : extensions) {
            if (filename.contains(extension))
                return true;
        }
        return false;
    }
}
