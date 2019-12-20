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
	
	public boolean HasActiveState() {
		boolean hasActiveState = false;
		mStateLock.lock();
		hasActiveState = (mState != null);
		mStateLock.unlock();
		return hasActiveState;
	}
	
	public void Notify(int aInput) {
		mStateLock.lock();
		mState.ProcessInput(aInput);
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
		while(HasActiveState()) {
			mStateLock.lock();
			mState.Update();
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