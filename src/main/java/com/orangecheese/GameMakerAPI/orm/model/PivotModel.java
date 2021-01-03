package com.orangecheese.GameMakerAPI.orm.model;

import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class PivotModel<S extends Model, U extends Model> extends Model {
    public PivotModel(ModelService modelService, ResultSet resultSet) throws SQLException {
        super(modelService, resultSet);
    }

    public PivotModel(ModelService modelService) {
        super(modelService);
    }

    public <T extends Model> T get(Class<T> modelClass) {
        S left = getLeft();
        U right = getRight();

        if(modelClass.equals(left.getClass())) {
            return (T) left;
        } else if(modelClass.equals(right.getClass())) {
            return (T) right;
        }

        return null;
    }

    public abstract S getLeft();
    public abstract U getRight();
}