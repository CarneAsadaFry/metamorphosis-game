package application;

import javafx.scene.layout.StackPane;
import java.util.*;

public class Map {
	private static final int EVENT_CHANCE = 20;
	
	Tile[][] layout;
	
	int width, height;
	
	Map(int width, int height) {
		this.width = width;
		this.height = height;
		
		layout = new Tile[width][height];
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				layout[i][j] = new Tile();
			}
		}
		
	}
	
	//Generates values for walls that creates a cohesive floor plan
	public void generateWalls() {
		int[][] adjmat = new int[width * height][width * height];
		
		for(int i = 0; i < width * height; i++) {
			for(int j = 0; j < width * height; j++) {
				adjmat[i][j] = -1;
			}
		}
		
		for(int i = 0; i < width * height; i++) {
			if(i % width != 0)
				adjmat[i][i - 1] = (int)(Math.random() * 100);
			if(i % width != height - 1)
				adjmat[i][i + 1] = (int)(Math.random() * 100);
			if(i / width != 0)
				adjmat[i][i - width] = (int)(Math.random() * 100);
			if(i / width != height - 1)
				adjmat[i][i + width] = (int)(Math.random() * 100);
		}
		
		PriorityQueue<PrimNode> pq = new PriorityQueue<>();
		
		int[] connected = new int[width * height];
		int[] connections = {1, 1, 1, 2, 2, 4};
		for(int i = 0; i < width * height; i++) {
			connected[i] = connections[(int)(Math.random() * connections.length)];
		}
		
		pq.offer(new PrimNode(0, height / 2 * width + height / 2, null));
		
		while(!pq.isEmpty()) {
			PrimNode node = pq.poll();
			
			if(connected[node.index] == 0)
				continue;
			
			if(node.parent != null) {
				//If parent is to the right, remove parent's left wall, child's right wall
				if(node.parent.index - node.index == 1) {
					layout[node.parent.index / 30][node.parent.index % 30].walls &= ~2;
					layout[node.index / 30][node.index % 30].walls &= ~1;
				} else if(node.index - node.parent.index == 1) {
					layout[node.index / 30][node.index % 30].walls &= ~2;
					layout[node.parent.index / 30][node.parent.index % 30].walls &= ~1;
				} else if(node.parent.index - node.index == width) {
					layout[node.parent.index / 30][node.parent.index % 30].walls &= ~8;
					layout[node.index / 30][node.index % 30].walls &= ~4;
				} else if(node.index - node.parent.index == width) {
					layout[node.index / 30][node.index % 30].walls &= ~8;
					layout[node.parent.index / 30][node.parent.index % 30].walls &= ~4;
				}
			}
			
			connected[node.index]--;
			
			for(int i = 0; i < width * height; i++){
				if(adjmat[node.index][i] != -1 && connected[i] != 0){
					pq.offer(new PrimNode(i, adjmat[node.index][i], node));
				}
			}
		}
	}
	
	//Assigns a random event to about EVENT_CHANCE% of all tiles
	public void generateEvents() {
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if((int)(Math.random() * 100) < EVENT_CHANCE)
					layout[i][j].generateEvent();
			}
		}
	}
	
	static class PrimNode implements Comparable<PrimNode>{
		int index;
		int distance;
		PrimNode parent;

		public PrimNode(int i, int d, PrimNode p){
			index = i;
			distance = d;
			parent = p;
		}

		@Override
		public int compareTo(PrimNode other) {
			return distance - other.distance;
		}
	}
}

class Tile {
	int walls; // Walls are encoded as follows: TBLR (e.g. 8 = T, 9 = T/R, 15 = T/B/L/R)
	Event event;
	StackPane pane;
	boolean visited;
	
	public Tile() {
		event = Event.NONE;
		walls = 15;
		pane = new StackPane();
	}
	
	//Selects a random event with uniform probability and assigns it to the tile
	public void generateEvent() {
		int len = Event.values().length;
		event = Event.values()[(int)(Math.random() * (len - 1))];
	}
}