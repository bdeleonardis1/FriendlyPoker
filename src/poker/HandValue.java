package poker;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

enum Hand {StraightFlush, Quads, FullHouse, Flush, Straight, Trips, TwoPair, Pair, High};

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
	private Card[] orderedCards;
	private Map<Rank, Integer> rankFreqs;
	private Card[] sortedOnRank;
	private TwoCardHand holeCards;
	private Card[] board;
	
	private HandValue(TwoCardHand holeCards, Card[] board) {
		this.holeCards = holeCards;
		this.board = board;
		
		orderedCards = new Card[5];
		
		buildRankFreqs();
		buildSortedRanks();
		
		if (checkStraightFlush()) return;
		if (checkQuads()) return;
		if (checkFullHouse()) return;
		if (checkFlush()) return;
		if (checkStraight()) return;
		if (checkTrips()) return;
		if (checkTwoPair()) return;
		if (checkPair()) return;
		checkHigh();
		
	}
	
	private void buildRankFreqs() {
		rankFreqs = new EnumMap<Rank, Integer>(Rank.class);
		rankFreqs.put(holeCards.first.getRank(), 1);
		if (rankFreqs.containsKey(holeCards.second.getRank())) {
			rankFreqs.put(holeCards.second.getRank(), 2); // it's a pocket pair
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
	
	private boolean checkStraightFlush() {
		Card prev = sortedOnRank[0];
		int count = 1;
		int lastIndex = -1;
		for (int i = 1; i < sortedOnRank.length; i++) {
			System.out.println("prev: " + prev.getNumericalRank());
			System.out.println("sortedRanks[i]" + sortedOnRank[i].getNumericalRank());
			if (prev.getNumericalRank() - 1 == sortedOnRank[i].getNumericalRank()
					&& prev.getSuit() == sortedOnRank[i].getSuit()) {
				count++;
			} else {
				System.out.println("Else");
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
				orderedCards[4 - i] = sortedOnRank[lastIndex - i];
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
				orderedCards[i] = new Card(quad, suit);
				i++;
			}
			
			i = 0;
			while (sortedOnRank[i].getRank() == quad) {
				i++;
			}
			System.out.println(Arrays.toString(sortedOnRank));
			orderedCards[4] = sortedOnRank[i];
		}		
		
		return quad != null;
	}
	
	private boolean checkFullHouse() {
		return false;
	}
	
	private boolean checkFlush() {
		return false;
	}
	
	private boolean checkStraight() {
		return false;
	}
	
	private boolean checkTrips() {
		return false;
	}
	
	private boolean checkTwoPair() {
		return false;
	}
	
	private boolean checkPair() {
		return false;
	}
	
	private boolean checkHigh() {
		return true;
	}
	
	public int compareTo(HandValue otherHand) {
		// TODO: Cleanse this (don't make multiple calls to get)
		if (handStrength.get(hand) != handStrength.get(otherHand.hand)) {
			return handStrength.get(hand) - handStrength.get(otherHand.hand);
		}
		
		for (int i = 0; i < 5; i++) {
			int compareCard = orderedCards[i].compareTo(otherHand.orderedCards[i]);
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
		return orderedCards;
	}
}
