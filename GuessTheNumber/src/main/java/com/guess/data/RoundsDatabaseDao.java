package com.guess.data;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.guess.models.Game;
import com.guess.models.Rounds;


@Repository
public class RoundsDatabaseDao implements RoundsDao
{
private final JdbcTemplate jdbc;
    
    @Autowired
    public RoundsDatabaseDao(JdbcTemplate jdbc)
    {
        this.jdbc = jdbc;
    }
    
    @Override
    @Transactional
    public Rounds sendGuess(Rounds rounds)
    {
        GameDaoDatabase g = new GameDaoDatabase(jdbc);
        Game game =  g.findGameByIdPlain(rounds.getGameId());
        
        Scorer scorer = findExactAndPartial(game.getResult(), rounds.getEntry());
        
        rounds.setExact(scorer.exact);
        rounds.setPartial(scorer.partial);
        rounds.setTime(LocalDateTime.now());
        
        final String INSERT_ROUNDS = "INSERT INTO rounds(gameId, entered, result, exact, partial, time)"
                + "Values(?,?,?,?,?,?)";
        
        jdbc.update(INSERT_ROUNDS, rounds.getGameId(),rounds.getEntry(),game.getResult(), rounds.getExact(),
                rounds.getPartial(), rounds.getTime());
        
        int temp = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        rounds.setId(temp);
        
        
        //this sets a game as finished
        if (scorer.exact == 4)
        {
            rounds.setResult(game.getResult());
            game.setFinished(true);
            g.updateGameStatus(game.getId());
        }
        else {
            rounds.setResult("****");
        }
        
        return rounds;
    }
    
    //helper function that determines cows and bulls
    //efficient because it can determine them in (O) N^2
    private static Scorer findExactAndPartial(String result, String entry) 
    {
        int exact = 0;
        int partial = 0;

        for (int i = 0; i < result.length(); i++)
        {
            for (int j = 0; j < result.length(); j++)
            {
                if ( result.charAt(i) == entry.charAt(j))
                {
                    if (i == j)
                        exact++;
                    else 
                        partial++; 
                }
            }
        }
        return new Scorer(exact, partial);
    }
   
    //helper class that helps with comparing the value entered to the set value
    public static class Scorer
    {
       int exact;
       int partial;
       
       public Scorer(int exact, int partial)
       {
        this.exact = exact;
        this.partial = partial;
       }  
    }
}
