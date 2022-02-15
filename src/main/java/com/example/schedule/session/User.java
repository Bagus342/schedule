package com.example.schedule.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    public String nama_user;
    public String kelas;

    public User(String nama_user, String kelas) {
        this.nama_user = nama_user;
        this.kelas = kelas;
    }
}
