package com.antonina.socialsynchro.common.gui.listeners;

import com.antonina.socialsynchro.common.database.repositories.BaseRepository;

public interface OnDatabaseErrorListener {
    void onError(BaseRepository repository, String error);
}
