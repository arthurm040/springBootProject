package com.guess.models;

import java.time.LocalDateTime;

public class Rounds
{
    private int id;
    private int gameId;
    private String result;
    private String entry;
    private int exact;
    private int partial;
    private LocalDateTime time;
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getGameId()
    {
        return gameId;
    }
    public void setGameId(int gameId)
    {
        this.gameId = gameId;
    }
    public String getResult()
    {
        return result;
    }
    public void setResult(String result)
    {
        this.result = result;
    }
    public String getEntry()
    {
        return entry;
    }
    public void setEntry(String entry)
    {
        this.entry = entry;
    }
    public int getExact()
    {
        return exact;
    }
    public void setExact(int exact)
    {
        this.exact = exact;
    }
    public int getPartial()
    {
        return partial;
    }
    public void setPartial(int partial)
    {
        this.partial = partial;
    }
    public LocalDateTime getTime()
    {
        return time;
    }
    public void setTime(LocalDateTime localDateTime)
    {
        this.time = localDateTime.withNano(0);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if((this.id == ((Rounds)obj).id) && this.gameId == ((Rounds)obj).gameId)
               return true;
        return false;
    }
}