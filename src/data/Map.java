package data;

import java.util.Vector;

public class Map {
	private Vector<Vector<MapTile>> mTiles = new Vector<Vector<MapTile>>();

	public Map() {
	}

	public void Clear() {
		mTiles.forEach((vector) -> vector.clear());
		mTiles.clear();
	}

	public final Vector<Vector<MapTile>> Get() {
		return mTiles;
	}

	public void GenerateNew(int width, int height) {
		Clear();
		Save();
	}

	public void LoadSave() {
	}

	public void Save() {
	}
}
