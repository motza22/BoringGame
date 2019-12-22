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
	public final int mX;
	public final int mY;

	MapTile(int x, int y, TileType type) {
		mType = type;
		mX = x;
		mY = y;
	}

	public Rectangle GetRectangle() {
		return new Rectangle(mX * sTileSize, mY * sTileSize, sTileSize, sTileSize);
	}
}
