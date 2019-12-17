package core;

import display.Terminal;

public class MainMenu extends State {
	private static final data.MenuOption sOptionHeader = new data.MenuOption("Main Menu\n\n", 0);	
	private static final data.MenuOption sOptionPlay = new data.MenuOption("1. Play Game\n", 1);	
	private static final data.MenuOption sOptionGenerate = new data.MenuOption("2. Generate New Map\n", 2);	
	private static final data.MenuOption sOptionExit = new data.MenuOption("3. Exit\n", 3);	
	
	public MainMenu() {
	}
	
	@Override
	public void Close() {
	}
	
	@Override
	protected void Initialize() {
		Terminal.DisplayMenu(sOptionHeader.mString);
		Terminal.DisplayMenuWResponse(sOptionPlay.mString + sOptionGenerate.mString + sOptionExit.mString, sOptionExit.mId);
	}
	
	@Override
	public void ProcessInput(int aInput) {
		if(aInput == sOptionPlay.mId) {
			Game.GetInstance().PushState(new DungeonCrawl());
		}
//		else if(aInput == sOptionGenerate.mId) {
//		}
		else { /* if(aInput == sOptionExit.mId) */
			Game.GetInstance().PopState();
		}
	}

	@Override
	public void Update() {
	}
}
