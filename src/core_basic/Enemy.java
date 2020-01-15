package core_basic;

import java.util.Vector;

import ai.Node;
import ai.PathFinder;
import data.Map;
import data.Move;
import data.Position;

public class Enemy extends NativeObject {
	public Enemy(Position aPosition) {
		super(aPosition);
	}

	public void UpdateTarget(Map aMap, Position aPosition) {
		mMoves.clear();
		Vector<Node> nodes = new Vector<Node>();
		nodes.add(new Node(aMap, mPos, aPosition));
		Node node = PathFinder.ProcessNodes(nodes, 10);
		if(node != null) {
			Vector<Move> moves = node.GetMoveList();
			if(!moves.isEmpty()) {
				moves.forEach(move -> {
					AddMove(new Move(move));
				});
			}
		}
	}

	@Override
	public void ExecuteMove(Map aMap) {
		if(!mMoves.isEmpty()) {
			mPos = new Position(mMoves.firstElement().mNewPos);
		}
		super.ExecuteMove(aMap);
	}
}
