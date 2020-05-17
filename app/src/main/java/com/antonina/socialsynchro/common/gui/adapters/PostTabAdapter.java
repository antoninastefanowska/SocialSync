package com.antonina.socialsynchro.common.gui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.gui.fragments.ChildPostFragment;
import com.antonina.socialsynchro.common.gui.fragments.NewPostFragment;
import com.antonina.socialsynchro.common.gui.fragments.ParentPostFragment;
import com.antonina.socialsynchro.common.gui.other.MaskTransformation;
import com.antonina.socialsynchro.common.model.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.model.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.model.posts.PostContainer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class PostTabAdapter extends FragmentStatePagerAdapter {
    private FragmentManager fragmentManager;
    private List<Fragment> fragments;
    private ParentPostContainer parent;
    private TabLayout tabLayout;

    public PostTabAdapter(FragmentManager manager, ParentPostContainer parent, TabLayout tabLayout) {
        super(manager);
        this.fragmentManager = manager;
        this.parent = parent;
        this.tabLayout = tabLayout;
        fragments = new ArrayList<>();
        fragments.add(ParentPostFragment.newInstance(parent));
        for (ChildPostContainer child : parent.getChildren())
            fragments.add(ChildPostFragment.newInstance(child));
        fragments.add(NewPostFragment.newInstance());
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void addItem(ChildPostContainer item) {
        parent.addChild(item);
        Fragment deletedFragment = fragments.remove(fragments.size() - 1);
        fragments.add(ChildPostFragment.newInstance(item));
        fragments.add(NewPostFragment.newInstance());
        fragmentManager.beginTransaction().remove(deletedFragment).commit();
        notifyDataSetChanged();
        loadIcons();
    }

    private void loadIcon(PostContainer post, int position) {
        Context context = tabLayout.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (post != null) {
            view = inflater.inflate(R.layout.layout_icon_picture, null);
            if (post != parent) {
                int imageSize = context.getResources().getDimensionPixelSize(R.dimen.profile_picture_size);
                ImageView imageView = view.findViewById(R.id.imageview_icon_picture);
                String url = ((ChildPostContainer) post).getAccount().getProfilePictureURL();

                RequestOptions options = new RequestOptions()
                        .override(imageSize)
                        .fitCenter()
                        .transform(new MaskTransformation(context));
                Glide.with(context)
                        .load(url)
                        .apply(options)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView);
            }
        } else
            view = inflater.inflate(R.layout.layout_icon_new, null);
        tabLayout.getTabAt(position).setCustomView(view);
    }

    public void removeItem(ChildPostContainer item) {
        if (!parent.getChildren().contains(item))
            return;
        int position = parent.getChildren().indexOf(item) + 1;
        parent.removeChild(item);
        fragments.remove(position);
        notifyDataSetChanged();
        loadIcons();
    }

    public void loadIcons() {
        loadIcon(parent, 0);
        for (int i = 0; i < parent.getChildren().size(); i++)
            loadIcon(parent.getChildren().get(i), i + 1);
        loadIcon(null, parent.getChildren().size() + 1);
    }
}
