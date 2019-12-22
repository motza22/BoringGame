package core;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import display.Button;
import display.JFrameApplication;
import util.CommonTools;

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
		if(CommonTools.CheckBounds(e.getX(), e.getY(), sPlayButton.GetRectangle())) {
			Game.GetInstance().PopPush( 1, new DungeonCrawl());
		} else if(CommonTools.CheckBounds(e.getX(), e.getY(), sMapButton.GetRectangle())) {
			Game.GetInstance().Push(new ViewMap());
		} else if(CommonTools.CheckBounds(e.getX(), e.getY(), sExitButton.GetRectangle())) {
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
