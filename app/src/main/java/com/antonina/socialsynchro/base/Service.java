package com.antonina.socialsynchro.base;

import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.ITable;
import com.antonina.socialsynchro.database.tables.ServiceTable;

public class Service implements IDatabaseEntity {
    private Long id;
    private String name;
    private String logoUrl;
    private String colorName;

    public Service(ITable table) {
        convertFromTable(table);
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLogoUrl() { return logoUrl; }

    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getColorName() { return colorName; }

    public void setColorName(String colorName) { this.colorName = colorName; }

    @Override
    public void convertFromTable(ITable table) {
        ServiceTable serviceTable = (ServiceTable)table;
        this.id = serviceTable.id;
        this.name = serviceTable.name;
        this.logoUrl = serviceTable.logoUrl;
        this.colorName = serviceTable.colorName;
    }
}
