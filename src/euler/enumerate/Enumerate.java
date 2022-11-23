package euler.enumerate;

import java.util.*;

import euler.*;
import euler.comparators.*;

public class Enumerate {
	
	
	/**
	 *
	 */
	public static void main(String[] args) {

//		for(int i = 1; i <=10; i++) {
//			outputNormalizedCombinations(1);
//			outputNormalizedCombinations(2);
			outputNormalizedCombinations(3);
//			outputNormalizedCombinations(4);
//	outputNormalizedCombinations(5);
//		}

		
//		outputCombinations(1);
//		outputCombinations(2);
//		outputCombinations(3);
//		outputCombinations(4);
//		outputCombinations(5);

	}
	
	public static long bruteForceCountTotal = 0;
	public static long bruteForceTimeTotal = 0;
	
	public static void outputCombinations(int numberOfContours) {

		bruteForceCountTotal = 0;
		bruteForceTimeTotal = 0;
		AbstractDiagram.resetIsomorphismCounts();
				
		ArrayList<String> zones = findAllZones(numberOfContours);
		long numberOfCombinations = (int)Math.pow(2,zones.size());
		ArrayList<AbstractDiagram> previousDiagrams = new ArrayList<AbstractDiagram>();

		long count = 0;
		long startMillis = System.currentTimeMillis();
		for(int combinationNumber = 0; combinationNumber < numberOfCombinations; combinationNumber++) {
			AbstractDiagram ad = findAbstractDiagram(combinationNumber,zones);
			boolean foundIsomorphism = false;
			for(AbstractDiagram ad2: previousDiagrams) {
				
				foundIsomorphism = ad.isomorphicTo(ad2);
				
				if(ad.getBruteForceApplied()) {
					bruteForceCountTotal++;
				}
				bruteForceTimeTotal += ad.getBruteForceTime();

				if(foundIsomorphism) {
					break;
				}
			}

			if(!foundIsomorphism) {
				count++;
				previousDiagrams.add(ad);
//				System.out.print(count+" "+combinationNumber+" ");
//				System.out.print((System.currentTimeMillis()-startMillis)/1000.0+" "+bruteForceTimeTotal/1000.0+" ");
				System.out.println(ad);
			}
		}
		double totalSeconds = (System.currentTimeMillis()-startMillis)/1000.0;
		
		System.out.println("Contours: "+numberOfContours+" "+zones);
		System.out.println("number of unique diagrams: "+count);
		System.out.println("number of combinations tried: "+numberOfCombinations);
		System.out.println("brute force count: "+bruteForceCountTotal);
		System.out.println("brute force time (seconds): "+bruteForceTimeTotal/1000.0);
		System.out.println("time taken (seconds): "+totalSeconds);
/*		
System.out.println("failOnTotalZoneCount "+AbstractDiagram.failOnTotalZoneCount);
System.out.println("failOnZoneSizeCount "+AbstractDiagram.failOnZoneSizeCount);
System.out.println("failOnTotalContourCount "+AbstractDiagram.failOnTotalContourCount);
System.out.println("failOnZoneSizeContourCount "+AbstractDiagram.failOnZoneSizeContourCount);
System.out.println("succeedAfterMapping "+AbstractDiagram.succeedAfterMapping);
System.out.println("failAfterMapping "+AbstractDiagram.failAfterMapping);
*/
	}
	
