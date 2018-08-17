package poker;

import java.util.Random;

public class PlayableDeck implements Deck {
	private Card[] cards;
	private int index;
	
	private PlayableDeck() {
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
		
		// TODO: change to whatever shuffling we're doing
		shuffle();
	}
	
	public static Deck getShuffledDeck() {
		return new PlayableDeck();
	}
	
	public Card dealCard() {
		return cards[index++];
	}
	
	// Destructively shuffles an array of cards
	private void shuffle() {
	    int index;
	    Card temp;
	    Random random = new Random();
	    for (int i = cards.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        temp = cards[index];
	        cards[index] = cards[i];
	        cards[i] = temp;
	    }
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
