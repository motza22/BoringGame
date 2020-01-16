package core_assets;

import java.awt.Color;

import data.MapTile.TileType;

public abstract class TileColor {
	public static final Color GetColor(TileType mType) {
		Color color = Color.BLACK;
		if(mType == TileType.INACCESSIBLE) {
			color = Color.DARK_GRAY;
		} else if(mType == TileType.EMPTY) {
			color = Color.LIGHT_GRAY;
		} else if(mType == TileType.PLAYER) {
			color = Color.BLUE;
		} else if(mType == TileType.ENEMY) {
			color = Color.RED;
		} else if(mType == TileType.GOAL) {
			color = Color.YELLOW;
		} else if(mType == TileType.BULLET) {
			color = Color.GREEN;
		} else if(mType == TileType.HEATMAP) {
			color = Color.GREEN;
		} else if(mType == TileType.PATH) {
			color = Color.ORANGE;
		}
		return color;
	}
}
