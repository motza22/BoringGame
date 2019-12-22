package core;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import display.JFrameApplication;
import display.SimpleRectangle;

public class DungeonCrawl extends State implements KeyListener {
	private static JFrameApplication mJFrApp = null;
	private static Random mRandom = new Random();

	public DungeonCrawl() {
	}

	@Override
	public void Close() {
		mJFrApp.removeKeyListener(this);
		mJFrApp.Clear();
	}

	@Override
	public void HandleNotify(int aInputId) {
	}

	@Override
	public void Initialize() {
		mJFrApp = JFrameApplication.GetInstance();
		mJFrApp.addKeyListener(this);
	}

	@Override
	public void OnDisplay() {
		mJFrApp.Clear();
	}

	@Override
	public void Update() {
		int width = mRandom.nextInt(11);
		int height = mRandom.nextInt(11);
		int x = mRandom.nextInt(JFrameApplication.WIDTH - width);
		int y = mRandom.nextInt(JFrameApplication.HEIGHT - height);
		mJFrApp.AddSprite(new SimpleRectangle(x, y, width, height, Color.BLUE));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_R:
			int width = mRandom.nextInt(55);
			int height = mRandom.nextInt(55);
			int x = mRandom.nextInt(JFrameApplication.WIDTH - width);
			int y = mRandom.nextInt(JFrameApplication.HEIGHT - height);
			mJFrApp.AddSprite(new SimpleRectangle(x, y, width, height, Color.RED));
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
