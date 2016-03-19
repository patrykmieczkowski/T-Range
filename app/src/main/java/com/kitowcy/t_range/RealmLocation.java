package com.kitowcy.t_range;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Łukasz Marczak
 *
 * @since 19.03.16
 */
public class RealmLocation extends RealmObject {

    @PrimaryKey
    private String uuid;
    private double latitude;
    private double longitude;
    private String name;
    private int strength;

    public RealmLocation(double latitude, double longitude, String name, int strength) {
        this.uuid = UUID.randomUUID().toString();
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.strength = strength;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public RealmLocation() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}
