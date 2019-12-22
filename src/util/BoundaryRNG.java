package util;

import java.util.Random;

public class BoundaryRNG {
	private static Random sRandom = new Random();

	public static int Upper(int aUpperLimit) {
		return sRandom.nextInt(aUpperLimit + 1);
	}

	public static int Range(int aLowerLimit, int aUpperLimit) {
		return Upper(aUpperLimit - aLowerLimit) + aLowerLimit;
	}
}
