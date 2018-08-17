package poker;

import java.util.Arrays;
import java.util.Scanner;

public class NLHEHandPlayer implements GamePlayer {
	private Table table;
	private Deck deck;
	private TwoCardHand[] hands;
	private boolean[] active;
	private double[] committed;
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
	}

	private void dealTurn() {
	}

	private void dealRiver() {
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
			committed[bigBlind] = table.getBigBlind();
			totalBet = bigBlind;
			currIndex = nextIndex(bigBlind);
			lastAggressor = bigBlind;
			System.out.println("Bottom of if");
		} else {
			currIndex = nextIndex(button);
			lastAggressor = currIndex;
		}
		
		Scanner scan = new Scanner(System.in);
		
		while (currIndex != lastAggressor) {
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
				committed[currIndex] = totalBet;
				break;
			case 'B':
			case 'R':
				int amount = Integer.parseInt(input[1]);
				
				// TODO: make sure it's at least as big as the last raise
				if (amount > totalBet) {
					pot += amount - committed[currIndex];
					totalBet = amount;
					lastAggressor = currIndex;
				}
			case 'F':
				active[currIndex] = false;
			}
			currIndex = nextIndex(currIndex);
		}
		
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
		return -1;
	}

	private void grantpot(int winner) {
		//
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
	
	private boolean doubleEquals(double a, double b) {
		return Math.abs(a - b) < 0.0001;
	}
}
