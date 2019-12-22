package core;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import data.Map;
import data.MapTile;
import data.MapTile.TileType;
import display.JFrameApplication;
import display.SimpleRectangle;

public class DungeonCrawl extends State implements KeyListener {
	private static JFrameApplication sJFrApp = null;
	private static int sVisibleRadius = 22;
	private Map mMapData;
	private int mPlayerX;
	private int mPlayerY;
	private int mPlayerSpeed;

	public DungeonCrawl() {
		mPlayerX = 0;
		mPlayerY = 0;
		mPlayerSpeed = 1;
	}

	private void TakeTurn() {
		Show();
	}

	private void ShowVisibleArea() {
		int nodeMinX = mMapData.CheckWidth(mPlayerX - sVisibleRadius);
		int nodeMaxX = mMapData.CheckWidth(mPlayerX + sVisibleRadius);
		int nodeMinY = mMapData.CheckHeight(mPlayerY - sVisibleRadius);
		int nodeMaxY = mMapData.CheckHeight(mPlayerY + sVisibleRadius);

		for(int i = nodeMinX; i <= nodeMaxX; i++)
		{
			for(int j = nodeMinY; j <= nodeMaxY; j++)
			{
				if(Math.sqrt(Math.pow((mPlayerX - i), 2) + Math.pow((mPlayerY - j), 2)) < sVisibleRadius)
				{
					MapTile tile = mMapData.Get().elementAt(i).elementAt(j);
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
		sJFrApp = JFrameApplication.GetInstance();
		sJFrApp.addKeyListener(this);
		mMapData = new Map();
		mMapData.LoadSave();
		if(!mMapData.IsSane()) {
			mMapData.GenerateNew(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
		}
		mMapData.Get().forEach((vector) -> vector.forEach((tile) -> {
			if(tile.mType == TileType.PLAYER) {
				mPlayerX = tile.mX;
				mPlayerY = tile.mY;
			}
		}));
	}

	@Override
	public void Show() {
		sJFrApp.Clear();
		sJFrApp.setBackground(Color.BLACK);
		ShowVisibleArea();
	}

	@Override
	public void Update() {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int newX;
		int newY;
		boolean consumed = false;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			newY = mMapData.CheckHeight(mPlayerY - mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerX, mPlayerY, mPlayerX, newY) ) {
				mPlayerY = newY;
				consumed = true;
			}
			break;

		case KeyEvent.VK_S:
			newY = mMapData.CheckHeight(mPlayerY + mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerX, mPlayerY, mPlayerX, newY)) {
				mPlayerY = newY;
				consumed = true;
			}
			break;

		case KeyEvent.VK_A:
			newX = mMapData.CheckWidth(mPlayerX - mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerX, mPlayerY, newX, mPlayerY)) {
				mPlayerX = newX;
				consumed = true;
			}
			break;

		case KeyEvent.VK_D:
			newX = mMapData.CheckWidth(mPlayerX + mPlayerSpeed);
			if( mMapData.MoveTile(mPlayerX, mPlayerY, newX, mPlayerY)) {
				mPlayerX = newX;
				consumed = true;
			}
			break;

		case KeyEvent.VK_K:
			Game.GetInstance().PopPush( 1, new MainMenu());
			break;
		}
		if(consumed) {
			TakeTurn();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
