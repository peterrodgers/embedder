package euler.enumerate;

import java.util.*;

import euler.*;


public class IsomorphismInvariants {
	
	public static boolean NORMALIZE = true;

	public static void main(String[] args) {

		for(int i = 0; i < 5; i++) {
			
			outputLabelSizeCombinations(1);
			outputZoneSizeCombinations(1);
			outputZonePartitionCombinations(1);
			outputLabelPartitionCombinations(1);
			outputlabelZoneSequenceCombinations(1);
			
			outputLabelSizeCombinations(2);
			outputZoneSizeCombinations(2);
			outputZonePartitionCombinations(2);
			outputLabelPartitionCombinations(2);
			outputlabelZoneSequenceCombinations(2);
			
			outputLabelSizeCombinations(3);
			outputZoneSizeCombinations(3);
			outputZonePartitionCombinations(3);
			outputLabelPartitionCombinations(3);
			outputlabelZoneSequenceCombinations(3);
			
System.out.println("In Iteration "+i);
			outputLabelSizeCombinations(4);
			outputZoneSizeCombinations(4);
			outputZonePartitionCombinations(4);
			outputLabelPartitionCombinations(4);
			outputlabelZoneSequenceCombinations(4);
		}

	}

	public static void outputZoneSizeCombinations(int numberOfContours) {
		System.out.println("START outputZoneSizeCombinations generating all unique Euler diagrams with "+numberOfContours+" zones");

		ArrayList<String> zones = Enumerate.findAllZones(numberOfContours);
		int numberOfCombinations = (int)Math.pow(2,zones.size());
		ArrayList<AbstractDiagram> previousDiagrams = new ArrayList<AbstractDiagram>(50000);
		HashSet<String> previousStrings = new HashSet<String>(50000); // store a representation of each diagram that works with a hash table

		ArrayList<Integer> numberAtZoneSize = new ArrayList<Integer>(zones.size());
		for(int i = 0; i <= zones.size(); i++) {
			numberAtZoneSize.add(0);
		}

		long count = 0;
		long startTime = System.currentTimeMillis();
		long zoneCount = 0;
		long isomorphismCount = 0;

		long zoneTotalTime = 0;
		
		for(long combinationNumber = 0; combinationNumber < numberOfCombinations; combinationNumber++) {
			boolean foundIsomorphism = false;
			AbstractDiagram ad = Enumerate.findAbstractDiagram(combinationNumber,zones);
			if(ad.toString().length() > 0 && ad.toString().charAt(0) != 'a') {
				// only deal with combinations starting with 'a'
				continue;
			}
			
			if(NORMALIZE) {
				ad.normalize();
			}
			
			if(NORMALIZE && previousStrings.contains(ad.toString())) {
				foundIsomorphism = true;
			} else {
				for(AbstractDiagram ad2: previousDiagrams) {
					
					long start = System.currentTimeMillis();
					boolean notIsomorphic = zoneSizes(ad, ad2);
					long duration = System.currentTimeMillis()-start;
					zoneTotalTime += duration;
					
					if(notIsomorphic) {
						zoneCount++;
						foundIsomorphism = false;
					} else {
						isomorphismCount++;
					
						foundIsomorphism = ad.isomorphicTo(ad2);
						
						if(foundIsomorphism) {
							break;
						}
					}
				}
			}

			if(!foundIsomorphism) {
				count++;
				previousDiagrams.add(ad);
				previousStrings.add(ad.toString());
			}
		}
		
		System.out.println("Contours: "+numberOfContours+" "+zones);
		System.out.println("number non-isomorphic by zoneSize: "+zoneCount);
		System.out.println("number requiring isomorphism test: "+isomorphismCount);
		System.out.println("time taken for zoneSize: "+zoneTotalTime);
		System.out.println("total time taken: "+(System.currentTimeMillis()-startTime));
		System.out.println("number of unique diagrams: "+previousDiagrams.size());

	}
			
	
	
	

