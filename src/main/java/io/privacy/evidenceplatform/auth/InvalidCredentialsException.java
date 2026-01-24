package io.privacy.evidenceplatform.auth;

public class InvalidCredentialsException  extends RuntimeException{

    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}
