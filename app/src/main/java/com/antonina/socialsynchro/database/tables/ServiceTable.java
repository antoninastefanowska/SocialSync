package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.base.Service;
import com.antonina.socialsynchro.database.IDatabaseEntity;

@Entity(tableName = "service")
public class ServiceTable implements ITable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "logo_url")
    public String logoUrl;

    @ColumnInfo(name = "color_name")
    public String colorName;

    public ServiceTable(long id, String name, String logoUrl, String colorName) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.colorName = colorName;
    }

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        this.id = entity.getID();
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        Service service = (Service)entity;
        this.name = service.getName();
        this.logoUrl = service.getLogoUrl();
        this.colorName = service.getLogoUrl();
    }
}
