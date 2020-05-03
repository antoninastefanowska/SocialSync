package com.antonina.socialsynchro.common.model.posts;

import android.databinding.Bindable;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.repositories.TagRepository;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.database.rows.TagRow;
import com.antonina.socialsynchro.common.gui.GUIItem;

import java.util.ArrayList;
import java.util.List;

public class Tag extends GUIItem implements IDatabaseEntity {
    private Long internalID;
    private String value;
    private Post parentPost;

    public Tag(String value) {
        this.value = correctValue(value);
    }

    public Tag(IDatabaseRow data) {
        createFromDatabaseRow(data);
    }

    private String correctValue(String value) {
        return value.replaceAll("[^a-zA-Z0-9]", "");
    }

    public String getProperValue() {
        return '#' + value;
    }

    @Bindable
    public String getValue() {
        return value;
    }

    public Post getParentPost() {
        return parentPost;
    }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;
        notifyGUI();
    }

    public static List<Tag> stringToTags(String content) {
        List<Tag> tags = new ArrayList<>();
        String[] words = content.split(" ");
        for (String word : words)
            if (!word.isEmpty())
                tags.add(new Tag(word));
        return tags;
    }

    public int getLength() {
        return value.length() + 1;
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        TagRow tagData = (TagRow)data;
        this.internalID = tagData.getID();
        this.value = tagData.value;
    }

    @Override
    public Long getInternalID() {
        return internalID;
    }

    @Override
    public void saveInDatabase() {
        if (internalID != null)
            updateInDatabase();
        else {
            TagRepository repository = TagRepository.getInstance();
            internalID = repository.insert(this);
        }
    }

    @Override
    public void updateInDatabase() {
        if (internalID == null)
            return;
        TagRepository repository = TagRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        if (internalID == null)
            return;
        TagRepository repository = TagRepository.getInstance();
        repository.delete(this);
    }
}
