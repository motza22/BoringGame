package core;

import java.awt.Color;
import java.util.Random;

import display.JFrameApplication;
import display.SimpleRectangle;
import display.Terminal;

public class MainMenu extends State {
	private static JFrameApplication mJFrApp = null;
	private static Random mRandom = new Random();
	
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
	public void Initialize() {
		mJFrApp = JFrameApplication.GetInstance();
		
		for(int i=0; i<100; i++) {
			int width = mRandom.nextInt(32);
	        int height = mRandom.nextInt(32);
	        int x = mRandom.nextInt(JFrameApplication.X - width);
	        int y = mRandom.nextInt(JFrameApplication.Y - height);
			mJFrApp.AddSprite(new SimpleRectangle(x, y, width, height, Color.RED));
		}
		
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