	public static void outputNormalizedCombinations(int numberOfContours) {
System.out.println("START generating all unique Euler diagrams with "+numberOfContours+" zones");

		bruteForceCountTotal = 0;
		bruteForceTimeTotal = 0;
		AbstractDiagram.resetIsomorphismCounts();
		
		ArrayList<String> zones = findAllZones(numberOfContours);
		int numberOfCombinations = (int)Math.pow(2,zones.size());
		ArrayList<AbstractDiagram> previousDiagrams = new ArrayList<AbstractDiagram>(50000);
		HashSet<String> previousStrings = new HashSet<String>(50000); // store a representation of each diagram that works with a hash table
//System.out.println("number of contour combinations "+numberOfCombinations);

		ArrayList<Integer> numberAtZoneSize = new ArrayList<Integer>(zones.size());
		for(int i = 0; i <= zones.size(); i++) {
			numberAtZoneSize.add(0);
		}

		long count = 0;
		long normalizedCount = 0;
		long isomorphismCount = 0;
		long startMillis = System.currentTimeMillis();
		for(long combinationNumber = 0; combinationNumber < numberOfCombinations; combinationNumber++) {
			boolean foundIsomorphism = false;
			AbstractDiagram ad = Enumerate.findAbstractDiagram(combinationNumber,zones);
			if(ad.toString().length() > 0 && ad.toString().charAt(0) != 'a') {
				// only deal with combinations starting with 'a'
				continue;
			}
			
			ad.normalize();

			if(previousStrings.contains(ad.toString())) {
				normalizedCount++;
				foundIsomorphism = true;
			} else {
				isomorphismCount++;
				for(AbstractDiagram ad2: previousDiagrams) {
					
					foundIsomorphism = ad.isomorphicTo(ad2);
//System.out.println(ad+"|"+ad2+" " +ad.getBruteForceApplied()+" "+ad.getBruteForceTime());
					
					if(ad.getBruteForceApplied()) {
						bruteForceCountTotal++;
					}
					bruteForceTimeTotal += ad.getBruteForceTime();

					if(foundIsomorphism) {
						break;
					}
				}
			}

			if(!foundIsomorphism) {
				count++;
				previousDiagrams.add(ad);
				previousStrings.add(ad.toString());
				int numberOfZonesAtSize = ad.getZoneList().size();
				int zoneSizeCount = numberAtZoneSize.get(numberOfZonesAtSize);
				zoneSizeCount++;
//				numberAtZoneSize.set(numberOfZonesAtSize,zoneSizeCount);
				System.out.print(count+" "+combinationNumber+" ");
				System.out.print((System.currentTimeMillis()-startMillis)/1000.0+" "+bruteForceTimeTotal/1000.0+" ");
				System.out.println(ad); // this outputs each unique diagram
			}
		}
		double totalSeconds = (System.currentTimeMillis()-startMillis)/1000.0;
		
		System.out.println("Contours: "+numberOfContours+" "+zones);
//		for(int i = 0; i <= zones.size(); i++) {
//			System.out.println("Number Of Zones: "+i+", count: "+numberAtZoneSize.get(i));
//		}
		System.out.println("number of combinations tried: "+numberOfCombinations);
		System.out.println("number equal by normalization: "+normalizedCount);
		System.out.println("number requiring isomorphism: "+isomorphismCount);
		System.out.println("total combinations: "+(long)Math.pow(2,zones.size()+1));// the zone generator misses '0'
		System.out.println("times brute force method applied: "+bruteForceCountTotal);
		System.out.println("brute force time (seconds): "+bruteForceTimeTotal/1000.0);
		System.out.println("time taken (seconds): "+totalSeconds);
		System.out.println("number of unique diagrams: "+count);

/*		
System.out.println("failOnTotalZoneCount "+AbstractDiagram.failOnTotalZoneCount);
System.out.println("failOnZoneSizeCount "+AbstractDiagram.failOnZoneSizeCount);
System.out.println("failOnTotalContourCount "+AbstractDiagram.failOnTotalContourCount);
System.out.println("failOnZoneSizeContourCount "+AbstractDiagram.failOnZoneSizeContourCount);
System.out.println("succeedAfterMapping "+AbstractDiagram.succeedAfterMapping);
System.out.println("failAfterMapping "+AbstractDiagram.failAfterMapping);
*/
	}
	

	/**
	 * Takes a combination number, which should be seen as a binary,
	 * a 1 or 0 indicating whether the zone in the zone list is in this diagram.
	 */
	public static AbstractDiagram findAbstractDiagram(long combination, ArrayList<String> zones) {
		String diagramString = "";
		long current = combination;
		for(int i = 0; i <= zones.size(); i++) {
			if(current%2 == 1) {
				diagramString += zones.get(i)+" ";
			}
			current = current/2;
		}
		AbstractDiagram ret = new AbstractDiagram(diagramString);
		return ret;
	}

	/**
	 * Returns a list of strings containing all the zone combinations for
	 * the contours, contours labelled with a single letter starting at "a".
	 * Does not return the outside contour.
	 */
	public static ArrayList<String> findAllZones(int numberOfContours) {
/*		
		if(contours == 1) {
			return new ArrayList<String>(Arrays.asList(ZONES1));
		}
		if(contours == 2) {
			return new ArrayList<String>(Arrays.asList(ZONES2));
		}
		if(contours == 3) {
			return new ArrayList<String>(Arrays.asList(ZONES3));
		}
		if(contours == 4) {
			return new ArrayList<String>(Arrays.asList(ZONES4));
		}
*/
		ArrayList<String> zoneList = new ArrayList<String>();
		
		double numberOfZones = (int)Math.pow(2,numberOfContours)-1;
		for(int zoneNumber = 1; zoneNumber <= numberOfZones; zoneNumber++) {
			String zone = findZone(zoneNumber);
			zoneList.add(zone);

		}
		ZoneStringComparator zComp = new ZoneStringComparator();
		Collections.sort(zoneList,zComp);

		return zoneList;
	}

	
	/**
	 * Takes a zone number, which should be seen as a binary,
	 * indicating whether each contour is in the zone.
	 * Contours are assumed to be labelled from "a" onwards.
	 */
	public static String findZone(int zoneNumber) {
		String zoneString = "";
		int current = zoneNumber;
		int i = 0;
		while(current != 0) {
			if(current%2 == 1) {
				char contourChar = (char)((int)'a'+i);
				zoneString += contourChar;
			}
			current = current/2;
			i++;
		}
		return zoneString;
	}

}


