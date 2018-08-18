package poker;

import java.util.EnumMap;
import java.util.Map;

enum Suit {heart, diamond, spade, club};
enum Rank {two, three, four, five, six, seven, eight, nine, ten, jack, queen, king, ace};

public class Card implements Comparable<Card> {
	private Rank rank;
	private Suit suit;
	
	private static Map<Rank, Integer> rankToValue = new EnumMap<Rank, Integer>(Rank.class);
	static {
		rankToValue.put(Rank.two, 2);
		rankToValue.put(Rank.three, 3);
		rankToValue.put(Rank.four, 4);
		rankToValue.put(Rank.five, 5);
		rankToValue.put(Rank.six, 6);
		rankToValue.put(Rank.seven, 7);
		rankToValue.put(Rank.eight, 8);
		rankToValue.put(Rank.nine, 9);
		rankToValue.put(Rank.ten, 10);
		rankToValue.put(Rank.jack, 11);
		rankToValue.put(Rank.queen, 12);
		rankToValue.put(Rank.king, 13);
		rankToValue.put(Rank.ace, 14);
	}
	
	
	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public Rank getRank() {
		return rank;
	}
	
	@Override 
	public String toString() {
		return rankToString(rank) + suitToString(suit);
	}
	
	public static String rankToString(Rank rank) {
		switch (rank) {
		case two:
			return "2";
		case three:
			return "3";
		case four: 
			return "4";
		case five:
			return "5";
		case six:
			return "6";
		case seven:
			return "7";
		case eight:
			return "8";
		case nine:
			return "9";
		case ten:
			return "T";
		case jack:
			return "J";
		case queen:
			return "Q";
		case king:
			return "K";
		case ace:
			return "A";
		default:
			return "Error";
		}
	}
	
	public static String suitToString(Suit suit) {
		switch (suit) {
		case heart:
			return "h";
		case diamond:
			return "d";
		case spade:
			return "s";
		case club:
			return "c";
		default:
			return "Error";
		}
	}
	
	public int getNumericalRank() {
		return rankToValue.get(rank);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Card) {
			Card other = (Card) o;
			return rank == other.rank && suit == other.suit;
		}
		
		return false;
	}
	
	@Override
	public int compareTo(Card card) {
		return rankToValue.get(rank) - rankToValue.get(card.rank);
	}
}
