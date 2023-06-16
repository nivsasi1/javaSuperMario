package main;

import object.OBJ_coin;

public class AssetSetter {

	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setObject() {
		createObject(0,5,7,1);
		createObject(1,6,7,1);
		createObject(2,7,7,1);
		createObject(3,8,7,1);
		createObject(4,9,7,1);
		createObject(5,10,7,1);
		createObject(6,4,7,1);
		createObject(7,5,5,1);
		createObject(8,6,5,1);
		createObject(9,7,5,1);
		createObject(10,8,5,1);
		createObject(11,9,5,1);
		createObject(12,10,5,1);
		createObject(13,4,5,1);
		createObject(14,6,3,1);
		createObject(15,7,3,1);
		createObject(16,8,3,1);
		createObject(17,9,3,1);
		createObject(18,5,3,1);

	}
	public void setObjectQuestion(int i, int row, int col, int map, int kind) {
		createObject(i, row, col, map);
	}

	public void createObject(int i, int x, int y, int map) {
		gp.obj[i] = new OBJ_coin();
		gp.obj[i].worldX = x * gp.tileSize;
		gp.obj[i].worldY = y * gp.tileSize;
		gp.obj[i].map = map;

	}
}
