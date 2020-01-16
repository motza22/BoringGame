package data;

import java.util.Vector;

public class GameStats {
	private static final String sPath = "C:/Users/Zach/java_workspace/boring_game/save/stats.csv";
	public int mBulletsFired;
	public int mEnemiesKilled;

	public GameStats() {
		mBulletsFired = 0;
		mEnemiesKilled = 0;
	}

	@SuppressWarnings("rawtypes")
	public void Load() {
		Vector data = FileReadWrite.Load(sPath);
		if(!data.isEmpty()) {
			mBulletsFired = (int)data.elementAt(0);
			mEnemiesKilled = (int)data.elementAt(1);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void Save() {
		Vector stats = new Vector();
		stats.add(mBulletsFired);
		stats.add(mEnemiesKilled);
		FileReadWrite.Save(sPath, stats);
	}
}
