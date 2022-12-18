package com.example.cinematicketbookingapp.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format(
                DefaultExceptionMessages.RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE, resourceName,id));
    }
}
