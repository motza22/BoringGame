package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

public class JFrameApplication extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	private static JFrameApplication sApplication = null;
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	private Vector<Sprite> mSprites = new Vector<Sprite>();
	private final ReentrantLock mSpriteLock = new ReentrantLock();

	public static JFrameApplication GetInstance() {
		if(sApplication == null) {
			sApplication = new JFrameApplication();
			Thread appThread = new Thread(sApplication);
			appThread.start();
		}
		return sApplication;
	}
	
	private JFrameApplication() {
		setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setVisible(true);
        setTitle("Boring Game.");
	}
	
	public void AddSprite(Sprite aSprite) {
		mSpriteLock.lock();
		mSprites.add(aSprite);
		mSpriteLock.unlock();
	}
	
	public void Draw(Graphics aGraphics) {		
		mSpriteLock.lock();
		for(int i=0; i<mSprites.size(); i++) {
			Sprite sprite = mSprites.get(i);
			sprite.Draw(aGraphics);
		}
		mSpriteLock.unlock();
	}
	
	public void RemoveLastSprite() {
		mSpriteLock.lock();
		if(mSprites.size() > 0) {
			mSprites.remove(mSprites.size() - 1);
		}
		mSpriteLock.unlock();
	}
	
	public void RemoveSprite(int aId) {
		mSpriteLock.lock();
		for(int i=0; i<mSprites.size(); i++) {
			Sprite sprite = mSprites.get(i);
			if(aId == sprite.mId) {
				mSprites.remove(i);
				break;
			}
		}
		mSpriteLock.unlock();
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
