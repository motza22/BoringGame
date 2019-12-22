package data;

import java.awt.Rectangle;
import java.util.Vector;

public class MapData {
	private Vector<Vector<Rectangle>> mTiles = new Vector<Vector<Rectangle>>();

	public MapData() {
	}

	public void Clear() {
		mTiles.forEach((vector) -> vector.clear());
		mTiles.clear();
	}

	public final Vector<Vector<Rectangle>> Get() {
		return mTiles;
	}

	public void GenerateNew(int width, int height) {
		Clear();
		// todo
		Save();
	}

	public void LoadSave() {
		Clear();
		// todo
	}

	public void Save() {

	}
}
