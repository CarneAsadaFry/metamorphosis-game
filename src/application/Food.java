package application;

public class Food {
	static enum Type {
		APPLE, CHICKEN, SOUR_MILK, MOLDY_BREAD, BORSCHT, BREAD, ROTTEN_CHICKEN, POTATO, MOLDY_POTATO
	}
	
	int hungerVal;
	int idealStage;
	String name = "";
	
	Food(String n, int i, int h){
		hungerVal = h;
		idealStage= i;
		name = n;
	}
	
}
