package com.orangecheese.GameMakerAPI.orm.exceptions;

import com.orangecheese.GameMakerAPI.orm.model.Model;

public class MissingModelConstructor extends Exception {
    public MissingModelConstructor(Class<? extends Model> model) {
        super("Missing a required create constructor for the given model (Model: [" + model.getSimpleName() + "])");
    }
}