package ie.gmit.sw.ai.spiders;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.node.Node;
import ie.gmit.sw.ai.spiders.Spider;
import ie.gmit.sw.ai.traversers.*;
import ie.gmit.sw.ai.traversers.Traversator;

public abstract class Spider implements Status, Runnable, Traversator{
	protected Node start;
	protected char type;
	protected TraverseAlgorithm algorithm;
	protected static Maze maze;
	protected static Player player;
	protected double health = 100;
	
	public Spider(Node start, char type, Traversator.TraverseAlgorithm algorithm, Maze maze, Player player){
		this.start = start;
		this.type = type;
		this.algorithm = algorithm;
		Spider.maze = maze;
		Spider.player = player;
		
		// Assign different health for different spiders
		switch(type){
			case '\u0036': // Black
				health = 30;
				break;
			case '\u0037': // Blue
				health = 20;
				break;
			case '\u0038': // Brown
			    health = 10;
				break;
			case '\u0039': // Green
				health = 10;
				break;
			case '\u003A': // Gray
				health = 10;
				break;
			case '\u003B': // Orange
				health = 20;
				break;
			case '\u003C': // Red
				health = 30;
				break;
			case '\u003D': // Yellow
				health = 20;
				break;
		}
	}

	public Node getStart() {
		return start;
	}

	public void setStart(Node p) {
		this.start = p;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}
	
	@Override
	public Result traverse(Maze maze, Node start) {
		if (algorithm == TraverseAlgorithm.DFS){
			BruteForceTraversator bft = new BruteForceTraversator(true);
			return bft.traverse(maze, start);
		}else if(algorithm == TraverseAlgorithm.AStar){
			AStarTraversator as = new AStarTraversator();
			return as.traverse(maze, start);
		}else if(algorithm == TraverseAlgorithm.BestFirst){
			BestFirstTraversator bestfirst = new BestFirstTraversator();
			return bestfirst.traverse(maze, start);
		}else if(algorithm == TraverseAlgorithm.SAHC){
			SteepestAscentHillClimbingTraversator sahc = new SteepestAscentHillClimbingTraversator();
			return sahc.traverse(maze, start);
		}else if(algorithm == TraverseAlgorithm.RW){
			RandomWalk rw = new RandomWalk();
			return rw.traverse(maze, start);
		}
		else{
			BruteForceTraversator bft = new BruteForceTraversator(false);
			return bft.traverse(maze, start);
		}
	}
	
	@Override
	public void run() {
		Result r = traverse(maze, start);
		
		if(!r.isFoundPlayer()){
			if(r.getVisitCount() == 1){
				maze.set(r.getFinish().getRow(), r.getFinish().getCol(), ' ');
				return; // Spider is stuck in the wall and can't move
			}
			else{
				// Traverse again
				run();
			}
		}else{
			// Player was found, let's engage
			// double health = engage(maze.);
			double engage = status(player.getPlayerHealth(), player.getAngerLevel());
			if(this.getClass() == FuzzySpider.class){
				
				// Engage 1 - 100
				if(engage > 50){
					System.out.println("Fuzzy spider attacks");
					// Fight
					attack(r);
				}else{
					System.out.println("Fuzzy spider runs");
					flee(r);
				}
			}else{
				// Engage 1 - 2 (1 - Attack, 2 - Run)
				if(engage == 1){
					System.out.println("NN spider attacks");
					// Attack
					attack(r);
				}else{
					System.out.println("NN spider runs");
					flee(r);
				}
			}
		}
	}
	
	private void flee(Result r) {
		if (this.health <= 0){
			maze.set(r.getFinish().getRow(), r.getFinish().getCol(), ' ');
			return;
		}
		else{
			if(isPlayerAdjacent(r.getFinish())){
				player.setPlayerHealth(player.getPlayerHealth() - 5);
				player.addAnger();
				this.health -= player.fight();
				GameRunner.status.setText("  Health: " + Double.toString(player.getPlayerHealth()) + "   GetHelp-Health: " + player.getKeepHealth() + "   Anger: " + player.getAngerLevel() + "  Bombs:  " + player.getBomb()
		    	+ "  H-Bombs  " + player.gethBomb());
				runAway(r.getFinish());
			}
		}
		
		flee(r);
	}

