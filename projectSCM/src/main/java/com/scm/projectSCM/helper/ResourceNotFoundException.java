package com.scm.projectSCM.helper;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(){
        super("user not found");
    }

    public ResourceNotFoundException(String message){
        super(message);
    }

}
