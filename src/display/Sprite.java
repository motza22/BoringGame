package display;

import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Sprite {
	private static final ReentrantLock sIdLock = new ReentrantLock();
	private static int sIdKey = 0;
	public final int mId;
	
	Sprite() {
		sIdLock.lock();
		mId = sIdKey++;
		sIdLock.unlock();
	}
	
	public abstract void Draw(Graphics aGraphics);
}
