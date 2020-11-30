package utilmisc;

import states.Level;

public class Prefab {

	public int[][] wallMap = new int[Level.localMapSize.height][Level.localMapSize.width];
	public int[][] floorMap = new int[Level.localMapSize.height][Level.localMapSize.width];
	public int[][] spawnMap = new int[Level.localMapSize.height][Level.localMapSize.width];

	public Prefab(int[][] wallMap, int[][] floorMap, int[][] spawnMap) {
		this.wallMap = wallMap;
		this.floorMap = floorMap;
		this.spawnMap = spawnMap;
	}
	
	public Prefab(int[][] wallMap, int[][] spawnMap) {
		this.wallMap = wallMap;
		this.spawnMap = spawnMap;
		for (int y = 0; y < Level.localMapSize.height; y++) {
			for (int x = 0; x < Level.localMapSize.width; x++) {
				floorMap[y][x] = 0;
			}
		}
	}
	
	public Prefab(int[][] wallMap) {
		this.wallMap = wallMap;
		for (int y = 0; y < Level.localMapSize.height; y++) {
			for (int x = 0; x < Level.localMapSize.width; x++) {
				floorMap[y][x] = 0;
				spawnMap[y][x] = 0;
			}
		}
	}
	
}
