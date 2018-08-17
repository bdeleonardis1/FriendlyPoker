package poker;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestDealing {
	
	@Test
	public void testDealing() {
		Table t = new Table(Game.nlhe, 6, 1, 2);
		t.joinGame("Brian", 200, 0);
		t.joinGame("Jesse", 200, 1);
		t.joinGame("Jake", 200, 2);
		t.startGame();
		NLHEHandPlayer player = new NLHEHandPlayer(t, new TestDeck());
		String expected = "[2s 3d, 2h 2c, 2d 3h, null, null, null]";
		String actual = player.getDeal();
		assertEquals(expected, actual);
	}
 }
