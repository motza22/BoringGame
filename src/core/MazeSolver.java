package core;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import ai.Node;
import ai.PathFinder;
import core_basic.TileColor;
import data.Map;
import data.MapTile;
import data.MapTile.TileType;
import data.MapUtil;
import data.Position;
import display.Button;
import display.JFrameApplication;
import display.SimpleRectangle;

public class MazeSolver extends State implements MouseListener {
	private static JFrameApplication sJFrApp = null;
	private static final Button sExitButton = new Button( 25,
			JFrameApplication.HEIGHT - Button.sHeight - 25, "Exit");
	private Map mMap;
	private Position mPlayerPos;
	private Position mGoalPos;
	private Vector<Node> mNodes = new Vector<Node>();
	private boolean mRun;

	public MazeSolver() {
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
		mMap.SetTileType(mPlayerPos, TileType.HEATMAP);
		mNodes = new Vector<Node>();
		mNodes.add(new Node(mMap, mPlayerPos, mGoalPos));
		mRun = true;
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
		sJFrApp.AddSprite(sExitButton);
		sJFrApp.mSpriteLock.unlock();
	}

	@Override
	public void Update() {
		if(!mNodes.isEmpty() && mRun) {
			Node node = PathFinder.ProcessNodes(mNodes, 3);
			if(node.IsAtGoal()) {
				for(int i=0; i<node.GetMoveList().size(); i++) {
					mMap.SetTileType(node.GetMoveList().elementAt(i).mNewPos, TileType.PATH);
				}
				mRun = false;
			}
			Show();
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(sExitButton.CheckBounds(e.getX(), e.getY())) {
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
