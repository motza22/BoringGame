package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import data.MapTile.TileType;
import util.BoundaryRNG;

public class Map {
	private static final String sPath = "C:/Users/Zach/java_workspace/boring_game/save/map.csv";
	private static final String sDelim = ",";
	private int mWidth;
	private int mHeight;
	private Vector<Vector<MapTile>> mTiles = new Vector<Vector<MapTile>>();

	public Map() {
		mWidth = 0;
		mHeight = 0;
	}

	public Map(int aWidth, int aHeight) {
		mWidth = aWidth;
		mHeight = aHeight;
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

	public void Clear() {
		mTiles.forEach((vector) -> vector.clear());
		mTiles.clear();
	}

	public void GenerateNew(int aWidth, int aHeight) {
		mWidth = aWidth;
		mHeight = aHeight;
		GenerateNew();
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

	public boolean IsSane() {
		return mWidth != 0 && mHeight != 0;
	}

	public void LoadSave() {
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(sPath));
			String row;
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.split(sDelim);
				int count = 0;
				mWidth = Integer.parseInt(data[count++]);
				mHeight = Integer.parseInt(data[count++]);
				Reinitialize();
				for(int i=0; i<mWidth && count<data.length; i++) {
					for(int j=0; j<mHeight && count<data.length; j++) {
						mTiles.elementAt(i).elementAt(j).mType = TileType.values()[Integer.parseInt(data[count++])];
					}
				}
			}
			csvReader.close();
		} catch (FileNotFoundException e) {
			// do nothing
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean MoveTile(int aOrigX, int aOrigY, int aNewX, int aNewY) {
		if(mTiles.elementAt(aNewX).elementAt(aNewY).mType != TileType.INACCESSIBLE) {
			mTiles.elementAt(aNewX).elementAt(aNewY).mType = mTiles.elementAt(aOrigX).elementAt(aOrigY).mType;
			mTiles.elementAt(aOrigX).elementAt(aOrigY).mType = TileType.EMPTY;
			return true;
		}
		return false;
	}

	public void Save() {
		try {
			FileWriter csvWriter = new FileWriter(sPath);
			csvWriter.append(Integer.toString(mWidth));
			csvWriter.append(sDelim);
			csvWriter.append(Integer.toString(mHeight));
			csvWriter.append(sDelim);
			for(int i=0; i<mWidth; i++) {
				for(int j=0; j<mHeight; j++) {
					csvWriter.append(Integer.toString(mTiles.elementAt(i).elementAt(j).mType.ordinal()));
					csvWriter.append(sDelim);
				}
			}
			csvWriter.flush();
			csvWriter.close();
		} catch (FileNotFoundException e) {
			// do nothing
		} catch (IOException e) {
			e.printStackTrace();
		}
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
