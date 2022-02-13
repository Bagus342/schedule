package com.example.schedule.models;

public class Ruangan {

    public String id_ruangan;
    public  String nama_ruangan;

    public Ruangan(String id, String ruangan) {
        this.id_ruangan = id;
        this.nama_ruangan = ruangan;
    }

    public String getId_ruangan() {
        return id_ruangan;
    }

    public void setId_ruangan(String id) {
        this.id_ruangan = id;
    }

    public String getNama_ruangan() {
        return nama_ruangan;
    }

    public void setNama_ruangan(String ruangan) {
        this.nama_ruangan = ruangan;
    }
}
