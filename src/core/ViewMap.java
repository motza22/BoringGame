package core;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import core_basic.TileColor;
import data.Map;
import data.MapTile;
import data.MapTile.TileType;
import data.MapUtil;
import display.Button;
import display.JFrameApplication;
import display.SimpleRectangle;

public class ViewMap extends State implements MouseListener {
	private static JFrameApplication sJFrApp = null;
	private static final Button sNewButton = new Button(JFrameApplication.WIDTH - Button.sWidth - 25,
			JFrameApplication.HEIGHT - Button.sHeight - 25, "New");
	private static final Button sPlayButton = new Button( (JFrameApplication.WIDTH - Button.sWidth ) / 2,
			JFrameApplication.HEIGHT - Button.sHeight - 25, "Play");
	private static final Button sExitButton = new Button( 25,
			JFrameApplication.HEIGHT - Button.sHeight - 25, "Exit");
	private Map mMap;

	public ViewMap() {
		sJFrApp = JFrameApplication.GetInstance();
		mMap = MapUtil.LoadSave();
		if(mMap == null) {
			mMap = MapUtil.GenerateNew(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
		}
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
