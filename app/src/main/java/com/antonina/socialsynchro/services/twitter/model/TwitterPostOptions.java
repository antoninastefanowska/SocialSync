package com.antonina.socialsynchro.services.twitter.model;

import android.databinding.Bindable;

import com.antonina.socialsynchro.common.model.posts.PostOptions;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.services.twitter.database.repositories.TwitterPostOptionsRepository;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterPostOptionsRow;

public class TwitterPostOptions extends PostOptions {
    private boolean possiblySensitive;

    public TwitterPostOptions(TwitterPostContainer parentPost) {
        this();
        setParentPost(parentPost);
    }

    public TwitterPostOptions() {
        possiblySensitive = false;
    }

    public TwitterPostOptions(IDatabaseRow data) {
        this();
        if (data != null)
            createFromDatabaseRow(data);
    }

    @Bindable
    public boolean isPossiblySensitive() {
        return possiblySensitive;
    }

    @Bindable
    public void setPossiblySensitive(boolean possiblySensitive) {
        this.possiblySensitive = possiblySensitive;
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        setInternalID(data.getID());
        TwitterPostOptionsRow dataRow = (TwitterPostOptionsRow)data;
        setPossiblySensitive(dataRow.possiblySensitive);
    }

    @Override
    public void saveInDatabase() {
        if (getInternalID() != null)
            updateInDatabase();
        else {
            setInternalID(getParentPost().getInternalID());
            TwitterPostOptionsRepository repository = TwitterPostOptionsRepository.getInstance();
            repository.insert(this);
        }
    }

    @Override
    public void updateInDatabase() {
        TwitterPostOptionsRepository repository = TwitterPostOptionsRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        TwitterPostOptionsRepository repository = TwitterPostOptionsRepository.getInstance();
        repository.delete(this);
    }
}
