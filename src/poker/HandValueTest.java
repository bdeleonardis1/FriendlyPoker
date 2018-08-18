package poker;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

// TODO: Fix all tests to follow expected, actual

public class HandValueTest {
	
	@Test
	public void testStraightFlush() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.three, Suit.spade);
		holeCards.second = new Card(Rank.five, Suit.spade);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.eight, Suit.club);
		board[1] = new Card(Rank.four, Suit.spade);
		board[2] = new Card(Rank.six, Suit.spade);
		board[3] = new Card(Rank.ace, Suit.diamond);
		board[4] = new Card(Rank.two, Suit.spade);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.StraightFlush);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0], new Card(Rank.six, Suit.spade));
		assertEquals(handCards[1], new Card(Rank.five, Suit.spade));
		assertEquals(handCards[2], new Card(Rank.four, Suit.spade));
		assertEquals(handCards[3], new Card(Rank.three, Suit.spade));
		assertEquals(handCards[4], new Card(Rank.two, Suit.spade));
	}
	
	@Test
	public void testQuads() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.six, Suit.spade);
		holeCards.second = new Card(Rank.six, Suit.club);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.eight, Suit.club);
		board[1] = new Card(Rank.four, Suit.spade);
		board[2] = new Card(Rank.six, Suit.diamond);
		board[3] = new Card(Rank.ace, Suit.diamond);
		board[4] = new Card(Rank.six, Suit.heart);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.Quads);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0].getRank(), Rank.six);
		assertEquals(handCards[1].getRank(), Rank.six);
		assertEquals(handCards[2].getRank(), Rank.six);
		assertEquals(handCards[3].getRank(), Rank.six);
		assertEquals(handCards[4], new Card(Rank.ace, Suit.diamond));
	}
	
	@Test
	public void testQuads2() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.six, Suit.spade);
		holeCards.second = new Card(Rank.six, Suit.club);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.two, Suit.club);
		board[1] = new Card(Rank.four, Suit.spade);
		board[2] = new Card(Rank.six, Suit.diamond);
		board[3] = new Card(Rank.two, Suit.diamond);
		board[4] = new Card(Rank.six, Suit.heart);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.Quads);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0].getRank(), Rank.six);
		assertEquals(handCards[1].getRank(), Rank.six);
		assertEquals(handCards[2].getRank(), Rank.six);
		assertEquals(handCards[3].getRank(), Rank.six);
		assertEquals(handCards[4], new Card(Rank.four, Suit.spade));
	}

}
