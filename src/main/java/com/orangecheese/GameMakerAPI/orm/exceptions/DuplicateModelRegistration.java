package com.orangecheese.GameMakerAPI.orm.exceptions;

import com.orangecheese.GameMakerAPI.orm.model.Model;

public class DuplicateModelRegistration extends Exception {
    public DuplicateModelRegistration(Class<? extends Model> modelClass) {
        super("An already-existing model was attempted to be registered (model: [" + modelClass.getSimpleName() + "])");
    }
}