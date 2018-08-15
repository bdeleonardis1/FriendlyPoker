package poker;

import java.util.Random;

enum Game {nlhe, plo};
enum Players {nine, six, two}

public class Table {
	private Game game;
	private Players players;
	private int gameCode; 
	
	private String[] seats; // TODO: change to players
	
	public Table(Game game, Players players, int gameCode) {
		this.game = game;
		this.players = players;
		this.gameCode = gameCode;
		
		
	}
}
