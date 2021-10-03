package com.dns;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BlackListAnalyzer {

    public boolean analyzer(String domain) throws SQLException {
        String query = "SELECT url FROM list WHERE url = \"" + domain + "\";";
        Connection conn = ConnectionDB.getConnection();
        try(Statement stmt = conn.createStatement();){
        ResultSet result = stmt.executeQuery(query);
        return result.next();
        }
    }
}
