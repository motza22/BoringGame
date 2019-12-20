package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;
import javax.swing.JFrame;

public class JFrameApplication extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	private static JFrameApplication sApplication = null;
	public static final int X = 640;
	public static final int Y = 480;
	private Vector<Sprite> mSprites = new Vector<Sprite>();

	public static JFrameApplication GetInstance() {
		if(sApplication == null) {
			sApplication = new JFrameApplication();
			Thread appThread = new Thread(sApplication);
			appThread.start();
		}
		return sApplication;
	}
	
	private JFrameApplication() {
		setSize(X, Y);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setVisible(true);
        setTitle("Boring Game.");
	}
	
	public void AddSprite(Sprite aSprite) {
		mSprites.add(aSprite);
	}
	
	public void Draw(Graphics aGraphics) {		
		for(int i=0; i<mSprites.size(); i++) {
			Sprite sprite = mSprites.get(i);
			sprite.Draw(aGraphics);
		}
	}
	
	public void RemoveSprite(int aId) {
		for(int i=0; i<mSprites.size(); i++) {
			Sprite sprite = mSprites.get(i);
			if(aId == sprite.ID) {
				mSprites.remove(i);
			}
		}
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
			repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
