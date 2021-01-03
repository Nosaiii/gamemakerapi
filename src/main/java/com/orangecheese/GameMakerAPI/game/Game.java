package com.orangecheese.GameMakerAPI.game;

import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

public class Game {
    private ModelService modelService;

    public Game(ModelService modelService) {
        this.modelService = modelService;

        createModels();
    }

    private void createModels() {
        // TODO: Create the game model and associated player models in the database
    }
}