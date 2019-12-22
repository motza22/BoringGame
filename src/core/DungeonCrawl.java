package core;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import data.Area;
import data.Map;
import data.MapTile;
import data.Position;
import data.MapTile.TileType;
import display.JFrameApplication;
import display.SimpleRectangle;
import util.BoundaryRNG;

public class DungeonCrawl extends State implements KeyListener {
	private static JFrameApplication sJFrApp = null;
	private static int sVisibleRadius = 32;
	private static int sThreatRadius = 50;
	private Map mMapData;
	private Position mPlayerPos = new Position(-1, -1);
	private Position mGoalPos = new Position(-1, -1);
	private int mPlayerSpeed  = 1;

	public DungeonCrawl() {
		sJFrApp = JFrameApplication.GetInstance();
		mMapData = new Map();
		mMapData.LoadSave();
		if(!mMapData.IsSane() || !mMapData.IsPlayable()) {
			mMapData.GenerateNew(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
		}
		mMapData.Get().forEach((vector) -> vector.forEach((tile) -> {
			if(tile.mType == TileType.PLAYER) {
				mPlayerPos.mX = tile.mX;
				mPlayerPos.mY = tile.mY;
			} else if(tile.mType == TileType.GOAL) {
				mGoalPos.mX = tile.mX;
				mGoalPos.mY = tile.mY;
			}
		}));
	}

	private void EndGame() {
		mMapData.Save();
		Game.GetInstance().PopPush(1, new ViewMap());
	}

	private void TakeTurn() {
		if(mPlayerPos.mX == mGoalPos.mX && mPlayerPos.mY == mGoalPos.mY) {
			EndGame();
		} else {
			boolean playerAlive = true;
			Area threatArea = new Area(mPlayerPos.mX, mPlayerPos.mY, sThreatRadius);
			for(int i = threatArea.mMinPos.mX; i <= threatArea.mMaxPos.mX; i++) {
				for(int j = threatArea.mMinPos.mY; j <= threatArea.mMaxPos.mY; j++) {
					if(i == mMapData.CheckWidth(i) && j == mMapData.CheckHeight(j) && threatArea.CheckCircle(i, j)) {
						if(mMapData.GetTile(i, j).mType == TileType.ENEMY) {
							int newX = mMapData.CheckWidth(BoundaryRNG.Range(i-1, i+1));
							int newY = mMapData.CheckHeight(BoundaryRNG.Range(j-1, j+1));
							if(mMapData.MoveTile(i, j, newX, newY, TileType.INACCESSIBLE, TileType.GOAL, TileType.ENEMY)) {
								if(mPlayerPos.mX == newX && mPlayerPos.mY == newY) {
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
		Area visibleArea = new Area(mPlayerPos.mX, mPlayerPos.mY, sVisibleRadius);
		for(int i = visibleArea.mMinPos.mX; i <= visibleArea.mMaxPos.mX; i++) {
			for(int j = visibleArea.mMinPos.mY; j <= visibleArea.mMaxPos.mY; j++) {
				if(i == mMapData.CheckWidth(i) && j == mMapData.CheckHeight(j) && visibleArea.CheckCircle(i, j)) {
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
		Position newPos = new Position(mPlayerPos.mX, mPlayerPos.mY);
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			newPos.mY = mMapData.CheckHeight(mPlayerPos.mY - mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerPos.mX, mPlayerPos.mY, mPlayerPos.mX, newPos.mY) ) {
				mPlayerPos.mY = newPos.mY;
			}
			break;

		case KeyEvent.VK_S:
			newPos.mY = mMapData.CheckHeight(mPlayerPos.mY + mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerPos.mX, mPlayerPos.mY, mPlayerPos.mX, newPos.mY)) {
				mPlayerPos.mY = newPos.mY;
			}
			break;

		case KeyEvent.VK_A:
			newPos.mX = mMapData.CheckWidth(mPlayerPos.mX - mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerPos.mX, mPlayerPos.mY, newPos.mX, mPlayerPos.mY)) {
				mPlayerPos.mX = newPos.mX;
			}
			break;

		case KeyEvent.VK_D:
			newPos.mX = mMapData.CheckWidth(mPlayerPos.mX + mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerPos.mX, mPlayerPos.mY, newPos.mX, mPlayerPos.mY)) {
				mPlayerPos.mX = newPos.mX;
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
