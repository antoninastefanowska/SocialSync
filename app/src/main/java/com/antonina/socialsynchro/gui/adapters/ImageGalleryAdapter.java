package com.antonina.socialsynchro.gui.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.attachments.ImageAttachment;
import com.antonina.socialsynchro.databinding.ImageGalleryItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ImageViewHolder> {
    private ImageAttachment[] images;
    private List<ImageAttachment> selectedImages;
    private Activity context;

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageGalleryItemBinding binding;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View view) {
            super(view);
            binding = ImageGalleryItemBinding.bind(view);
            imageView = view.findViewById(R.id.imageview_image);
        }
    }

    public ImageGalleryAdapter(Activity context) {
        images = new ImageAttachment[0];
        selectedImages = new ArrayList<ImageAttachment>();
        this.context = context;
    }

    public void loadData() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        String sortOrder = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC";
        Cursor cursor = null;

        String[] directories = null;
        if (uri != null)
            cursor = context.managedQuery(uri, projection, null, null, sortOrder);
        if (cursor != null && cursor.moveToFirst()) {
            SortedSet<String> sortedDirectories = new TreeSet<String>();
            do {
                String directory = cursor.getString(0);
                directory = directory.substring(0, directory.lastIndexOf("/"));
                sortedDirectories.add(directory);
            } while (cursor.moveToNext());
            directories = new String[sortedDirectories.size()];
            sortedDirectories.toArray(directories);
        }

        SortedSet<ImageAttachment> sortedImages = new TreeSet<ImageAttachment>(new Comparator<ImageAttachment>() {
            @Override
            public int compare(ImageAttachment o1, ImageAttachment o2) {
                if (o1.getFile().lastModified() > o2.getFile().lastModified())
                    return -1;
                else if (o1.getFile().lastModified() < o2.getFile().lastModified())
                    return 1;
                else
                    return 0;
            }
        });
        for (String directoryPath : directories) {
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();
            if (files == null)
                continue;
            for (File file : files) {
                if (file.isDirectory())
                    files = file.listFiles();
                if (isImage(file)) {
                    ImageAttachment image = new ImageAttachment(file);
                    sortedImages.add(image);
                }
            }
        }
        images = new ImageAttachment[sortedImages.size()];
        sortedImages.toArray(images);
        notifyDataSetChanged();
    }

    private static boolean isImage(File file) {
        final String[] extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        String filename = file.getName().toLowerCase();
        for (String extension : extensions) {
            if (filename.contains(extension))
                return true;
        }
        return false;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View imageItemView = inflater.inflate(R.layout.image_gallery_item, parent, false);
        final ImageViewHolder viewHolder = new ImageViewHolder(imageItemView);

        imageItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ImageAttachment item = getItem(position);
                if (item.isSelected())
                    unselectItem(position);
                else
                    selectItem(position);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder viewHolder, int position) {
        ImageAttachment item = getItem(position);
        viewHolder.binding.setImage(item);
        viewHolder.binding.executePendingBindings();

        RequestOptions options = new RequestOptions().override(50, 50).fitCenter();
        Glide.with(context)
                .load(item.getFile())
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    private ImageAttachment getItem(int position) {
        return images[position];
    }

    public void selectItem(int position) {
        ImageAttachment item = getItem(position);
        if (item.isSelected())
            return;
        item.select();
        selectedImages.add(item);
        notifyItemChanged(position);
    }

    public void unselectItem(int position) {
        ImageAttachment item = getItem(position);
        if (!item.isSelected())
            return;
        item.unselect();
        selectedImages.remove(item);
        notifyItemChanged(position);
    }

    public List<ImageAttachment> getSelectedItems() {
        return selectedImages;
    }
}
