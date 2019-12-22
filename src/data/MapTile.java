package data;

import java.awt.Rectangle;

public class MapTile {
	public enum TileType {
		INACCESSIBLE,
		FREE,
		PLAYER,
		ENEMY,
		GOAL
	}
	public static final int sTileSize = 10;
	public final TileType mType;
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
