package com.yashlan.monitoringkorona;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataProvinsi {

    @SerializedName("attributes")
    private Attributes dataProvinsiList;

    public Attributes getDataProvinsiList() {
        return dataProvinsiList;
    }

    public static class Attributes {

        @SerializedName("FID")
        private String FID;

        @SerializedName("Kode_Provi")
        private String kode_province;

        @SerializedName("Provinsi")
        private String provinsi;

        @SerializedName("Kasus_Posi")
        private String kasus_positif;

        @SerializedName("Kasus_Semb")
        private String kasus_sembuh;

        @SerializedName("Kasus_Meni")
        private String kasus_meninggal;

        public String getFID() {
            return FID;
        }

        public String getKode_province() {
            return kode_province;
        }

        public String getProvinsi() {
            return provinsi;
        }

        public String getKasus_positif() {
            return kasus_positif;
        }

        public String getKasus_sembuh() {
            return kasus_sembuh;
        }

        public String getKasus_meninggal() {
            return kasus_meninggal;
        }
    }

}
