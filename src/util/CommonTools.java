package util;

import java.awt.Rectangle;

public class CommonTools {
	public static boolean CheckBounds(int aX, int aY, final Rectangle aRectangle) {
		return aX >= aRectangle.x &&
				aX < aRectangle.x + aRectangle.width &&
				aY >= aRectangle.y &&
				aY < aRectangle.y + aRectangle.height;
	}
}
