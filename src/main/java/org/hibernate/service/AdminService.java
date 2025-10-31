package org.hibernate.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AdminService {

    private static final String DB_PROPS_PATH = "src/main/resources/db.properties";

    // Validate Admin login using DB credentials
    public boolean login(String username, String password) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(DB_PROPS_PATH)) {
            props.load(fis);
            String dbUser = props.getProperty("hibernate.connection.username");
            String dbPass = props.getProperty("hibernate.connection.password");

            if (dbUser == null || dbPass == null) {
                System.out.println("⚠️ Could not read DB credentials from db.properties");
                return false;
            }

            if (dbUser.equals(username) && dbPass.equals(password)) {
                System.out.println("✅ Admin authenticated with DB credentials!");
                return true;
            } else {
                System.out.println("❌ Invalid Admin credentials.");
                return false;
            }

        } catch (IOException e) {
            System.out.println("⚠️ Error reading db.properties: " + e.getMessage());
            return false;
        }
    }
}
