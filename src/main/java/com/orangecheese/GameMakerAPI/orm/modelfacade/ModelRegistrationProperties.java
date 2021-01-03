package com.orangecheese.GameMakerAPI.orm.modelfacade;

import com.orangecheese.GameMakerAPI.orm.model.IModelMapper;
import com.orangecheese.GameMakerAPI.orm.model.Model;

public class ModelRegistrationProperties {
    private Class<? extends Model> modelClass;
    private IModelMapper modelMapper;
    private String tableName;

    public ModelRegistrationProperties(Class<? extends Model> modelClass, IModelMapper modelMapper) {
        this.modelClass = modelClass;
        this.modelMapper = modelMapper;

        tableName = modelClass.getSimpleName().replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public ModelRegistrationProperties(Class<? extends Model> modelClass, String tableName, IModelMapper modelMapper) {
        this(modelClass, modelMapper);
        this.tableName = tableName;
    }

    public Class<? extends Model> getModelClass() {
        return modelClass;
    }

    public IModelMapper getModelMapper() {
        return modelMapper;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}