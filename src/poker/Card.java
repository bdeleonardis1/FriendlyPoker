package poker;

enum Suit {heart, diamond, spade, club};
enum Rank {two, three, four, five, six, seven, eight, nine, ten, jack, queen, king, ace};

public class Card {
	private Rank rank;
	private Suit suit;
	
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
	public boolean equals(Object o) {
		Card otherCard = null;
		try {
			otherCard = (Card) o;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return false;
		}
		
		return o != null && otherCard.getSuit() == suit && otherCard.getRank() == rank;
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
}
