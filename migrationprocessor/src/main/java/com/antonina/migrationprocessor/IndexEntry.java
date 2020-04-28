package com.antonina.migrationprocessor;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

public class IndexEntry {
    private Entity entityAnnotation;
    private ColumnInfo columnAnnotation;

    public IndexEntry(Entity entityAnnotation, ColumnInfo columnAnnotation) {
        this.entityAnnotation = entityAnnotation;
        this.columnAnnotation = columnAnnotation;
    }

    public Entity getEntityAnnotation() {
        return entityAnnotation;
    }

    public ColumnInfo getColumnAnnotation() {
        return columnAnnotation;
    }
}
