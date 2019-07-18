package com.antonina.socialsynchro.base;

import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.ITable;
import com.antonina.socialsynchro.database.tables.ServiceTable;

public class Service implements IDatabaseEntity {
    private long id;
    private String name;
    private String logoUrl;
    private String colorName;

    public Service(ITable table) {
        createFromData(table);
    }

    @Override
    public long getID() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLogoUrl() { return logoUrl; }

    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getColorName() { return colorName; }

    public void setColorName(String colorName) { this.colorName = colorName; }

    @Override
    public void createFromData(ITable data) {
        ServiceTable serviceData = (ServiceTable)data;
        this.id = serviceData.id;
        this.name = serviceData.name;
        this.logoUrl = serviceData.logoUrl;
        this.colorName = serviceData.colorName;
    }
}
