package data;

import java.util.Vector;

import data.MapTile.TileType;
import util.BoundaryRNG;

public abstract class MapUtil {
	private static final String sPath = "C:/Users/Zach/java_workspace/boring_game/save/map.csv";

	public static final Map GenerateNew(int aWidth, int aHeight) {
		Map map = new Map(aWidth, aHeight);

		int nodeLimit = (int)(((Math.sqrt(map.mWidth * map.mHeight) / 20) * BoundaryRNG.Range(2, 4)) + BoundaryRNG.Range(4, 6));
		int enemyLimit = (int)((Math.sqrt(map.mWidth * map.mHeight) / 10) * BoundaryRNG.Range(8, 16));

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

		for(int i=0; i<map.Get().size(); i++) {
			for(int j=0; j<map.Get().elementAt(i).size(); j++) {
				if(map.GetTileType(i, j) == TileType.PLAYER) {
					Position pos = new Position(i, j);
					for(int k=map.CheckWidth(pos.mX-10); k<=map.CheckWidth(pos.mX+10); k++) {
						for(int l=map.CheckHeight(pos.mY-10); l<=map.CheckHeight(pos.mY+10); l++) {
							if(map.GetTileType(k, l) == TileType.ENEMY) {
								map.SetTileType(k, l, TileType.EMPTY);
							}
						}
					}
				}
			}
		}

		map.Get().forEach(vector -> vector.forEach(tile -> {
			if(tile.mType == TileType.PLAYER) {

				return;
			}
		}));

		Save(map);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static final Map LoadSave() {
		Map map = null;
		Vector data = FileReadWrite.Load(sPath);
		if(!data.isEmpty()) {
			int width = (int)data.firstElement();
			data.remove(data.firstElement());
			int height = (int)data.firstElement();
			data.remove(data.firstElement());
			map = new Map(width, height);

			int count = 0;
			for(int i=0; i<map.mWidth; i++) {
				for(int j=0; j<map.mHeight; j++) {
					map.SetTileType(i, j, TileType.values()[(int) data.elementAt(count++)]);
					if(count >= data.size()) {
						return map;
					}
				}
			}
		}
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final void Save(Map aMap) {
		Vector data = new Vector();
		data.add(aMap.mWidth);
		data.add(aMap.mHeight);
		for(int i=0; i<aMap.mWidth; i++) {
			for(int j=0; j<aMap.mHeight; j++) {
				data.add(aMap.GetTileType(i, j).ordinal());
			}
		}
		FileReadWrite.Save(sPath, data);
	}

	private static final void CreatePlayableNode(Map aMap, TileType aTileType) {
		boolean nodeCreated = false;

		while(!nodeCreated) {
			Position nodePos = new Position(BoundaryRNG.Range(0, aMap.mWidth - 1), BoundaryRNG.Range(0, aMap.mHeight - 1));

			if(aMap.GetTileType(nodePos) == TileType.INACCESSIBLE)
			{
				int radius = BoundaryRNG.Range(5, 25);
				Area nodeArea = new Area(nodePos.mX, nodePos.mY, radius);

				nodeCreated = true;
				aMap.SetTileType(nodePos, aTileType);

				for(int i = nodeArea.mMinPos.mX; i <= nodeArea.mMaxPos.mX; i++) {
					for(int j = nodeArea.mMinPos.mY; j <= nodeArea.mMaxPos.mY; j++) {
						if(nodeArea.CheckCircle(i, j) && i == aMap.CheckWidth(i) && j == aMap.CheckHeight(j)) {
							if(aMap.GetTileType(i, j) == TileType.EMPTY) {
							} else if(aMap.GetTileType(i, j) == TileType.INACCESSIBLE) {
								aMap.SetTileType(i, j, TileType.EMPTY);
							}
						}
					}
				}

				if(aTileType == TileType.PLAYER || aTileType == TileType.GOAL) {
					MakeTunnels(aMap, aMap.CheckWidth(BoundaryRNG.Range(nodePos.mX - radius, nodePos.mX + radius)),
							aMap.CheckHeight(BoundaryRNG.Range(nodePos.mY - radius, nodePos.mY + radius)), 8, true);
				} else {
					MakeTunnels(aMap, aMap.CheckWidth(BoundaryRNG.Range(nodePos.mX - radius, nodePos.mX + radius)),
							aMap.CheckHeight(BoundaryRNG.Range(nodePos.mY - radius, nodePos.mY + radius)), 3, false);
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
					if(aMap.GetTileType(j, aY) == TileType.INACCESSIBLE) {
						aMap.SetTileType(j, aY, TileType.EMPTY);
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
					if(aMap.GetTileType(aX, j) == TileType.INACCESSIBLE) {
						aMap.SetTileType(aX, j, TileType.EMPTY);
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
			Position enemyPos = new Position(BoundaryRNG.Range(0, aMap.mWidth - 1), BoundaryRNG.Range(0, aMap.mHeight - 1));

			if(aMap.GetTileType(enemyPos) == TileType.EMPTY)
			{
				clusterPlaced = true;
				aMap.SetTileType(enemyPos, TileType.ENEMY);

				int groupSize = (BoundaryRNG.Range(1, 20) > 15 ? BoundaryRNG.Range(1, 4) : 0);
				int runCount = 0;

				while(runCount++ < 8 && groupSize > 0)
				{
					enemyPos.mX = aMap.CheckWidth(enemyPos.mX + BoundaryRNG.Range(-1, 1));
					enemyPos.mY = aMap.CheckHeight(enemyPos.mY + BoundaryRNG.Range(-1, 1));

					if(aMap.GetTileType(enemyPos) == TileType.EMPTY)
					{
						aMap.SetTileType(enemyPos, TileType.ENEMY);
						groupSize--;
						break;
					}
				}
			}
		}
	}
}
