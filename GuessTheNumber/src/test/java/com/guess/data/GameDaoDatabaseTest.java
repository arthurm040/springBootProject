package com.guess.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.guess.TestApplicationConfiguration;
import com.guess.models.Game;
import com.guess.models.Rounds;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoDatabaseTest extends TestCase
{
    @Autowired
    GameDaoDatabase gDao;
    
    @Autowired
    RoundsDatabaseDao rDao;

   //cleans out the database before executing a test
    // also test the delete method;
    @Before
    public void setUp() throws Exception
    {
       List<Game> list = gDao.getAllGames();
       
       for (Game game : list)
           gDao.deleteGame(game.getId());
    }

    @After
    public void tearDown() throws Exception
    {
        
    }

    //verify that created game is also saved in memory
    @Test
    public void testStartGame()
    {
        Game game = gDao.startGame();
        Game otherGame = new Game();
        otherGame.setId(game.getId());
        otherGame.setResult(game.getResult());
        otherGame.setFinished(game.isFinished());
        assertEquals(game, otherGame);
    }
    
    // test that 
    @Test
    public void testGetAllGames()
    {
        Game game = gDao.startGame();
        Game otherGame = gDao.startGame();
        
        List<Game> list = gDao.getAllGames();
        
        assertEquals(2, list.size());
        assertTrue(list.contains(game));
        assertTrue(list.contains(otherGame));
    }

    
    @Test
    public void testFindGameById()
    {
        Game game = gDao.startGame();
        Game otherGame = gDao.findGameById(game.getId());
        assertEquals(game, otherGame);
    }
    

    @Test
    public void testGetRounds()
    {
        Game game = gDao.startGame();
        Rounds round1 = new Rounds();
        round1.setGameId(game.getId());
        round1.setEntry("1234");
        rDao.sendGuess(round1);
        
        Rounds round2 = new Rounds();
        round2.setGameId(game.getId());
        round2.setEntry("1234");
        rDao.sendGuess(round2);
        
        List<Rounds> list = new ArrayList<>();;
        list.add(round1);
        list.add(round2);
        
        assertEquals(gDao.getRounds(game.getId()), list);
    }
    

    @Test
    public void testUpdateGameStatus()
    {
        Game game = gDao.startGame();
        gDao.updateGameStatus(game.getId());
        
        assertTrue(gDao.findGameById(game.getId()).isFinished());
    }
}
