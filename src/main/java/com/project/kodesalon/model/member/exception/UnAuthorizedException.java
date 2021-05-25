package com.project.kodesalon.model.member.exception;

public class UnAuthorizedException extends IllegalStateException{
    public UnAuthorizedException(String message) {
        super(message);
    }
}
