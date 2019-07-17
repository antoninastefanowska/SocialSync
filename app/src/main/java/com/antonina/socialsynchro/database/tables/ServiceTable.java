package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.base.Service;
import com.antonina.socialsynchro.database.IDatabaseEntity;

@Entity(tableName = "service")
public class ServiceTable implements ITable {
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "logo_url")
    public String logoUrl;

    @ColumnInfo(name = "color_name")
    public String colorName;

    @Override
    public void convertFromEntity(IDatabaseEntity entity) {
        Service service = (Service)entity;
        this.id = service.getId();
        this.name = service.getName();
        this.logoUrl = service.getLogoUrl();
        this.colorName = service.getLogoUrl();
    }
}
