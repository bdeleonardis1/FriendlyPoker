package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class NLHEHandPlayer implements GamePlayer {
	private Table table;
	private Deck deck;
	private TwoCardHand[] hands;
	private boolean[] active;
	private double[] committed;
	private Card[] board;
	private int button; // convenience
	private double pot;
	private int totalBet;
	private int lastAggressor;
	private int currIndex;
	private boolean bettingRoundStarted;

	public NLHEHandPlayer(Table table, Deck deck) {
		this.table = table;
		this.deck = deck;
		this.hands = new TwoCardHand[table.getSeats().length];
		this.active = new boolean[hands.length];
		this.committed = new double[hands.length];
		this.button = table.getButtonIndex();
		this.pot = 0;
		this.totalBet = 0;
		this.currIndex = -1;
		this.lastAggressor = button;
		this.board = new Card[5];
		bettingRoundStarted = true;
	}

	@Override
	public void playHand() {
//		int winner;
//		dealHand();
//		setupPreflop();
//		winner = betting(true);
//		if (winner != -1) {
//			
//			grantpot(winner);
//			return;
//		}
//		dealFlop();
//		setupOtherStreets();
//		winner = betting(false);
//		if (winner != -1) {
//			grantpot(winner);
//			return;
//		}
//		dealTurn();
//		setupOtherStreets();
//		winner = betting(false);
//		if (winner != -1) {
//			grantpot(winner);
//			return;
//		}
//		dealRiver();
//		setupOtherStreets();
//		winner = betting(false);
//		if (winner != -1) {
//			grantpot(winner);
//			return;
//		}
//		List<Integer> winners = showdown();
//		grantpot(winners);
		dealHand();
		setupPreflop();
		waitForInput();
	}

	private void dealHand() {
		int currIndex = Table.nextPlayerIndex(table.getSeats(), button);
		while (currIndex != button) {
			hands[currIndex] = new TwoCardHand();
			hands[currIndex].first = deck.dealCard();

			currIndex = Table.nextPlayerIndex(table.getSeats(), currIndex);
		}
		hands[button] = new TwoCardHand();
		hands[button].first = deck.dealCard();

		currIndex = Table.nextPlayerIndex(table.getSeats(), button);
		while (currIndex != button) {
			hands[currIndex].second = deck.dealCard();

			currIndex = Table.nextPlayerIndex(table.getSeats(), currIndex);

		}
		hands[button].second = deck.dealCard();
	}

	private void dealFlop() {
		deck.dealCard(); // burn
		board[0] = deck.dealCard();
		board[1] = deck.dealCard();
		board[2] = deck.dealCard();
		System.out.println("The board is now: " + Arrays.toString(board));
	}

	private void dealTurn() {
		deck.dealCard(); // burn
		board[3] = deck.dealCard();
		System.out.println("The board is now: " + Arrays.toString(board));
	}

	private void dealRiver() {
		deck.dealCard(); // burn
		board[4] = deck.dealCard();
		System.out.println("The board is now: " + Arrays.toString(board));
	}
	
	private void setupPreflop() {
		for (int i = 0; i < active.length; i++) {
			if (table.getPlayer(i) != null) {
				active[i] = true;
			}
		}
		
		int smallBlind = nextIndex(button), bigBlind = nextIndex(smallBlind);
		committed[smallBlind] = table.getSmallBlind();
		table.removeFromStack(smallBlind, table.getSmallBlind());
		committed[bigBlind] = table.getBigBlind();
		table.removeFromStack(bigBlind, table.getBigBlind());
		totalBet = bigBlind;
		currIndex = nextIndex(bigBlind);
		lastAggressor = nextIndex(bigBlind);
		pot += table.getSmallBlind() + table.getBigBlind();
		bettingRoundStarted = true;
	}
	
	private void setupOtherStreets() {
		currIndex = nextIndex(button);
		lastAggressor = currIndex;
	}

//	private int betting(boolean preflop) {		
////		do {
////			String[] input = waitForInput();
////			handleBet(input);
////		} while (currIndex != lastAggressor);
////		
////		for (int i = 0; i < committed.length; i++) {
////			committed[i] = 0;
////		}
////		totalBet = 0;
////		return nonshowdownWinner();
//	}
	
	private void waitForInput() {
		Scanner scan = new Scanner(System.in);

		System.out.println(table.getPlayer(currIndex) + ", what would you like to do? Your hand is: " + hands[currIndex]);
		if (doubleEquals(committed[currIndex], totalBet)) {
			System.out.println("C - check");
			System.out.println("B # - bet # amount");
		} else {
			System.out.println("F - fold");
			System.out.println("C - call " + (totalBet - committed[currIndex]));
			System.out.println("R # - raise to #");
		}
		String[] input = scan.nextLine().split(" ");
		handleBet(input);
	}
	
	private void handleBet(String[] input) {
		switch(input[0].charAt(0)) {
		case 'C':
			pot += totalBet - committed[currIndex];
			table.removeFromStack(currIndex, totalBet - committed[currIndex]);
			committed[currIndex] = totalBet;
			break;
		case 'B':
		case 'R':
			int amount = Integer.parseInt(input[1]);
			
			// TODO: make sure it's at least as big as the last raise
			if (amount > totalBet) {
				pot += amount - committed[currIndex];
				table.removeFromStack(currIndex, amount - committed[currIndex]);
				committed[currIndex] = amount;
				totalBet = amount;
				lastAggressor = currIndex;
			}
			break;
		case 'F':
			active[currIndex] = false;
			break;
		}
		currIndex = nextIndex(currIndex);
		
		if (currIndex == lastAggressor) {
			System.out.println("Now way?");
			for (int i = 0; i < committed.length; i++) {
				committed[i] = 0;
			}
			totalBet = 0;
			
			if (board[2] == null) {
				if (nonshowdownWinner()) {
					return;
				}
				dealFlop();
				setupOtherStreets();
			} else if (board[3] == null) {
				if (nonshowdownWinner()) {
					return;
				}
				dealTurn();
				setupOtherStreets();
			} else if (board[4] == null) {
				if (nonshowdownWinner()) {
					return;
				}
				dealRiver();
				setupOtherStreets();
			} else {
				showdown();
				return;
			}
		} 
		waitForInput();
	}
	
	private boolean nonshowdownWinner() {
		int winner = -1;
		int actives = 0;
		for (int i = 0; i < active.length; i++) {
			if (active[i]) {
				winner = i;
				actives += 1;
				
				if (actives > 1) {
					return false;
				}
			}
		}
		
		if (actives == 1) {
			grantpot(winner);
		}
		
		return actives == 1;
	}

	private void showdown() {
		HandValue[] handValues = new HandValue[hands.length];
		for (int i = 0; i < hands.length; i++) {
			if (active[i]) {
				System.out.println("Still active ma dude");
				handValues[i] = HandValue.getHandValue(hands[i], board); 
			}
		}
		
		List<Integer> winners = new ArrayList<>();
		HandValue max = getMax(handValues);
		for (int i = 0; i < handValues.length; i++) {
			if (handValues[i] != null && handValues[i].compareTo(max) == 0) {
				winners.add(i);
			}
		}
		
		grantpot(winners);
	}
	
	private void grantpot(int winner) {
		System.out.println("Congratulations, " + table.getPlayer(winner) + ", you won a " + pot + " chip pot.");
		table.addToStack(winner, pot);
	}

	private void grantpot(List<Integer> winners) {
		double winnings = pot / winners.size(); 
		String winnerList = "";
		for (int winner : winners) {
			table.addToStack(winner, winnings);
			winnerList += table.getPlayer(winner) + ", ";
		}
		winnerList = winnerList.substring(0, winnerList.length() - 2);
		System.out.println("Congratulations, " + winnerList + ", you won a " + winnings + " chip pot.");
	}

	// TODO: Delete
	private void printHands() {
		System.out.println("Button: " + button);
		for (int i = 0; i < hands.length; i++) {
			if (table.getPlayer(i) != null) {
				System.out.println("Position: " + i);
				System.out.println("Name: " + table.getPlayer(i));
				System.out.println("Hand:" + hands[i]);
				System.out.println();
			}
		}
	}

	public String getDeal() {
		dealHand();
		// String ret = "";
		// for (int i = 0; i < hands.length; i++) {
		// if (table.getPlayer(i) != null) {
		// ret += "Position: " + i + "\n";
		// ret += "Name: " + table.getPlayer(i) + "\n";
		// ret += "Hand:" + hands[i] + "\n";
		// ret += "\n";
		// }
		// }

		return Arrays.toString(hands);
	}

	private int nextIndex(int curr) {
		curr = (curr + 1) % hands.length;
		while (!active[curr]) {
			curr = (curr + 1) % hands.length;
		}
		
		return curr;
	}
	
	public static boolean doubleEquals(double a, double b) {
		return Math.abs(a - b) < 0.0001;
	}
	
	public static HandValue getMax(HandValue[] array) {
		HandValue max = array[0]; // TODO: Check null/0 length
		int i = 0;
		while (max == null) {
			i++;
			max = array[i];
		}
		for (HandValue hand : array) {
			if (hand != null && hand.compareTo(max) > 0) {
				max = hand;
			}
		}
		
		return max;
	}
}


//System.out.println("----------------------------------------------");
//System.out.println("currIndex: " + currIndex);
//System.out.println("active: " + Arrays.toString(active));
//System.out.println("committed: " + Arrays.toString(committed));
//System.out.println("seats: " + Arrays.toString(table.getSeats()));
//System.out.println("----------------------------------------------");
