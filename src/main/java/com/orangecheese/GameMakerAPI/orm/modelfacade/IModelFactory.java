package com.orangecheese.GameMakerAPI.orm.modelfacade;

import com.orangecheese.GameMakerAPI.orm.Query;
import com.orangecheese.GameMakerAPI.orm.exceptions.MissingModelConstructor;
import com.orangecheese.GameMakerAPI.orm.model.Model;

public interface IModelFactory<T extends Model> {
    String buildQuery();
    Query<T> all();
    T create() throws MissingModelConstructor;
}
