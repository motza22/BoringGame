package core;

public class DungeonCrawl extends State {
	
	public DungeonCrawl() {
		System.out.println("Hello World.");
	}

	@Override
	public void Close() {
	}

	@Override
	public void ProcessInput(int aInput) {
	}

	@Override
	public void Update() {
		Game.GetInstance().PopState();
	}
}
