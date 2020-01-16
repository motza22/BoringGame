package core;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import core_assets.TileColor;
import data.GameStats;
import data.Map;
import data.MapTile;
import data.MapTile.TileType;
import data.MapUtil;
import display.Button;
import display.JFrameApplication;
import display.JString;
import display.SimpleRectangle;

public class ViewMap extends State implements MouseListener {
	private static JFrameApplication sJFrApp = null;
	private static final Button sNewButton = new Button(JFrameApplication.WIDTH - Button.sWidth - 25,
			JFrameApplication.HEIGHT - Button.sHeight - 25, "New");
	private static final Button sPlayButton = new Button( (JFrameApplication.WIDTH - Button.sWidth ) / 2,
			JFrameApplication.HEIGHT - Button.sHeight - 25, "Play");
	private static final Button sExitButton = new Button( 25,
			JFrameApplication.HEIGHT - Button.sHeight - 25, "Exit");
	private static JString sBulletsFired = new JString(25, JFrameApplication.HEIGHT - 150, "");
	private static JString sEnemiesKilled = new JString(25, JFrameApplication.HEIGHT - 125, "");
	private Map mMap;
	private GameStats mStats;

	public ViewMap() {
		sJFrApp = JFrameApplication.GetInstance();
		mMap = MapUtil.LoadSave();
		if(mMap == null) {
			mMap = MapUtil.GenerateNew(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
		}
		mStats = new GameStats();
		mStats.Load();
	}

	private void ShowMap() {
		mMap.Get().forEach(vector -> vector.forEach(tile -> {
			if(tile.mType != TileType.INACCESSIBLE) {
				sJFrApp.AddSprite(new SimpleRectangle(tile.GetRectangle(), TileColor.GetColor(tile.mType)));
			}
		}));
	}

	private void ShowStats() {
		sBulletsFired.SetText("Bullets Fired: " + mStats.mBulletsFired + ".");
		sJFrApp.AddSprite(sBulletsFired);
		sEnemiesKilled.SetText("Enemies Killed: " + mStats.mEnemiesKilled + ".");
		sJFrApp.AddSprite(sEnemiesKilled);
	}

	@Override
	public void Close() {
		sJFrApp.removeMouseListener(this);
		sJFrApp.Clear();
	}

	@Override
	public void Initialize() {
		sJFrApp.addMouseListener(this);
	}

	@Override
	public void Show() {
		sJFrApp.setBackground(Color.DARK_GRAY);
		sJFrApp.mSpriteLock.lock();
		sJFrApp.Clear();
		ShowMap();
		ShowStats();
		sJFrApp.AddSprite(sNewButton);
		sJFrApp.AddSprite(sPlayButton);
		sJFrApp.AddSprite(sExitButton);
		sJFrApp.mSpriteLock.unlock();
	}

	@Override
	public void Update() {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(sNewButton.CheckBounds(e.getX(), e.getY())) {
			mMap = MapUtil.GenerateNew(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
			Show();
		} else if(sPlayButton.CheckBounds(e.getX(), e.getY())) {
			Game.GetInstance().PopPush(1, new DungeonCrawl());
		} else if(sExitButton.CheckBounds(e.getX(), e.getY())) {
			Game.GetInstance().Pop(1);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
