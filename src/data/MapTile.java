package data;

import java.awt.Rectangle;

public class MapTile {
	public enum TileType {
		EMPTY,
		INACCESSIBLE,
		PLAYER,
		ENEMY,
		GOAL,
		BULLET,
		HEATMAP,
		PATH
	}
	public static final int sTileSize = 5;
	public TileType mType;
	public final Position mPos;

	MapTile(int aX, int aY, TileType aType) {
		this(new Position(aX, aY), aType);
	}

	MapTile(Position aPos, TileType aType) {
		mType = aType;
		mPos = aPos;
	}

	public Rectangle GetRectangle() {
		return new Rectangle(mPos.mX * sTileSize, mPos.mY * sTileSize, sTileSize, sTileSize);
	}
}
