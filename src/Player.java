import javax.swing.JOptionPane;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Player {
	static final int WIDTH = 30, HEIGHT = 30;
	final int INV_SIZE = 5;
	
	int xPos, yPos;
	int health, hunger, family, restless, cleanliness, clarity;
	Map stage;
	
	ArrayList<Food> inventory;
	
	Stage window;
		
	Player(Stage w) {
		health = 100;
		hunger = 75;
		family = 75;
		restless = 25;
		clarity = 0;
		cleanliness = 100;
		
		xPos = WIDTH / 2;
		yPos = HEIGHT / 2;
		
		inventory = new ArrayList<>();
		stage = new Map(WIDTH, HEIGHT);
		
		window = w;
	}
	
	public void updateTurn() {
		clarity += 2;
		hunger += 2;
		restless += 4;
		cleanliness -= 3;
		if(hunger >= 100) {
			hunger = 100;
			health -= 5;
		}
		if(restless >= 100){
			restless = 100;
		}
		if(cleanliness < 0){
			cleanliness = 0;
		}
		if(hunger <= 0){
			hunger = 0;
		}
		if(clarity > 100){
			clarity = 100;
		}
	}
	
	public void updateEvent() {
		Event e = stage.layout[xPos][yPos].event;
		
		switch(e){
		case FATHER: father(); break;
			
		case MUSIC: music(); break;
		
		case MOTHER: mother(); break;
		
		case SISTER: sister(); break;
		
		case CLEANER: cleaner(); break;
		
		case FOOD: food(); break;
				
		case SCURRY: scurry(); break;
		
		case FURNITURE: furniture(); break;
		
		default: break;
		}
	}
	
	public void consume(int ind) {
		if(inventory.size() > ind){
			Food f = inventory.get(ind);
			inventory.remove(ind);
			hunger -= (f.hungerVal - f.hungerVal * Math.abs(clarity / 33 - f.idealStage) / 3);
		}
	}
	
	public void checkEnd() {
		if(health <= 0.0 && clarity >= 99 && family >= 50.0){
			display("Your eyes shut for the last time.\nIn your final moments, you think only of your love for your family.\nVictory, such as it is...");
			window.close();
		}else if(health <= 0.0){
			display("Your eyes shut for the last time.\nIn your final moments, you question your undying devotion for your family.\nAll has been for naught...");
			window.close();
		}
	}
	
	private boolean canMove(Move m) {
		boolean canMove = true;
		
		switch(m) {
		case UP: canMove = (stage.layout[xPos][yPos].walls & 2) == 0; break;
		case DOWN: canMove = (stage.layout[xPos][yPos].walls & 1) == 0; break;
		case LEFT: canMove = (stage.layout[xPos][yPos].walls & 8) == 0; break; 
		case RIGHT: canMove = (stage.layout[xPos][yPos].walls & 4) == 0; break; 
		default: break;
		}
		
		return canMove;
	}
	
	public boolean move(Move m) {
		if(!canMove(m)) {
			return false;
		} else {
			switch(m) {
			case UP: yPos--; break;
			case DOWN: yPos++; break;
			case LEFT: xPos--; break;
			case RIGHT: xPos++; break;
			default: break;
			}
		}
		return true;	
	}
	
	
//	FATHER, MOTHER, SISTER, CLEANER, MUSIC, SCURRY, ROOMMATES, FURNITURE, FOOD, NONE

	private void father() {
		if(clarity / 33 >= 2) {
			health -= 25;
			display("You have been attacked by your father!\n He throws an apple at you, lowering your health by 25.");
		} else {
			family += 15;
			display("Your father nods sternly at you.\n His act of solidarity reminds you of your love for him, raising family ties by 15.");
		}
	}
	
	private void mother() {
		if(clarity / 33 >= 2) {
			family -= 10;
			display("Your mother screams when she sees you!\n You feel shame and doubt, lowering family ties by 10.");
		} else {
			family += 15;
			display("Your mother offers some comforting words.\n Her soft words remind you of your humanity, raising family ties by 15.");
		}
	}
	
	private void sister() {
		if(clarity / 33 >= 3) {
			family -= 10;
			display("Your sister hurries out of your sight.\n You are disgusted by what you have become, lowering your family ties by 10.");
		} else {
			if(inventory.size() < INV_SIZE) {
				inventory.add(randFood());
				display("Your sister hands you some food.\nYou are now carrying " + inventory.get(inventory.size() - 1).name + ".");
			} else {
				display("Your sister hands you some, but you are holding too much to take it.");
			}
		}
	}
	
	private void cleaner() {
		if(clarity / 33 >= 3) {
			if(inventory.size() < INV_SIZE) {
				inventory.add(randFood());
				display("Despite your hideous form, the charwoman offers you some food.\nYou are now carrying " + inventory.get(inventory.size() - 1).name + ".");
			} else {
				display("Despite your hideous form, the charwoman offers you some food. However, you are holding too much to take it.");
			}
		} else {
			if(inventory.size() < INV_SIZE) {
				inventory.add(randFood());
				display("The charwoman looks at you amusedly before offering some food.\nYou are now carrying " + inventory.get(inventory.size() - 1).name + ".");
			} else {
				display("The charwoman looks at you amusedly before offering some food. However, you are holding too much to take it.");
			}
		}
	}
	//	int health, hunger, family, restless, cleanliness, clarity;

	private void music() {
		restless -= 25;
		family += 15;
		clarity += 8;
		display("You hear your sister play the violin.\nThe triumphant noise fills you with a sense of purpose.\nYou restlessness decreases by 25, your familial ties increase by 15, and your clarity increases by 8.");
	}
	
	private void scurry() {
		if(restless >= 50){
			hunger += 10;
			display("In a fit of restlessness, you scurry over across the walls and ceiling.\nYou tire yourself, increasing your hunger by 10.");
		}else{
			display("Despite your urge to scurry on the walls and ceilings, your dedication to civility prevents you.");
		}
	}
	
	private void furniture() {
		if(clarity / 33 >= 1) {
			restless -= 20;
			clarity += 8;
			display("You notice the furniture has been cleared to make room for you. You feel oddly comforted.\nYour restlessness decreases by 20 and your clarity increases by 8.");
		} else {
			family -= 20;
			display("You notice the furniture has been cleared to make room for you. You feel slightly betrayed.\nYour familial ties decrease by 20.");
		}
	}
	
	private void food() {
		if(inventory.size() <= INV_SIZE) {
			inventory.add(randFood());
			display("You discover some food on the ground.\nYou are now carrying " + inventory.get(inventory.size() - 1).name + ".");
		} else {
			display("You discover some food on the ground, but you are holding too much to take it.");
		}
	}
		
	private void display(String s){
		JOptionPane.showMessageDialog(null, s);
	}
	
	private Food randFood(){
		Food.Type ft = Food.Type.values()[(int)(Math.random() * Food.Type.values().length)];
		switch(ft){
		case APPLE: return new Food("Apple", 0, 30);
		case CHICKEN: return new Food("Chicken", 0, 40);
		case SOUR_MILK: return new Food("Sour Milk", 3, 20);
		case MOLDY_BREAD: return new Food("Moldy Bread", 3, 50);
		case BORSCHT: return new Food("Borscht", 0, 100);
		case BREAD: return new Food("Bread", 0, 40);
		case ROTTEN_CHICKEN: return new Food("Rotten Chicken", 3, 40);
		case POTATO: return new Food("Potato", 0, 40);
		case MOLDY_POTATO: return new Food("Moldy Potato", 3, 40);
		default: return new Food("UNDEFINED", 0, 0);
		}
	}
	
}