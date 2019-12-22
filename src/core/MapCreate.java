package core;

import java.awt.Color;

import data.Map;
import data.MapTile;
import data.MapTile.TileType;
import display.JFrameApplication;
import display.SimpleRectangle;
import display.Terminal;

public class MapCreate extends State {
	private static JFrameApplication sJFrApp = null;
	private static final data.MenuOption sOptionHeader = new data.MenuOption("Map Create\n\n", 0);
	private static final data.MenuOption sOptionGenerateMap = new data.MenuOption("1. Generate New Map\n", 1);
	private static final data.MenuOption sOptionExit = new data.MenuOption("2. Exit\n", 2);
	private Map mMapData;

	private void ShowMap() {
		sJFrApp.Clear();
		sJFrApp.setBackground(Color.LIGHT_GRAY);
		mMapData.Get().forEach((vector) -> vector.forEach((tile) -> {
			Color color = Color.WHITE;

			if(tile.mType == TileType.INACCESSIBLE) {
				color = Color.DARK_GRAY;
			} else if(tile.mType == TileType.PLAYER) {
				color = Color.BLUE;
			} else if(tile.mType == TileType.ENEMY) {
				color = Color.RED;
			} else if(tile.mType == TileType.GOAL) {
				color = Color.YELLOW;
			}

			if(tile.mType != TileType.EMPTY) {
				sJFrApp.AddSprite(new SimpleRectangle(tile.GetRectangle(), color));
			}
		}));
	}

	@Override
	public void Close() {
		sJFrApp.Clear();
	}

	@Override
	public void HandleNotify(int aInputId) {
		if(aInputId == sOptionGenerateMap.mId) {
			mMapData.GenerateNew();
			OnDisplay();
		}
		else { /* if(aInput == sOptionExit.mId) */
			Game.GetInstance().Pop(1);
		}
	}

	@Override
	public void Initialize() {
		sJFrApp = JFrameApplication.GetInstance();
		mMapData = new Map(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
		// todo:
		// mMapData.LoadSave();
		mMapData.GenerateNew();
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
