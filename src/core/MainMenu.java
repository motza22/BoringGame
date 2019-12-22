package core;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import display.Button;
import display.JFrameApplication;

public class MainMenu extends State implements MouseListener {
	private static JFrameApplication sJFrApp = null;
	private static final Button sPlayButton = new Button((JFrameApplication.WIDTH - Button.sWidth ) / 2,
			(JFrameApplication.HEIGHT - Button.sHeight ) / 3, "Play");
	private static final Button sMapButton = new Button((JFrameApplication.WIDTH - Button.sWidth ) / 2,
			(JFrameApplication.HEIGHT - Button.sHeight ) / 2, "Map");
	private static final Button sExitButton = new Button((JFrameApplication.WIDTH - Button.sWidth ) / 2,
			((JFrameApplication.HEIGHT - Button.sHeight ) * 2) / 3, "Exit");

	public MainMenu() {
	}

	@Override
	public void Close() {
		sJFrApp.Clear();
		sJFrApp.removeMouseListener(this);
	}

	@Override
	public void Initialize() {
		sJFrApp = JFrameApplication.GetInstance();
		sJFrApp.addMouseListener(this);
	}

	@Override
	public void Show() {
		sJFrApp.Clear();
		sJFrApp.setBackground(Color.LIGHT_GRAY);
		sJFrApp.AddSprite(sPlayButton);
		sJFrApp.AddSprite(sMapButton);
		sJFrApp.AddSprite(sExitButton);
	}

	@Override
	public void Update() {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(sPlayButton.CheckBounds(e.getX(), e.getY())) {
			Game.GetInstance().PopPush( 1, new DungeonCrawl());
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
}
