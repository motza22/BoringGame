package core;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import display.JFrameApplication;
import display.SimpleRectangle;
import util.BoundaryRNG;

public class DungeonCrawl extends State implements KeyListener {
	private static JFrameApplication sJFrApp = null;

	public DungeonCrawl() {
	}

	@Override
	public void Close() {
		sJFrApp.removeKeyListener(this);
		sJFrApp.Clear();
	}

	@Override
	public void Initialize() {
		sJFrApp = JFrameApplication.GetInstance();
		sJFrApp.addKeyListener(this);
	}

	@Override
	public void Show() {
		sJFrApp.Clear();
	}

	@Override
	public void Update() {
		int width = BoundaryRNG.Upper(11);
		int height = BoundaryRNG.Upper(11);
		int x = BoundaryRNG.Upper(JFrameApplication.WIDTH - width);
		int y = BoundaryRNG.Upper(JFrameApplication.HEIGHT - height);
		sJFrApp.AddSprite(new SimpleRectangle(x, y, width, height, Color.BLUE));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_R:
			int width = BoundaryRNG.Upper(55);
			int height = BoundaryRNG.Upper(55);
			int x = BoundaryRNG.Upper(JFrameApplication.WIDTH - width);
			int y = BoundaryRNG.Upper(JFrameApplication.HEIGHT - height);
			sJFrApp.AddSprite(new SimpleRectangle(x, y, width, height, Color.RED));
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
