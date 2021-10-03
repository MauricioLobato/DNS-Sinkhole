package com.dns;

import java.sql.Statement;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class RefreshDB {

    private RefreshDB() {
    }

    public static void refresh() throws SQLException, IOException {
        File file = new File(System.getProperty("user.dir") + "/list.txt");
        if (!file.createNewFile()) {
            Scanner scan = new Scanner(file);
            ArrayList<String> list = new ArrayList<>();
            while (scan.hasNextLine()) {
                list.add(scan.nextLine());
            }
            scan.close();
            try (Connection conn = ConnectionDB.getConnection();) {
                try (Statement stmt = conn.createStatement();) {
                    for (String s : list) {
                        String query = "INSERT OR IGNORE INTO list (url) VALUES (" + "\"" + s + "\");";
                        stmt.executeUpdate(query);
                    }
                }
            }
        }
    }
}
