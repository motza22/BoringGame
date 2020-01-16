package core;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import core_assets.Bullet;
import core_assets.Enemy;
import core_assets.TileColor;
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
	public final ReentrantLock mMapLock = new ReentrantLock();
	private static JFrameApplication sJFrApp = null;
	private static int sVisibleRadius = 32;
	private static int sThreatRadius = 50;
	private Map mMap;
	private Position mPlayerPos;
	private Position mGoalPos;
	private int mPlayerSpeed = 1;
	private Vector<Bullet> mBullets;
	private Vector<Enemy> mEnemies;
	private boolean mMoveEnemies;

	public DungeonCrawl() {
		mMapLock.lock();
		sJFrApp = JFrameApplication.GetInstance();
		mMap = MapUtil.LoadSave();
		if(mMap == null || !mMap.IsSane() || !mMap.IsPlayable()) {
			mMap = MapUtil.GenerateNew(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
		}
		mMap.Get().forEach(vector -> vector.forEach(tile -> {
			if(tile.mType == TileType.PLAYER) {
				mPlayerPos = new Position(tile.mPos);
			} else if(tile.mType == TileType.GOAL) {
				mGoalPos = new Position(tile.mPos);
			}
		}));
		mBullets = new Vector<Bullet>();
		mEnemies = new Vector<Enemy>();
		FindEnemies();
		mMoveEnemies = true;
		mMapLock.unlock();
	}

	private void EndGame() {
		mMapLock.lock();
		MapUtil.Save(mMap);
		mMapLock.unlock();
		Game.GetInstance().PopPush(1, new ViewMap());
	}

	private void MovePlayer(Position aPos) {
		mMapLock.lock();
		if(mMap.MoveTile(mPlayerPos, aPos, TileType.INACCESSIBLE, TileType.BULLET)) {
			mPlayerPos = new Position(aPos);
			FindEnemies();
		}
		mMapLock.unlock();
	}

	private void AddBullet(Position aPos, Direction aDir) {
		mMapLock.lock();
		if(mMap.GetTileType(aPos) == TileType.ENEMY) {
			RemoveEnemyAtPosition(aPos);
		}
		if(mMap.TryMoveTile(mPlayerPos, aPos, TileType.INACCESSIBLE, TileType.BULLET, TileType.GOAL)) {
			mMap.SetTileType(aPos, TileType.BULLET);
			mBullets.add(new Bullet(aPos, aDir));
		}
		mMapLock.unlock();
	}

	private void MoveBullets() {
		mMapLock.lock();
		for(int i=mBullets.size()-1; i>=0; i--) {
			if(mBullets.elementAt(i).IsMoving()) {
				if(mMap.GetTileType(mBullets.elementAt(i).GetNextPosition(mMap)) == TileType.ENEMY) {
					RemoveEnemyAtPosition(mBullets.elementAt(i).GetNextPosition(mMap));
				}
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
		boolean isPlayerAlive = true;
		for(int i=0; i<mEnemies.size(); i++) {
			mEnemies.elementAt(i).UpdateTarget(mPlayerPos);
			mEnemies.elementAt(i).ExecuteMove();
			if(mEnemies.elementAt(i).GetPosition().Compare(mPlayerPos)) {
				isPlayerAlive = false;
				EndGame();
				break;
			}
		}
		if(isPlayerAlive) {
			mMap.SetTileType(mPlayerPos, TileType.PLAYER);
		}
		mMapLock.unlock();
	}

	private void FindEnemies() {
		mMapLock.lock();
		Area threatArea = new Area(mPlayerPos.mX, mPlayerPos.mY, sThreatRadius);
		for(int i = threatArea.mMinPos.mX; i <= threatArea.mMaxPos.mX; i++) {
			for(int j = threatArea.mMinPos.mY; j <= threatArea.mMaxPos.mY; j++) {
				if(threatArea.CheckCircle(i, j) && i == mMap.CheckWidth(i) && j == mMap.CheckHeight(j)) {
					Position pos = new Position(i, j);
					if(mMap.GetTileType(pos) == TileType.ENEMY) {
						if(mEnemies.isEmpty()) {
							mEnemies.add(new Enemy(pos, mMap));
						} else {
							boolean found = false;
							for(int k=0; k<mEnemies.size(); k++) {
								if(mEnemies.elementAt(k).GetPosition().Compare(pos)) {
									found = true;
								}
							}
							if(!found) {
								mEnemies.add(new Enemy(pos, mMap));
							}
						}
					}
				}
			}
		}
		mMapLock.unlock();
	}

	private void FinishTurn() {
		mMapLock.lock();
		if(mPlayerPos.mX == mGoalPos.mX && mPlayerPos.mY == mGoalPos.mY) {
			EndGame();
		} else {
			mMap.Get().forEach(vector -> vector.forEach(tile -> {
				if(tile.mType == TileType.PLAYER) {
					Show();
					return;
				}
			}));
		}
		mMapLock.unlock();
	}

	private void RemoveEnemyAtPosition(Position aPos) {
		for(int i=0; i<mEnemies.size(); i++) {
			if(mEnemies.elementAt(i).GetPosition().Compare(aPos)) {
				mEnemies.remove(i);
				break;
			}
		}
	}

	private void ShowVisibleArea() {
		mMapLock.lock();
		Area visibleArea = new Area(mPlayerPos.mX, mPlayerPos.mY, sVisibleRadius);
		for(int i = visibleArea.mMinPos.mX; i <= visibleArea.mMaxPos.mX; i++) {
			for(int j = visibleArea.mMinPos.mY; j <= visibleArea.mMaxPos.mY; j++) {
				if(visibleArea.CheckCircle(i, j) && i == mMap.CheckWidth(i) && j == mMap.CheckHeight(j)) {
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
		if(mMoveEnemies) {
			MoveEnemies();
			mMoveEnemies = false;
		} else {
			mMoveEnemies = true;
		}
		FinishTurn();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Position newPos = new Position(mPlayerPos.mX, mPlayerPos.mY);
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			newPos.mY = mMap.CheckHeight(mPlayerPos.mY - mPlayerSpeed);
			MovePlayer(newPos);
			break;

		case KeyEvent.VK_S:
			newPos.mY = mMap.CheckHeight(mPlayerPos.mY + mPlayerSpeed);
			MovePlayer(newPos);
			break;

		case KeyEvent.VK_A:
			newPos.mX = mMap.CheckWidth(mPlayerPos.mX - mPlayerSpeed);
			MovePlayer(newPos);
			break;

		case KeyEvent.VK_D:
			newPos.mX = mMap.CheckWidth(mPlayerPos.mX + mPlayerSpeed);
			MovePlayer(newPos);
			break;

		case KeyEvent.VK_UP:
			newPos.mY = mMap.CheckHeight(mPlayerPos.mY - mPlayerSpeed);
			AddBullet(newPos, Direction.BOW);
			break;

		case KeyEvent.VK_DOWN:
			newPos.mY = mMap.CheckHeight(mPlayerPos.mY + mPlayerSpeed);
			AddBullet(newPos, Direction.STERN);
			break;

		case KeyEvent.VK_LEFT:
			newPos.mX = mMap.CheckWidth(mPlayerPos.mX - mPlayerSpeed);
			AddBullet(newPos, Direction.PORT);
			break;

		case KeyEvent.VK_RIGHT:
			newPos.mX = mMap.CheckWidth(mPlayerPos.mX + mPlayerSpeed);
			AddBullet(newPos, Direction.STARBOARD);
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
