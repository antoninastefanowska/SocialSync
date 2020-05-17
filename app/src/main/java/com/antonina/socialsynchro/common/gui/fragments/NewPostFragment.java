package com.antonina.socialsynchro.common.gui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.gui.activities.EditActivity;

public class NewPostFragment extends Fragment {
    public NewPostFragment() { }

    public static NewPostFragment newInstance() {
        return new NewPostFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_post, container, false);
        final EditActivity activity = (EditActivity)getActivity();
        Button addNewButton = view.findViewById(R.id.button_add_new);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addChild();
            }
        });
        return view;
    }
}
