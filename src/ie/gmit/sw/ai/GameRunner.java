package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import org.jfree.ui.tabbedui.VerticalLayout;

import ie.gmit.sw.ai.spiders.Spider;
public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 100;
	private static final int IMAGE_COUNT = 17;
	private static final int MAX_THREADS = 50;
	private GameView view;
	private ControledPlayer controlledplayer;
	private static Maze model;
	private int currentRow;
	private int currentCol;
	public static long time = System.currentTimeMillis();
	public static Player player = new Player();
	public static JLabel status = new JLabel();
	
	public GameRunner() throws Exception{
		// This Creates a maze and a view component when the game is ran
		model = new Maze(MAZE_DIMENSION);
    	view = new GameView(model);
    	
    	// This Adds sprites to the view component
    	Sprite[] sprites = getSprites();
    	view.setSprites(sprites);
    	
    	// the player is placed in the maze and a timer starts
    	placePlayer();
    	
    	// Set the dimensions of the maze game to 1000x1000
    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	// A label above the game view which will display the current stats of the player
    	status.setText("  Health: " + Double.toString(player.getPlayerHealth()) + "   Durability: " + player.getKeepHealth() + "   Anger: " + player.getAngerLevel() + "  Bombs:  " + player.getBomb()
    	+ "  H-Bombs  " + player.gethBomb());
    	status.setBackground(Color.YELLOW);
    	status.setForeground(Color.BLACK);
    	status.setOpaque(true);
    	
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.addKeyListener(this);      
        f.getContentPane().setLayout(new VerticalLayout());
        f.add(status);
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
	}
	
	private void placePlayer(){   	
    	currentRow = (int) (MAZE_DIMENSION * Math.random());
    	currentCol = (int) (MAZE_DIMENSION * Math.random());
    	model.set(currentRow, currentCol, '5'); //A player warrior is at index 5
    	updateView(); 		
	}
	
	private void updateView(){
		model.updateGoal(currentRow, currentCol);
		
		status.setText("  Health: " + Double.toString(player.getPlayerHealth()) + "   Get help-swords: " + player.getKeepHealth() + "   Anger: " + player.getAngerLevel() + "  Bombs:  " + player.getBomb()
    	+ "  H-Bombs  " + player.gethBomb());
		
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

	// Player interactions (button key events)
    @Override
	public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        		if (isValidMove(currentRow, currentCol + 1)) currentCol++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        		if (isValidMove(currentRow, currentCol - 1)) currentCol--;	
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
        		if (isValidMove(currentRow - 1, currentCol)) currentRow--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        		if (isValidMove(currentRow + 1, currentCol)) currentRow++;        	  	
        }
        else if (e.getKeyCode() == KeyEvent.VK_Z){
        		view.toggleZoom();
        	}
        else{
        	return;
        }
        
        // Update view happens once per move (Might need to update traversers here)
        updateView();       
    }
    
    @Override
	public void keyReleased(KeyEvent e) {} //Ignore
	@Override
	public void keyTyped(KeyEvent e) {} //Ignore

    // Check if the move player is making, is right
	private boolean isValidMove(int row, int col){
		// Win clause here - If player exits the maze, the game exits
		if(isExitNode(row, col)){
			System.out.println("Victory. You completed the maze in: " + (System.currentTimeMillis() - time) + "ms"); 
			System.exit(0);
			return false;
		}else if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == ' '){
			// Show the bomb after it's thrown and the player walks off it
			if (model.get(currentRow, currentCol) == '\u003E'){
				model.set(currentRow, currentCol, '\u003E');
			}else if (model.get(currentRow, currentCol) == '\u003F'){
				model.set(currentRow, currentCol, '\u003F');
			}else{
				model.set(currentRow, currentCol, '\u0020');
			}
		
			model.set(row, col, '5');
			return true;
		}else{
			// Check if the player is trying to walk into a bush to collect items
			if(model.get(row, col) == '1'){
				// 1 is sword
				player.setSword(1);
				model.set(row, col, '0');
			}else if(model.get(row, col) == '2'){
				// 2 is help (health)
				if(player.getPlayerHealth() < 100){
					if(player.getPlayerHealth() >= 80){
						player.setPlayerHealth(100);
					}else{
						player.setPlayerHealth(player.getPlayerHealth() + 20);
						
						if(player.getAngerLevel() >= 20){
							player.setAngerLevel(player.getAngerLevel() - 20);
						}
						else{
							player.setAngerLevel(0);
						}
						
					}
					model.set(row, col, '0');
				}
			}else if(model.get(row, col) == '3'){
				// 3 is bomb
				player.setBomb(player.getBomb()+1);
				model.set(row, col, '0');
			}else if(model.get(row, col) == '4'){
				// 4 is h_bomb
				player.sethBomb(player.gethBomb()+1);
				model.set(row, col, '0');
			}
			
			return false; //Can't move
		}
	}
	
	private Sprite[] getSprites() throws Exception{
		//Read in the images from the resources directory as sprites. Note that each
		//sprite will be referenced by its index in the array, e.g. a 3 implies a Bomb...
		//Ideally, the array should dynamically created from the images... 
		
		controlledplayer = new ControledPlayer("Main Player", 3, 500, "resources/images/player/d1.png",
				"resources/images/player/d2.png", "resources/images/player/d3.png", "resources/images/player/l1.png",
				"resources/images/player/l2.png", "resources/images/player/l3.png", "resources/images/player/r1.png",
				"resources/images/player/r2.png", "resources/images/player/r3.png");

		
		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new Sprite("Hedge", 1, "resources/images/objects/hedge.png");
		sprites[1] = new Sprite("Sword", 1, "resources/images/objects/sword.png");
		sprites[2] = new Sprite("Help", 1, "resources/images/objects/help.png");
		sprites[3] = new Sprite("Bomb", 1, "resources/images/objects/bomb.png");
		sprites[4] = new Sprite("Hydrogen Bomb", 1, "resources/images/objects/h_bomb.png");
		sprites[5] = controlledplayer;
		sprites[6] = new Sprite("Black Spider", 2, "resources/images/spiders/black_spider_1.png", "resources/images/spiders/black_spider_2.png");
		sprites[7] = new Sprite("Blue Spider", 2, "resources/images/spiders/blue_spider_1.png", "resources/images/spiders/blue_spider_2.png");
		sprites[8] = new Sprite("Brown Spider", 2, "resources/images/spiders/brown_spider_1.png", "resources/images/spiders/brown_spider_2.png");
		sprites[9] = new Sprite("Green Spider", 2, "resources/images/spiders/green_spider_1.png", "resources/images/spiders/green_spider_2.png");
		sprites[10] = new Sprite("Grey Spider", 2, "resources/images/spiders/grey_spider_1.png", "resources/images/spiders/grey_spider_2.png");
		sprites[11] = new Sprite("Orange Spider", 2, "resources/images/spiders/orange_spider_1.png", "resources/images/spiders/orange_spider_2.png");
		sprites[12] = new Sprite("Red Spider", 2, "resources/images/spiders/red_spider_1.png", "resources/images/spiders/red_spider_2.png");
		sprites[13] = new Sprite("Yellow Spider", 2, "resources/images/spiders/yellow_spider_1.png", "resources/images/spiders/yellow_spider_2.png");
		sprites[14] = new Sprite("Bomb Thrown", "resources/bomb_throw.png");
		sprites[15] = new Sprite("HBomb Thrown", "resources/h_bomb_throw.png");
		sprites[16] = new Sprite("Help Thrown", "resources/help_throw.png");
		return sprites;
	}
	
	public boolean inSpiders(char c){
		if (c == '\u0036' || c == '\u0037' || c == '\u0038' || c == '\u0039' || c == '\u003A' || c == '\u003B' || c == '\u003C' || c == '\u003D') return true;
		return false;
	}
	
	public boolean isExitNode(int row, int col){
		if(row == -1 || row == 100 || col == -1 || col == 100){
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		new GameRunner();
		
		List<Spider> spiders = new ArrayList<Spider>();
		spiders = model.getSpiders(player);
		ExecutorService es = Executors.newFixedThreadPool(MAX_THREADS);
		while(spiders.size() != 0){
			int num = (int) (Math.random() * spiders.size());
			es.submit(spiders.get(num));
			spiders.remove(num);
		}
		es.shutdown();
	}
}