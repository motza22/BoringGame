package core;

import java.awt.Color;
import java.awt.event.WindowEvent;

import display.JFrameApplication;
import display.SimpleRectangle;
import display.Terminal;
import util.BoundaryRNG;

public class MainMenu extends State {
	private static JFrameApplication sJFrApp = null;

	private static final data.MenuOption sOptionHeader = new data.MenuOption("Main Menu\n\n", 0);
	private static final data.MenuOption sOptionPlay = new data.MenuOption("1. Play Game\n", 1);
	private static final data.MenuOption sOptionGenerate = new data.MenuOption("2. View Map\n", 2);
	private static final data.MenuOption sOptionExit = new data.MenuOption("3. Exit\n", 3);

	public MainMenu() {
	}

	@Override
	public void Close() {
		sJFrApp.Clear();
	}

	@Override
	public void HandleNotify(int aInputId) {
		if(aInputId == sOptionPlay.mId) {
			Game.GetInstance().PopPush( 1, new DungeonCrawl());
		}
		else if(aInputId == sOptionGenerate.mId) {
			Game.GetInstance().Push(new ViewMap());
		}
		else { /* if(aInput == sOptionExit.mId) */
			sJFrApp.dispatchEvent(new WindowEvent(sJFrApp, WindowEvent.WINDOW_CLOSING));
			Game.GetInstance().Pop(1);
		}
	}

	@Override
	public void Initialize() {
		sJFrApp = JFrameApplication.GetInstance();
	}

	@Override
	public void Show() {
		sJFrApp.Clear();
		for(int i=0; i<100; i++) {
			int width = BoundaryRNG.Upper(32);
			int height = BoundaryRNG.Upper(32);
			int x = BoundaryRNG.Upper(JFrameApplication.WIDTH - width);
			int y = BoundaryRNG.Upper(JFrameApplication.HEIGHT - height);
			sJFrApp.AddSprite(new SimpleRectangle(x, y, width, height, Color.YELLOW));
		}

		Terminal.DisplayMenu(sOptionHeader.mString);
		Terminal.DisplayMenuWResponse(sOptionPlay.mString + sOptionGenerate.mString + sOptionExit.mString, sOptionExit.mId);
	}

	@Override
	public void Update() {
	}
}
