package com.example.schedule.session;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.prefs.Preferences;

public class UserSession {

    public static void setUserSession(User user) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("nama_user", user.nama_user);
        preferences.put("kelas", user.kelas);
    }

    public static User getUserSession() {
        Preferences preferences = Preferences.userRoot();
        var nama_user = preferences.get("nama_user", "String");
        var kelas_siswa = preferences.get("kelas", "String");
        return new User(nama_user, kelas_siswa);
    }

}
