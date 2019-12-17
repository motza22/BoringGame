package core;

import display.JFrameApplication;

public class DungeonCrawl extends State {
	private static JFrameApplication mJFrApp = null;
	
	public DungeonCrawl() {
	}

	@Override
	public void Close() {
	}
	
	@Override
	protected void Initialize() {
		mJFrApp = JFrameApplication.StartApplication();
	}

	@Override
	public void ProcessInput(int aInput) {
	}

	@Override
	public void Update() {
	}
}
