package core;

import display.Terminal;

public class MainMenu extends State {
	private static final display.MenuOption sOptionHeader = new display.MenuOption("Main Menu", 0);	
	private static final display.MenuOption sOptionPlay = new display.MenuOption("1. Play Game", 1);	
	private static final display.MenuOption sOptionGenerate = new display.MenuOption("2. Generate New Map", 2);	
	private static final display.MenuOption sOptionExit = new display.MenuOption("3. Exit", 3);	
	private boolean mIsInit = true;
	
	public MainMenu() {
	}
	
	@Override
	public void Close() {
	}
	
	@Override
	public void ProcessInput(int aInput) {
		if(aInput == sOptionPlay.mId) {
			Game.GetInstance().PushState(new DungeonCrawl());
		}
//		else if(aInput == sOptionGenerate.mId) {
//		}
//		else if(aInput == sOptionExit.mId) {
		else {
			Game.GetInstance().PopState();
		}
	}

	@Override
	public void Update() {
		if(mIsInit) {
			Terminal.DisplayMenuHeader(sOptionHeader);
			Terminal.DisplayMenuOptions(sOptionPlay, sOptionGenerate, sOptionExit);
			mIsInit = false;
		}
	}
}
