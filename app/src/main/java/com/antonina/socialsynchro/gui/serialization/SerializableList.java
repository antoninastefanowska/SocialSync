package com.antonina.socialsynchro.gui.serialization;

import java.io.Serializable;
import java.util.List;

public class SerializableList<ItemClass> implements Serializable {
    private List<ItemClass> list;

    public SerializableList(List<ItemClass> list) {
        this.list = list;
    }

    public List<ItemClass> getList() {
        return list;
    }
}
