package com.crud.controller;

public class TaskNotFoundException extends Exception{
    public TaskNotFoundException(final String message) {
        super(message);
    }
}
