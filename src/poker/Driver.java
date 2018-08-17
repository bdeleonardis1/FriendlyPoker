package poker;

public class Driver {

	public static void main(String[] args) {
		Table t = new Table(Game.nlhe, 6, 1, 2);
		t.joinGame("Brian", 200, 0);
		t.joinGame("Jesse", 200, 1);
		t.joinGame("Jake", 200, 2);
		t.startGame();
		NLHEHandPlayer player = new NLHEHandPlayer(t, new TestDeck());
		player.playHand();
	}

}
