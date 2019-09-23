package com.antonina.socialsynchro.gui.listeners;

import com.antonina.socialsynchro.services.IServiceEntity;

public interface OnSynchronizedListener {
    void onSynchronized(IServiceEntity entity);
    void onError(IServiceEntity entity, String error);
}
