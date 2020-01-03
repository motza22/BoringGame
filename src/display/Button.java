package display;

import java.awt.Color;
import java.awt.Graphics;

public class Button extends SimpleRectangle {
	public static final int sWidth = 250;
	public static final int sHeight = 75;
	private JString mJString;

	public Button(int aX, int aY, String aString) {
		super(aX, aY, sWidth, sHeight, Color.BLACK);
		mJString = new JString(aX, aY, aString);
	}

	public boolean CheckBounds(int aX, int aY) {
		return aX >= mRectangle.x &&
				aX < mRectangle.x + mRectangle.width &&
				aY >= mRectangle.y &&
				aY < mRectangle.y + mRectangle.height;
	}

	@Override
	public void Draw(Graphics aGraphics) {
		super.Draw(aGraphics);
		mJString.Draw(aGraphics);
	}
}