	public static void outputLabelSizeCombinations(int numberOfContours) {
		System.out.println("START outputLabelSizeCombinations generating all unique Euler diagrams with "+numberOfContours+" zones");

		ArrayList<String> zones = Enumerate.findAllZones(numberOfContours);
		int numberOfCombinations = (int)Math.pow(2,zones.size());
		ArrayList<AbstractDiagram> previousDiagrams = new ArrayList<AbstractDiagram>(50000);
		HashSet<String> previousStrings = new HashSet<String>(50000); // store a representation of each diagram that works with a hash table

		ArrayList<Integer> numberAtZoneSize = new ArrayList<Integer>(zones.size());
		for(int i = 0; i <= zones.size(); i++) {
			numberAtZoneSize.add(0);
		}

		long count = 0;
		long startTime = System.currentTimeMillis();
		long labelCount = 0;
		long isomorphismCount = 0;

		long labelTotalTime = 0;
		
		for(long combinationNumber = 0; combinationNumber < numberOfCombinations; combinationNumber++) {
			boolean foundIsomorphism = false;
			AbstractDiagram ad = Enumerate.findAbstractDiagram(combinationNumber,zones);
			if(ad.toString().length() > 0 && ad.toString().charAt(0) != 'a') {
				// only deal with combinations starting with 'a'
				continue;
			}
					
			if(NORMALIZE) {
				ad.normalize();
			}
			
			if(NORMALIZE && previousStrings.contains(ad.toString())) {
				foundIsomorphism = true;
			} else {
				for(AbstractDiagram ad2: previousDiagrams) {
					
					long start = System.currentTimeMillis();
					boolean notIsomorphic = labelSizes(ad, ad2);
					long duration = System.currentTimeMillis()-start;
					labelTotalTime += duration;
					
					if(notIsomorphic) {
						labelCount++;
						foundIsomorphism = false;
					} else {
						isomorphismCount++;
					
						foundIsomorphism = ad.isomorphicTo(ad2);
						
						if(foundIsomorphism) {
							break;
						}
					}
				}
			}

			if(!foundIsomorphism) {
				count++;
				previousDiagrams.add(ad);
				previousStrings.add(ad.toString());
			}
		}
		
		System.out.println("Contours: "+numberOfContours+" "+zones);
		System.out.println("number non-isomorphic by labelSize: "+labelCount);
		System.out.println("number requiring isomorphism test: "+isomorphismCount);
		System.out.println("time taken for labelSize: "+labelTotalTime);
		System.out.println("total time taken: "+(System.currentTimeMillis()-startTime));
		System.out.println("number of unique diagrams: "+previousDiagrams.size());

	}
			
	
	

	public static void outputZonePartitionCombinations(int numberOfContours) {
		System.out.println("START outputZonePartitionCombinations generating all unique Euler diagrams with "+numberOfContours+" zones");

		ArrayList<String> zones = Enumerate.findAllZones(numberOfContours);
		int numberOfCombinations = (int)Math.pow(2,zones.size());
		ArrayList<AbstractDiagram> previousDiagrams = new ArrayList<AbstractDiagram>(50000);
		HashSet<String> previousStrings = new HashSet<String>(50000); // store a representation of each diagram that works with a hash table

		ArrayList<Integer> numberAtZoneSize = new ArrayList<Integer>(zones.size());
		for(int i = 0; i <= zones.size(); i++) {
			numberAtZoneSize.add(0);
		}

		long count = 0;
		long startTime = System.currentTimeMillis();
		long zonePartitionCount = 0;
		long isomorphismCount = 0;

		long zonePartitionTotalTime = 0;
		
		for(long combinationNumber = 0; combinationNumber < numberOfCombinations; combinationNumber++) {
			boolean foundIsomorphism = false;
			AbstractDiagram ad = Enumerate.findAbstractDiagram(combinationNumber,zones);
			if(ad.toString().length() > 0 && ad.toString().charAt(0) != 'a') {
			// only deal with combinations starting with 'a'
				continue;
			}
					
			if(NORMALIZE) {
				ad.normalize();
			}
			
			if(NORMALIZE && previousStrings.contains(ad.toString())) {
				foundIsomorphism = true;
			} else {
				for(AbstractDiagram ad2: previousDiagrams) {
					
					long start = System.currentTimeMillis();
					boolean notIsomorphic = zonePartition(ad, ad2);
					long duration = System.currentTimeMillis()-start;
					zonePartitionTotalTime += duration;
					
					if(notIsomorphic) {
						zonePartitionCount++;
						foundIsomorphism = false;
					} else {
						if(zoneSizes(ad,ad2)) {System.out.println("NOTE NOTE NOTE in outputZonePartitionCombinations: zoneSizes not subsumed for diagrams "+ad+"|"+ad2);}

						isomorphismCount++;
					
						foundIsomorphism = ad.isomorphicTo(ad2);
						
						if(foundIsomorphism) {
							break;
						}
					}
				}
			}

			if(!foundIsomorphism) {
				count++;
				previousDiagrams.add(ad);
				previousStrings.add(ad.toString());
			}
		}
		
		System.out.println("Contours: "+numberOfContours+" "+zones);
		System.out.println("number non-isomorphic by zonePartitionCount: "+zonePartitionCount);
		System.out.println("number requiring isomorphism test: "+isomorphismCount);
		System.out.println("time taken for zonePartition: "+zonePartitionTotalTime);
		System.out.println("total time taken: "+(System.currentTimeMillis()-startTime));
		System.out.println("number of unique diagrams: "+previousDiagrams.size());

	}
			
	
	
