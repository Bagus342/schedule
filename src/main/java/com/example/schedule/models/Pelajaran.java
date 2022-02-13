package com.example.schedule.models;

public class Pelajaran {

    public String id;
    public String mapel;

    public Pelajaran(String id_mapel, String nama_mapel) {
        this.id = id_mapel;
        this.mapel = nama_mapel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id_mapel) {
        this.id = id_mapel;
    }

    public String getMapel() {
        return mapel;
    }

    public void setMapel(String nama_mapel) {
        this.mapel = nama_mapel;
    }

}
