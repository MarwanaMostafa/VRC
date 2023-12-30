package com.example.vrc.authentication.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserPasswordEncryption {
    private static final PasswordEncoder passwordEndEncoder = new BCryptPasswordEncoder(12);

    public static String encodePassword(String password) {
        return passwordEndEncoder.encode(password);
    }

    public static boolean matchPasswords(String rowPassword, String encodedPassword) {
        return passwordEndEncoder.matches(rowPassword, encodedPassword);
    }
}
