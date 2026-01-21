package edu.aitu.oop3.exceptions;

public class MenuItemNotAvailableException extends RuntimeException {
    public MenuItemNotAvailableException(String message) {
        super(message);
    }
}
