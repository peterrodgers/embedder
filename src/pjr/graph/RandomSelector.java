package pjr.graph;

import java.util.Random;

/**
 * Utility class to generate a random integer within a range
 * @author Leishi Zhang
 * */

public class RandomSelector{
	private Random randomGenerator;
	private int size;
	
	public RandomSelector(Random randomGenerator,int size){
		super();
		this.randomGenerator = randomGenerator;
		this.size = size;
	}
			
	public int next(){
		return randomGenerator.nextInt(size);
	}
	
	public int[] nextN(int select){
		int[] result = new int[select];
		for(int counter = 0;counter < select;counter++){
			result[counter] = next();
		}		
		return result;
		}
}