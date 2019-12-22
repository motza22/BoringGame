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

		int nodeLimit = (int)(((Math.sqrt(mWidth * mHeight) / 20) * BoundaryRNG.Range(1, 3)) + BoundaryRNG.Range(2, 4));
		int enemyLimit = (int)((Math.sqrt(mWidth * mHeight) / 10) * BoundaryRNG.Range(20, 40));

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
				mTiles.elementAt(nodeX).elementAt(nodeY).mType = aTileType;

				int radius = BoundaryRNG.Range(4, 14);
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
							if(mTiles.elementAt(i).elementAt(j).mType == TileType.INACCESSIBLE)
							{
								mTiles.elementAt(i).elementAt(j).mType = TileType.EMPTY;
							}
						}
					}
				}

				nodeCreated = true;
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

				while(runCount++ < 16 && groupSize > 0)
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
