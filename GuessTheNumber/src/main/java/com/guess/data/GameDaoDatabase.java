package com.guess.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.guess.models.Game;
import com.guess.models.Rounds;

@Repository
public class GameDaoDatabase implements GameDao
{
    private final JdbcTemplate jdbc;
    
    @Autowired
    public GameDaoDatabase(JdbcTemplate jdbc)
    {
        this.jdbc = jdbc;
    }
    
    @Override
    @Transactional
    public Game startGame()
    {
      Game game = new Game();
      game.setResult(game.numberSelector());
      final String INSERT_GAME = "INSERT INTO games(result) VALUES(?)";
      jdbc.update(INSERT_GAME, game.getResult());
      
      int id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
      
      game.setId(id);
      game.setFinished(false);
        
      return game;
    }
    
    public List<Game> getAllGames()
    {
        final String SELECT_ALL = "SELECT * FROM games";
        return jdbc.query(SELECT_ALL, new GameMapper());
    }

    public Game findGameById(int id)
    {
        try
        {
            final String SELECT_BY_ID = "SELECT * FROM games WHERE gameId = ?";
            return jdbc.queryForObject(SELECT_BY_ID, new GameMapper(), id);
        } catch (DataAccessException e)
        {
            return null;
        }
    }
    
    public Game findGameByIdPlain(int id)
    {
        try
        {
            final String SELECT_BY_ID = "SELECT * FROM games WHERE gameId = ?";
            return jdbc.queryForObject(SELECT_BY_ID, new GameMapperPlain(), id);
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    public List<Rounds> getRounds(int id)
    {
        try
        {
            final String GET_ROUNDS = "SELECT * FROM rounds WHERE gameId = ? ORDER BY time";
            return jdbc.query(GET_ROUNDS,new RoundsMapper(), id);
        } catch (DataAccessException e)
        {
            return null;
        }
    }
    
    //only updates status 
    @Override
    public Game updateGameStatus(int id)
    {
        Game game = jdbc.queryForObject("SELECT * FROM games Where gameId = ?",new GameMapper(),
                id);
        game.setFinished(true);
        jdbc.update("UPDATE games SET finished = true WHERE gameId = ?" , game.getId());
        
        return game;
    }
    
    
    @Override
    @Transactional
    public void deleteGame(int id)
    {
        final String DELETE_ROUNDS = "DELETE FROM rounds "
                + "WHERE gameId = ?";
        jdbc.update(DELETE_ROUNDS, id);
        final String DELETE_GAME = "DELETE FROM games "
                + "WHERE gameId = ?";
        jdbc.update(DELETE_GAME, id);
    }
    
    
    //this rowmapper hides games that are finished
    private static final class GameMapper implements RowMapper<Game>
    {
        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException
        {
            Game game = new Game();
            game.setId(rs.getInt("gameId"));
            
            if (rs.getBoolean("finished") == true)
                game.setResult(rs.getString("result"));
            else 
                game.setResult("****");
           
            game.setFinished(rs.getBoolean("finished"));
            return game;
        }
    }
    
    //returns the games with result not hidden
    private static final class GameMapperPlain implements RowMapper<Game>
    {
        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException
        {
            Game game = new Game();
            game.setId(rs.getInt("gameId"));
            game.setResult(rs.getString("result"));
            game.setFinished(rs.getBoolean("finished"));
            return game;
        }
    }
    
    public static final class RoundsMapper implements RowMapper<Rounds>
    {
        @Transactional
        @Override
        public Rounds mapRow(ResultSet rs, int rowNum) throws SQLException
        {     
            Rounds round = new Rounds();
            round.setId(rs.getInt("roundId"));
            round.setGameId(rs.getInt("gameId"));
            round.setEntry(rs.getString("entered"));
            round.setExact(rs.getInt("exact"));
            round.setPartial(rs.getInt("partial"));
            round.setTime((rs.getTimestamp("time").toLocalDateTime()));
            
            return round;
        }
    }
}