package poker;

import java.util.Random;

public class TestDeck implements Deck {
	private Card[] cards;
	private int index;
	
	public TestDeck() {
		cards = new Card[52];
		index = 0;
		
		// Fills cards (unshuffled)
		int i = 0;
		for (Rank rank: Rank.values()) {
			for (Suit suit : Suit.values()) {
				cards[i] = new Card(rank, suit);
				i++;
			}
		}
		
	}
	
	public Card dealCard() {
		return cards[index++];
	}
	
	@Override
	public String toString() {
		String ret = "";
		for (Card c : cards) {
			ret += c.toString() + " ";
		}
		return ret;
	}
}
