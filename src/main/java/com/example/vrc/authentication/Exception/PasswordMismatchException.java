package com.example.vrc.authentication.Exception;
public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("Password and repeated password do not match");
    }
}