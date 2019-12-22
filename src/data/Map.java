package data;

import java.util.Vector;

import data.MapTile.TileType;
import util.BoundaryRNG;

public class Map {
	private final int mWidth;
	private final int mHeight;
	private Vector<Vector<MapTile>> mTiles = new Vector<Vector<MapTile>>();

	public Map(int aWidth, int aHeight) {
		mWidth = aWidth;
		mHeight = aHeight;
	}

	public void Clear() {
		mTiles.forEach((vector) -> vector.clear());
		mTiles.clear();
	}

	public void GenerateNew() {
		Reinitialize();

		int nodeLimit = (int)(((Math.sqrt(mWidth * mHeight) / 20) * BoundaryRNG.Range(2, 4)) + BoundaryRNG.Range(4, 6));
		int enemyLimit = (int)((Math.sqrt(mWidth * mHeight) / 10) * BoundaryRNG.Range(15, 25));

		for(int i = 0; i < nodeLimit; i++) {
			if(i==0) {
				CreatePlayableNode(TileType.PLAYER);
			} else if(i==1) {
				CreatePlayableNode(TileType.GOAL);
			} else {
				CreatePlayableNode(TileType.EMPTY);
			}
		}

		for(int i = 0; i < enemyLimit; i++) {
			PlaceEnemyCluster();
		}

		Save();
	}

	public final Vector<Vector<MapTile>> Get() {
		return mTiles;
	}

	public void LoadSave() {
	}

	public void Save() {
	}

	private void Reinitialize() {
		Clear();

		for(int i=0; i<mWidth; i++) {
			Vector<MapTile> vector = new Vector<MapTile>();
			mTiles.add(vector);
			for(int j=0; j<mHeight; j++) {
				vector.add(new MapTile(i, j, TileType.INACCESSIBLE));
			}
		}
	}

	private int CheckWidth(int aX) {
		if(aX < 0) {
			return 0;
		}
		if(aX > mWidth - 1) {
			return mWidth - 1;
		}
		return aX;
	}

	private int CheckHeight(int aY) {
		if(aY < 0) {
			return 0;
		}
		if(aY > mHeight - 1) {
			return mHeight - 1;
		}
		return aY;
	}

	private void CreatePlayableNode(TileType aTileType) {
		boolean nodeCreated = false;

		while(!nodeCreated) {
			int nodeX = BoundaryRNG.Range(0, mWidth - 1);
			int nodeY = BoundaryRNG.Range(0, mHeight - 1);

			if(mTiles.elementAt(nodeX).elementAt(nodeY).mType == TileType.INACCESSIBLE)
			{
				nodeCreated = true;
				mTiles.elementAt(nodeX).elementAt(nodeY).mType = aTileType;

				int radius = BoundaryRNG.Range(5, 25);
				int nodeMinX = CheckWidth(nodeX - radius);
				int nodeMaxX = CheckWidth(nodeX + radius);
				int nodeMinY = CheckHeight(nodeY - radius);
				int nodeMaxY = CheckHeight(nodeY + radius);

				for(int i = nodeMinX; i <= nodeMaxX; i++)
				{
					for(int j = nodeMinY; j <= nodeMaxY; j++)
					{
						if(Math.sqrt(Math.pow((nodeX - i), 2) + Math.pow((nodeY - j), 2)) < radius)
						{
							if(mTiles.elementAt(i).elementAt(j).mType == TileType.EMPTY) {
							} else if(mTiles.elementAt(i).elementAt(j).mType == TileType.INACCESSIBLE) {
								mTiles.elementAt(i).elementAt(j).mType = TileType.EMPTY;
							}
						}
					}
				}

				if(aTileType == TileType.PLAYER || aTileType == TileType.GOAL) {
					MakeTunnels( CheckWidth(BoundaryRNG.Range(nodeX - radius, nodeX + radius)),
							CheckHeight(BoundaryRNG.Range(nodeY - radius, nodeY + radius)), 8, true);
				} else {
					MakeTunnels( CheckWidth(BoundaryRNG.Range(nodeX - radius, nodeX + radius)),
							CheckHeight(BoundaryRNG.Range(nodeY - radius, nodeY + radius)), 3, false);
				}
			}
		}
	}

	private void MakeTunnels(int aX, int aY, int aCount, boolean recursive) {
		for(int i=0; i<aCount; i++) {
			int direction = BoundaryRNG.Upper(2);
			if(direction == 0) {
				int lowerIdx = CheckWidth(BoundaryRNG.Range(aX - 100, aX));
				int upperIdx = CheckWidth(BoundaryRNG.Range(aX, aX + 100));
				for(int j = lowerIdx; j <= upperIdx; j++) {
					if(mTiles.elementAt(j).elementAt(aY).mType == TileType.INACCESSIBLE) {
						mTiles.elementAt(j).elementAt(aY).mType = TileType.EMPTY;
					}
				}
				if(recursive) {
					MakeTunnels(lowerIdx, aY, aCount, false);
					MakeTunnels(upperIdx, aY, aCount, false);
				}

			} else {
				int lowerIdx = CheckHeight(BoundaryRNG.Range(aY - 100, aY));
				int upperIdx = CheckHeight(BoundaryRNG.Range(aY, aY + 100));
				for(int j = lowerIdx; j <= upperIdx; j++) {
					if(mTiles.elementAt(aX).elementAt(j).mType == TileType.INACCESSIBLE) {
						mTiles.elementAt(aX).elementAt(j).mType = TileType.EMPTY;
					}
				}
				if(recursive) {
					MakeTunnels(lowerIdx, aY, aCount, false);
					MakeTunnels(upperIdx, aY, aCount, false);
				}
			}
		}
	}

	private void PlaceEnemyCluster() {
		boolean clusterPlaced = false;

		while(!clusterPlaced) {
			int enemyX = BoundaryRNG.Range(0, mWidth - 1);
			int enemyY = BoundaryRNG.Range(0, mHeight - 1);

			if(mTiles.elementAt(enemyX).elementAt(enemyY).mType == TileType.EMPTY)
			{
				clusterPlaced = true;
				mTiles.elementAt(enemyX).elementAt(enemyY).mType =  TileType.ENEMY;

				int groupSize = (BoundaryRNG.Range(1, 20) > 15 ? BoundaryRNG.Range(1, 5) : 0);
				int runCount = 0;

				while(runCount++ < 8 && groupSize > 0)
				{
					enemyX += CheckWidth(BoundaryRNG.Range(-1, 1));
					enemyY += CheckHeight(BoundaryRNG.Range(-1, 1));

					if(mTiles.elementAt(enemyX).elementAt(enemyY).mType == TileType.EMPTY)
					{
						mTiles.elementAt(enemyX).elementAt(enemyY).mType =  TileType.ENEMY;
						groupSize--;
						break;
					}
				}
			}
		}
	}
}
