package core;

import java.awt.Color;

import data.MapData;
import display.JFrameApplication;
import display.SimpleRectangle;
import display.Terminal;

public class MapCreate extends State {
	private static JFrameApplication mJFrApp = null;
	private static final data.MenuOption sOptionHeader = new data.MenuOption("Map Create\n\n", 0);	
	private static final data.MenuOption sOptionGenerateMap = new data.MenuOption("1. Generate New Map\n", 1);	
	private static final data.MenuOption sOptionExit = new data.MenuOption("2. Exit\n", 2);
	private MapData mMapData;
	
	private void DisplayMap() {
		mJFrApp.Clear();
		mMapData.Get().forEach((vector) -> vector.forEach((rect) -> mJFrApp.AddSprite(new SimpleRectangle(rect, Color.BLACK))));
	}

	@Override
	public void Close() {
		mJFrApp.Clear();
	}
	
	@Override
	public void HandleNotify(int aInputId) {
		if(aInputId == sOptionGenerateMap.mId) {
			mMapData.GenerateNew(JFrameApplication.WIDTH, JFrameApplication.HEIGHT);
			DisplayMap();
		}
		else { /* if(aInput == sOptionExit.mId) */
			Game.GetInstance().Pop(1);
		}
	}

	@Override
	public void Initialize() {
		mJFrApp = JFrameApplication.GetInstance();
		mMapData = new MapData();
		mMapData.LoadSave();
	}
	
	@Override
	public void OnDisplay() {
		DisplayMap();
		Terminal.DisplayMenu(sOptionHeader.mString);
		Terminal.DisplayMenuWResponse(sOptionGenerateMap.mString + sOptionExit.mString, sOptionExit.mId);
	}

	@Override
	public void Update() {
	}
}
