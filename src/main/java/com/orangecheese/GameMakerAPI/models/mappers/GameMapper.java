package com.orangecheese.GameMakerAPI.models.mappers;

import com.orangecheese.GameMakerAPI.models.GameData;
import com.orangecheese.GameMakerAPI.orm.model.IModelMapper;
import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements IModelMapper {
    @Override
    public Model Map(ModelService modelService, ResultSet resultSet) throws SQLException {
        return new GameData(modelService, resultSet);
    }
}
