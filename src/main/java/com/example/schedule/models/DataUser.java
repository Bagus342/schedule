package com.example.schedule.models;

public class DataUser {
    public String id;
    public String nama_user;
    public String nis;
    public String kelas;
    public String password;
    public String role;

    public DataUser (String id, String nama_user, String nis, String kelas, String password, String role) {
        this.id = id;
        this.nama_user = nama_user;
        this.nis = nis;
        this.kelas = kelas;
        this.password = password;
        this.role = role;
    }
    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
