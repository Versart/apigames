package com.api.gamesapi.util;

import com.api.gamesapi.domain.model.Category;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.model.Game;

import java.time.LocalDate;

public class GameCreator {

    public static Game createGameToBeSaved() {
        return Game.builder()
                .category(Category.RPG)
                .name("Jogo Teste")
                .build();
    }
}
