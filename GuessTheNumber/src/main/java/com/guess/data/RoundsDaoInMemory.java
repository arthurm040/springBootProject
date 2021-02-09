package com.guess.data;

import java.time.LocalDateTime;

import com.guess.models.Game;
import com.guess.models.Rounds;

public class RoundsDaoInMemory implements RoundsDao
{
    static int primarykey = 1;
    GameDaoInMemory g = new GameDaoInMemory();
    
    @Override
    public Rounds sendGuess(Rounds rounds)
    {
        Game game = g.findGameById(rounds.getGameId());
        Scorer scorer = findExactAndPartial(game.getResult(), rounds.getEntry());
        rounds.setId(primarykey);
        primarykey++;
        
        rounds.setExact(scorer.exact);
        rounds.setPartial(scorer.partial);
        rounds.setTime(LocalDateTime.now());
        return rounds;
    }
    
    private static Scorer findExactAndPartial(String result, String entry) 
    {
        int exact = 0;
        int partial = 0;
        
        System.out.println(result);

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