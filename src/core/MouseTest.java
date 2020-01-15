package core;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import ai.PathFinder;
import core_basic.TileColor;
import data.Map;
import data.MapTile;
import data.MapTile.TileType;
import data.MapUtil;
import data.Move;
import data.Position;
import display.JFrameApplication;
import display.SimpleRectangle;

public class MouseTest extends State implements MouseMotionListener, KeyListener {
	private static JFrameApplication sJFrApp = null;
	private Map mMap;
	private Position mPlayerPos;
	Vector<Move> mPath;

	MouseTest() {
		sJFrApp = JFrameApplication.GetInstance();
		mMap = MapUtil.LoadSave();
		if(mMap == null || !mMap.IsSane() || !mMap.IsPlayable()) {
			mMap = MapUtil.GenerateNew(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
		}
		mMap.Get().forEach((vector) -> vector.forEach((tile) -> {
			if(tile.mType == TileType.PLAYER) {
				mPlayerPos = new Position(tile.mPos);
			}
		}));
		mPath = new Vector<Move>();
	}

	private void ShowMap() {
		mMap.Get().forEach((vector) -> vector.forEach((tile) -> {
			if(tile.mType != TileType.INACCESSIBLE) {
				sJFrApp.AddSprite(new SimpleRectangle(tile.GetRectangle(), TileColor.GetColor(tile.mType)));
			}
		}));
	}

	@Override
	public void Close() {
		sJFrApp.removeKeyListener(this);
		sJFrApp.removeMouseMotionListener(this);
		sJFrApp.Clear();
	}

	@Override
	public void Initialize() {
		sJFrApp.addMouseMotionListener(this);
		sJFrApp.addKeyListener(this);
	}

	@Override
	public void Show() {
		sJFrApp.setBackground(Color.DARK_GRAY);
		sJFrApp.mSpriteLock.lock();
		sJFrApp.Clear();
		ShowMap();
		sJFrApp.mSpriteLock.unlock();
	}

	@Override
	public void Update() {
		Show();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		while(!mPath.isEmpty()) {
			mMap.SetTileType(mPath.firstElement().mNewPos, TileType.EMPTY);
			mPath.remove(mPath.firstElement());
		}
		Vector<Move> moveList = PathFinder.Calculate(new Map(mMap), mPlayerPos, new Position(e.getX() / MapTile.sTileSize, e.getY() / MapTile.sTileSize));
		if(moveList != null) {
			mPath = moveList;
			for(int i=0; i<mPath.size(); i++) {
				mMap.SetTileType(mPath.elementAt(i).mNewPos, TileType.HEATMAP);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_K:
			Game.GetInstance().Pop(1);
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
