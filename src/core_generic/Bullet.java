package core_generic;

import data.Map;
import data.MapTile.TileType;
import data.Move;
import data.Move.Direction;
import data.Position;

public class Bullet extends NativeObject {
	private Direction mDirection;
	private boolean mIsMoving;

	public Bullet(Position aPosition, Direction aDirection) {
		super(aPosition);
		mDirection = aDirection;
		mIsMoving = true;
	}

	public boolean IsMoving() {
		return mIsMoving;
	}

	@Override
	public void ExecuteMove(Map aMap) {
		Position newPos = new Position(mPos);
		if(mDirection == Direction.BOW) {
			newPos.mY = aMap.CheckHeight(newPos.mY - 1);
		} else if(mDirection == Direction.STERN) {
			newPos.mY = aMap.CheckHeight(newPos.mY + 1);
		} else if(mDirection == Direction.PORT) {
			newPos.mX = aMap.CheckWidth(newPos.mX - 1);
		} else if(mDirection == Direction.STARBOARD) {
			newPos.mX = aMap.CheckWidth(newPos.mX + 1);
		}
		if(!mPos.Compare(newPos) &&
				aMap.GetTile(newPos).mType != TileType.INACCESSIBLE &&
				aMap.GetTile(newPos).mType != TileType.GOAL) {
			AddMove(new Move(mPos, newPos));
			mPos = newPos;
			mIsMoving = true;
			super.ExecuteMove(aMap);
		} else {
			mIsMoving = false;
		}
	}
}
