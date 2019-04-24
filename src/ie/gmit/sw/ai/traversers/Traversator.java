package ie.gmit.sw.ai.traversers;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.Result;
import ie.gmit.sw.ai.node.Node;

public interface Traversator {
	public enum TraverseAlgorithm {
		BFS, DFS, AStar, BestFirst, SAHC, RW
	}

	public Result traverse(Maze maze, Node node);
}