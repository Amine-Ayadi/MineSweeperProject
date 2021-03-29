package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApp extends Application {



	    private static final int TILE_SIZE = 40;
	    private static final int W = 800;
	    private static final int H = 600;

	    private static final int X_TILES = W / TILE_SIZE;
	    private static final int Y_TILES = H / TILE_SIZE;

	    private Tile[][] grid = new Tile[X_TILES][Y_TILES];
	    private Scene scene;
	   
	    

	    private Parent createContent() {
	    	
	    	Pane root = new Pane();
	        HBox hbox = new HBox();
	        
	        Text scoreText = new Text("Score: ");
	        Text score = new Text("0");
	        
	        hbox.setAlignment(Pos.CENTER);
	        hbox.getChildren().addAll(scoreText,score);
	        
	        
	        BorderPane frame = new BorderPane(root,hbox,null,null,null);
	        
	        root.setPrefSize(W, H);

	        
	        
	        for (int y = 0; y < Y_TILES; y++) {
	            for (int x = 0; x < X_TILES; x++) {
	                Tile tile = new Tile(x, y, Math.random() < 0.2,false);

	                grid[x][y] = tile;
	                root.getChildren().add(tile);
	            }
	        }

	        for (int y = 0; y < Y_TILES; y++) {
	            for (int x = 0; x < X_TILES; x++) {
	                Tile tile = grid[x][y];

	                if (tile.hasBomb)
	                	tile.getImgV().setVisible(true);
	                	//continue;

	                long bombs = getNeighbors(tile).stream().filter(t -> t.hasBomb).count();

	                if (bombs > 0)
	                    tile.text.setText(String.valueOf(bombs));
	            }
	        }

	        return frame;
	    }

	    private boolean checkWin() {
	    	    BorderPane frame = (BorderPane) scene.getRoot();
	    	    Pane root = (Pane) frame.getCenter();
		        
	    	    for(Object o : root.getChildren()) {
	    	    	Tile tile = (Tile) o;
	    	    	if(!tile.isOpen && !tile.hasBomb) {
	    	    		System.out.println("[checkWin]: False");
	    	    		return false;
	    	    	}
	    	    	
	    	    }

	    	    System.out.println("[checkWin]: True");   
	    	    return true;
	        }
	       
	    
	    
	    
	    private List<Tile> getNeighbors(Tile tile) {
	        List<Tile> neighbors = new ArrayList<>();

	        // ttt
	        // tXt
	        // ttt

	        int[] points = new int[] {
	              -1, -1,
	              -1, 0,
	              -1, 1,
	              0, -1,
	              0, 1,
	              1, -1,
	              1, 0,
	              1, 1
	        };

	        for (int i = 0; i < points.length; i++) {
	            int dx = points[i];
	            int dy = points[++i];

	            int newX = tile.x + dx;
	            int newY = tile.y + dy;

	            if (newX >= 0 && newX < X_TILES
	                    && newY >= 0 && newY < Y_TILES) {
	                neighbors.add(grid[newX][newY]);
	            }
	        }

	        return neighbors;
	    }

	    private class Tile extends StackPane {
	       
	    	private int x, y;
	        private boolean hasBomb;
	        private boolean isOpen = false;
	        private boolean hasFlag = false;

	        private Rectangle border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);
	      
	        private Text text = new Text();
	        private Image img = new Image("ressources/flag.png");
	        private ImageView imgV = new ImageView(img);
	        
	        

	        public Tile(int x, int y, boolean hasBomb, boolean hasFlag) {
	           
	        	this.x = x;
	            this.y = y;
	            this.hasBomb = hasBomb;
	            this.hasFlag = hasFlag;
	           
	            imgV.setFitWidth(TILE_SIZE - 2);
	            imgV.setFitHeight(TILE_SIZE - 2);
	            imgV.setVisible(false);
	            
	            border.setStroke(Color.LIGHTGRAY);

	            text.setFont(Font.font(18));
	            text.setText(hasBomb ? "X" : "");
	            text.setVisible(false);

	           
	            
	            getChildren().addAll(border, text, imgV);

	            setTranslateX(x * TILE_SIZE);
	            setTranslateY(y * TILE_SIZE);

	            setOnMouseClicked(event -> action(event));
	            
	          /*  EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
	                @Override 
	                public void handle(MouseEvent e) { 
	                    if(checkWin()) {
			        	 	
	  		        	  Alert alert = new Alert(AlertType.CONFIRMATION);
	  		              alert.setTitle("YOU WIN");
	  		    
	  		               
	  		               // alert.setHeaderText("Results:");
	  		               alert.setContentText("Do you want to retry ?");
	  		               alert.setResizable(false);
	  		           
	  			               Optional<ButtonType> result = alert.showAndWait();
	  	  		           if(!result.isPresent())
	  	  		        	 scene.setRoot(createContent());
	  	  		        	   
	  	  		           else if(result.get() == ButtonType.OK)
	  	  		        	 scene.setRoot(createContent());
	  	  		           
	  	  		           else if(result.get() == ButtonType.CANCEL)
	  	  		        	 scene.setRoot(createContent());
	  	  		           	
	  	  		           return;
	  		         }
	                } 
	             };  
	             //Registering the event filter 
	             this.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandler);   */
	              
	           
	            
	            //
	       }
	        
	        public ImageView getImgV() {
	        	return this.imgV;
	        }
	       
	        
	        public void open() {
	        	 if (isOpen || this.hasFlag)
		                return;

	        	 
	        	 
	        	 if (hasBomb) {
		               System.out.println("Game Over");
		               //For each Tile Display the all (Bombs in red and others on white)
		               /*Alert*/
		               Alert alert = new Alert(AlertType.CONFIRMATION);
		               alert.setTitle("Game Over");
		    
		               
		               // alert.setHeaderText("Results:");
		               alert.setContentText("Do you want to retry ?");
		               alert.setResizable(false);
		           
			               Optional<ButtonType> result = alert.showAndWait();
	  		           if(!result.isPresent())
	  		        	 scene.setRoot(createContent());
	  		        	   
	  		           else if(result.get() == ButtonType.OK)
	  		        	 scene.setRoot(createContent());
	  		           
	  		           else if(result.get() == ButtonType.CANCEL)
	  		        	 scene.setRoot(createContent());
	  		           	
	  		           return;
		            }
	        	 
	        	  	isOpen = true;
		            text.setVisible(true);
		            border.setFill(null);
		            

		            if (text.getText().isEmpty()) {
		                getNeighbors(this).forEach(Tile::open);

		            
             }else if(checkWin()) {
	        	 	
		        	  Alert alert = new Alert(AlertType.CONFIRMATION);
		              alert.setTitle("YOU WIN");
		    
		               
		               // alert.setHeaderText("Results:");
		               alert.setContentText("Do you want to retry ?");
		               alert.setResizable(false);
		           
			               Optional<ButtonType> result = alert.showAndWait();
	  		           if(!result.isPresent())
	  		        	 scene.setRoot(createContent());
	  		        	   
	  		           else if(result.get() == ButtonType.OK)
	  		        	 scene.setRoot(createContent());
	  		           
	  		           else if(result.get() == ButtonType.CANCEL)
	  		        	 scene.setRoot(createContent());
	  		           	
	  		           return;
		         }
		            
		      else {
             	BorderPane frame = (BorderPane) scene.getRoot();
  		        HBox hbox = (HBox)frame.getTop();
  		        Text score = (Text) hbox.getChildren().get(1);
  		        score.setText(String.valueOf(Integer.valueOf(text.getText()) + Integer.valueOf(score.getText())));
             }
	        }
	        
	        public void action(MouseEvent event) {
	        	
                if (event.getButton() == MouseButton.PRIMARY)
                {
  	        	 	open();
                
		            }
		            else if (event.getButton() == MouseButton.SECONDARY)
                {
		            	if (this.hasFlag) {
		            		imgV.setVisible(false);
	  		            	this.hasFlag=false;
	  		            
		            	}
		            	else if(!this.hasFlag && !isOpen){
		            		
		            		imgV.setVisible(true);
		                	this.hasFlag=true;
		                	
		                	
		            	}
		            	else if(isOpen)
		            		return;
                 
                }
             
               
            }
	    }
	    
	    @Override
	    public void start(Stage stage) throws Exception {
	        scene = new Scene(createContent());
	        System.out.println("type : "+scene.toString());

	        stage.setScene(scene);
	        stage.show();
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }



}
