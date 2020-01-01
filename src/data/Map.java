package data;

import java.util.Vector;

import data.MapTile.TileType;

public class Map {
	public final int mWidth;
	public final int mHeight;
	private Vector<Vector<MapTile>> mTiles = new Vector<Vector<MapTile>>();

	public Map(int aWidth, int aHeight)  {
		mWidth = aWidth;
		mHeight = aHeight;

		for(int i=0; i<mWidth; i++) {
			Vector<MapTile> vector = new Vector<MapTile>();
			mTiles.add(vector);
			for(int j=0; j<mHeight; j++) {
				vector.add(new MapTile(i, j, TileType.INACCESSIBLE));
			}
		}
	}

	public int CheckWidth(int aX) {
		if(aX < 0) {
			return 0;
		}
		if(aX > mWidth - 1) {
			return mWidth - 1;
		}
		return aX;
	}

	public int CheckHeight(int aY) {
		if(aY < 0) {
			return 0;
		}
		if(aY > mHeight - 1) {
			return mHeight - 1;
		}
		return aY;
	}

	public final Vector<Vector<MapTile>> Get() {
		return mTiles;
	}

	public MapTile GetTile(int aX, int aY) {
		return mTiles.elementAt(aX).elementAt(aY);
	}

	public boolean IsSane() {
		return mWidth != 0 && mHeight != 0;
	}

	public boolean IsPlayable() {
		boolean playerFound = false;
		boolean goalFound = false;
		for(int i=0; i<mWidth; i++) {
			for( int j=0; j<mHeight; j++) {
				if(GetTile(i, j).mType == TileType.PLAYER) {
					playerFound = true;
				} else if(GetTile(i, j).mType == TileType.GOAL) {
					goalFound = true;
				}
			}
		}
		return playerFound && goalFound;
	}

	public boolean MoveTile(int aOrigX, int aOrigY, int aNewX, int aNewY) {
		return MoveTile(aOrigX, aOrigY, aNewX, aNewY, TileType.INACCESSIBLE);
	}

	public boolean MoveTile(int aOrigX, int aOrigY, int aNewX, int aNewY, TileType ...aInvalidTiles) {
		boolean doMove = false;
		if(aOrigX != aNewX || aOrigY != aNewY) {
			doMove = true;
			for(TileType tileType : aInvalidTiles) {
				if(GetTile(aNewX, aNewY).mType == tileType) {
					doMove = false;
				}
			}
		}
		if(doMove) {
			SetTileType(aNewX, aNewY, GetTile(aOrigX, aOrigY).mType);
			SetTileType(aOrigX, aOrigY, TileType.EMPTY);
			return true;
		}
		return false;
	}

	public void SetTileType(int aX, int aY, TileType aTileType) {
		GetTile(aX, aY).mType = aTileType;
	}
}
