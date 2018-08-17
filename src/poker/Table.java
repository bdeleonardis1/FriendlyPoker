package poker;

import java.util.Random;
import java.util.Arrays;

enum Game {nlhe, plo};

public class Table {
	private int gameCode;

	private Game game;
	private int handed;
	private boolean startedPlay;

	private int buttonIndex;
	private double smallBlind, bigBlind;
	
	// TODO: change to players
	private String[] seats; 
	private double[] stacks;
	private int activePlayers;
	
	public Table(Game game, int handed, int gameCode, double bigBlind) {
		this.game = game;
		this.handed = handed;
		this.gameCode = gameCode;
		this.smallBlind = bigBlind / 2;
		this.bigBlind = bigBlind;
		
		if (handed >= 9) {
			handed = 9;
		} else if (handed >= 6) {
			handed = 6;
		} else {
			handed = 2;
		}
		this.handed = handed;
		this.seats = new String[handed];
		this.stacks = new double[handed];
		
		this.buttonIndex = -1;
		this.startedPlay = false;
		this.activePlayers = 0;
	}
	
	public boolean joinGame(String name, double buyin, int position) {
		if (position < 0 || position >= handed) {
			return false;
		}
		
		if (seats[position] != null) {
			return false;
		}
		
		seats[position] = name;
		stacks[position] = buyin;
		
		activePlayers++;
		return true;
	}
	
	public double leaveGame(String player, int position) {
		if (position < 0 || position >= handed) {
			throw new IllegalArgumentException("Position must be between 0 and " + handed);
		}
		
		if (player == null) {
			throw new IllegalArgumentException("The player cannot be null.");
		}
		
		if (!player.equals(seats[position])) {
			throw new IllegalArgumentException("The player provided does not match the player in the seat");
		}
		
		seats[position] = null;
		double stack = stacks[position];
		stacks[position] = 0;
		
		activePlayers--;
		return stack;
	}
	
	public boolean removeFromStack(int position, String player, double amount) {
		if (position < 0 || position >= handed) {
			return false;
		}
		
		if (player == null || !player.equals(seats[position])) {
			return false;
		}
		
		if (amount > stacks[position]) {
			return false;
		}
		
		stacks[position] -= amount;
		return true;
	}
	
	public boolean addToStack(int position, String player, double amount) {
		if (position < 0 || position >= handed) {
			return false;
		}
		
		if (player == null || !player.equals(seats[position])) {
			return false;
		}
		
		stacks[position] += amount;
		return true;
	}
	
	public void startGame() {
		if (activePlayers >= 2) {
			startedPlay = true;
			buttonIndex = 0;
		}
	}
	
	public int getGameCode() {
		return gameCode;
	}
	
	public Game getGame() {
		return game;
	}
	
	public int getHanded() {
		return handed;
	}
	
	public boolean getStartedPlay() {
		return startedPlay;
	}
	
	public double getSmallBlind() {
		return smallBlind;
	}
	
	public double getBigBlind() {
		return bigBlind;
	}
	
	public int getButtonIndex() {
		return buttonIndex;
	}
	
	public String[] getSeats() {
		return seats.clone();
	}
	
	public double getStack(int position) {
		if (position < 0 || position >= handed || seats[position] == null) {
			throw new IllegalArgumentException("Not a valid position.");
		}
		
		return stacks[position];
	}
	
	public void playHand() {
		GamePlayer handPlayer = new NLHEHandPlayer(this, PlayableDeck.getShuffledDeck());
		handPlayer.playHand();
		buttonIndex = nextPlayerIndex(seats, buttonIndex);
	}
	
	public static int nextPlayerIndex(String[] players, int index) {		
		int currIndex = (index+1) % players.length;
		while (players[currIndex] == null) {
			currIndex = (currIndex+1) % players.length;
		}
		
		return currIndex;
	}
	
	public String getPlayer(int index) {
		return seats[index];
	}
}
 