package core;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import core_basic.Bullet;
import core_basic.Enemy;
import core_basic.TileColor;
import data.Area;
import data.Map;
import data.MapTile;
import data.MapTile.TileType;
import data.MapUtil;
import data.Move.Direction;
import data.Position;
import display.JFrameApplication;
import display.SimpleRectangle;

public class DungeonCrawl extends State implements KeyListener {
	private static JFrameApplication sJFrApp = null;
	private static int sVisibleRadius = 32;
	private static int sThreatRadius = 50;
	private Map mMap;
	private Position mPlayerPos;
	private Position mGoalPos;
	private int mPlayerSpeed = 1;
	private Vector<Bullet> mBullets;
	private Vector<Enemy> mEnemies;
	public final ReentrantLock mMapLock = new ReentrantLock();

	public DungeonCrawl() {
		mMapLock.lock();
		sJFrApp = JFrameApplication.GetInstance();
		mMap = MapUtil.LoadSave();
		if(mMap == null || !mMap.IsSane() || !mMap.IsPlayable()) {
			mMap = MapUtil.GenerateNew(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
		}
		mMap.Get().forEach((vector) -> vector.forEach((tile) -> {
			if(tile.mType == TileType.PLAYER) {
				mPlayerPos = new Position(tile.mPos);
			} else if(tile.mType == TileType.GOAL) {
				mGoalPos = new Position(tile.mPos);
			}
		}));
		mBullets = new Vector<Bullet>();
		mEnemies = new Vector<Enemy>();
		mMapLock.unlock();
	}

	private void EndGame() {
		mMapLock.lock();
		MapUtil.Save(mMap);
		mMapLock.unlock();
		Game.GetInstance().PopPush(1, new ViewMap());
	}

	private void MoveBullets() {
		mMapLock.lock();
		for(int i=mBullets.size()-1; i>=0; i--) {
			if(mBullets.elementAt(i).IsMoving()) {
				mBullets.elementAt(i).ExecuteMove(mMap);
			} else {
				mMap.SetTileType(mBullets.elementAt(i).GetPosition(), TileType.EMPTY);
				mBullets.remove(i);
			}
		}
		mMapLock.unlock();
	}

	private void MoveEnemies() {
		mMapLock.lock();
		Area threatArea = new Area(mPlayerPos.mX, mPlayerPos.mY, sThreatRadius);
		for(int i = threatArea.mMinPos.mX; i <= threatArea.mMaxPos.mX; i++) {
			for(int j = threatArea.mMinPos.mY; j <= threatArea.mMaxPos.mY; j++) {
				if(i == mMap.CheckWidth(i) && j == mMap.CheckHeight(j) && threatArea.CheckCircle(i, j)) {
					Position pos = new Position(i, j);
					if(mMap.GetTile(pos).mType == TileType.ENEMY) {
						TryAddEnemy(pos);
					}
				}
			}
		}
		mEnemies.forEach(enemy -> {
			enemy.UpdateTarget(mMap, mPlayerPos);
			enemy.ExecuteMove(mMap);
		});
		mMapLock.unlock();
	}

	private void TryAddEnemy(Position aPos) {
		mMapLock.lock();
		if(mEnemies.isEmpty()) {
			mEnemies.add(new Enemy(aPos));
		} else {
			boolean found = false;
			for(int i=0; i<mEnemies.size(); i++) {
				if(mEnemies.elementAt(i).GetPosition().Compare(aPos)) {
					found = true;
				}
			}
			if(!found) {
				mEnemies.add(new Enemy(aPos));
			}
		}
		mMapLock.unlock();
	}

	private void FinishTurn() {
		mMapLock.lock();
		if(mPlayerPos.mX == mGoalPos.mX && mPlayerPos.mY == mGoalPos.mY) {
			EndGame();
		} else {
			mMap.Get().forEach((vector) -> vector.forEach((tile) -> {
				if(tile.mType == TileType.PLAYER) {
					Show();
					return;
				}
			}));
		}
		mMapLock.unlock();
	}

	private void ShowVisibleArea() {
		mMapLock.lock();
		Area visibleArea = new Area(mPlayerPos.mX, mPlayerPos.mY, sVisibleRadius);
		for(int i = visibleArea.mMinPos.mX; i <= visibleArea.mMaxPos.mX; i++) {
			for(int j = visibleArea.mMinPos.mY; j <= visibleArea.mMaxPos.mY; j++) {
				if(i == mMap.CheckWidth(i) && j == mMap.CheckHeight(j) && visibleArea.CheckCircle(i, j)) {
					MapTile tile = mMap.GetTile(new Position(i, j));
					sJFrApp.AddSprite(new SimpleRectangle(tile.GetRectangle(), TileColor.GetColor(tile.mType)));
				}
			}
		}
		mMapLock.unlock();
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
		sJFrApp.setBackground(Color.BLACK);
		sJFrApp.mSpriteLock.lock();
		sJFrApp.Clear();
		ShowVisibleArea();
		sJFrApp.mSpriteLock.unlock();
	}

	@Override
	public void Update() {
		MoveBullets();
		MoveEnemies();
		FinishTurn();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Position newPos = new Position(mPlayerPos.mX, mPlayerPos.mY);
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			mMapLock.lock();
			newPos.mY = mMap.CheckHeight(mPlayerPos.mY - mPlayerSpeed);
			if(mMap.MoveTile(mPlayerPos, newPos, TileType.INACCESSIBLE, TileType.BULLET)) {
				mPlayerPos.mY = newPos.mY;
			}
			mMapLock.unlock();
			break;

		case KeyEvent.VK_S:
			mMapLock.lock();
			newPos.mY = mMap.CheckHeight(mPlayerPos.mY + mPlayerSpeed);
			if(mMap.MoveTile(mPlayerPos, newPos, TileType.INACCESSIBLE, TileType.BULLET)) {
				mPlayerPos.mY = newPos.mY;
			}
			mMapLock.unlock();
			break;

		case KeyEvent.VK_A:
			mMapLock.lock();
			newPos.mX = mMap.CheckWidth(mPlayerPos.mX - mPlayerSpeed);
			if(mMap.MoveTile(mPlayerPos, newPos, TileType.INACCESSIBLE, TileType.BULLET)) {
				mPlayerPos.mX = newPos.mX;
			}
			mMapLock.unlock();
			break;

		case KeyEvent.VK_D:
			mMapLock.lock();
			newPos.mX = mMap.CheckWidth(mPlayerPos.mX + mPlayerSpeed);
			if(mMap.MoveTile(mPlayerPos, newPos, TileType.INACCESSIBLE, TileType.BULLET)) {
				mPlayerPos.mX = newPos.mX;
			}
			mMapLock.unlock();
			break;

		case KeyEvent.VK_UP:
			mMapLock.lock();
			newPos.mY = mMap.CheckHeight(mPlayerPos.mY - mPlayerSpeed);
			if(mMap.TryMoveTile(mPlayerPos, newPos, TileType.INACCESSIBLE, TileType.BULLET)) {
				mMap.SetTileType(newPos, TileType.BULLET);
				mBullets.add(new Bullet(newPos, Direction.BOW));
			}
			mMapLock.unlock();
			break;

		case KeyEvent.VK_DOWN:
			mMapLock.lock();
			newPos.mY = mMap.CheckHeight(mPlayerPos.mY + mPlayerSpeed);
			if(mMap.TryMoveTile(mPlayerPos, newPos, TileType.INACCESSIBLE, TileType.BULLET)) {
				mMap.SetTileType(newPos, TileType.BULLET);
				mBullets.add(new Bullet(newPos, Direction.STERN));
			}
			mMapLock.unlock();
			break;

		case KeyEvent.VK_LEFT:
			mMapLock.lock();
			newPos.mX = mMap.CheckWidth(mPlayerPos.mX - mPlayerSpeed);
			if(mMap.TryMoveTile(mPlayerPos, newPos, TileType.INACCESSIBLE, TileType.BULLET)) {
				mMap.SetTileType(newPos, TileType.BULLET);
				mBullets.add(new Bullet(newPos, Direction.PORT));
			}
			mMapLock.unlock();
			break;

		case KeyEvent.VK_RIGHT:
			mMapLock.lock();
			newPos.mX = mMap.CheckWidth(mPlayerPos.mX + mPlayerSpeed);
			if(mMap.TryMoveTile(mPlayerPos, newPos, TileType.INACCESSIBLE, TileType.BULLET)) {
				mMap.SetTileType(newPos, TileType.BULLET);
				mBullets.add(new Bullet(newPos, Direction.STARBOARD));
			}
			mMapLock.unlock();
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
