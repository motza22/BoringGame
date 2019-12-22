package core;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
	private final ReentrantLock mStateLock = new ReentrantLock();
	private static Game sInstance = null;
	private Vector<core.State> mStateStack = new Vector<core.State>();

	public static void main(String [] args) {
		Game.GetInstance().start();
	}

	public static Game GetInstance() {
		if(sInstance == null) {
			sInstance = new Game();
		}
		return sInstance;
	}

	private Game() {
	}

	public void Notify(int aInputId) {
		mStateLock.lock();
		mStateStack.firstElement().HandleNotify(aInputId);
		mStateLock.unlock();
	}

	public void Pop(int aPopCount) {
		Pop(aPopCount, true);
	}

	public void Pop(int aPopCount, boolean aDisplay) {
		mStateLock.lock();
		while(mStateStack.size() > 0 && aPopCount > 0) {
			mStateStack.firstElement().Close();
			mStateStack.remove(mStateStack.firstElement());
			aPopCount--;
		}
		if(aDisplay && mStateStack.size() > 0) {
			mStateStack.firstElement().OnDisplay();
		}
		mStateLock.unlock();
	}

	public void Push(core.State aState) {
		mStateLock.lock();
		mStateStack.insertElementAt(aState, 0);
		mStateStack.firstElement().Initialize();
		mStateStack.firstElement().OnDisplay();
		mStateLock.unlock();
	}

	public void PopPush(int aPopCount, core.State aState) {
		Pop(aPopCount, false);
		Push(aState);
	}

	@Override
	public void run() {
		Push(new MainMenu());
		boolean hasActiveState = true;
		while(hasActiveState) {
			mStateLock.lock();
			if(mStateStack.size() > 0) {
				mStateStack.firstElement().Update();
			}
			else {
				hasActiveState = false;
			}
			mStateLock.unlock();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
