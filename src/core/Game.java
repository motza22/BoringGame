package core;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
	private final ReentrantLock mStateLock = new ReentrantLock();
	private static Game sInstance = null;
	private Vector<core.State> mState = new Vector<core.State>();
	
	private Game() {
		// do nothing
	}
	
	public static Game GetInstance() {
		if(sInstance == null) {
			sInstance = new Game();
		}
		return sInstance;
	}
	
	public void Notify(int aInputId) {
		mStateLock.lock();
		mState.firstElement().HandleNotify(aInputId);
		mStateLock.unlock();
	}
	
	public void PopState() {
		mStateLock.lock();
		if(mState.size() > 0) {
			mState.firstElement().Close();
			mState.remove(mState.firstElement());
		}
		mStateLock.unlock();
	}
	
	public void PushState(core.State aState) {
		mStateLock.lock();
		if(mState.size() > 0) {
			mState.firstElement().Close();
		}
		mState.insertElementAt(aState, 0);
		mState.firstElement().Initialize();
		mStateLock.unlock();
	}
	
	@Override
	public void run() {
		PushState(new MainMenu());
		boolean hasActiveState = true;
		while(hasActiveState) {
			mStateLock.lock();
			if(mState.size() > 0) {
				mState.firstElement().Update();
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
	
	public static void main(String [] args) {
		Game.GetInstance().start();
	}
}
