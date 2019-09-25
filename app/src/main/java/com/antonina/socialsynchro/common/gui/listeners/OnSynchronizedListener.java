package com.antonina.socialsynchro.common.gui.listeners;

import com.antonina.socialsynchro.common.rest.IServiceEntity;

public interface OnSynchronizedListener {
    void onSynchronized(IServiceEntity entity);
    void onError(IServiceEntity entity, String error);
}
