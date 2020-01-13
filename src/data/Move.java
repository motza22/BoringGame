package data;

public class Move {
	public enum Direction {
		BOW,
		STERN,
		PORT,
		STARBOARD
	}

	public Position mOrigPos;
	public Position mNewPos;

	public Move(Move aMove) {
		this(aMove.mOrigPos, aMove.mNewPos);
	}

	public Move(Position aOrigPos, Position aNewPos) {
		mOrigPos = new Position(aOrigPos);
		mNewPos = new Position(aNewPos);
	}
}