	public static void outputLabelPartitionCombinations(int numberOfContours) {
		System.out.println("START outputLabelPartitionCombinations generating all unique Euler diagrams with "+numberOfContours+" zones");

		ArrayList<String> zones = Enumerate.findAllZones(numberOfContours);
		int numberOfCombinations = (int)Math.pow(2,zones.size());
		ArrayList<AbstractDiagram> previousDiagrams = new ArrayList<AbstractDiagram>(50000);
		HashSet<String> previousStrings = new HashSet<String>(50000); // store a representation of each diagram that works with a hash table

		ArrayList<Integer> numberAtZoneSize = new ArrayList<Integer>(zones.size());
		for(int i = 0; i <= zones.size(); i++) {
			numberAtZoneSize.add(0);
		}

		long count = 0;
		long startTime = System.currentTimeMillis();
		long labelPartitionCount = 0;
		long isomorphismCount = 0;

		long labelPartitionTotalTime = 0;
		
		for(long combinationNumber = 0; combinationNumber < numberOfCombinations; combinationNumber++) {
			boolean foundIsomorphism = false;
			AbstractDiagram ad = Enumerate.findAbstractDiagram(combinationNumber,zones);
			if(ad.toString().length() > 0 && ad.toString().charAt(0) != 'a') {
			// only deal with combinations starting with 'a'
				continue;
			}
					
			if(NORMALIZE) {
				ad.normalize();
			}
			
			if(NORMALIZE && previousStrings.contains(ad.toString())) {
				foundIsomorphism = true;
			} else {
				for(AbstractDiagram ad2: previousDiagrams) {
					
					long start = System.currentTimeMillis();
					boolean notIsomorphic = labelPartition(ad, ad2);
					long duration = System.currentTimeMillis()-start;
					labelPartitionTotalTime += duration;
					
					if(notIsomorphic) {
						labelPartitionCount++;
						foundIsomorphism = false;
					} else {
						if(labelSizes(ad,ad2)) {System.out.println("NOTE NOTE NOTE in outputZonePartitionCombinations: labelSizes not subsumed for diagrams "+ad+"|"+ad2);}

						isomorphismCount++;
					
						foundIsomorphism = ad.isomorphicTo(ad2);
						
						if(foundIsomorphism) {
							break;
						}
					}
				}
			}

			if(!foundIsomorphism) {
				count++;
				previousDiagrams.add(ad);
				previousStrings.add(ad.toString());
			}
		}
		
		System.out.println("Contours: "+numberOfContours+" "+zones);
		System.out.println("number non-isomorphic by labelPartitionCount: "+labelPartitionCount);
		System.out.println("number requiring isomorphism test: "+isomorphismCount);
		System.out.println("time taken for labelPartition: "+labelPartitionTotalTime);
		System.out.println("total time taken: "+(System.currentTimeMillis()-startTime));
		System.out.println("number of unique diagrams: "+previousDiagrams.size());

	}
			
	
	
