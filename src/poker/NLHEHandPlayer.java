package poker;

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
	private int pot;
	private int totalBet;
	private int lastAggressor;

	public NLHEHandPlayer(Table table, Deck deck) {
		this.table = table;
		this.deck = deck;
		this.hands = new TwoCardHand[table.getSeats().length];
		this.active = new boolean[hands.length];
		this.committed = new double[hands.length];
		this.button = table.getButtonIndex();
		this.pot = 0;
		this.totalBet = 0;
		this.lastAggressor = button;
		this.board = new Card[5];
	}

	@Override
	public void playHand() {
		int winner;
		dealHand();
		System.out.println("Dealt the hand.");
		winner = betting(true);
		if (winner != -1) {
			grantpot(winner);
			return;
		}
		dealFlop();
		winner = betting(false);
		if (winner != -1) {
			grantpot(winner);
			return;
		}
		dealTurn();
		winner = betting(false);
		if (winner != -1) {
			grantpot(winner);
			return;
		}
		dealRiver();
		winner = betting(false);
		if (winner != -1) {
			grantpot(winner);
			return;
		}
		winner = showdown();
		grantpot(winner);
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
	}

	private void dealTurn() {
		deck.dealCard(); // burn
		board[3] = deck.dealCard();
	}

	private void dealRiver() {
		deck.dealCard(); // burn
		board[4] = deck.dealCard();
	}

	private int betting(boolean preflop) {
		int currIndex = -1;
		if (preflop) {
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
			lastAggressor = bigBlind;
			pot += table.getSmallBlind() + table.getBigBlind();
		} else {
			currIndex = nextIndex(button);
			lastAggressor = currIndex;
		}
		
		Scanner scan = new Scanner(System.in);
				
		do {
			System.out.println(table.getPlayer(currIndex) + ", what would you like to do?");
			if (doubleEquals(committed[currIndex], totalBet)) {
				System.out.println("C - check");
				System.out.println("B # - bet # amount");
			} else {
				System.out.println("F - fold");
				System.out.println("C - call " + (totalBet - committed[currIndex]));
				System.out.println("R # - raise to #");
			}
			String[] input = scan.nextLine().split(" ");
			
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
		} while (currIndex != lastAggressor);
		
		for (int i = 0; i < committed.length; i++) {
			committed[i] = 0;
		}
		totalBet = 0;
		return nonshowdownWinner();
	}
	
	private int nonshowdownWinner() {
		int winner = -1;
		int actives = 0;
		for (int i = 0; i < active.length; i++) {
			if (active[i]) {
				winner = i;
				actives += 1;
				
				if (actives > 1) {
					return -1;
				}
			}
		}
		
		return winner;
	}

	private int showdown() {
		List<Integer> activeHands = new LinkedList<>();
		for (int i = 0; i < active.length; i++) {
			if (active[i]) {
				activeHands.add(i);
			}
			
		}
		return getWinner(hands, activeHands, board);
	}

	private void grantpot(int winner) {
		System.out.println("Congratulations, " + table.getPlayer(winner) + ", you won a " + pot + " chip pot.");
		table.addToStack(winner, pot);
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
	
	public static int getWinner(TwoCardHand[] hands, List<Integer> activeHands, Card[] board) {
		Arrays.sort(board);
		
		return -1;
	}

}


//System.out.println("----------------------------------------------");
//System.out.println("currIndex: " + currIndex);
//System.out.println("active: " + Arrays.toString(active));
//System.out.println("committed: " + Arrays.toString(committed));
//System.out.println("seats: " + Arrays.toString(table.getSeats()));
//System.out.println("----------------------------------------------");
