package data;

import java.util.Vector;

import data.MapTile.TileType;

public class Map {
	public final int mWidth;
	public final int mHeight;
	private Vector<Vector<MapTile>> mTiles = new Vector<Vector<MapTile>>();

	public Map(Map aMap) {
		mWidth = aMap.mWidth;
		mHeight = aMap.mHeight;

		for(int i=0; i<mWidth; i++) {
			Vector<MapTile> vector = new Vector<MapTile>();
			mTiles.add(vector);
			for(int j=0; j<mHeight; j++) {
				vector.add(new MapTile(i, j, aMap.GetTileType(i, j)));
			}
		}
	}

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

	public MapTile GetTile(Position aPos) {
		return mTiles.elementAt(aPos.mX).elementAt(aPos.mY);
	}

	public TileType GetTileType(int aX, int aY) {
		return GetTileType(new Position(aX, aY));
	}

	public TileType GetTileType(Position aPos) {
		return GetTile(aPos).mType;
	}

	public boolean IsSane() {
		return mWidth != 0 && mHeight != 0;
	}

	public boolean IsPlayable() {
		boolean playerFound = false;
		boolean goalFound = false;
		for(int i=0; i<mWidth; i++) {
			for( int j=0; j<mHeight; j++) {
				if(GetTileType(i, j) == TileType.PLAYER) {
					playerFound = true;
				} else if(GetTileType(i, j) == TileType.GOAL) {
					goalFound = true;
				}
			}
		}
		return playerFound && goalFound;
	}


	public void SetTileType(int aX, int aY, TileType aTileType) {
		SetTileType(new Position(aX, aY), aTileType);
	}

	public void SetTileType(Position aPos, TileType aTileType) {
		GetTile(aPos).mType = aTileType;
	}

	public boolean TryMoveTile(Position aOrigPos, Position aNewPos) {
		return MoveTile(aOrigPos, aNewPos, true, TileType.INACCESSIBLE);
	}

	public boolean TryMoveTile(Position aOrigPos, Position aNewPos, TileType ...aInvalidTiles) {
		return MoveTile(aOrigPos, aNewPos, true, aInvalidTiles);
	}

	public boolean MoveTile(Position aOrigPos, Position aNewPos) {
		return MoveTile(aOrigPos, aNewPos, false, TileType.INACCESSIBLE);
	}

	public boolean MoveTile(Position aOrigPos, Position aNewPos, TileType ...aInvalidTiles) {
		return MoveTile(aOrigPos, aNewPos, false, aInvalidTiles);
	}

	public boolean MoveTile(Position aOrigPos, Position aNewPos, boolean aReadOnly, TileType ...aInvalidTiles) {
		boolean doMove = false;
		if(!aOrigPos.Compare(aNewPos)) {
			doMove = true;
			for(TileType tileType : aInvalidTiles) {
				if(GetTileType(aNewPos) == tileType) {
					doMove = false;
				}
			}
		}
		if(doMove && !aReadOnly) {
			SetTileType(aNewPos, GetTileType(aOrigPos));
			SetTileType(aOrigPos, TileType.EMPTY);
		}
		return doMove;
	}
}
