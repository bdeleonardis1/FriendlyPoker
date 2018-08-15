package poker;

import java.util.Random;

public class Deck {
	private Card[] cards;
	private int index;
	
	private Deck() {
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
		shuffle(cards);
	}
	
	public static Deck getShuffledDeck() {
		return new Deck();
	}
	
	public Card dealCard() {
		return cards[index++];
	}
	
	// Destructively shuffles an array of cards
	// TODO: Fix!!!
	private static void shuffle(Card[] cards) {
		Random rng = new Random();
		
		for (int i = 0; i < cards.length; i++) {
			int newIndex = rng.nextInt(52);
			Card temp = cards[i];
			cards[i] = cards[newIndex];
			cards[newIndex] = temp;
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
