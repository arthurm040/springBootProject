package com.guess.data;

import com.guess.models.Rounds;

public interface RoundsDao
{
    //Rounds sendGuess(int id, String guess);
    Rounds sendGuess(Rounds rounds);

}
