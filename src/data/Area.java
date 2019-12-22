package data;

public class Area {
	public Position mCenter;
	public Position mMinPos;
	public Position mMaxPos;
	public int mRadius;

	public Area(int aX, int aY, int aRadius) {
		mRadius = aRadius;
		mCenter = new Position(aX, aY);
		mMinPos = new Position(mCenter.mX - mRadius, mCenter.mY - mRadius);
		mMaxPos = new Position(mCenter.mX + mRadius, mCenter.mY + mRadius);
	}

	public boolean CheckCircle(int aX, int aY) {
		return Math.sqrt(Math.pow((mCenter.mX - aX), 2) + Math.pow((mCenter.mY - aY), 2)) < mRadius;
	}
}
