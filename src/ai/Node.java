package ai;

import java.util.Vector;

import data.Map;
import data.MapTile.TileType;
import data.Move;
import data.Position;

public class Node {
	public enum PathDirection {
		BOW,
		STERN,
		PORT,
		STARBOARD
	}

	private Map mMap;
	private Position mPos;
	private final Position mTargetPos;
	private int mCost;
	private int mCostToGoal;
	private Vector<Move> mMoves;

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
	public Node(Node aNode, PathDirection aDirection) {
		mMap = aNode.mMap;
		mPos = aNode.mPos;
		mTargetPos = aNode.mTargetPos;
		mCost = aNode.mCost;
		mCostToGoal = aNode.mCostToGoal;
		mMoves = (Vector<Move>)aNode.mMoves.clone();

		Position newPos = new Position(mPos.mX, mPos.mY);

		if(aDirection == PathDirection.BOW) {
			newPos.mY = mMap.CheckHeight(newPos.mY - 1);
		} else if(aDirection == PathDirection.STERN) {
			newPos.mY = mMap.CheckHeight(newPos.mY + 1);
		} else if(aDirection == PathDirection.PORT) {
			newPos.mX = mMap.CheckWidth(newPos.mX - 1);
		} else if(aDirection == PathDirection.STARBOARD) {
			newPos.mX = mMap.CheckWidth(newPos.mX + 1);
		}

		if(mMap.MoveTile(mPos, newPos, TileType.INACCESSIBLE, TileType.ENEMY, TileType.HEATMAP)) {
			mMap.SetTileType(mPos, TileType.HEATMAP);
			mMoves.add(new Move(mPos, newPos));
			mPos = newPos;
			mCost++;
			mCostToGoal = CalcManhattanDist(mPos, mTargetPos);
		}
	}

	public final Vector<Move> GetMoveList() {
		return mMoves;
	}

	public final Position GetPosition() {
		return mPos;
	}

	public int GetTotalCost() {
		return mCost + mCostToGoal;
	}

	public boolean IsAtGoal() {
		return mPos.Compare(mTargetPos);
	}
}
