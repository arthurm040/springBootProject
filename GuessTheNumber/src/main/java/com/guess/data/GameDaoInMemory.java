package com.guess.data;

import java.util.ArrayList;
import java.util.List;

import com.guess.models.Game;
import com.guess.models.Rounds;


public class GameDaoInMemory implements GameDao
{
    private static final List<Game> games = new ArrayList<>();
    private List<Rounds> rounds;
    
    @Override
    public Game startGame()
    {
        Game game = new Game();
        int nextId = games.stream()
                .mapToInt(i -> i.getId()).max().orElse(0) + 1;
        
        game.setId(nextId);
        game.setFinished(false);
        game.setResult(game.numberSelector());
        games.add(game);
        return game;
    }

    @Override
    public List<Game> getAllGames()
    {
        return games;
    }

    @Override
    public Game findGameById(int id)
    {
        return games.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Rounds> getRounds(int id)
    {
        return rounds;
    }

    @Override
    public Game updateGameStatus(int id)
    {
        for (Game game : games)
        {
            if (game.getId() == id)
            {
                game.setFinished(true);
                return game;
            }
        }
        return null;
    }

    @Override
    public void deleteGame(int id)
    {
        // TODO Auto-generated method stub 
    }

}