	/**
	 * @return true if the abstract diagrams are not isomorphic due to the optimization.
	 */
	public static boolean zoneSizes(AbstractDiagram ad1, AbstractDiagram ad2) {
		
		ArrayList<String> zoneList1 = ad1.getZoneList();
		ArrayList<String> zoneList2 = ad2.getZoneList();

		// test if the diagrams have different size zones
		if(zoneList1.size() != zoneList2.size()) {
			return true;
		}

		
		return false;

	}
	
	/**
	 * @return true if the abstract diagrams are not isomorphic due to the optimization.
	 */
	public static boolean labelSizes(AbstractDiagram ad1, AbstractDiagram ad2) {
		
		ArrayList<String> contourList1 = ad1.getContours();
		ArrayList<String> contourList2 = ad2.getContours();
		
		// test if the diagrams have different size zones
		if(contourList1.size() != contourList2.size()) {
			return true;
		}
		
		return false;

	}
	
	
	/**
	 * @return true if the abstract diagrams are not isomorphic due to the optimization.
	 */
	public static boolean zonePartition(AbstractDiagram ad1, AbstractDiagram ad2) {
		
		ArrayList<String> zoneList1 = ad1.getZoneList();
		ArrayList<String> zoneList2 = ad2.getZoneList();
		
		Integer zonePartition1[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		Integer zonePartition2[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		
		int maxZone = 0;
		for(String zone : zoneList1) {
			int zoneLength = zone.length();
			zonePartition1[zoneLength]++;
			if(zoneLength > maxZone) {
				maxZone = zoneLength; 
			}
		}
		
		for(String zone : zoneList2) {
			int zoneLength = zone.length();
			zonePartition2[zoneLength]++;
			if(zoneLength > maxZone) {
				maxZone = zoneLength; 
			}
		}
		
		
		for(int i = 0; i <= maxZone; i++) {
			if(zonePartition1[i] != zonePartition2[i]) {
				return true;
			}
		}
		
		return false;

	}

	
	/**
	 * @return true if the abstract diagrams are not isomorphic due to the optimization.
	 */
	public static boolean labelPartition(AbstractDiagram ad1, AbstractDiagram ad2) {
		
		ArrayList<String> labelList1 = ad1.getContours();
		ArrayList<String> labelList2 = ad2.getContours();
		
		Integer labelPartition1[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		Integer labelPartition2[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		
		int maxLabel = 0;
		for(String label : labelList1) {
			int labelCount = ad1.countZonesWithContour(label);
			labelPartition1[labelCount]++;
			if(labelCount > maxLabel) {
				maxLabel = labelCount; 
			}
		}
		
		for(String label : labelList2) {
			int labelCount = ad2.countZonesWithContour(label);
			labelPartition2[labelCount]++;
			if(labelCount > maxLabel) {
				maxLabel = labelCount; 
			}
		}
		
		
		for(int i = 0; i <= maxLabel; i++) {
			if(labelPartition1[i] != labelPartition2[i]) {
				return true;
			}
		}
		
		return false;

	}



	public static void outputlabelZoneSequenceCombinations(int numberOfContours) {
		System.out.println("START outputZoneSequenceCombinations generating all unique Euler diagrams with "+numberOfContours+" zones");

		ArrayList<String> zones = Enumerate.findAllZones(numberOfContours);
		int numberOfCombinations = (int)Math.pow(2,zones.size());
		ArrayList<AbstractDiagram> previousDiagrams = new ArrayList<AbstractDiagram>(50000);
		HashSet<String> previousStrings = new HashSet<String>(50000); // store a representation of each diagram that works with a hash table

		ArrayList<Integer> numberAtZoneSize = new ArrayList<Integer>(zones.size());
		for(int i = 0; i <= zones.size(); i++) {
			numberAtZoneSize.add(0);
		}

		long count = 0;
		long startTime = System.currentTimeMillis();
		long labelZoneSequenceCount = 0;
		long isomorphismCount = 0;

		long labelZoneSequenceTotalTime = 0;
		
		for(long combinationNumber = 0; combinationNumber < numberOfCombinations; combinationNumber++) {
			boolean foundIsomorphism = false;
			AbstractDiagram ad = Enumerate.findAbstractDiagram(combinationNumber,zones);
			if(ad.toString().length() > 0 && ad.toString().charAt(0) != 'a') {
			// only deal with combinations starting with 'a'
				continue;
			}
					
			if(NORMALIZE) {
				ad.normalize();
			}
			
			if(NORMALIZE && previousStrings.contains(ad.toString())) {
				foundIsomorphism = true;
			} else {
				for(AbstractDiagram ad2: previousDiagrams) {
					
					long start = System.currentTimeMillis();
					boolean notIsomorphic = labelZoneSequence(ad, ad2);
					long duration = System.currentTimeMillis()-start;
					labelZoneSequenceTotalTime += duration;
					
					if(notIsomorphic) {
						labelZoneSequenceCount++;
						foundIsomorphism = false;
					} else {
						if(zoneSizes(ad,ad2)) {System.out.println("NOTE NOTE NOTE in zoneSequenceCombinations: zoneSizes not subsumed for diagrams "+ad+"|"+ad2);}
						if(labelSizes(ad,ad2)) {System.out.println("NOTE NOTE NOTE in zoneSequenceCombinations: labelSizes not subsumed for diagrams "+ad+"|"+ad2);}
						if(zonePartition(ad,ad2)) {System.out.println("NOTE NOTE NOTE in zoneSequenceCombinations: zonePartition not subsumed for diagrams "+ad+"|"+ad2);}
						if(labelPartition(ad,ad2)) {System.out.println("NOTE NOTE NOTE in zoneSequenceCombinations: labelPartition not subsumed for diagrams "+ad+"|"+ad2);}
						
						isomorphismCount++;
					
						foundIsomorphism = ad.isomorphicTo(ad2);
						
						if(foundIsomorphism) {
							break;
						}
					}
				}
			}

			if(!foundIsomorphism) {
				count++;
				previousDiagrams.add(ad);
				previousStrings.add(ad.toString());
			}
		}
		
		System.out.println("Contours: "+numberOfContours+" "+zones);
		System.out.println("number non-isomorphic by labelZoneSequence: "+labelZoneSequenceCount);
		System.out.println("number requiring isomorphism test: "+isomorphismCount);
		System.out.println("time taken for labelZoneSequence: "+labelZoneSequenceTotalTime);
		System.out.println("total time taken: "+(System.currentTimeMillis()-startTime));
		System.out.println("number of unique diagrams: "+previousDiagrams.size());

	}
			
	
	
	/**
	 * @return true if the abstract diagrams are not isomorphic due to the optimization.
	 */
	public static boolean labelZoneSequence(AbstractDiagram ad1, AbstractDiagram ad2) {
		
		HashMap<String,String> singleMaps = new HashMap<String,String>();
		ArrayList<Integer> listSizes = new ArrayList<Integer>();
		ArrayList<ArrayList<ArrayList<ContourZoneOccurrence>>> pairsForTesting = new ArrayList<ArrayList<ArrayList<ContourZoneOccurrence>>>();
		ArrayList<ArrayList<ContourZoneOccurrence>> contourMaps1 = ad1.findContourMaps();
		ArrayList<ArrayList<ContourZoneOccurrence>> contourMaps2 = ad2.findContourMaps();
		
		if(contourMaps1.size() != contourMaps2.size()) {
			return true;
		}
		
		for(ArrayList<ContourZoneOccurrence> czos1: contourMaps1) {
			ContourZoneOccurrence czo1 = czos1.get(0);			
			boolean foundMatch = false;
			for(ArrayList<ContourZoneOccurrence> czos2: contourMaps2) {
				if(czos1.size() != czos2.size()) {
					continue;
				}
				ContourZoneOccurrence czo2 = czos2.get(0);
				if(czo1.compareLists(czo2) == 0) {
					foundMatch = true;
					if(czos1.size() == 1) {
						// if there is only one contour, just set the mapping
						singleMaps.put(czo1.getContour(), czo2.getContour());
					} else {
						ArrayList<ArrayList<ContourZoneOccurrence>> pair = new ArrayList<ArrayList<ContourZoneOccurrence>>();
						pair.add(czos1);
						pair.add(czos2);
						pairsForTesting.add(pair);
						listSizes.add(czos1.size());
					}

					break;
				}
			}
			if(!foundMatch) {
				return true;
			}
			
		}
		return false;

	}


}
