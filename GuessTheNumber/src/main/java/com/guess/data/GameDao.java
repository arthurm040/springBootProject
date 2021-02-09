package com.guess.data;

import java.util.List;

import com.guess.models.Game;
import com.guess.models.Rounds;

public interface GameDao
{
    Game startGame();
    
    List<Game> getAllGames();
    
    Game findGameById(int id);
    
    List<Rounds> getRounds(int id);
    
    Game updateGameStatus(int id);
    
    void deleteGame(int id);
}
