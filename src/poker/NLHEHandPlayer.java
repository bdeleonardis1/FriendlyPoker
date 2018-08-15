package poker;

public class NLHEHandPlayer implements GamePlayer {
	Table table;
	
	public NLHEHandPlayer(Table table) {
		this.table = table;
	}

	@Override
	public void PlayHand() {
		System.out.println("Hello world");
	}

}
