package core_assets;

import data.Map;
import data.MapTile.TileType;
import data.Move;
import data.Move.Direction;
import data.Position;

public class Bullet extends NativeObject {
	private Direction mDirection;

	public Bullet(Position aPosition, Direction aDirection) {
		super(aPosition);
		mDirection = aDirection;
	}

	public Position GetNextPosition(final Map aMap) {
		Position nextPos = new Position(mPos);
		if(mDirection == Direction.BOW) {
			nextPos.mY = aMap.CheckHeight(nextPos.mY - 1);
		} else if(mDirection == Direction.STERN) {
			nextPos.mY = aMap.CheckHeight(nextPos.mY + 1);
		} else if(mDirection == Direction.PORT) {
			nextPos.mX = aMap.CheckWidth(nextPos.mX - 1);
		} else if(mDirection == Direction.STARBOARD) {
			nextPos.mX = aMap.CheckWidth(nextPos.mX + 1);
		}
		return nextPos;
	}

	@Override
	public void ExecuteMove(Map aMap) {
		Position newPos = GetNextPosition(aMap);
		if(!mPos.Compare(newPos) &&
				aMap.TryMoveTile(mPos, newPos, TileType.INACCESSIBLE, TileType.GOAL)) {
			AddMove(new Move(mPos, newPos));
			mPos = newPos;
			super.ExecuteMove(aMap);
		} else {
		}
	}

}
