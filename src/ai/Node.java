package ai;

import java.util.Vector;

import data.Map;
import data.MapTile.TileType;
import data.Move;
import data.Move.Direction;
import data.Position;

public class Node {
	private Map mMap;
	private final Position mPos;
	private final Position mTargetPos;
	private final int mCost;
	private final int mCostToGoal;
	private final Vector<Move> mMoves;

	public static final int CalcManhattanDist(Position aPos, Position aTargetPos) {
		return Math.abs(aTargetPos.mX - aPos.mX) + Math.abs(aTargetPos.mY - aPos.mY);
	}

	public Node(Map aMap, Position aPos, Position aTargetPos) {
		mMap = aMap;
		mPos = aPos;
		mTargetPos = aTargetPos;
		mCost = 0;
		mCostToGoal = CalcManhattanDist(mPos, mTargetPos);
		mMoves = new Vector<Move>();
	}

	@SuppressWarnings("unchecked")
	public Node(Node aNode, Direction aDirection) {
		mMap = aNode.mMap;
		mTargetPos = new Position(aNode.mTargetPos);

		Position newPos = new Position(aNode.mPos);
		if(aDirection == Direction.BOW) {
			newPos.mY = mMap.CheckHeight(newPos.mY - 1);
		} else if(aDirection == Direction.STERN) {
			newPos.mY = mMap.CheckHeight(newPos.mY + 1);
		} else if(aDirection == Direction.PORT) {
			newPos.mX = mMap.CheckWidth(newPos.mX - 1);
		} else if(aDirection == Direction.STARBOARD) {
			newPos.mX = mMap.CheckWidth(newPos.mX + 1);
		}

		if(mMap.MoveTile(aNode.mPos, newPos, TileType.INACCESSIBLE, TileType.ENEMY, TileType.HEATMAP)) {
			mPos = newPos;
			mCost = aNode.mCost + 1;
			mCostToGoal = CalcManhattanDist(mPos, mTargetPos);
			mMoves = (Vector<Move>)aNode.mMoves.clone();
			mMoves.add(new Move(aNode.mPos, mPos));
			mMap.SetTileType(aNode.mPos, TileType.HEATMAP);
		} else {
			mPos = aNode.mPos;
			mCost = aNode.mCost;
			mCostToGoal = aNode.mCostToGoal;
			mMoves = (Vector<Move>)aNode.mMoves.clone();
		}
	}

	@SuppressWarnings("unchecked")
	public Vector<Move> GetMoveList() {
		return (Vector<Move>)mMoves.clone();
	}

	public Position GetPosition() {
		return new Position(mPos);
	}

	public int GetTotalCost() {
		//		return mCost + mCostToGoal;
		//		return mCost + 2 * mCostToGoal;
		return mCostToGoal;
	}

	public boolean IsAtGoal() {
		return mPos.Compare(mTargetPos);
	}
}
