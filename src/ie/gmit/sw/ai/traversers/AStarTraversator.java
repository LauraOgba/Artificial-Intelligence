package ie.gmit.sw.ai.traversers;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.Result;
import ie.gmit.sw.ai.node.Node;
public class AStarTraversator implements Traversator{
	private Node goal;
	private List<Node> fullPath = null;
	PriorityQueue<Node> open = new PriorityQueue<Node>(20, (Node current, Node next)-> (current.getPathCost() + current.getHeuristic(goal)) - (next.getPathCost() + next.getHeuristic(goal)));
	java.util.List<Node> closed = new ArrayList<Node>();
	   	
	
	public AStarTraversator(Node goal){
		this.goal = goal;
	}
	
	public AStarTraversator(){
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
			        System.out.println("Found Player , Astar in :" + time + "ms");
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
			
			//Process adjacent nodes
			Node[] children = node.children(nMaze);
			for (int i = 0; i < children.length; i++) {
				Node child = children[i];
				int score = node.getPathCost() + 1 + child.getHeuristic(maze.getGoal());
				int existing = child.getPathCost() + child.getHeuristic(maze.getGoal());
				
				if ((open.contains(child) || closed.contains(child)) && existing < score){
					continue;
				}else{
					open.remove(child);
					closed.remove(child);
					child.setParent(node);
					child.setPathCost(node.getPathCost() + 1);
					open.add(child);
				}
			}				
		}
		r = new Result(visitCount, false, node);
		return r;
	}
	
	public List<Node> getPath(){
		return fullPath;
	}

}
