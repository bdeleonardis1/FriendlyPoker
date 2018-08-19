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
	
	@Test
	public void testFullHouse() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.eight, Suit.spade);
		holeCards.second = new Card(Rank.nine, Suit.spade);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.nine, Suit.club);
		board[1] = new Card(Rank.four, Suit.spade);
		board[2] = new Card(Rank.six, Suit.spade);
		board[3] = new Card(Rank.eight, Suit.diamond);
		board[4] = new Card(Rank.eight, Suit.heart);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.FullHouse);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0].getRank(), Rank.eight);
		assertEquals(handCards[1].getRank(), Rank.eight);
		assertEquals(handCards[2].getRank(), Rank.eight);
		assertEquals(handCards[3].getRank(), Rank.nine);
		assertEquals(handCards[4].getRank(), Rank.nine);
	}
	
	@Test
	public void testStraight() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.three, Suit.spade);
		holeCards.second = new Card(Rank.five, Suit.spade);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.eight, Suit.club);
		board[1] = new Card(Rank.four, Suit.spade);
		board[2] = new Card(Rank.six, Suit.spade);
		board[3] = new Card(Rank.ace, Suit.diamond);
		board[4] = new Card(Rank.two, Suit.club);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.Straight);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0], new Card(Rank.six, Suit.spade));
		assertEquals(handCards[1], new Card(Rank.five, Suit.spade));
		assertEquals(handCards[2], new Card(Rank.four, Suit.spade));
		assertEquals(handCards[3], new Card(Rank.three, Suit.spade));
		assertEquals(handCards[4], new Card(Rank.two, Suit.club));
	}
	
	@Test
	public void testTrip() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.six, Suit.spade);
		holeCards.second = new Card(Rank.six, Suit.club);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.eight, Suit.club);
		board[1] = new Card(Rank.four, Suit.spade);
		board[2] = new Card(Rank.six, Suit.diamond);
		board[3] = new Card(Rank.ace, Suit.diamond);
		board[4] = new Card(Rank.three, Suit.heart);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.Trips);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0].getRank(), Rank.six);
		assertEquals(handCards[1].getRank(), Rank.six);
		assertEquals(handCards[2].getRank(), Rank.six);
		assertEquals(handCards[3], new Card(Rank.ace, Suit.diamond));
		assertEquals(handCards[4], new Card(Rank.eight, Suit.club));
	}
	
	@Test
	public void testTripTrips() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.six, Suit.spade);
		holeCards.second = new Card(Rank.six, Suit.club);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.ace, Suit.club);
		board[1] = new Card(Rank.ace, Suit.spade);
		board[2] = new Card(Rank.six, Suit.diamond);
		board[3] = new Card(Rank.ace, Suit.diamond);
		board[4] = new Card(Rank.three, Suit.heart);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.FullHouse);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0].getRank(), Rank.ace);
		assertEquals(handCards[1].getRank(), Rank.ace);
		assertEquals(handCards[2].getRank(), Rank.ace);
		assertEquals(handCards[3].getRank(), Rank.six);
		assertEquals(handCards[4].getRank(), Rank.six);
	}
	
	@Test
	public void testTwoPair() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.six, Suit.spade);
		holeCards.second = new Card(Rank.six, Suit.club);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.ace, Suit.club);
		board[1] = new Card(Rank.ace, Suit.spade);
		board[2] = new Card(Rank.three, Suit.diamond);
		board[3] = new Card(Rank.queen, Suit.diamond);
		board[4] = new Card(Rank.three, Suit.heart);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.TwoPair);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0].getRank(), Rank.ace);
		assertEquals(handCards[1].getRank(), Rank.ace);
		assertEquals(handCards[2].getRank(), Rank.six);
		assertEquals(handCards[3].getRank(), Rank.six);
		assertEquals(handCards[4].getRank(), Rank.queen);
	}
	
	@Test
	public void testPair() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.ace, Suit.spade);
		holeCards.second = new Card(Rank.six, Suit.club);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.ace, Suit.club);
		board[1] = new Card(Rank.king, Suit.spade);
		board[2] = new Card(Rank.three, Suit.diamond);
		board[3] = new Card(Rank.queen, Suit.diamond);
		board[4] = new Card(Rank.two, Suit.heart);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.Pair);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0].getRank(), Rank.ace);
		assertEquals(handCards[1].getRank(), Rank.ace);
		assertEquals(handCards[2].getRank(), Rank.king);
		assertEquals(handCards[3].getRank(), Rank.queen);
		assertEquals(handCards[4].getRank(), Rank.six);
	}
	
	@Test
	public void testHighCard() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.ace, Suit.spade);
		holeCards.second = new Card(Rank.six, Suit.club);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.nine, Suit.club);
		board[1] = new Card(Rank.king, Suit.spade);
		board[2] = new Card(Rank.three, Suit.diamond);
		board[3] = new Card(Rank.queen, Suit.diamond);
		board[4] = new Card(Rank.two, Suit.heart);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.High);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0].getRank(), Rank.ace);
		assertEquals(handCards[1].getRank(), Rank.king);
		assertEquals(handCards[2].getRank(), Rank.queen);
		assertEquals(handCards[3].getRank(), Rank.nine);
		assertEquals(handCards[4].getRank(), Rank.six);
	}
	
	@Test
	public void testFlush() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.ace, Suit.spade);
		holeCards.second = new Card(Rank.five, Suit.spade);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.eight, Suit.club);
		board[1] = new Card(Rank.four, Suit.spade);
		board[2] = new Card(Rank.six, Suit.spade);
		board[3] = new Card(Rank.ace, Suit.diamond);
		board[4] = new Card(Rank.two, Suit.spade);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.Flush);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0], new Card(Rank.ace, Suit.spade));
		assertEquals(handCards[1], new Card(Rank.six, Suit.spade));
		assertEquals(handCards[2], new Card(Rank.five, Suit.spade));
		assertEquals(handCards[3], new Card(Rank.four, Suit.spade));
		assertEquals(handCards[4], new Card(Rank.two, Suit.spade));
	}
	
	@Test
	public void testWheelStraight() {
		TwoCardHand holeCards = new TwoCardHand();
		holeCards.first = new Card(Rank.three, Suit.spade);
		holeCards.second = new Card(Rank.five, Suit.spade);
		Card[] board = new Card[5];
		board[0] = new Card(Rank.eight, Suit.club);
		board[1] = new Card(Rank.four, Suit.spade);
		board[2] = new Card(Rank.ten, Suit.spade);
		board[3] = new Card(Rank.ace, Suit.diamond);
		board[4] = new Card(Rank.two, Suit.club);
		HandValue v = HandValue.getHandValue(holeCards, board);
		assertEquals(v.getHand(), Hand.Straight);
		Card[] handCards = v.getOrderedCards();
		assertEquals(handCards[0], new Card(Rank.five, Suit.spade));
		assertEquals(handCards[1], new Card(Rank.four, Suit.spade));
		assertEquals(handCards[2], new Card(Rank.three, Suit.spade));
		assertEquals(handCards[3], new Card(Rank.two, Suit.club));
		assertEquals(handCards[4], new Card(Rank.ace, Suit.diamond));
	}

}
