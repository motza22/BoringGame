package core;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import data.Map;
import data.MapTile;
import data.MapTile.TileType;
import display.Button;
import display.JFrameApplication;
import display.SimpleRectangle;

public class ViewMap extends State implements MouseListener {
	private static JFrameApplication sJFrApp = null;
	private static final Button sNewButton = new Button(JFrameApplication.WIDTH - Button.sWidth - 25,
			JFrameApplication.HEIGHT - Button.sHeight - 25, "New");
	private static final Button sPlayButton = new Button( (JFrameApplication.WIDTH - Button.sWidth) / 2,
			JFrameApplication.HEIGHT - Button.sHeight - 25, "Play");
	private static final Button sExitButton = new Button( 25,
			JFrameApplication.HEIGHT - Button.sHeight - 25, "Exit");
	private Map mMapData;

	public ViewMap() {
		sJFrApp = JFrameApplication.GetInstance();
		mMapData = new Map();
		mMapData.LoadSave();
	}

	private void ShowMap() {
		mMapData.Get().forEach((vector) -> vector.forEach((tile) -> {
			Color color = Color.BLACK;

			if(tile.mType == TileType.EMPTY) {
				color = Color.LIGHT_GRAY;
			} else if(tile.mType == TileType.PLAYER) {
				color = Color.BLUE;
			} else if(tile.mType == TileType.ENEMY) {
				color = Color.RED;
			} else if(tile.mType == TileType.GOAL) {
				color = Color.YELLOW;
			}

			if(tile.mType != TileType.INACCESSIBLE) {
				sJFrApp.AddSprite(new SimpleRectangle(tile.GetRectangle(), color));
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
		sJFrApp.Clear();
		sJFrApp.setBackground(Color.DARK_GRAY);
		ShowMap();
		sJFrApp.AddSprite(sNewButton);
		sJFrApp.AddSprite(sPlayButton);
		sJFrApp.AddSprite(sExitButton);
	}

	@Override
	public void Update() {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(sNewButton.CheckBounds(e.getX(), e.getY())) {
			mMapData.GenerateNew(JFrameApplication.WIDTH / MapTile.sTileSize, JFrameApplication.HEIGHT / MapTile.sTileSize);
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
