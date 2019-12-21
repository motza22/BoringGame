package core;

import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
	private final ReentrantLock mStateLock = new ReentrantLock();
	private static Game sInstance = null;
	private core.State mState = null;
	
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
		mState.HandleNotify(aInputId);
		mStateLock.unlock();
	}
	
	public void PopState() {
		mStateLock.lock();
		if(mState != null) {
			mState.Close();
			mState = null;
		}
		mStateLock.unlock();
	}
	
	public void PushState(core.State aState) {
		mStateLock.lock();
		if(mState != null) {
			mState.Close();
		}
		mState = aState;
		mState.Initialize();
		mStateLock.unlock();
	}
	
	@Override
	public void run() {
		PushState(new MainMenu());
		boolean hasActiveState = true;
		while(hasActiveState) {
			mStateLock.lock();
			if(mState != null) {
				mState.Update();
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
