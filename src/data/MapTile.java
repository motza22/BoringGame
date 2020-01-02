package data;

import java.awt.Rectangle;

public class MapTile {
	public enum TileType {
		EMPTY,
		INACCESSIBLE,
		PLAYER,
		ENEMY,
		GOAL
	}
	public static final int sTileSize = 5;
	public TileType mType;
	public Position mPos;

	MapTile(Position aPos, TileType type) {
		mType = type;
		mPos = aPos;
	}

	public Rectangle GetRectangle() {
		return new Rectangle(mPos.mX * sTileSize, mPos.mY * sTileSize, sTileSize, sTileSize);
	}
}
