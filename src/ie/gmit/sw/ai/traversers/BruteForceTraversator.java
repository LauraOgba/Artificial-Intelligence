package ie.gmit.sw.ai.traversers;

import java.util.Deque;
import java.util.LinkedList;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.Result;
//import ie.gmit.sw.ai.audio.SoundEffects;
import ie.gmit.sw.ai.node.Node;
public class BruteForceTraversator implements Traversator{
	private boolean dfs = false;
	
	public BruteForceTraversator(boolean depthFirst){
		this.dfs = depthFirst;
		}
	
	@Override
	public Result traverse(Maze maze, Node node) {
        long time = System.currentTimeMillis();
        int visitCount = 0;
        Node[][] nMaze = maze.getMazeAsNodes();
		Node goal = maze.getGoal();
        
		Deque<Node> queue = new LinkedList<Node>();
		queue.offer(node);
		
		Result r = null;
		char spider = node.getType();
		
		while (!queue.isEmpty()){
			nMaze[goal.getRow()][goal.getCol()].setGoalNode(false);
			goal = maze.getGoal();
			nMaze[goal.getRow()][goal.getCol()].setGoalNode(true);
			// Check what sprite was here before (Who's moving?) and replace it with a space
			maze.set(node.getRow(), node.getCol(), ' ');
			
			node = queue.poll();
			
			// Change the sprite's location in the maze as they traverse it
			maze.set(node.getRow(), node.getCol(), spider);
			
			nMaze[node.getRow()][node.getCol()].setVisited(true);
			
			visitCount++;
			
			Node[] nodes = node.adjacentNodes(nMaze);
			for (Node n : nodes){
				if (n.isGoalNode()){
			        time = System.currentTimeMillis() - time; //Stop the clock
			        //TraversatorStats.printStats(node, time, visitCount);
			        System.out.println("Found Player, brute force in " + time + "ms");
			        System.out.println("Visited: " + visitCount + " nodes");
			        r = new Result(visitCount, true, node);
			        return r;
				}
			}
			
			try { //Simulate processing each expanded node				
				Thread.sleep(500);						
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Node[] children = node.children(nMaze);
			for (int i = 0; i < children.length; i++) {
				if (children[i] != null && !children[i].isVisited()){
					children[i].setParent(node);
					if (dfs){
						queue.addFirst(children[i]);
					}else{
						queue.addLast(children[i]);
					}
				}									
			}			
		}
		r = new Result(visitCount, false, node);
		return r;
	}
}