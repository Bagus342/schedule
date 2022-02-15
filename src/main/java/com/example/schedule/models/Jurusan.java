package com.example.schedule.models;

public class Jurusan {

    public String id_jurusan;
    public String nama_jurusan;

    public Jurusan(String id, String jurusan) {
        this.id_jurusan = id;
        this.nama_jurusan = jurusan;
    }

    public String getId_jurusan() {
        return id_jurusan;
    }

    public void setId_jurusan(String id) {
        this.id_jurusan = id;
    }

    public String getNama_jurusan() {
        return nama_jurusan;
    }

    public void setNama_jurusan(String jurusan) {
        this.nama_jurusan = jurusan;
    }
}
