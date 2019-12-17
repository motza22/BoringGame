package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class JFrameApplication extends JFrame implements KeyListener, Runnable {
	private static final long serialVersionUID = 1L;
	public static int BOARD_SIZE_X = 640;
	public static int BOARD_SIZE_Y = 480;

	public static JFrameApplication StartApplication() {
		JFrameApplication app = new JFrameApplication();
		Thread appThread = new Thread(app);
		appThread.start();
		return app;
	}
	
	private JFrameApplication() {
		setSize(BOARD_SIZE_X, BOARD_SIZE_Y);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setVisible(true);
        setTitle("Boring Game.");
        addKeyListener(this);
	}
	
	public void Draw(Graphics aGraphics) {		
//		for(int i=0; i<_playerUnits.size(); i++) {
//			Unit unit = _playerUnits.get(i);
//            unit.draw(graphics);
//		}
//		
//		for(int i=0; i<_enemyUnits.size(); i++) {
//			Unit unit = _enemyUnits.get(i);
//            unit.draw(graphics);
//		}
//		
//		repaint();
	}
	
	@Override
    public void paint(Graphics aGraphics) {
        Image image = createImage(getWidth(), getHeight());
        Graphics background = image.getGraphics();
        Draw(background);
        aGraphics.drawImage(image, 0, 0, this);
    }

	@Override
	public void run() {
		while( true ) {
//			for(int i=0; i<_playerUnits.size(); i++) {
//				Unit unit = _playerUnits.get(i);
//				if(unit.health() > 0) {
//					unit.takeTurn();
//				}
//				else {
//					_playerUnits.remove(i);
//				}
//			}
//				
//			for(int i=0; i<_enemyUnits.size(); i++) {
//				Unit unit = _enemyUnits.get(i);
//				if(unit.health() > 0) {
//					unit.takeTurn();
//				}
//				else {
//					_enemyUnits.remove(i);
//				}
//			}	
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
//		switch(e.getKeyCode()) {
//			case KeyEvent.VK_W:
//				newPosition.y++;
//				break;
//				
//			case KeyEvent.VK_A:
//				newPosition.x--;
//				break;
//				
//			case KeyEvent.VK_S:
//				newPosition.y--;
//				break;
//				
//			case KeyEvent.VK_D:
//				newPosition.x++;
//				break;
//		}
	}
}
