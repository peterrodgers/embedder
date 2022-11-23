package euler.enumerate;



import euler.AbstractDiagram;


public class IsomorphismTest {
	

	public static void main(String[] args) {
		
		final int numberOfContours = 6;
		final int numberOfIterations = 100000;
		
		AbstractDiagram adLongest1 = null;
		AbstractDiagram adLongest2 = null;
		
		long longestTime = 0;
		
		for(int i =0; i < numberOfIterations; i++) {
		
			AbstractDiagram.resetIsomorphismCounts();
			
			AbstractDiagram ad1 = AbstractDiagram.randomDiagramFactory(numberOfContours);
			AbstractDiagram ad2 = AbstractDiagram.randomDiagramFactory(numberOfContours);
	
			long startMillis = System.currentTimeMillis();
			long bruteForceCountTotal = 0;
	
			if(ad1.isomorphicTo(ad2)) {
				System.out.println("\n"+ad1+" isomorphic to "+ad2);
			}
			bruteForceCountTotal += ad1.getBruteForceCount();
			long totalMillis = System.currentTimeMillis()-startMillis;
			if(totalMillis > longestTime) {
				longestTime = totalMillis;
				adLongest1 = ad1;
				adLongest2 = ad2;
System.out.println("\ncurrent longest for "+numberOfContours+ " contours ");
System.out.println(adLongest1+" | "+adLongest2);
System.out.println("brute force count: "+bruteForceCountTotal);
System.out.println("time taken (millis): "+longestTime);
			}
		}
		

		
	}

	
}
