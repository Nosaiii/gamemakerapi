package com.orangecheese.GameMakerAPI.models.mappers;

import com.orangecheese.GameMakerAPI.models.PlayerInfo;
import com.orangecheese.GameMakerAPI.orm.model.IModelMapper;
import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInfoMapper implements IModelMapper {
    @Override
    public Model Map(ModelService modelService, ResultSet resultSet) throws SQLException {
        return new PlayerInfo(modelService, resultSet);
    }
}
