package com.api.gamesapi.util;

import com.api.gamesapi.domain.model.Category;
import com.api.gamesapi.domain.model.Game;

public class GameCreator {

    public static Game createGameToBeSaved() {
        return Game.builder()
                .category(Category.RPG)
                .name("Jogo Teste")
                .build();
    }
    public static Game createValidGame() {
        return Game.builder()
                .id(1l)
                .category(Category.RPG)
                .name("Jogo Teste")
                .company(CompanyCreator.createValidCompany())
                .build();
    }
}
