package me.smwa.courses.exceptions;

public class SetUserException extends Exception {
    public SetUserException(int m) {
        super(String.valueOf(m));
    }
}
