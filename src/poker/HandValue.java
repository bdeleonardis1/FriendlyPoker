package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

enum Hand {
	StraightFlush, Quads, FullHouse, Flush, Straight, Trips, TwoPair, Pair, High
};

public class HandValue implements Comparable<HandValue> {
	public static HandValue getHandValue(TwoCardHand hand, Card[] board) {
		return new HandValue(hand, board);
	}

	private static Map<Hand, Integer> handStrength = new EnumMap<>(Hand.class);
	static {
		handStrength.put(Hand.StraightFlush, 9);
		handStrength.put(Hand.Quads, 8);
		handStrength.put(Hand.FullHouse, 7);
		handStrength.put(Hand.Flush, 6);
		handStrength.put(Hand.Straight, 5);
		handStrength.put(Hand.Trips, 4);
		handStrength.put(Hand.TwoPair, 3);
		handStrength.put(Hand.Pair, 2);
		handStrength.put(Hand.High, 1);
	}

	private Hand hand;
	private Card[] bestFiveInOrder;
	private Map<Rank, Integer> rankFreqs;
	private Card[] sortedOnRank; // descending order
	private TwoCardHand holeCards;
	private Card[] board;

	private HandValue(TwoCardHand holeCards, Card[] board) {
		this.holeCards = holeCards;
		this.board = board;

		bestFiveInOrder = new Card[5];

		buildRankFreqs();
		buildSortedRanks();

		if (checkStraightFlush())
			return;
		if (checkQuads())
			return;
		if (checkFullHouse())
			return;
		if (checkFlush())
			return;
		if (checkStraight())
			return;
		if (checkTrips())
			return;
		if (checkTwoPair())
			return;
		if (checkPair())
			return;
		checkHigh();

	}

	private void buildRankFreqs() {
		rankFreqs = new EnumMap<Rank, Integer>(Rank.class);
		rankFreqs.put(holeCards.first.getRank(), 1);

		if (rankFreqs.containsKey(holeCards.second.getRank())) {
			rankFreqs.put(holeCards.second.getRank(), 2); // it's a pocket pair
		} else {
			rankFreqs.put(holeCards.second.getRank(), 1);
		}

		for (Card c : board) {
			if (rankFreqs.containsKey(c.getRank())) {
				rankFreqs.put(c.getRank(), rankFreqs.get(c.getRank()) + 1);
			} else {
				rankFreqs.put(c.getRank(), 1);
			}
		}

	}

	private void buildSortedRanks() {
		sortedOnRank = new Card[7];
		for (int i = 0; i < 5; i++) {
			sortedOnRank[i] = board[i];
		}
		sortedOnRank[5] = holeCards.first;
		sortedOnRank[6] = holeCards.second;

		Arrays.sort(sortedOnRank, Collections.reverseOrder());
	}

	// TODO: fix so that wheel works
	// TODO: behavior is undefined when there are two of the same rank:
	// 2s 2c 3s 4s 5s 6s Jc
	private boolean checkStraightFlush() {
		Card prev = sortedOnRank[0];
		int count = 1;
		int lastIndex = -1;
		for (int i = 1; i < sortedOnRank.length; i++) {
			if (prev.getNumericalRank() - 1 == sortedOnRank[i].getNumericalRank()
					&& prev.getSuit() == sortedOnRank[i].getSuit()) {
				count++;
			} else {
				if (count >= 5) {
					lastIndex = i - 1;
				}
				count = 1;
			}
			prev = sortedOnRank[i];
		}

		if (count >= 5) {
			lastIndex = sortedOnRank.length - 1;
		}

		if (lastIndex != -1) {
			for (int i = 0; i < 5; i++) {
				bestFiveInOrder[4 - i] = sortedOnRank[lastIndex - i];
			}
			hand = Hand.StraightFlush;
		}

		return lastIndex != -1;
	}

	private boolean checkQuads() {
		Rank quad = null;
		for (Entry<Rank, Integer> entries : rankFreqs.entrySet()) {
			if (entries.getValue() == 4) {
				quad = entries.getKey();
			}
		}

		if (quad != null) {
			hand = Hand.Quads;
			int i = 0;
			for (Suit suit : Suit.values()) {
				bestFiveInOrder[i] = new Card(quad, suit);
				i++;
			}

			i = 0;
			while (sortedOnRank[i].getRank() == quad) {
				i++;
			}
			System.out.println(Arrays.toString(sortedOnRank));
			bestFiveInOrder[4] = sortedOnRank[i];
		}

		return quad != null;
	}

	private boolean checkFullHouse() {
		Rank[] ranks = Rank.values();
		Rank trips = null, pair = null;
		for (int i = 0; i < ranks.length; i++) {
			Rank rank = ranks[i];
			if (rankFreqs.containsKey(rank)) {
				if (rankFreqs.get(rank) == 3) {
					if (trips == null) {
						trips = rank;
					} else {
						pair = trips;
						trips = rank;
					}
				} else if (rankFreqs.get(rank) == 2) {
					pair = rank;
				}
			}
		}

		if (trips != null && pair != null) {
			hand = Hand.FullHouse;
			System.out.println("HREE");
			bestFiveInOrder[0] = getIthRank(holeCards, board, trips, 0);
			bestFiveInOrder[1] = getIthRank(holeCards, board, trips, 1);
			bestFiveInOrder[2] = getIthRank(holeCards, board, trips, 2);
			bestFiveInOrder[3] = getIthRank(holeCards, board, pair, 0);
			bestFiveInOrder[4] = getIthRank(holeCards, board, pair, 1);
		}

		return trips != null && pair != null;
	}

