package ie.gmit.sw.ai;

public class Bomb extends Thread{
	private char type;
	private Maze model;
	private int row;
	private int col;
	
	public Bomb(char type, Maze model, int currentRow, int currentCol){
		this.type = type;
		this.model = model;
		this.row = currentRow;
		this.col = currentCol;
	}
	
	@Override
	public void run() {
		try {
			// This Thread sleeps for 1.5 seconds to allow player to get away from explosion
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(type == '\u003E'){
			// Explosion for when player interacts with a spider enemy.
			for (int i = row-1; i <= row+1; i++){
				for (int j = col-1; j <= col+1; j ++){
					// Blow up help sprites (turn them to hedges)
					if(model.get(i, j) == '\u0031' || model.get(i, j) == '\u0032' || model.get(i, j) == '\u0033' || model.get(i, j) == '\u0034'){
						model.set(i, j, '0');
					}
					
					if(model.get(i, j) == '\u0035'){
						// The players health is decreased 
						GameRunner.player.setPlayerHealth(GameRunner.player.getPlayerHealth() - 15);
						GameRunner.status.setText("  Health: " + Double.toString(GameRunner.player.getPlayerHealth()) + "   Durability: " + GameRunner.player.getKeepHealth() + "   Anger: " + GameRunner.player.getAngerLevel() + "  Bombs:  " + GameRunner.player.getBomb()
				    	+ "  H-Bombs  " + GameRunner.player.gethBomb());
					}
				}
			}
			
			if(model.getGoal().getRow() == row && model.getGoal().getCol() == col){
				GameRunner.player.setPlayerHealth(GameRunner.player.getPlayerHealth() - 15);
				GameRunner.status.setText("  Health: " + Double.toString(GameRunner.player.getPlayerHealth()) + "   Durability: " + GameRunner.player.getKeepHealth() + "   Anger: " + GameRunner.player.getAngerLevel() + "  Bombs:  " + GameRunner.player.getBomb()
		    	+ "  H-Bombs  " + GameRunner.player.gethBomb());
			}
			
			model.set(row, col, ' ');
		}else{
			// Do hydrogen explosion when in contact with spider enemy.
			for (int i = row-1; i <= row+1; i++){
				for (int j = col-1; j <= col+1; j ++){
					// "Blow up" help sprites (turn them to hedges)
					if(model.get(i, j) == '\u0031' || model.get(i, j) == '\u0032' || model.get(i, j) == '\u0033' || model.get(i, j) == '\u0034' || model.get(i, j) == '0' ||  model.get(i, j) == '\u003F'){
						model.set(i, j, ' ');
					}
					
					if(model.get(i, j) == '\u0035'){
						// Do damage to player
						GameRunner.player.setPlayerHealth(GameRunner.player.getPlayerHealth() - 30);
						GameRunner.status.setText("  Health: " + Double.toString(GameRunner.player.getPlayerHealth()) + "   Durability: " + GameRunner.player.getKeepHealth() + "   Anger: " + GameRunner.player.getAngerLevel() + "  Bombs:  " + GameRunner.player.getBomb()
				    	+ "  H-Bombs  " + GameRunner.player.gethBomb());
					}
				}
			}
			
			if(model.getGoal().getRow() == row && model.getGoal().getCol() == col){
				GameRunner.player.setPlayerHealth(GameRunner.player.getPlayerHealth() - 30);
				GameRunner.status.setText("  Health: " + Double.toString(GameRunner.player.getPlayerHealth()) + "   Durability: " + GameRunner.player.getKeepHealth() + "   Anger: " + GameRunner.player.getAngerLevel() + "  Bombs:  " + GameRunner.player.getBomb()
		    	+ "  H-Bombs  " + GameRunner.player.gethBomb());
			}
		}
	}
}
