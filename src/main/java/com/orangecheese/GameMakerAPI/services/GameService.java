package com.orangecheese.GameMakerAPI.services;

import com.orangecheese.GameMakerAPI.models.Game;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

public class GameService {
    private Game game;

    public GameService(ModelService modelService) {

    }

    public GameService(ModelService modelService, Game game) {

    }
}