package com.antonina.socialsynchro.common.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.databinding.ServiceDialogItemBinding;
import com.antonina.socialsynchro.common.gui.listeners.OnServiceSelectedListener;
import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.content.services.Services;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class ServiceDialogAdapter extends BaseAdapter<Service, ServiceDialogAdapter.ServiceViewHolder> {
    private final OnServiceSelectedListener listener;

    protected static class ServiceViewHolder extends BaseAdapter.BaseViewHolder<ServiceDialogItemBinding> {

        public ServiceViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected ServiceDialogItemBinding getBinding(View view) {
            return ServiceDialogItemBinding.bind(view);
        }
    }

    public ServiceDialogAdapter(AppCompatActivity context, OnServiceSelectedListener listener) {
        super(context);
        this.listener = listener;
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.service_dialog_item;
    }

    @Override
    protected void setItemBinding(ServiceViewHolder viewHolder, Service item) {
        viewHolder.binding.setService(item);
    }

    @Override
    protected ServiceViewHolder createViewHolder(View view) {
        return new ServiceViewHolder(view);
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        final ServiceViewHolder viewHolder = super.onCreateViewHolder(parent, position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Service item = getItem(position);
                listener.onServiceSelected(item);
            }
        });
        return viewHolder;
    }

    @Override
    public void loadData() {
        Service[] array = Services.getServices();
        items = new ArrayList<>(Arrays.asList(array));
        notifyDataSetChanged();
    }
}