	private void runAway(Node spider) {
		// Player is above you
		if(maze.getGoal().getRow() == spider.getRow() - 1 && maze.getGoal().getCol() == spider.getCol()  && maze.get(spider.getRow() + 1, spider.getCol()) == ' '){
			maze.set(spider.getRow(), spider.getCol(), ' ');
			maze.set(spider.getRow() + 1, spider.getCol(), type);
			spider.setRow(spider.getRow() + 1);
		}
		// Player is below you
		else if(maze.getGoal().getRow() == spider.getRow() + 1 && maze.getGoal().getCol() == spider.getCol()  && maze.get(spider.getRow() - 1, spider.getCol()) == ' '){
			maze.set(spider.getRow(), spider.getCol(), ' ');
			maze.set(spider.getRow() - 1, spider.getCol(), type);
			spider.setRow(spider.getRow() - 1);
		}
		// Player is to the left
		else if(maze.getGoal().getCol() == spider.getCol() - 1 && maze.getGoal().getRow() == spider.getRow()  && maze.get(spider.getRow(), spider.getCol() + 1) == ' '){
			maze.set(spider.getRow(), spider.getCol(), ' ');
			maze.set(spider.getRow(), spider.getCol() + 1, type);
			spider.setCol(spider.getCol() + 1);
		}
		// Player is to the right
		else if(maze.getGoal().getCol() == spider.getCol() + 1 && maze.getGoal().getRow() == spider.getRow()  && maze.get(spider.getRow(), spider.getCol() - 1) == ' '){
			maze.set(spider.getRow(), spider.getCol(), ' ');
			maze.set(spider.getRow(), spider.getCol() - 1, type);
			spider.setCol(spider.getCol() - 1);
		}
	}

	public void attack(Result r){
		System.out.println("Player health: " + player.getPlayerHealth() + ", Spider health: " + this.health);
		if (this.health <= 0){
			maze.set(r.getFinish().getRow(), r.getFinish().getCol(), ' ');
			return;
		}else{
			if(!isPlayerAdjacent(r.getFinish())){
				r.setFinish(follow(r.getFinish()));
			}
			else{
				player.setPlayerHealth(player.getPlayerHealth() - 5);
				player.addAnger();
				this.health -= player.fight();
				GameRunner.status.setText("  Health: " + Double.toString(player.getPlayerHealth()) + "   GetHelp-Health: " + player.getKeepHealth() + "   Anger: " + player.getAngerLevel() + "  Bombs:  " + player.getBomb()
		    	+ "  H-Bombs  " + player.gethBomb());
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			attack(r);
		}
	}
	
	public Node follow(Node spider){
		if(isPlayerAdjacent(spider)){
			return spider;
		}else {
			if(maze.getGoal().getRow() > spider.getRow() && !(isPlayerAdjacent(spider)) && maze.get(spider.getRow() + 1, spider.getCol()) == ' '){
				maze.set(spider.getRow(), spider.getCol(), ' ');
				maze.set(spider.getRow() + 1, spider.getCol(), type);
				spider.setRow(spider.getRow() + 1);
			}
			
			if(maze.getGoal().getRow() < spider.getRow() && !(isPlayerAdjacent(spider)) && maze.get(spider.getRow() - 1, spider.getCol()) == ' '){
				maze.set(spider.getRow(), spider.getCol(), ' ');
				maze.set(spider.getRow() - 1, spider.getCol(), type);
				spider.setRow(spider.getRow() - 1);
			}
			
			if(maze.getGoal().getCol() > spider.getCol() && !(isPlayerAdjacent(spider)) && maze.get(spider.getRow(), spider.getCol() + 1) == ' '){
				maze.set(spider.getRow(), spider.getCol(), ' ');
				maze.set(spider.getRow(), spider.getCol() + 1, type);
				spider.setCol(spider.getCol() + 1);
			}
			
			if(maze.getGoal().getCol() > spider.getCol() && !(isPlayerAdjacent(spider)) && maze.get(spider.getRow(), spider.getCol() - 1) == ' '){
				maze.set(spider.getRow(), spider.getCol(), ' ');
				maze.set(spider.getRow(), spider.getCol() - 1, type);
				spider.setCol(spider.getCol() - 1);
			}
			
			follow(spider);
		}
		
		return spider;
	}
	
	public boolean isPlayerAdjacent(Node spider){
		boolean adjacent = false;
		
		if(spider.getRow() == maze.getGoal().getRow() -1 && spider.getCol() == maze.getGoal().getCol()){
			adjacent = true;
		}else if (spider.getRow() == maze.getGoal().getRow() +1 && spider.getCol() == maze.getGoal().getCol()){
			adjacent = true;
		}else if (spider.getRow() == maze.getGoal().getRow() && spider.getCol() == maze.getGoal().getCol() + 1){
			adjacent = true;
		}else if (spider.getRow() == maze.getGoal().getRow() && spider.getCol() == maze.getGoal().getCol() - 1){
			adjacent = true;
		}

		return adjacent;
	}
}
