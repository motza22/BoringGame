package core_assets;

import java.util.Vector;

import ai.Node;
import ai.PathFinder;
import data.Map;
import data.MapTile.TileType;
import data.Move;
import data.Position;

public class Enemy extends NativeObject {
	Map mMap;

	public Enemy(Position aPosition, Map aMap) {
		super(aPosition);
		mMap = aMap;
	}

	public void UpdateTarget(Position aPosition) {
		mMap.SetTileType(mPos, TileType.HEATMAP);
		mMoves.clear();
		Vector<Node> nodes = new Vector<Node>();
		nodes.add(new Node(mMap, mPos, aPosition));
		Node node = PathFinder.ProcessNodes(nodes, 10);
		if(node != null) {
			Vector<Move> moves = node.GetMoveList();
			if(!moves.isEmpty()) {
				moves.forEach(move -> {
					AddMove(new Move(move));
				});
			}
		}
		for(int i = mMap.CheckWidth(mPos.mX-10); i <= mMap.CheckWidth(mPos.mX+10); i++) {
			for(int j = mMap.CheckHeight(mPos.mY-10); j <= mMap.CheckHeight(mPos.mY+10); j++) {
				if(mMap.GetTileType(i, j) == TileType.HEATMAP) {
					mMap.SetTileType(i, j, TileType.EMPTY);
				}
			}
		}
		mMap.SetTileType(mPos, TileType.ENEMY);
	}

	public void ExecuteMove() {
		if(!mMoves.isEmpty()) {
			mPos = new Position(mMoves.firstElement().mNewPos);
		}
		super.ExecuteMove(mMap);
	}
}