	private boolean checkFlush() {
		Map<Suit, Integer> suitCount = new EnumMap<>(Suit.class);
		for (Suit suit : Suit.values()) {
			suitCount.put(suit, 0);
		}
		
		for (Card c : sortedOnRank) {
			Suit suit = c.getSuit();
			suitCount.put(suit, suitCount.get(suit) + 1);
		}
		
		for (Entry<Suit, Integer> freqs : suitCount.entrySet()) {
			if (freqs.getValue() >= 5) {
				hand = Hand.Flush;
				int i = 0;
				for (Card c : sortedOnRank) {
					if (c.getSuit() == freqs.getKey()) {
						bestFiveInOrder[i] = c;
						i++;
					}
					
					if (i == 5) {
						break;
					}
				}
			}
		}
		
		return hand == Hand.Flush;
	}

	// TODO: fix wheel
	private boolean checkStraight() {
		Card prev = sortedOnRank[0];
		int count = 1;
		int lastIndex = -1;
		for (int i = 1; i < sortedOnRank.length; i++) {
			if (prev.getNumericalRank() - 1 == sortedOnRank[i].getNumericalRank()) {
				count++;
			} else {
				if (count >= 5) {
					lastIndex = i - 1;
				}
				count = 1;
			}
			prev = sortedOnRank[i];
		}

		if (count >= 5) {
			lastIndex = sortedOnRank.length - 1;
		}

		if (lastIndex != -1) {
			for (int i = 0; i < 5; i++) {
				bestFiveInOrder[4 - i] = sortedOnRank[lastIndex - i];
			}
			hand = Hand.Straight;
		}

		return lastIndex != -1;
	}

	private boolean checkTrips() {
		Rank trip = null;
		for (Entry<Rank, Integer> entries : rankFreqs.entrySet()) {
			if (entries.getValue() == 3) {
				trip = entries.getKey();
			}
		}

		if (trip != null) {
			hand = Hand.Trips;
			for (int j = 0; j < 3; j++) {
				bestFiveInOrder[j] = getIthRank(holeCards, board, trip, j);
			}

			int i = 0;
			while (sortedOnRank[i].getRank() == trip) {
				i++;
			}
			bestFiveInOrder[3] = sortedOnRank[i];
			i++;
			while (sortedOnRank[i].getRank() == trip) {
				i++;
			}
			bestFiveInOrder[4] = sortedOnRank[i];
		}

		return trip != null;
	}

	private boolean checkTwoPair() {
		Rank lower = null, higher = null;
		Rank[] ranks = Rank.values();
		for (int i = ranks.length - 1; i >= 0; i--) {
			Rank rank = ranks[i];
			if (rankFreqs.containsKey(rank) && rankFreqs.get(rank) == 2) {
				if (higher == null) {
					higher = rank;
				} else {
					lower = rank;
					break;
				}
			}
		}
		
		if (lower != null && higher != null) {
			hand = Hand.TwoPair;
			bestFiveInOrder[0] = getIthRank(holeCards, board, higher, 0);
			bestFiveInOrder[1] = getIthRank(holeCards, board, higher, 1);
			bestFiveInOrder[2] = getIthRank(holeCards, board, lower, 0);
			bestFiveInOrder[3] = getIthRank(holeCards, board, lower, 1);
			
			int i = 0;
			while (sortedOnRank[i].getRank() == lower || sortedOnRank[i].getRank() == higher) {
				i++;
			}
			bestFiveInOrder[4] = sortedOnRank[i];
		}
		
		return lower != null && higher != null;
	}

	private boolean checkPair() {
		Rank pair = null;
		for (Entry<Rank, Integer> freq : rankFreqs.entrySet()) {
			if (freq.getValue() == 2) {
				pair = freq.getKey();
			}
		}
		
		if (pair != null) {
			hand = Hand.Pair;
			bestFiveInOrder[0] = getIthRank(holeCards, board, pair, 0);
			bestFiveInOrder[1] = getIthRank(holeCards, board, pair, 1);
			
			int i = 0;
			for (int c = 2; c < 5; c++) {
				while (sortedOnRank[i].getRank() == pair) {
					i++;
				}
				bestFiveInOrder[c] = sortedOnRank[i];
				i++;
			}
		}
		
		return pair != null;
	
	}

	private boolean checkHigh() {
		hand = Hand.High;
		for (int i = 0; i < 5; i++) {
			bestFiveInOrder[i] = sortedOnRank[i];
		}
		return true;
	}

	public int compareTo(HandValue otherHand) {
		// TODO: Cleanse this (don't make multiple calls to get)
		if (handStrength.get(hand) != handStrength.get(otherHand.hand)) {
			return handStrength.get(hand) - handStrength.get(otherHand.hand);
		}

		for (int i = 0; i < 5; i++) {
			int compareCard = bestFiveInOrder[i].compareTo(otherHand.bestFiveInOrder[i]);
			if (compareCard != 0) {
				return compareCard;
			}
		}

		return 0;
	}

	public Hand getHand() {
		return hand;
	}

	public Card[] getOrderedCards() {
		return bestFiveInOrder;
	}

	private Card getIthRank(TwoCardHand holeCards, Card[] board, Rank r, int i) {
		i++;
		List<Card> cards = new ArrayList<Card>();
		cards.add(holeCards.first);
		cards.add(holeCards.second);
		for (Card card : board) {
			cards.add(card);
		}

		int count = 0;

		for (Card card : cards) {
			if (card.getRank() == r) {
				count++;
			}

			if (count == i) {
				return card;
			}
		}

		return null;
	}
}
