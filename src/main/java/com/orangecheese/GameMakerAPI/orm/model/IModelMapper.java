package com.orangecheese.GameMakerAPI.orm.model;

import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IModelMapper {
    Model Map(ModelService modelService, ResultSet resultSet) throws SQLException;
}