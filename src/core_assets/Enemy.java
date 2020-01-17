package core_assets;

import java.util.Vector;

import ai.Node;
import ai.PathFinder;
import data.Map;
import data.MapTile.TileType;
import data.Move;
import data.Position;

public class Enemy extends NativeObject {
	private static final int sIntelligence = 50;
	private Map mMap;
	private int mAge;

	public Enemy(Position aPosition, Map aMap) {
		super(aPosition);
		mMap = aMap;
		mAge = 0;
	}

	public int GetAge() {
		return mAge;
	}

	public void UpdatePath(Position aPosition) {
		mMap.SetTileType(mPos, TileType.HEATMAP);
		mMoves.clear();
		Vector<Node> nodes = new Vector<Node>();
		nodes.add(new Node(mMap, mPos, aPosition));
		Node node = PathFinder.ProcessNodes(nodes, sIntelligence);
		if(node != null) {
			Vector<Move> moves = node.GetMoveList();
			if(!moves.isEmpty()) {
				moves.forEach(move -> {
					AddMove(new Move(move));
				});
			}
		}
		for(int i = mMap.CheckWidth(mPos.mX-sIntelligence); i <= mMap.CheckWidth(mPos.mX+sIntelligence); i++) {
			for(int j = mMap.CheckHeight(mPos.mY-sIntelligence); j <= mMap.CheckHeight(mPos.mY+sIntelligence); j++) {
				if(mMap.GetTileType(i, j) == TileType.HEATMAP) {
					mMap.SetTileType(i, j, TileType.EMPTY);
				}
			}
		}
		mMap.SetTileType(mPos, TileType.ENEMY);
		mAge = 0;
	}

	public void ExecuteMove() {
		if(!mMoves.isEmpty() && mMap.TryMoveTile(mMoves.firstElement().mOrigPos, mMoves.firstElement().mNewPos,
				TileType.INACCESSIBLE, TileType.BULLET, TileType.ENEMY)) {
			super.ExecuteMove(mMap);
		}
		mAge++;
	}
}
