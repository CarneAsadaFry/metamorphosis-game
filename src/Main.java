import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	private final int SQ_SIZE = 120;
	private final int WINDOW_WIDTH = 1080;
	private final int WINDOW_HEIGHT = 600;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	StackPane base = new StackPane();
        GridPane root = new GridPane();
        Pane ui = new Pane();
        base.getChildren().addAll(root, ui);
    	Scene scene = new Scene(base, WINDOW_WIDTH, WINDOW_HEIGHT);
    	
    	Player p1 = new Player(primaryStage);
    	p1.stage.generateEvents();
    	p1.stage.generateWalls();
    	
//    	for(int i = 0; i < Player.WIDTH; i++) {
//    		for(int j = 0; j < Player.HEIGHT; j++) {
//    			System.out.print(p1.stage.layout[i][j].event + " ");
//    		}
//    		System.out.println();
//    	}
    	
        primaryStage.setTitle("MetaMorph Deluxe");
        
        for(int i = 0; i < Player.WIDTH; i++) {
        	for(int j = 0; j < Player.HEIGHT; j++) {
        		p1.stage.layout[i][j].pane.setMinSize(SQ_SIZE, SQ_SIZE);
        		p1.stage.layout[i][j].pane.getChildren().add(new Rectangle(SQ_SIZE, SQ_SIZE, (i + j) % 2 == 0 ? Color.WHITE : Color.WHITE));
        		int wall = p1.stage.layout[i][j].walls;
        		String s = ((wall & 2) == 0 ? "0" : "5") + " 0 0 " + ((wall & 8) == 0 ? "0" : "5");
        		p1.stage.layout[i][j].pane.setStyle("-fx-border-width: " + s + "; -fx-border-color: black");
//        		p1.stage.layout[i][j].pane.getChildren().add(new Text(i + ", " + j));
                root.add(p1.stage.layout[i][j].pane, i, j);
        	}
        }
        
        base.setTranslateX(-62);
        base.setTranslateY(-62);
        
        p1.stage.layout[15][15].pane.getChildren().add(new ImageView(new Image("img/F1.jpg", SQ_SIZE, SQ_SIZE, false, false)));
         
//         for(Tile[] a : p1.stage.layout){
//        	 for(Tile t : a){
//        		 System.out.println(t.event);
//        	 }
//         }
        
        Rectangle background = new Rectangle(300, 700, Color.BLACK);
        Text stats = new Text("Health: 100\nHunger: 75\nFamilial Ties: 75\nRestlessness: 25\nCleanliness: 100\nClarity: 0\n\n\n\n\n\n\n\n\n\nInventory: \n");
        background.setTranslateX(900);
        background.setTranslateY(50);
        stats.setTranslateX(920);
        stats.setTranslateY(100);
        stats.setFill(Color.WHITE);
        stats.setFont(Font.font("Courier New", 18));
        ui.getChildren().addAll(background, stats);
        
        scene.setOnKeyPressed(e -> {
	        p1.stage.layout[p1.xPos][p1.yPos].pane.getChildren().remove(p1.stage.layout[p1.xPos][p1.yPos].pane.getChildren().size() - 1);
	        int X = p1.xPos;
	        int Y = p1.yPos;
        	switch (e.getCode()) {
			case UP: 
				if(p1.move(Move.UP)) root.setTranslateY(root.getTranslateY() + SQ_SIZE); break;
			case DOWN: 
				if(p1.move(Move.DOWN)) root.setTranslateY(root.getTranslateY() - SQ_SIZE); break;
			case LEFT: 
				if(p1.move(Move.LEFT)) root.setTranslateX(root.getTranslateX() + SQ_SIZE); break;
			case RIGHT: 
				if(p1.move(Move.RIGHT)) root.setTranslateX(root.getTranslateX() - SQ_SIZE); break;
			case DIGIT1:
				p1.consume(0);
			case DIGIT2:
				p1.consume(1);
			case DIGIT3:
				p1.consume(2);
			case DIGIT4:
				p1.consume(3);
			case DIGIT5:
				p1.consume(4);
			default: break;
			}
        	
        	ImageView m;
        	if(p1.clarity >= 99){
        		m = new ImageView(new Image("img/F4.jpg", SQ_SIZE, SQ_SIZE, false, false));
        	}else if(p1.clarity >= 66){
        		m = new ImageView(new Image("img/F3.jpg", SQ_SIZE, SQ_SIZE, false, false));
        	}else if(p1.clarity >= 33){
        		m = new ImageView(new Image("img/F2.jpg", SQ_SIZE, SQ_SIZE, false, false));
        	}else{
        		m = new ImageView(new Image("img/F1.jpg", SQ_SIZE, SQ_SIZE, false, false));
        	}
        	
        	p1.stage.layout[p1.xPos][p1.yPos].pane.getChildren().add(m);
        	if(p1.xPos != X || p1.yPos != Y) { 
        		p1.updateEvent();
        		p1.updateTurn();
        		p1.checkEnd();
        	}
        	String inv = "";
    		for(int i = 0; i < p1.inventory.size(); i++){
    			Food f = p1.inventory.get(i);
    			inv += (i + 1) + ". " + f.name + "\n";
    		}
    		stats.setText("Health: " + p1.health + 
    				"\nHunger: " + p1.hunger + 
    				"\nFamilial Ties: " + p1.family + 
    				"\nRestlessness: " + p1.restless + 
    				"\nCleanliness: " + p1.cleanliness + 
    				"\nClarity: " + p1.clarity + 
    				"\n\n\n\n\n\n\n\n\n\nInventory: \n" + inv);
        });        
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}