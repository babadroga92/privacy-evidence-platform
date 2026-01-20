package io.privacy.evidenceplatform.user;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String email){
        super("User not found for email:  " + email);
    }
}
