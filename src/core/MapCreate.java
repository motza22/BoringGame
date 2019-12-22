package core;

import java.awt.Color;

import data.Map;
import data.MapTile.TileType;
import display.JFrameApplication;
import display.SimpleRectangle;
import display.Terminal;

public class MapCreate extends State {
	private static JFrameApplication mJFrApp = null;
	private static final data.MenuOption sOptionHeader = new data.MenuOption("Map Create\n\n", 0);
	private static final data.MenuOption sOptionGenerateMap = new data.MenuOption("1. Generate New Map\n", 1);
	private static final data.MenuOption sOptionExit = new data.MenuOption("2. Exit\n", 2);
	private Map mMapData;

	private void ShowMap() {
		mJFrApp.Clear();
		mMapData.Get().forEach((vector) -> vector.forEach((tile) -> {
			Color color = Color.WHITE;

			if(tile.mType == TileType.INACCESSIBLE) {
				color = Color.BLACK;
			} else if(tile.mType == TileType.PLAYER) {
				color = Color.BLUE;
			} else if(tile.mType == TileType.ENEMY) {
				color = Color.RED;
			} else if(tile.mType == TileType.GOAL) {
				color = Color.YELLOW;
			}

			if(tile.mType != TileType.FREE) {
				mJFrApp.AddSprite(new SimpleRectangle(tile.GetRectangle(), color));
			}
		}));
	}

	@Override
	public void Close() {
		mJFrApp.Clear();
	}

	@Override
	public void HandleNotify(int aInputId) {
		if(aInputId == sOptionGenerateMap.mId) {
			mMapData.GenerateNew(JFrameApplication.WIDTH, JFrameApplication.HEIGHT);
			ShowMap();
		}
		else { /* if(aInput == sOptionExit.mId) */
			Game.GetInstance().Pop(1);
		}
	}

	@Override
	public void Initialize() {
		mJFrApp = JFrameApplication.GetInstance();
		mMapData = new Map();
		// todo:
		// mMapData.LoadSave();
		mMapData.GenerateNew(JFrameApplication.WIDTH, JFrameApplication.HEIGHT);
	}

	@Override
	public void OnDisplay() {
		ShowMap();
		Terminal.DisplayMenu(sOptionHeader.mString);
		Terminal.DisplayMenuWResponse(sOptionGenerateMap.mString + sOptionExit.mString, sOptionExit.mId);
	}

	@Override
	public void Update() {
	}
}
