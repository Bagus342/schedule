package com.example.schedule.models;

public class Guru {

    public String id_guru;
    public String nama_guru;
    public String kode_guru;
    public String mapel;

    public Guru(String id, String guru,String kode, String pelajaran) {
        this.id_guru = id;
        this.nama_guru = guru;
        this.kode_guru = kode;
        this.mapel = pelajaran;
    }

    public String getId_guru() {
        return id_guru;
    }

    public void setId_guru(String id) {
        this.id_guru = id;
    }

    public String getNama_guru() {
        return nama_guru;
    }

    public void setNama_guru(String guru) {
        this.nama_guru = guru;
    }

    public String getKode_guru() {
        return kode_guru;
    }

    public void setKode_guru(String kode) {
        this.kode_guru = kode;
    }

    public String getMapel() {
        return mapel;
    }

    public void setMapel(String pelajaran) {
        this.mapel = pelajaran;
    }

}
