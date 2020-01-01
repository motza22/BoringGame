package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import data.MapTile.TileType;
import util.BoundaryRNG;

public abstract class MapUtil {
	private static final String sPath = "C:/Users/Zach/java_workspace/boring_game/save/aMap.csv";
	private static final String sDelim = ",";

	public static final Map GenerateNew(int aWidth, int aHeight) {
		Map map = new Map(aWidth, aHeight);

		int nodeLimit = (int)(((Math.sqrt(map.mWidth * map.mHeight) / 20) * BoundaryRNG.Range(2, 4)) + BoundaryRNG.Range(4, 6));
		int enemyLimit = (int)((Math.sqrt(map.mWidth * map.mHeight) / 10) * BoundaryRNG.Range(15, 25));

		for(int i = 0; i < nodeLimit; i++) {
			if(i==0) {
				CreatePlayableNode(map, TileType.PLAYER);
			} else if(i<=1) {
				CreatePlayableNode(map, TileType.GOAL);
			} else {
				CreatePlayableNode(map, TileType.EMPTY);
			}
		}

		for(int i = 0; i < enemyLimit; i++) {
			PlaceEnemyCluster(map);
		}

		Save(map);
		return map;
	}

	public static final Map LoadSave() {
		Map map = null;

		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(sPath));
			String row;
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.split(sDelim);
				int count = 0;
				int width = Integer.parseInt(data[count++]);
				int height = Integer.parseInt(data[count++]);
				map = new Map(width, height);
				for(int i=0; i<map.mWidth && count<data.length; i++) {
					for(int j=0; j<map.mHeight && count<data.length; j++) {
						map.GetTile(i, j).mType = TileType.values()[Integer.parseInt(data[count++])];
					}
				}
			}
			csvReader.close();
		} catch (FileNotFoundException e) {
			// do nothing
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	public static final void Save(Map aMap) {
		try {
			FileWriter csvWriter = new FileWriter(sPath);
			csvWriter.append(Integer.toString(aMap.mWidth));
			csvWriter.append(sDelim);
			csvWriter.append(Integer.toString(aMap.mHeight));
			csvWriter.append(sDelim);
			for(int i=0; i<aMap.mWidth; i++) {
				for(int j=0; j<aMap.mHeight; j++) {
					csvWriter.append(Integer.toString(aMap.GetTile(i, j).mType.ordinal()));
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

	private static final void CreatePlayableNode(Map aMap, TileType aTileType) {
		boolean nodeCreated = false;

		while(!nodeCreated) {
			int nodeX = BoundaryRNG.Range(0, aMap.mWidth - 1);
			int nodeY = BoundaryRNG.Range(0, aMap.mHeight - 1);

			if(aMap.GetTile(nodeX, nodeY).mType == TileType.INACCESSIBLE)
			{
				int radius = BoundaryRNG.Range(5, 25);
				Area nodeArea = new Area(nodeX, nodeY, radius);

				nodeCreated = true;
				aMap.GetTile(nodeX, nodeY).mType = aTileType;

				for(int i = nodeArea.mMinPos.mX; i <= nodeArea.mMaxPos.mX; i++) {
					for(int j = nodeArea.mMinPos.mY; j <= nodeArea.mMaxPos.mY; j++) {
						if(i == aMap.CheckWidth(i) && j == aMap.CheckHeight(j) && nodeArea.CheckCircle(i, j)) {
							if(aMap.GetTile(i, j).mType == TileType.EMPTY) {
							} else if(aMap.GetTile(i, j).mType == TileType.INACCESSIBLE) {
								aMap.GetTile(i, j).mType = TileType.EMPTY;
							}
						}
					}
				}

				if(aTileType == TileType.PLAYER || aTileType == TileType.GOAL) {
					MakeTunnels(aMap, aMap.CheckWidth(BoundaryRNG.Range(nodeX - radius, nodeX + radius)),
							aMap.CheckHeight(BoundaryRNG.Range(nodeY - radius, nodeY + radius)), 8, true);
				} else {
					MakeTunnels(aMap, aMap.CheckWidth(BoundaryRNG.Range(nodeX - radius, nodeX + radius)),
							aMap.CheckHeight(BoundaryRNG.Range(nodeY - radius, nodeY + radius)), 3, false);
				}
			}
		}
	}

	private static final void MakeTunnels(Map aMap, int aX, int aY, int aCount, boolean recursive) {
		for(int i=0; i<aCount; i++) {
			int direction = BoundaryRNG.Upper(2);
			if(direction == 0) {
				int lowerIdx = aMap.CheckWidth(BoundaryRNG.Range(aX - 100, aX));
				int upperIdx = aMap.CheckWidth(BoundaryRNG.Range(aX, aX + 100));
				for(int j = lowerIdx; j <= upperIdx; j++) {
					if(aMap.GetTile(j, aY).mType == TileType.INACCESSIBLE) {
						aMap.GetTile(j, aY).mType = TileType.EMPTY;
					}
				}
				if(recursive) {
					MakeTunnels(aMap, lowerIdx, aY, aCount, false);
					MakeTunnels(aMap, upperIdx, aY, aCount, false);
				}

			} else {
				int lowerIdx = aMap.CheckHeight(BoundaryRNG.Range(aY - 100, aY));
				int upperIdx = aMap.CheckHeight(BoundaryRNG.Range(aY, aY + 100));
				for(int j = lowerIdx; j <= upperIdx; j++) {
					if(aMap.GetTile(aX, j).mType == TileType.INACCESSIBLE) {
						aMap.GetTile(aX, j).mType = TileType.EMPTY;
					}
				}
				if(recursive) {
					MakeTunnels(aMap, lowerIdx, aY, aCount, false);
					MakeTunnels(aMap, upperIdx, aY, aCount, false);
				}
			}
		}
	}

	private static final void PlaceEnemyCluster(Map aMap) {
		boolean clusterPlaced = false;

		while(!clusterPlaced) {
			int enemyX = BoundaryRNG.Range(0, aMap.mWidth - 1);
			int enemyY = BoundaryRNG.Range(0, aMap.mHeight - 1);

			if(aMap.GetTile(enemyX, enemyY).mType == TileType.EMPTY)
			{
				clusterPlaced = true;
				aMap.GetTile(enemyX, enemyY).mType =  TileType.ENEMY;

				int groupSize = (BoundaryRNG.Range(1, 20) > 15 ? BoundaryRNG.Range(1, 5) : 0);
				int runCount = 0;

				while(runCount++ < 8 && groupSize > 0)
				{
					enemyX = aMap.CheckWidth(enemyX + BoundaryRNG.Range(-1, 1));
					enemyY = aMap.CheckHeight(enemyY + BoundaryRNG.Range(-1, 1));

					if(aMap.GetTile(enemyX, enemyY).mType == TileType.EMPTY)
					{
						aMap.GetTile(enemyX, enemyY).mType =  TileType.ENEMY;
						groupSize--;
						break;
					}
				}
			}
		}
	}
}
