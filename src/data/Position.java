package data;

public class Position {
	public int mX = -1;
	public int mY = -1;

	public Position(int aX, int aY) {
		mX = aX;
		mY = aY;
	}

	public Position(Position aPos) {
		mX = aPos.mX;
		mY = aPos.mY;
	}

	public boolean Compare(Position aPos) {
		return mX == aPos.mX && mY == aPos.mY;
	}
}
