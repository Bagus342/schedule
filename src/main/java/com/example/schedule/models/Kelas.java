package com.example.schedule.models;

public class Kelas {

    public String id;
    public String nama_kelas;

    public Kelas(String id_kelas,String kelas) {
        this.id = id_kelas;
        this.nama_kelas = kelas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id_kelas) {
        this.id = id_kelas;
    }

    public String getNama_kelas() {
        return nama_kelas;
    }

    public void setNama_kelas(String kelas) {
        this.nama_kelas = kelas;
    }
}
