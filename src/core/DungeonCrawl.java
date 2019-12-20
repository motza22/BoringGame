package core;

import java.awt.Color;
import java.util.Random;

import display.JFrameApplication;
import display.SimpleRectangle;

public class DungeonCrawl extends State {
	private static JFrameApplication mJFrApp = null;
	private static Random mRandom = new Random();
	
	public DungeonCrawl() {
	}

	@Override
	public void Close() {
	}
	
	@Override
	public void Initialize() {
		mJFrApp = JFrameApplication.GetInstance();
	}

	@Override
	public void ProcessInput(int aInput) {
	}

	@Override
	public void Update() {
		int width = mRandom.nextInt(11);
        int height = mRandom.nextInt(11);
        int x = mRandom.nextInt(JFrameApplication.X - width);
        int y = mRandom.nextInt(JFrameApplication.Y - height);
		mJFrApp.AddSprite(new SimpleRectangle(x, y, width, height, Color.BLUE));
	}
}
