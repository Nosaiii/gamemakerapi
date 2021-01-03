package com.orangecheese.GameMakerAPI.orm.exceptions;

import com.orangecheese.GameMakerAPI.orm.model.Model;

public class UndefinedModelException extends Exception {
    public UndefinedModelException(Class<? extends Model> modelClass) {
        super("An undefined model was attempted to be accessed (model: [" + modelClass.getSimpleName() + "])");
    }
}