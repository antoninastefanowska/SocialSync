package com.antonina.socialsynchro.services;

import java.io.Serializable;

public interface IService extends Serializable {
    ServiceID getID();
    String getName();
    String getLogoUrl();
    String getColorName();
}
