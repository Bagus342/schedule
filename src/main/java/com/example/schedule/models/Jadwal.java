package com.example.schedule.models;

public class Jadwal {

    public String id_jadwal;
    public String hari;
    public String mata_pelajaran;
    public String nama_guru;
    public String nama_kelas;
    public String mulai;
    public String selesai;
    public String nama_ruangan;

    public Jadwal(String id, String nama_hari, String mapel, String guru, String kelas, String start, String end, String ruangan) {
        this.id_jadwal = id;
        this.hari = nama_hari;
        this.mata_pelajaran = mapel;
        this.nama_guru = guru;
        this.nama_kelas = kelas;
        this.mulai = start;
        this.selesai = end;
        this.nama_ruangan = ruangan;
    }

    public String getId_jadwal() {
        return id_jadwal;
    }

    public void setId_jadwal(String id) {
        this.id_jadwal = id;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getMata_pelajaran() {
        return mata_pelajaran;
    }

    public void setMata_pelajaran(String mapel) {
        this.mata_pelajaran = mapel;
    }

    public String getNama_guru() {
        return nama_guru;
    }

    public void setNama_guru(String guru) {
        this.nama_guru = guru;
    }

    public String getNama_kelas() {
        return nama_kelas;
    }

    public void setNama_kelas(String kelas) {
        this.nama_kelas = kelas;
    }

    public String getMulai() {
        return mulai;
    }

    public void setMulai(String start) {
        this.mulai = start;
    }

    public String getSelesai() {
        return selesai;
    }

    public void setSelesai(String end) {
        this.selesai = end;
    }

    public String getNama_ruangan() {
        return nama_ruangan;
    }

    public void setNama_ruangan(String ruangan) {
        this.nama_ruangan = ruangan;
    }
}
