package core_assets;

import java.util.Vector;

import data.Map;
import data.Move;
import data.Position;

public abstract class NativeObject {
	protected Position mPos;
	protected Vector<Move> mMoves;

	public NativeObject(Position aPos) {
		mPos = new Position(aPos);
		mMoves = new Vector<Move>();
	}

	public Position GetPosition() {
		return new Position(mPos);
	}

	public void AddMove(Move aMove) {
		mMoves.add(aMove);
	}

	public void ExecuteMove(Map aMap) {
		if(!mMoves.isEmpty()) {
			aMap.MoveTile(mMoves.firstElement().mOrigPos, mMoves.firstElement().mNewPos);
			mMoves.remove(mMoves.firstElement());
		}
	}
}
