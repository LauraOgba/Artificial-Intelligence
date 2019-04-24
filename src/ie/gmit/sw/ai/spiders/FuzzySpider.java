package ie.gmit.sw.ai.spiders;

import ie.gmit.sw.ai.Maze;
import ie.gmit.sw.ai.Player;
import ie.gmit.sw.ai.node.Node;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzySpider extends Spider{
	public FuzzySpider(Node start, char type, TraverseAlgorithm algorithm, Maze maze, Player spartan) {
		super(start, type, algorithm, maze, spartan);
	}
	
	@Override
	public double status(double health, double anger) {
		FIS fis = FIS.load("./resources/fuzzy/fuzzylogic.fcl", true);
		fis.getFunctionBlock("FuzzyLogic");
		
		//JFuzzyChart.get().chart(fb);
		fis.setVariable("playerHealth", player.getPlayerHealth());
		fis.setVariable("angerLevel", player.getAngerLevel());
		fis.evaluate();
		Variable engage = fis.getVariable("fuzzyLogic");
		return engage.getValue();
	}
}
