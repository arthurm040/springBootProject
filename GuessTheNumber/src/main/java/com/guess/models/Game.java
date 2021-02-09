package com.guess.models;

import java.util.ArrayList;
import java.util.Random;

public class Game
{
    private int id;
    private String result;
    private boolean finished;
    
    
  //helper function that creates the secret value;
    public String numberSelector()
    {
        Random random = new Random();
        ArrayList<String> l = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            l.add(String.valueOf(i));
        
        String result = "";
        int position = 0;
        
        for (int i = 0; i < 4; i++)
        {
            position = random.nextInt(l.size());
            result += l.get(position);
            l.remove(position);
        }
        return result;
    }
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getResult()
    {
        return result;
    }
    public void setResult(String result)
    {
        this.result = result;
    }
    public boolean isFinished()
    {
        return finished;
    }
    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }
    
    public String publicView()
    {
        return "\"gameId:\" " + String.valueOf(getId()) + "\n\"finished:\" " +  isFinished();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if((this.id == ((Game)obj).id))
               return true;
        return false;
    }

    @Override
    public int hashCode()
    {
        return this.id;
    }
    
}