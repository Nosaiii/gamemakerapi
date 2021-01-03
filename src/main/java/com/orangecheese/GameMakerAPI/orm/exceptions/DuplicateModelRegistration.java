package com.orangecheese.GameMakerAPI.orm.exceptions;

public class DuplicateModelRegistration extends Exception {
    public DuplicateModelRegistration() {
        super("Duplicate model registration attempted in the model facade");
    }
}