package core;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import display.Button;
import display.JFrameApplication;
import display.JString;

public class MainMenu extends State implements MouseListener, KeyListener {
	private static JFrameApplication sJFrApp = null;
	private static final Button sPlayButton = new Button((JFrameApplication.WIDTH - Button.sWidth ) / 2,
			(JFrameApplication.HEIGHT - Button.sHeight ) / 3, "Play");
	private static final Button sMapButton = new Button((JFrameApplication.WIDTH - Button.sWidth ) / 2,
			(JFrameApplication.HEIGHT - Button.sHeight ) / 2, "Map");
	private static final Button sExitButton = new Button((JFrameApplication.WIDTH - Button.sWidth ) / 2,
			((JFrameApplication.HEIGHT - Button.sHeight ) * 2) / 3, "Exit");
	private static final JString sMouseTestTip = new JString(25, JFrameApplication.HEIGHT - 50, "X - Mouse Test");
	private static final JString sMazeSolverTip = new JString(25, JFrameApplication.HEIGHT - 25, "Z - Maze Solver");

	public MainMenu() {
		sJFrApp = JFrameApplication.GetInstance();
	}

	@Override
	public void Close() {
		sJFrApp.removeKeyListener(this);
		sJFrApp.removeMouseListener(this);
		sJFrApp.Clear();
	}

	@Override
	public void Initialize() {
		sJFrApp.addMouseListener(this);
		sJFrApp.addKeyListener(this);
	}

	@Override
	public void Show() {
		sJFrApp.setBackground(Color.LIGHT_GRAY);
		sJFrApp.mSpriteLock.lock();
		sJFrApp.Clear();
		sJFrApp.AddSprite(sPlayButton);
		sJFrApp.AddSprite(sMapButton);
		sJFrApp.AddSprite(sExitButton);
		sJFrApp.AddSprite(sMouseTestTip);
		sJFrApp.AddSprite(sMazeSolverTip);
		sJFrApp.mSpriteLock.unlock();
	}

	@Override
	public void Update() {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(sPlayButton.CheckBounds(e.getX(), e.getY())) {
			Game.GetInstance().Push(new DungeonCrawl());
		} else if(sMapButton.CheckBounds(e.getX(), e.getY())) {
			Game.GetInstance().Push(new ViewMap());
		} else if(sExitButton.CheckBounds(e.getX(), e.getY())) {
			sJFrApp.dispatchEvent(new WindowEvent(sJFrApp, WindowEvent.WINDOW_CLOSING));
			Game.GetInstance().Pop(1);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_X:
			Game.GetInstance().Push(new MouseTest());
			break;
		case KeyEvent.VK_Z:
			Game.GetInstance().Push(new MazeSolver());
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
