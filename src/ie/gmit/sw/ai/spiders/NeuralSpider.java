package ie.gmit.sw.ai.spiders;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.Player;
import ie.gmit.sw.ai.nn.NNFight;
import ie.gmit.sw.ai.node.Node;

public class NeuralSpider extends Spider{
	public NeuralSpider(Node start, char type, TraverseAlgorithm algorithm, Maze maze, Player spartan) {
		super(start, type, algorithm, maze, spartan);
	}

	@Override
	public double status(double health, double anger) {
		NNFight items = new NNFight();
		
		if (health <= 33){
			health = 0;
		}else if(health <= 66){
			health = 1;
		}else{
			health = 2;
		}
		
		if (anger <= 33){
			anger = 0;
		}else if(anger <= 66){
			anger = 1;
		}else{
			anger = 2;
		}
		
		return items.action(health, player.getSword(), anger);
	}
}
