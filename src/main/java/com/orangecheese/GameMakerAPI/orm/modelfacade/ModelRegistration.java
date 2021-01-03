package com.orangecheese.GameMakerAPI.orm.modelfacade;

import com.orangecheese.GameMakerAPI.orm.model.IModelMapper;
import com.orangecheese.GameMakerAPI.orm.model.Model;

public class ModelRegistration {
    private final IModelMapper mapper;
    private final IModelFactory<? extends Model> factory;
    private final String tableName;

    public ModelRegistration(IModelMapper mapper, IModelFactory<? extends Model> factory, String tableName) {
        this.mapper = mapper;
        this.factory = factory;
        this.tableName = tableName;
    }

    public IModelMapper getMapper() {
        return mapper;
    }

    public IModelFactory<? extends Model> getFactory() {
        return factory;
    }

    public String getTableName() {
        return tableName;
    }
}
