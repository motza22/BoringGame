package core;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import data.Map;
import data.MapTile;
import data.MapTile.TileType;
import display.JFrameApplication;
import display.SimpleRectangle;
import util.BoundaryRNG;

public class DungeonCrawl extends State implements KeyListener {
	private static JFrameApplication sJFrApp = null;
	private static int sVisibleRadius = 32;
	private static int sThreatRadius = 50;
	private Map mMapData;
	private int mPlayerX;
	private int mPlayerY;
	private int mGoalX;
	private int mGoalY;
	private int mPlayerSpeed;

	public DungeonCrawl() {
		mPlayerX = -1;
		mPlayerY = -1;
		mGoalX = -1;
		mGoalY = -1;
		mPlayerSpeed = 1;

		sJFrApp = JFrameApplication.GetInstance();
		mMapData = new Map();
		mMapData.LoadSave();
		if(!mMapData.IsSane() || !mMapData.IsPlayable()) {
			mMapData.GenerateNew(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
		}
		mMapData.Get().forEach((vector) -> vector.forEach((tile) -> {
			if(tile.mType == TileType.PLAYER) {
				mPlayerX = tile.mX;
				mPlayerY = tile.mY;
			} else if(tile.mType == TileType.GOAL) {
				mGoalX = tile.mX;
				mGoalY = tile.mY;
			}
		}));
	}

	private void EndGame() {
		mMapData.Save();
		Game.GetInstance().PopPush(1, new ViewMap());
	}

	private void TakeTurn() {
		if(mPlayerX == mGoalX && mPlayerY == mGoalY) {
			EndGame();
		} else {
			boolean playerAlive = true;
			int nodeMinX = mMapData.CheckWidth(mPlayerX - sThreatRadius);
			int nodeMaxX = mMapData.CheckWidth(mPlayerX + sThreatRadius);
			int nodeMinY = mMapData.CheckHeight(mPlayerY - sThreatRadius);
			int nodeMaxY = mMapData.CheckHeight(mPlayerY + sThreatRadius);

			for(int i = nodeMinX; i <= nodeMaxX; i++) {
				for(int j = nodeMinY; j <= nodeMaxY; j++) {
					if(Math.sqrt(Math.pow((mPlayerX - i), 2) + Math.pow((mPlayerY - j), 2)) < sVisibleRadius) {
						if(mMapData.GetTile(i, j).mType == TileType.ENEMY) {
							int newX = mMapData.CheckWidth(BoundaryRNG.Range(i-1, i+1));
							int newY = mMapData.CheckHeight(BoundaryRNG.Range(j-1, j+1));
							if(mMapData.MoveTile(i, j, newX, newY, TileType.INACCESSIBLE, TileType.GOAL, TileType.ENEMY)) {
								if(mPlayerX == newX && mPlayerY == newY) {
									playerAlive = false;
									EndGame();
								}
							}
						}
					}
				}
			}

			if(playerAlive) {
				Show();
			}
		}
	}

	private void ShowVisibleArea() {
		int nodeMinX = mMapData.CheckWidth(mPlayerX - sVisibleRadius);
		int nodeMaxX = mMapData.CheckWidth(mPlayerX + sVisibleRadius);
		int nodeMinY = mMapData.CheckHeight(mPlayerY - sVisibleRadius);
		int nodeMaxY = mMapData.CheckHeight(mPlayerY + sVisibleRadius);

		for(int i = nodeMinX; i <= nodeMaxX; i++) {
			for(int j = nodeMinY; j <= nodeMaxY; j++) {
				if(Math.sqrt(Math.pow((mPlayerX - i), 2) + Math.pow((mPlayerY - j), 2)) < sVisibleRadius) {
					MapTile tile = mMapData.GetTile(i, j);
					Color color = Color.BLACK;

					if(tile.mType == TileType.INACCESSIBLE) {
						color = Color.DARK_GRAY;
					} else if(tile.mType == TileType.EMPTY) {
						color = Color.LIGHT_GRAY;
					} else if(tile.mType == TileType.PLAYER) {
						color = Color.BLUE;
					} else if(tile.mType == TileType.ENEMY) {
						color = Color.RED;
					} else if(tile.mType == TileType.GOAL) {
						color = Color.YELLOW;
					}
					sJFrApp.AddSprite(new SimpleRectangle(tile.GetRectangle(), color));
				}
			}
		}
	}

	@Override
	public void Close() {
		sJFrApp.removeKeyListener(this);
		sJFrApp.Clear();
	}

	@Override
	public void Initialize() {
		sJFrApp.addKeyListener(this);
	}

	@Override
	public void Show() {
		sJFrApp.Clear();
		sJFrApp.setBackground(Color.BLACK);
		ShowVisibleArea();
	}

	@Override
	public void Update() {
		TakeTurn();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int newX;
		int newY;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			newY = mMapData.CheckHeight(mPlayerY - mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerX, mPlayerY, mPlayerX, newY) ) {
				mPlayerY = newY;
			}
			break;

		case KeyEvent.VK_S:
			newY = mMapData.CheckHeight(mPlayerY + mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerX, mPlayerY, mPlayerX, newY)) {
				mPlayerY = newY;
			}
			break;

		case KeyEvent.VK_A:
			newX = mMapData.CheckWidth(mPlayerX - mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerX, mPlayerY, newX, mPlayerY)) {
				mPlayerX = newX;
			}
			break;

		case KeyEvent.VK_D:
			newX = mMapData.CheckWidth(mPlayerX + mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerX, mPlayerY, newX, mPlayerY)) {
				mPlayerX = newX;
			}
			break;

		case KeyEvent.VK_K:
			EndGame();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
