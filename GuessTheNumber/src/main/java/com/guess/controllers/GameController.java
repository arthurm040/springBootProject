package com.guess.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.guess.data.GameDaoDatabase;
import com.guess.data.RoundsDatabaseDao;
import com.guess.models.Game;
import com.guess.models.Rounds;

@RestController
@RequestMapping("/game")
public class GameController
{
    private final GameDaoDatabase gameDao;
    private final RoundsDatabaseDao roundsDao;
    
    public GameController(GameDaoDatabase gameDao, RoundsDatabaseDao roundsDao)
    {
        this.gameDao = gameDao;
        this.roundsDao = roundsDao;
    }
    
    //starts a game and returns the game Object

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String StartGame()
    {
        return gameDao.startGame().publicView();
    }
    
    @PostMapping("/guess")
    public Rounds sendGuess(@RequestBody Rounds rounds)
    {
        return roundsDao.sendGuess(rounds);
    }
    
    @GetMapping
    public List<Game> getAllGames()
    {
        return gameDao.getAllGames();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/{id}")
    public ResponseEntity<Game> findGameById(@PathVariable int id)
    {
        Game result = gameDao.findGameById(id);
        if (result == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(result);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/{id}/rounds")
    public ResponseEntity<List<Rounds>> getRounds(@PathVariable int id)
    {
        List<Rounds> result = gameDao.getRounds(id);
        if(result == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(result); 
    }
}
