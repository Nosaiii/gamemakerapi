package com.orangecheese.GameMakerAPI.orm.exceptions;

import com.orangecheese.GameMakerAPI.orm.model.Model;

public class ModelNotSyncedWithDatabaseException extends Exception {
    public ModelNotSyncedWithDatabaseException(Class<? extends Model> modelClass) {
        super("The given model is not synced with the database. Try to save the model to the database first (Model: [" + modelClass.getSimpleName() + "])");
    }
}
