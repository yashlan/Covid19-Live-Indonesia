package com.yashlan.monitoringkorona;

import com.google.gson.annotations.SerializedName;

public class DataRegion {

    @SerializedName("name")
    private String RegionName;

    @SerializedName("positif")
    private String positif;

    @SerializedName("sembuh")
    private String sembuh;

    @SerializedName("meninggal")
    private String meninggal;

    @SerializedName("dirawat")
    private String dirawat;

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    public String getPositif() {
        return positif;
    }

    public void setPositif(String positif) {
        this.positif = positif;
    }

    public String getSembuh() {
        return sembuh;
    }

    public void setSembuh(String sembuh) {
        this.sembuh = sembuh;
    }

    public String getMeninggal() {
        return meninggal;
    }

    public void setMeninggal(String meninggal) {
        this.meninggal = meninggal;
    }

    public String getDirawat() {
        return dirawat;
    }

    public void setDirawat(String dirawat) {
        this.dirawat = dirawat;
    }
}
