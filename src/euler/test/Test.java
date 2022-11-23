package euler.test;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import pjr.graph.*;
import euler.*;
import euler.comparators.*;
import euler.construction.*;
import euler.drawers.*;
import euler.enumerate.*;
import euler.inductive.*;
import euler.utilities.*;

public class Test {

	/**
	 * Test
	 */
	public static void main(String[] args) {

		euler.Util.reportErrors = false;
		test1();
		test2();
		test3();
		test4();
		test5();
		test6();
		test7();
		test8();
		test9();
		test10();
/*		test11();
		test12();
		test13();
*/		test14();
		test15();
		test16();
		test17();
		test18();
		test19();
	
	}

	
	protected static void out(String s) {
		System.out.print(s);
	}
	
	protected static void outln(String s) {
		System.out.println(s);
	}
	
	
	protected static void test1() {
		outln("Starting method test1");
		ArrayList<String> list;
		ContourZoneOccurrence czo1,czo2;
		ArrayList<ArrayList<ContourZoneOccurrence>> mapList;
		ArrayList<ContourZoneOccurrence> czoList;
		ArrayList<Integer> listI1,listI2;
		ArrayList<GroupMap> listGM;
		ArrayList<String> zones;
		GroupMap gm1;
		AbstractDiagram ad1,ad2;
		AtomicAbstractDiagram aad1,aad2;
		HashMap<String,String> map;
		Graph g1;
		//DualGraph dg1;
		Edge e1;

		list = new ArrayList<String>();
		if(AbstractDiagram.hasDuplicatesInSortedList(list)) {outln("Failed Test 1.0.1");}
		if(AbstractDiagram.removeDuplicatesFromSortedList(list)) {outln("Failed Test 1.0.2");}
		if(list.size() != 0) {outln("Failed Test 1.0.3");}

		list = new ArrayList<String>();
		list.add("a");
		if(AbstractDiagram.hasDuplicatesInSortedList(list)) {outln("Failed Test 1.0.4");}
		if(AbstractDiagram.removeDuplicatesFromSortedList(list)) {outln("Failed Test 1.0.5");}
		if(list.size() != 1) {outln("Failed Test 1.0.6");}

		list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		if(AbstractDiagram.hasDuplicatesInSortedList(list)) {outln("Failed Test 1.0.7");}
		if(AbstractDiagram.removeDuplicatesFromSortedList(list)) {outln("Failed Test 1.0.8");}
		if(list.size() != 2) {outln("Failed Test 1.0.9");}
		if(!list.get(1).equals("b")) {outln("Failed Test 1.0.10");}

		list = new ArrayList<String>();
		list.add("a");
		list.add("a");
		if(!AbstractDiagram.hasDuplicatesInSortedList(list)) {outln("Failed Test 1.0.11");}
		if(!AbstractDiagram.removeDuplicatesFromSortedList(list)) {outln("Failed Test 1.0.12");}
		if(list.size() != 1)  {outln("Failed Test 1.0.3");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.0.13");}

		list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		if(AbstractDiagram.hasDuplicatesInSortedList(list)) {outln("Failed Test 1.0.14");}
		if(AbstractDiagram.removeDuplicatesFromSortedList(list)) {outln("Failed Test 1.0.15");}
		if(list.size() != 4) {outln("Failed Test 1.0.16");}
		if(!list.get(3).equals("d")) {outln("Failed Test 1.0.17");}

		list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("c");
		list.add("d");
		list.add("d");
		if(!AbstractDiagram.hasDuplicatesInSortedList(list)) {outln("Failed Test 1.0.18");}
		if(!AbstractDiagram.removeDuplicatesFromSortedList(list)) {outln("Failed Test 1.0.19");}
		if(list.size() != 4) {outln("Failed Test 1.0.20");}
		if(!list.get(3).equals("d")) {outln("Failed Test 1.0.21");}


		
		if(!AbstractDiagram.findZoneList("").toString().equals("[]")) {outln("Failed Test 1.1.1");}
		if(!AbstractDiagram.findZoneList("a").toString().equals("[a]")) {outln("Failed Test 1.1.2");}
		if(!AbstractDiagram.findZoneList("a b c ab bc").toString().equals("[a, b, c, ab, bc]")) {outln("Failed Test 1.1.3");}
		if(!AbstractDiagram.findZoneList("  a  b c ").toString().equals("[a, b, c]")) {outln("Failed Test 1.1.4");}
		if(!AbstractDiagram.findZoneList("cb").toString().equals("[bc]")) {outln("Failed Test 1.1.5");}
		if(!AbstractDiagram.findZoneList("a cba b c").toString().equals("[a, b, c, abc]")) {outln("Failed Test 1.1.6");}
		if(!AbstractDiagram.findZoneList("a b c ba cb bca").toString().equals("[a, b, c, ab, bc, abc]")) {outln("Failed Test 1.1.7");}
		if(!AbstractDiagram.findZoneList("ba a b c cb cabd").toString().equals("[a, b, c, ab, bc, abcd]")) {outln("Failed Test 1.1.8");}
		if(AbstractDiagram.findZoneList("aa") != null) {outln("Failed Test 1.1.9");}
		if(AbstractDiagram.findZoneList("dbadc") != null) {outln("Failed Test 1.1.10");}
		if(AbstractDiagram.findZoneList("a b c d abc abd daabadc") != null) {outln("Failed Test 1.1.11");}
		if(AbstractDiagram.findZoneList("a a") != null) {outln("Failed Test 1.1.12");}
		if(AbstractDiagram.findZoneList("a cd b c cd ac ab abc") != null) {outln("Failed Test 1.1.13");}
		if(!AbstractDiagram.findZoneList("0").toString().equals("[]")) {outln("Failed Test 1.1.14");}
		if(AbstractDiagram.findZoneList("0").indexOf("")!= 0) {outln("Failed Test 1.1.15");}
		if(!AbstractDiagram.findZoneList("a b c ab 0").toString().equals("[, a, b, c, ab]")) {outln("Failed Test 1.1.16");}
		if(AbstractDiagram.findZoneList("0").indexOf("")!= 0) {outln("Failed Test 1.1.17");}
		if(AbstractDiagram.findZoneList("0 0") != null) {outln("Failed Test 1.1.18");}
		if(AbstractDiagram.findZoneList("a cd b 0 c cd ac ab abc 0") != null) {outln("Failed Test 1.1.19");}
				
		ad1 = new AbstractDiagram("");
		if(!ad1.toString().equals("")) {outln("Failed Test 1.3.1");}
		if(ad1.getZoneList().size() != 0) {outln("Failed Test 1.3.1b");}
		ad1 = new AbstractDiagram("0");
		if(!ad1.toString().equals("0")) {outln("Failed Test 1.3.1b");}
		if(ad1.getZoneList().size() != 1) {outln("Failed Test 1.3.1c");}
		ad1 = new AbstractDiagram("a");
		if(!ad1.toString().equals("a")) {outln("Failed Test 1.3.2");}
		ad1 = new AbstractDiagram("b a");
		if(!ad1.toString().equals("a b")) {outln("Failed Test 1.3.3");}
		ad1 = new AbstractDiagram("ba c bc b a");
		if(!ad1.toString().equals("a b c ab bc")) {outln("Failed Test 1.3.4");}
		ad1 = new AbstractDiagram(" ba c  abdc   bc b a  ");
		if(!ad1.toString().equals("a b c ab bc abcd")) {outln("Failed Test 1.3.5");}
		ad1 = new AbstractDiagram("0");
		if(!ad1.toString().equals("0")) {outln("Failed Test 1.3.6");}
		ad1 = new AbstractDiagram("ab b a 0");
		if(!ad1.toString().equals("0 a b ab")) {outln("Failed Test 1.3.7");}
		list = new ArrayList<String>();
		ad1 = new AbstractDiagram(list);
		if(!ad1.toString().equals("")) {outln("Failed Test 1.3.8");}
		list = new ArrayList<String>();
		list.add("a");
		ad1 = new AbstractDiagram(list);
		if(!ad1.toString().equals("a")) {outln("Failed Test 1.3.9");}
		list = new ArrayList<String>();
		list.add("0");
		list.add("a");
		list.add("ab");
		list.add("abc");
		ad1 = new AbstractDiagram(list);
		if(!ad1.toString().equals("0 a ab abc")) {outln("Failed Test 1.3.10");}
		list = new ArrayList<String>();
		list.add("");
		list.add("a");
		list.add("ba");
		ad1 = new AbstractDiagram(list);
		if(!ad1.toString().equals("0 a ab")) {outln("Failed Test 1.3.11");}
		
		ad1 = new AbstractDiagram("");
		ad2 = new AbstractDiagram("");
		if(ad1.compareTo(ad2) != 0) {outln("Failed Test 1.4.1");}
		if(ad2.compareTo(ad1) != 0) {outln("Failed Test 1.4.2");}
		ad1 = new AbstractDiagram("a");
		ad2 = new AbstractDiagram("a");
		if(ad1.compareTo(ad2) != 0) {outln("Failed Test 1.4.2");}
		if(ad2.compareTo(ad1) != 0) {outln("Failed Test 1.4.3");}
		ad1 = new AbstractDiagram("a");
		ad2 = new AbstractDiagram("a b");
		if(ad1.compareTo(ad2) != -1) {outln("Failed Test 1.4.4");}
		if(ad2.compareTo(ad1) != 1) {outln("Failed Test 1.4.5");}
		ad1 = new AbstractDiagram("d");
		ad2 = new AbstractDiagram("c b");
		if(ad1.compareTo(ad2) != -1) {outln("Failed Test 1.4.6");}
		if(ad2.compareTo(ad1) != 1) {outln("Failed Test 1.4.7");}
		ad1 = new AbstractDiagram("a");
		ad2 = new AbstractDiagram("c b");
		if(ad1.compareTo(ad2) != -1) {outln("Failed Test 1.4.8");}
		if(ad2.compareTo(ad1) != 1) {outln("Failed Test 1.4.9");}
		ad1 = new AbstractDiagram("b");
		ad2 = new AbstractDiagram("ab");
		if(ad1.compareTo(ad2) != 1) {outln("Failed Test 1.4.10");}
		if(ad2.compareTo(ad1) != -1) {outln("Failed Test 1.4.11");}
		ad1 = new AbstractDiagram("a");
		ad2 = new AbstractDiagram("b");
		if(ad1.compareTo(ad2) != -1) {outln("Failed Test 1.4.12");}
		if(ad2.compareTo(ad1) != 1) {outln("Failed Test 1.4.13");}
		ad1 = new AbstractDiagram("a b c");
		ad2 = new AbstractDiagram("a b cd");
		if(ad1.compareTo(ad2) != 1) {outln("Failed Test 1.4.14");}
		if(ad2.compareTo(ad1) != -1) {outln("Failed Test 1.4.15");}
		ad1 = new AbstractDiagram("a b c");
		ad2 = new AbstractDiagram("a b d");
		if(ad1.compareTo(ad2) != -1) {outln("Failed Test 1.4.16");}
		if(ad2.compareTo(ad1) != 1) {outln("Failed Test 1.4.17");}
		ad1 = new AbstractDiagram("a b c ef");
		ad2 = new AbstractDiagram("a b cd ef");
		if(ad1.compareTo(ad2) != 1) {outln("Failed Test 1.4.18");}
		if(ad2.compareTo(ad1) != -1) {outln("Failed Test 1.4.19");}
		ad1 = new AbstractDiagram("a b c e");
		ad2 = new AbstractDiagram("a b d e");
		if(ad1.compareTo(ad2) != -1) {outln("Failed Test 1.4.20");}
		if(ad2.compareTo(ad1) != 1) {outln("Failed Test 1.4.21");}
		ad1 = new AbstractDiagram("a b c d");
		ad2 = new AbstractDiagram("a b c d");
		if(ad1.compareTo(ad2) != 0) {outln("Failed Test 1.4.22");}
		if(ad2.compareTo(ad1) != 0) {outln("Failed Test 1.4.23");}
		ad1 = new AbstractDiagram("a b c ab ac bc abc");
		ad2 = new AbstractDiagram("a b c ab ac bc abc");
		if(ad1.compareTo(ad2) != 0) {outln("Failed Test 1.4.24");}
		if(ad2.compareTo(ad1) != 0) {outln("Failed Test 1.4.25");}
		ad1 = new AbstractDiagram("  a  b c ab ac bc abc");
		ad2 = new AbstractDiagram("acb a b ab bc  c ac ");
		if(ad1.compareTo(ad2) != 0) {outln("Failed Test 1.4.24");}
		if(ad2.compareTo(ad1) != 0) {outln("Failed Test 1.4.25");}
		
		if(!Enumerate.findZone(0).equals("")) {outln("Failed Test 1.4.25.1");}
		if(!Enumerate.findZone(1).equals("a")) {outln("Failed Test 1.4.25.2");}
		if(!Enumerate.findZone(2).equals("b")) {outln("Failed Test 1.4.25.3");}
		if(!Enumerate.findZone(6).equals("bc")) {outln("Failed Test 1.4.25.4");}
		if(!Enumerate.findZone(127).equals("abcdefg")) {outln("Failed Test 1.4.25.5");}
		
		if(Enumerate.findAllZones(1).size() != (int)Math.pow(2,1)-1) {outln("Failed Test 1.4.26");}
		if(Enumerate.findAllZones(2).size() != (int)Math.pow(2,2)-1) {outln("Failed Test 1.4.27");}
		if(Enumerate.findAllZones(3).size() != (int)Math.pow(2,3)-1) {outln("Failed Test 1.4.28");}
		if(Enumerate.findAllZones(4).size() != (int)Math.pow(2,4)-1) {outln("Failed Test 1.4.29");}
		if(Enumerate.findAllZones(5).size() != (int)Math.pow(2,5)-1) {outln("Failed Test 1.4.30");}
		if(Enumerate.findAllZones(6).size() != (int)Math.pow(2,6)-1) {outln("Failed Test 1.4.31");}
		if(Enumerate.findAllZones(7).size() != (int)Math.pow(2,7)-1) {outln("Failed Test 1.4.32");}
		if(Enumerate.findAllZones(0).size() != 0) {outln("Failed Test 1.4.32.1");}

		if(!Enumerate.findAllZones(1).get(0).equals("a")) {outln("Failed Test 1.4.33");}
		if(!Enumerate.findAllZones(2).get(0).equals("a")) {outln("Failed Test 1.4.34");}
		if(!Enumerate.findAllZones(2).get(2).equals("ab")) {outln("Failed Test 1.4.35");}
		if(!Enumerate.findAllZones(2).get(0).equals("a")) {outln("Failed Test 1.4.36");}
		if(!Enumerate.findAllZones(2).get(2).equals("ab")) {outln("Failed Test 1.4.37");}
		if(!Enumerate.findAllZones(3).get(0).equals("a")) {outln("Failed Test 1.4.38");}
		if(!Enumerate.findAllZones(3).get(3).equals("ab")) {outln("Failed Test 1.4.39");}
		if(!Enumerate.findAllZones(3).get(6).equals("abc")) {outln("Failed Test 1.4.40");}
		if(!Enumerate.findAllZones(4).get(3).equals("d")) {outln("Failed Test 1.4.41");}
		if(!Enumerate.findAllZones(4).get(5).equals("ac")) {outln("Failed Test 1.4.42");}
		if(!Enumerate.findAllZones(4).get(13).equals("bcd")) {outln("Failed Test 1.4.43");}
		if(!Enumerate.findAllZones(4).get(14).equals("abcd")) {outln("Failed Test 1.4.44");}
		
		zones = Enumerate.findAllZones(3);
		ad1 = new AbstractDiagram("");
		if(Enumerate.findAbstractDiagram(0,zones).compareTo(ad1) != 0) {outln("Failed Test 1.5.1");}
		ad1 = new AbstractDiagram("a b c ab ac bc abc");
		if(Enumerate.findAbstractDiagram(127,zones).compareTo(ad1) != 0) {outln("Failed Test 1.5.2");}
		ad1 = new AbstractDiagram("a");
		if(Enumerate.findAbstractDiagram(1,zones).compareTo(ad1) != 0) {outln("Failed Test 1.5.3");}
		ad1 = new AbstractDiagram("a abc");
		if(Enumerate.findAbstractDiagram(65,zones).compareTo(ad1) != 0) {outln("Failed Test 1.5.4");}

		zones = Enumerate.findAllZones(1);
		ad1 = new AbstractDiagram("");
		if(Enumerate.findAbstractDiagram(0,zones).compareTo(ad1) != 0) {outln("Failed Test 1.5.5");}
		ad1 = new AbstractDiagram("a");
		if(Enumerate.findAbstractDiagram(1,zones).compareTo(ad1) != 0) {outln("Failed Test 1.5.6");}
		
		zones = Enumerate.findAllZones(4);
		ad1 = new AbstractDiagram("");
		if(Enumerate.findAbstractDiagram(0,zones).compareTo(ad1) != 0) {outln("Failed Test 1.5.7");}
		ad1 = new AbstractDiagram("bc");
		if(Enumerate.findAbstractDiagram(128,zones).compareTo(ad1) != 0) {outln("Failed Test 1.5.8");}
		ad1 = new AbstractDiagram("a b bc cd");
		if(Enumerate.findAbstractDiagram(643,zones).compareTo(ad1) != 0) {outln("Failed Test 1.5.9");}

		ad1 = new AbstractDiagram("");
		if(ad1.getContours().size() != 0) {outln("Failed Test 1.5.10");}
		
		ad1 = new AbstractDiagram("a");
		if(ad1.getContours().size() != 1) {outln("Failed Test 1.5.11");}
		if(!ad1.getContours().get(0).equals("a"))  {outln("Failed Test 1.5.12");}
		
		ad1 = new AbstractDiagram("ba");
		if(ad1.getContours().size() != 2) {outln("Failed Test 1.5.13");}
		if(!ad1.getContours().get(1).equals("b"))  {outln("Failed Test 1.5.14");}
		
		ad1 = new AbstractDiagram("a b");
		if(ad1.getContours().size() != 2) {outln("Failed Test 1.5.15");}
		if(!ad1.getContours().get(1).equals("b"))  {outln("Failed Test 1.5.16");}
		
		ad1 = new AbstractDiagram("a b ab");
		if(ad1.getContours().size() != 2) {outln("Failed Test 1.5.17");}
		if(!ad1.getContours().get(1).equals("b"))  {outln("Failed Test 1.5.18");}
		
		ad1 = new AbstractDiagram("a b c d abc aef abde abcdef");
		if(ad1.getContours().size() != 6) {outln("Failed Test 1.5.19");}
		if(!ad1.getContours().get(0).equals("a"))  {outln("Failed Test 1.5.20");}
		if(!ad1.getContours().get(2).equals("c"))  {outln("Failed Test 1.5.21");}
		if(!ad1.getContours().get(5).equals("f"))  {outln("Failed Test 1.5.22");}

		if(!AbstractDiagram.orderZone("").equals("")) {outln("Failed Test 1.6.1");}
		if(!AbstractDiagram.orderZone("a").equals("a")) {outln("Failed Test 1.6.2");}
		if(!AbstractDiagram.orderZone("abc").equals("abc")) {outln("Failed Test 1.6.3");}
		if(!AbstractDiagram.orderZone("ba").equals("ab")) {outln("Failed Test 1.6.4");}
		if(!AbstractDiagram.orderZone("bac").equals("abc")) {outln("Failed Test 1.6.5");}
		if(AbstractDiagram.orderZone("aa") != null) {outln("Failed Test 1.6.6");}
		if(AbstractDiagram.orderZone("bacbd") != null) {outln("Failed Test 1.6.7");}
		if(AbstractDiagram.orderZone("dbadcd") != null) {outln("Failed Test 1.6.8");}

		ad1 = new AbstractDiagram("");
		map = new HashMap<String,String>();
		if(!ad1.remapContourStrings(map)) {outln("Failed Test 1.7.1");}
		if(!ad1.toString().equals("")) {outln("Failed Test 1.7.2");}
		
		ad1 = new AbstractDiagram("");
		map = new HashMap<String,String>();
		map.put("a","b");
		if(!ad1.remapContourStrings(map)) {outln("Failed Test 1.7.3");}
		if(!ad1.toString().equals("")) {outln("Failed Test 1.7.4");}
		
		ad1 = new AbstractDiagram("c");
		map = new HashMap<String,String>();
		map.put("a","b");
		if(!ad1.remapContourStrings(map)) {outln("Failed Test 1.7.5");}
		if(!ad1.toString().equals("c")) {outln("Failed Test 1.7.6");}
		
		ad1 = new AbstractDiagram("a");
		map = new HashMap<String,String>();
		map.put("a","b");
		if(!ad1.remapContourStrings(map)) {outln("Failed Test 1.7.7");}
		if(!ad1.toString().equals("b")) {outln("Failed Test 1.7.8");}
		
		ad1 = new AbstractDiagram("a b ab");
		map = new HashMap<String,String>();
		map.put("a","c");
		if(!ad1.remapContourStrings(map)) {outln("Failed Test 1.7.9");}
		if(!ad1.toString().equals("b c bc")) {outln("Failed Test 1.7.10");}
		
		ad1 = new AbstractDiagram("ab");
		map = new HashMap<String,String>();
		map.put("a","b");
		if(ad1.remapContourStrings(map)) {outln("Failed Test 1.7.11");}
		if(!ad1.toString().equals("ab")) {outln("Failed Test 1.7.12");}

		ad1 = new AbstractDiagram("ab");
		map = new HashMap<String,String>();
		map.put("a","b");
		if(ad1.remapContourStrings(map)) {outln("Failed Test 1.7.13");}
		if(!ad1.toString().equals("ab")) {outln("Failed Test 1.7.14");}

		ad1 = new AbstractDiagram("a b");
		map = new HashMap<String,String>();
		map.put("a","b");
		if(ad1.remapContourStrings(map)) {outln("Failed Test 1.7.15");}
		if(!ad1.toString().equals("a b")) {outln("Failed Test 1.7.16");}

		ad1 = new AbstractDiagram("a b c ab ac abc abcd");
		map = new HashMap<String,String>();
		map.put("a","d");
		if(ad1.remapContourStrings(map)) {outln("Failed Test 1.7.17");}
		if(!ad1.toString().equals("a b c ab ac abc abcd")) {outln("Failed Test 1.7.18");}

		ad1 = new AbstractDiagram("a b c ab ac abc abcd");
		map = new HashMap<String,String>();
		map.put("d","a");
		if(ad1.remapContourStrings(map)) {outln("Failed Test 1.7.19");}
		if(!ad1.toString().equals("a b c ab ac abc abcd")) {outln("Failed Test 1.7.20");}

		ad1 = new AbstractDiagram("a b ab ac acd");
		map = new HashMap<String,String>();
		map.put("c","b");
		if(ad1.remapContourStrings(map)) {outln("Failed Test 1.7.21");}
		if(!ad1.toString().equals("a b ab ac acd")) {outln("Failed Test 1.7.22");}

		ad1 = new AbstractDiagram("a b ab ac acd");
		map = new HashMap<String,String>();
		map.put("c","z");
		map.put("b","z");
		if(ad1.remapContourStrings(map)) {outln("Failed Test 1.7.23");}
		if(!ad1.toString().equals("a b ab ac acd")) {outln("Failed Test 1.7.24");}

		ad1 = new AbstractDiagram("a b ab acd");
		map = new HashMap<String,String>();
		map.put("a","z");
		map.put("b","z");
		if(ad1.remapContourStrings(map)) {outln("Failed Test 1.7.25");}
		if(!ad1.toString().equals("a b ab acd")) {outln("Failed Test 1.7.26");}

		ad1 = new AbstractDiagram("a");
		map = new HashMap<String,String>();
		map.put("a","b");
		map.put("b","c");
		if(!ad1.remapContourStrings(map)) {outln("Failed Test 1.7.27");}
		if(!ad1.toString().equals("b")) {outln("Failed Test 1.7.28");}
		
		ad1 = new AbstractDiagram("a b");
		map = new HashMap<String,String>();
		map.put("a","b");
		map.put("b","a");
		if(!ad1.remapContourStrings(map)) {outln("Failed Test 1.7.29");}
		if(!ad1.toString().equals("a b")) {outln("Failed Test 1.7.30");}
		
		ad1 = new AbstractDiagram("a b c ab ac bc abc");
		map = new HashMap<String,String>();
		map.put("a","x");
		map.put("b","y");
		map.put("c","z");
		if(!ad1.remapContourStrings(map)) {outln("Failed Test 1.7.31");}
		if(!ad1.toString().equals("x y z xy xz yz xyz")) {outln("Failed Test 1.7.32");}
		
		ad1 = new AbstractDiagram("a b c ab ac bc abc");
		map = new HashMap<String,String>();
		map.put("a","b");
		map.put("b","c");
		map.put("c","a");
		if(!ad1.remapContourStrings(map)) {outln("Failed Test 1.7.33");}
		if(!ad1.toString().equals("a b c ab ac bc abc")) {outln("Failed Test 1.7.34");}
		
		
		// test number of zones
		ad1 = new AbstractDiagram("a");
		ad2 = new AbstractDiagram("");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.1");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.2");}
		
		ad1 = new AbstractDiagram("a");
		ad2 = new AbstractDiagram("a b");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.2");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.3");}
		
		ad1 = new AbstractDiagram("a");
		ad2 = new AbstractDiagram("x y");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.4");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.5");}
		
		ad1 = new AbstractDiagram("a b c ab ac bc abc");
		ad2 = new AbstractDiagram("a b c ab ac abc");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.6");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.7");}
		
		// test number of zones with same number of contours
		ad1 = new AbstractDiagram("a b c ab bc abc");
		ad2 = new AbstractDiagram("a b ab ac bc abc");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.8");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.9");}	
		
		ad1 = new AbstractDiagram("a b c ab bc abc");
		ad2 = new AbstractDiagram("x y z xy xz yz");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.10");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.11");}
	
		ad1 = new AbstractDiagram("a b c ab bc abc");
		ad2 = new AbstractDiagram("a b c ab bd abc");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.12");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.13");}
		
		ad1 = new AbstractDiagram("a b c ab bc abc");
		ad2 = new AbstractDiagram("x y z xy ye xyz");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.14");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.15");}
		
		ad1 = new AbstractDiagram("a b c ab bc abc");
		ad2 = new AbstractDiagram("c b a cb da cba");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.16");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.17");}
		
		// test some isomorphic diagrams
		ad1 = new AbstractDiagram("");
		ad2 = new AbstractDiagram("");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.18");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.19");}
		
		ad1 = new AbstractDiagram("a");
		ad2 = new AbstractDiagram("a");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.20");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.21");}
		
		ad1 = new AbstractDiagram("a");
		ad2 = new AbstractDiagram("c");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.22");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.23");}
		
		ad1 = new AbstractDiagram("a b c ab bc abc");
		ad2 = new AbstractDiagram("c b a cb ba cba");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.24");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.25");}
		
		ad1 = new AbstractDiagram("a b c ab bc abc");
		ad2 = new AbstractDiagram("x y z xy yz xyz");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.26");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.27");}
		
		//test the number of contours in zones of the same number
		ad1 = new AbstractDiagram("a b c ab bc abd");
		ad2 = new AbstractDiagram("c b a ab cd cba");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.28");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.29");}
		
		ad1 = new AbstractDiagram("a b c ab bc abd");
		ad2 = new AbstractDiagram("x y z xy zq xyz");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.30");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.31");}

		ad1 = new AbstractDiagram("a b c ab bc abd");
		ad2 = new AbstractDiagram("d c b ad bc acd");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.32");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.33");}

		ad1 = new AbstractDiagram("a b c ef fg efgl");
		ad2 = new AbstractDiagram("d c b hi jk hijk");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.34");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.35");}

		//test the brute force bit
		ad1 = new AbstractDiagram("a ab ac bc abc");
		ad2 = new AbstractDiagram("a ab ac bc abc");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.36");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.37");}

		ad1 = new AbstractDiagram("c ab ac bc abc");
		ad2 = new AbstractDiagram("a ab ac bc abc");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.38");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.39");}

		ad1 = new AbstractDiagram("c ab ac bc abc");
		ad2 = new AbstractDiagram("z xy xz yz xyz");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.40");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.41");}

		ad1 = new AbstractDiagram("c ab ac bc abc");
		ad2 = new AbstractDiagram("b bc ba ca bca");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.42");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.43");}

		ad1 = new AbstractDiagram("c d abd acd bc abcd");
		ad2 = new AbstractDiagram("z q xyq xzq yz xyzq");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.44");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.45");}

		ad1 = new AbstractDiagram("c d abd acd bc abcd");
		ad2 = new AbstractDiagram("b c dac dbc ab dabc");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.46");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.47");}

		ad1 = new AbstractDiagram("c d abd acd abc bc abcd");
		ad2 = new AbstractDiagram("c d abd acd abc bc abcd");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.48");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.49");}

		ad1 = new AbstractDiagram("c d ab ad bc abcd");
		ad2 = new AbstractDiagram("c d ab ad bc abcd");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.50");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.51");}

		ad1 = new AbstractDiagram("a b d e ad bd cd de abc bcd abcd abcde");
		ad2 = new AbstractDiagram("a b d e ad bd cd de abc bcd abcd abcde");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.51.1");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.51.2");}

		ad1 = new AbstractDiagram("a b d e ad bd cd de abc bcd abcd abcde");
		ad2 = new AbstractDiagram("b c e a be ce de ea bcd cde bcde abcde");
		if(!ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.52.3");}
		if(!ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.52.4");}

		// test no brute force mapping cases
		ad1 = new AbstractDiagram("c d ac ad bc abcd");
		ad2 = new AbstractDiagram("c d ac ab bd abcd");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.52");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.53");}

		ad1 = new AbstractDiagram("a c d ac ad bc abcd");
		ad2 = new AbstractDiagram("a c d ab ad bc abcd");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.54");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.55");}

		ad1 = new AbstractDiagram("a c d ac ad bc abc adc abcd");
		ad2 = new AbstractDiagram("a c d ac ad bc abc bcd abcd");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.56");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.57");}

		ad1 = new AbstractDiagram("b d ad bd cd bcd");
		ad2 = new AbstractDiagram("a d ad bd cd abc");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.58");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.59");}

		ad1 = new AbstractDiagram("b d e ad bd cd de abc bcd bcde");
		ad2 = new AbstractDiagram("a d e ad bd cd de abc bcd abce");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.60");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.61");}

		// distinct graphs/hypergraphs with node degrees (contour zone membership) equivalent
		ad1 = new AbstractDiagram("ab ad af bc be cd de ef");
		ad2 = new AbstractDiagram("ab ae af bc bd cd de ef");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.62");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.63");}

		ad1 = new AbstractDiagram("a ab acd");
		ad2 = new AbstractDiagram("a ab bcd");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.64");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.65");}

		ad1 = new AbstractDiagram("a b c d ab ac bcd");
		ad2 = new AbstractDiagram("a b c d ab cd abc");
		if(ad1.isomorphicTo(ad2)) {outln("Failed Test 1.8.66");}
		if(ad2.isomorphicTo(ad1)) {outln("Failed Test 1.8.67");}


		ad1 = new AbstractDiagram("");
		if(!ad1.addZone("b")) {outln("Failed Test 1.9.1");}
		if(ad1.getZoneList().size() != 1) {outln("Failed Test 1.9.2");}
		if(!ad1.getZoneList().get(0).equals("b")) {outln("Failed Test 1.9.3");}
		if(ad1.addZone("b")) {outln("Failed Test 1.9.4");}
			
		if(!ad1.addZone("a"))  {outln("Failed Test 1.9.5");}
		if(ad1.getZoneList().size() != 2) {outln("Failed Test 1.9.6");}
		if(!ad1.getZoneList().get(0).equals("a")) {outln("Failed Test 1.9.7");}
		if(ad1.addZone("a")) {outln("Failed Test 1.9.8");}
		
		if(!ad1.addZone("ba"))  {outln("Failed Test 1.9.9");}
		if(ad1.getZoneList().size() != 3) {outln("Failed Test 1.9.10");}
		if(!ad1.getZoneList().get(2).equals("ab")) {outln("Failed Test 1.9.11");}
		if(ad1.addZone("ab")) {outln("Failed Test 1.9.12");}
		
		if(!ad1.addZone("c"))  {outln("Failed Test 1.9.13");}
		if(ad1.getZoneList().size() != 4) {outln("Failed Test 1.9.14");}
		if(!ad1.getZoneList().get(2).equals("c")) {outln("Failed Test 1.9.15");}
		if(ad1.addZone("b")) {outln("Failed Test 1.9.16");}
			
		if(!ad1.addZone(""))  {outln("Failed Test 1.9.17");}
		if(ad1.getZoneList().size() != 5) {outln("Failed Test 1.9.18");}
		if(!ad1.getZoneList().get(0).equals("")) {outln("Failed Test 1.9.19");}
		if(ad1.addZone("")) {outln("Failed Test 1.9.20");}
		if(ad1.addZone("0"))  {outln("Failed Test 1.9.20a");}
		if(ad1.getZoneList().size() != 5) {outln("Failed Test 1.9.20b");}
		
		ad1 = new AbstractDiagram("");
		if(!ad1.addZone("0"))  {outln("Failed Test 1.9.20c");}
		if(ad1.getZoneList().size() != 1) {outln("Failed Test 1.9.20d");}
		if(!ad1.getZoneList().get(0).equals("")) {outln("Failed Test 1.9.20e");}
		
		ad1 = new AbstractDiagram("");
		if(!ad1.addZone("")) {outln("Failed Test 1.9.21");}
		if(ad1.getZoneList().size() != 1) {outln("Failed Test 1.9.22");}
		if(!ad1.getZoneList().get(0).equals("")) {outln("Failed Test 1.9.23");}
		if(ad1.addZone("")) {outln("Failed Test 1.9.24");}

		ad1 = new AbstractDiagram("");
		if(ad1.removeZone("a")) {outln("Failed Test 1.9.25");}
		if(ad1.getZoneList().size() != 0) {outln("Failed Test 1.9.26");}
		ad1.addZone("a");
		if(!ad1.removeZone("a")) {outln("Failed Test 1.9.27");}
		if(ad1.getZoneList().size() != 0) {outln("Failed Test 1.9.28");}

		ad1 = new AbstractDiagram("0 a b ab abc");
		if(!ad1.removeZone("ab")) {outln("Failed Test 1.9.29");}
		if(!ad1.toString().equals("0 a b abc")) {outln("Failed Test 1.9.30");}
		ad1.addZone("ab");
		if(!ad1.toString().equals("0 a b ab abc")) {outln("Failed Test 1.9.31");}
		if(!ad1.removeZone("ab")) {outln("Failed Test 1.9.32");}
		if(!ad1.toString().equals("0 a b abc")) {outln("Failed Test 1.9.33");}
		if(ad1.removeZone("xyz")) {outln("Failed Test 1.9.34");}
		if(!ad1.toString().equals("0 a b abc")) {outln("Failed Test 1.9.35");}
		if(!ad1.removeZone("a")) {outln("Failed Test 1.9.36");}
		if(!ad1.toString().equals("0 b abc")) {outln("Failed Test 1.9.37");}
		if(!ad1.removeZone("0")) {outln("Failed Test 1.9.38");}
		if(!ad1.toString().equals("b abc")) {outln("Failed Test 1.9.39");}
		if(!ad1.removeZone("abc")) {outln("Failed Test 1.9.40");}
		if(!ad1.toString().equals("b")) {outln("Failed Test 1.9.41");}
		if(ad1.removeZone("")) {outln("Failed Test 1.9.42");}
		if(!ad1.toString().equals("b")) {outln("Failed Test 1.9.43");}
		if(!ad1.removeZone("b")) {outln("Failed Test 1.9.44");}
		if(!ad1.toString().equals("")) {outln("Failed Test 1.9.45");}
		if(ad1.getZoneList().size() != 0) {outln("Failed Test 1.9.46");}

		ad1 = AbstractDiagram.VennFactory(0);
		if(!ad1.toString().equals("0")) {outln("Failed Test 1.10.1");}
		ad1 = AbstractDiagram.VennFactory(1);
		if(!ad1.toString().equals("0 a")) {outln("Failed Test 1.10.2");}
		ad1 = AbstractDiagram.VennFactory(2);
		if(!ad1.toString().equals("0 a b ab")) {outln("Failed Test 1.10.3");}
		ad1 = AbstractDiagram.VennFactory(3);
		if(!ad1.toString().equals("0 a b c ab ac bc abc")) {outln("Failed Test 1.10.4");}
		ad1 = AbstractDiagram.VennFactory(4);
		if(!ad1.toString().equals("0 a b c d ab ac ad bc bd cd abc abd acd bcd abcd")) {outln("Failed Test 1.10.5");}
		
		ad1 = new AbstractDiagram("0 ab");
		if(!ad1.contourContainment("a","b")) {outln("Failed Test 1.11.1");}
		if(!ad1.contourContainment("b","a")) {outln("Failed Test 1.11.1a");}
		
		ad1 = new AbstractDiagram("0 a ab");
		if(!ad1.contourContainment("a","b")) {outln("Failed Test 1.11.1b");}
		if(ad1.contourContainment("b","a")) {outln("Failed Test 1.11.1c");}
		
		ad1 = new AbstractDiagram("0 ab ac");
		if(!ad1.contourContainment("a","b")) {outln("Failed Test 1.11.2");}
		if(!ad1.contourContainment("a","c")) {outln("Failed Test 1.11.3");}
		if(ad1.contourContainment("b","c")) {outln("Failed Test 1.11.4");}
		if(ad1.contourContainment("b","a")) {outln("Failed Test 1.11.5");}
		if(ad1.contourContainment("c","a")) {outln("Failed Test 1.11.6");}
		
		ad1 = AbstractDiagram.VennFactory(4);
		if(ad1.contourContainment("a","b")) {outln("Failed Test 1.11.7");}
		if(ad1.contourContainment("b","d")) {outln("Failed Test 1.11.8");}

		ad1 = new AbstractDiagram("0 ab ac abd abe abde");
		if(!ad1.contourContainment("a","b")) {outln("Failed Test 1.11.9");}
		if(!ad1.contourContainment("a","c")) {outln("Failed Test 1.11.10");}
		if(ad1.contourContainment("b","c")) {outln("Failed Test 1.11.11");}
		if(ad1.contourContainment("b","a")) {outln("Failed Test 1.11.12");}
		if(ad1.contourContainment("c","a")) {outln("Failed Test 1.11.13");}
		if(!ad1.contourContainment("a","e")) {outln("Failed Test 1.11.14");}
		if(ad1.contourContainment("e","a")) {outln("Failed Test 1.11.15");}
		if(!ad1.contourContainment("a","d")) {outln("Failed Test 1.11.16");}
		if(ad1.contourContainment("d","a")) {outln("Failed Test 1.11.17");}
		if(!ad1.contourContainment("b","e")) {outln("Failed Test 1.11.18");}
		if(ad1.contourContainment("e","b")) {outln("Failed Test 1.11.19");}
		if(ad1.contourContainment("c","e")) {outln("Failed Test 1.11.20");}
		if(ad1.contourContainment("e","c")) {outln("Failed Test 1.11.21");}
		
		
		ad1 = new AbstractDiagram("0 ab");
		if(!ad1.contoursConcurrent("a","b")) {outln("Failed Test 1.12.1");}
		if(!ad1.contoursConcurrent("b","a")) {outln("Failed Test 1.12.2");}
		
		ad1 = new AbstractDiagram("0 a ab");
		if(ad1.contoursConcurrent("a","b")) {outln("Failed Test 1.12.3");}
		if(ad1.contoursConcurrent("b","a")) {outln("Failed Test 1.12.4");}
		
		ad1 = new AbstractDiagram("0 ab ac");
		if(ad1.contoursConcurrent("a","b")) {outln("Failed Test 1.12.5");}
		if(ad1.contoursConcurrent("b","a")) {outln("Failed Test 1.12.6");}
		if(ad1.contoursConcurrent("c","b")) {outln("Failed Test 1.12.7");}
		if(ad1.contoursConcurrent("b","c")) {outln("Failed Test 1.12.8");}
		
		
		ad1 = new AbstractDiagram("0 a abc");
		if(ad1.contoursConcurrent("a","b")) {outln("Failed Test 1.12.9");}
		if(ad1.contoursConcurrent("b","a")) {outln("Failed Test 1.12.10");}
		if(!ad1.contoursConcurrent("c","b")) {outln("Failed Test 1.12.11");}
		if(!ad1.contoursConcurrent("b","c")) {outln("Failed Test 1.12.12");}
		
		ad1 = new AbstractDiagram("0 a b abc abcdef");
		if(ad1.contoursConcurrent("a","f")) {outln("Failed Test 1.12.13");}
		if(ad1.contoursConcurrent("f","a")) {outln("Failed Test 1.12.14");}
		if(ad1.contoursConcurrent("c","b")) {outln("Failed Test 1.12.15");}
		if(!ad1.contoursConcurrent("d","e")) {outln("Failed Test 1.12.16");}
		if(!ad1.contoursConcurrent("e","d")) {outln("Failed Test 1.12.17");}
		if(!ad1.contoursConcurrent("d","f")) {outln("Failed Test 1.12.18");}
		if(!ad1.contoursConcurrent("f","d")) {outln("Failed Test 1.12.19");}
		if(!ad1.contoursConcurrent("f","e")) {outln("Failed Test 1.12.20");}
		if(!ad1.contoursConcurrent("e","f")) {outln("Failed Test 1.12.21");}
		
		ad1 = new AbstractDiagram("0 a b");
		if(ad1.contoursIntersect("a","b")) {outln("Failed Test 1.13.1");}
		if(ad1.contoursIntersect("b","a")) {outln("Failed Test 1.13.2");}

		ad1 = new AbstractDiagram("0 a b ab");
		if(!ad1.contoursIntersect("a","b")) {outln("Failed Test 1.13.3");}
		if(!ad1.contoursIntersect("b","a")) {outln("Failed Test 1.13.4");}

		ad1 = new AbstractDiagram("0 ab");
		if(!ad1.contoursIntersect("a","b")) {outln("Failed Test 1.13.5");}
		if(!ad1.contoursIntersect("b","a")) {outln("Failed Test 1.13.6");}

		ad1 = new AbstractDiagram("0 a ab");
		if(!ad1.contoursIntersect("a","b")) {outln("Failed Test 1.13.7");}
		if(!ad1.contoursIntersect("b","a")) {outln("Failed Test 1.13.8");}

		ad1 = new AbstractDiagram("0 a b c d ab abc ce df");
		if(!ad1.contoursIntersect("a","b")) {outln("Failed Test 1.13.9");}
		if(!ad1.contoursIntersect("b","a")) {outln("Failed Test 1.13.10");}
		if(ad1.contoursIntersect("e","f")) {outln("Failed Test 1.13.11");}
		if(ad1.contoursIntersect("f","e")) {outln("Failed Test 1.13.12");}
		if(ad1.contoursIntersect("b","d")) {outln("Failed Test 1.13.13");}
		if(ad1.contoursIntersect("d","b")) {outln("Failed Test 1.13.14");}
		if(!ad1.contoursIntersect("a","c")) {outln("Failed Test 1.13.15");}
		if(!ad1.contoursIntersect("c","a")) {outln("Failed Test 1.13.16");}

		ad1 = new AbstractDiagram("0");
		list = ad1.findIntersectionGroups();
		if(list.size() != 0) {outln("Failed Test 1.14.1");}
		
		ad1 = new AbstractDiagram("0 a");
		list = ad1.findIntersectionGroups();
		if(list.size() != 1) {outln("Failed Test 1.14.2");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.14.3");}
		
		ad1 = new AbstractDiagram("0 a b");
		list = ad1.findIntersectionGroups();
		if(list.size() != 2) {outln("Failed Test 1.14.4");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.14.5");}
		if(!list.get(1).equals("b")) {outln("Failed Test 1.14.6");}
		
		ad1 = new AbstractDiagram("0 a b ab");
		list = ad1.findIntersectionGroups();
		if(list.size() != 1) {outln("Failed Test 1.14.7");}
		if(!list.get(0).equals("ab")) {outln("Failed Test 1.14.8");}

		ad1 = new AbstractDiagram("0 a ab ac");
		list = ad1.findIntersectionGroups();
		if(list.size() != 3) {outln("Failed Test 1.14.7");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.14.9");}
		if(!list.get(1).equals("b")) {outln("Failed Test 1.14.10");}
		if(!list.get(2).equals("c")) {outln("Failed Test 1.14.11");}

		ad1 = new AbstractDiagram("0 a ab ac abc");
		list = ad1.findIntersectionGroups();
		if(list.size() != 2) {outln("Failed Test 1.14.12");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.14.13");}
		if(!list.get(1).equals("bc")) {outln("Failed Test 1.14.14");}

		ad1 = new AbstractDiagram("0 a ab ac abcd");
		list = ad1.findIntersectionGroups();
		if(list.size() != 3) {outln("Failed Test 1.14.13");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.14.14");}
		if(!list.get(1).equals("d")) {outln("Failed Test 1.14.15");}
		if(!list.get(2).equals("bc")) {outln("Failed Test 1.14.16");}

		ad1 = new AbstractDiagram("0 a e ab ac abcd ef eg eh egij");
		list = ad1.findIntersectionGroups();
		if(list.size() != 9) {outln("Failed Test 1.14.17");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.14.18");}
		if(!list.get(1).equals("d")) {outln("Failed Test 1.14.19");}
		if(!list.get(2).equals("e")) {outln("Failed Test 1.14.20");}
		if(!list.get(3).equals("f")) {outln("Failed Test 1.14.21");}
		if(!list.get(4).equals("g")) {outln("Failed Test 1.14.22");}
		if(!list.get(5).equals("h")) {outln("Failed Test 1.14.23");}
		if(!list.get(6).equals("i")) {outln("Failed Test 1.14.24");}
		if(!list.get(7).equals("j")) {outln("Failed Test 1.14.25");}
		if(!list.get(8).equals("bc")) {outln("Failed Test 1.14.26");}

		ad1 = new AbstractDiagram("0 a b ab c d cd");
		list = ad1.findIntersectionGroups();
		if(list.size() != 2) {outln("Failed Test 1.14.27");}
		if(!list.get(0).equals("ab")) {outln("Failed Test 1.14.28");}
		if(!list.get(1).equals("cd")) {outln("Failed Test 1.14.29");}

		ad1 = new AbstractDiagram("0 a b ab c d cd cde cdf cdef");
		list = ad1.findIntersectionGroups();
		if(list.size() != 3) {outln("Failed Test 1.14.30");}
		if(!list.get(0).equals("ab")) {outln("Failed Test 1.14.31");}
		if(!list.get(1).equals("cd")) {outln("Failed Test 1.14.32");}
		if(!list.get(2).equals("ef")) {outln("Failed Test 1.14.33");}

		ad1 = new AbstractDiagram("0 a b ab c d cd cde cdf cdef cdg cdh cdgh");
		list = ad1.findIntersectionGroups();
		if(list.size() != 4) {outln("Failed Test 1.14.34");}
		if(!list.get(0).equals("ab")) {outln("Failed Test 1.14.35");}
		if(!list.get(1).equals("cd")) {outln("Failed Test 1.14.36");}
		if(!list.get(2).equals("ef")) {outln("Failed Test 1.14.37");}
		if(!list.get(3).equals("gh")) {outln("Failed Test 1.14.38");}

		ad1 = new AbstractDiagram("0 a b ab c d cd cde cdf cdef cdg cdh cdgh cdghi cdghj cdghij");
		list = ad1.findIntersectionGroups();
		if(list.size() != 5) {outln("Failed Test 1.14.39");}
		if(!list.get(0).equals("ab")) {outln("Failed Test 1.14.40");}
		if(!list.get(1).equals("cd")) {outln("Failed Test 1.14.41");}
		if(!list.get(2).equals("ef")) {outln("Failed Test 1.14.42");}
		if(!list.get(3).equals("gh")) {outln("Failed Test 1.14.43");}
		if(!list.get(4).equals("ij")) {outln("Failed Test 1.14.44");}
		
		ad1 = AbstractDiagram.VennFactory(3);
		list = ad1.findIntersectionGroups();
		if(list.size() != 1) {outln("Failed Test 1.14.45");}
		if(!list.get(0).equals("abc")) {outln("Failed Test 1.14.46");}
		
		ad1 = AbstractDiagram.VennFactory(8);
		list = ad1.findIntersectionGroups();
		if(list.size() != 1) {outln("Failed Test 1.14.47");}
		if(!list.get(0).equals("abcdefgh")) {outln("Failed Test 1.14.48");}
		
		ad1 = new AbstractDiagram("0 a b ac bc");
		list = ad1.findIntersectionGroups();
		if(list.size() != 1) {outln("Failed Test 1.14.49");}
		if(!list.get(0).equals("abc")) {outln("Failed Test 1.14.50");}
		
		ad1 = new AbstractDiagram("0 c bc");
		list = ad1.findIntersectionGroups();
		if(list.size() != 2) {outln("Failed Test 1.14.51");}
		if(!list.get(0).equals("b")) {outln("Failed Test 1.14.52");}
		if(!list.get(1).equals("c")) {outln("Failed Test 1.14.53");}

		
		
		ad1 = new AbstractDiagram("0 a b");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 2) {outln("Failed Test 1.15.1");}
		if(g1.getEdges().size() != 0) {outln("Failed Test 1.15.2");}
		
		ad1 = new AbstractDiagram("0 a ab");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 2) {outln("Failed Test 1.15.3");}
		if(g1.getEdges().size() != 1) {outln("Failed Test 1.15.4");}
		if(!g1.getEdges().get(0).getFrom().getLabel().equals("b")) {outln("Failed Test 1.15.5");}
		if(!g1.getEdges().get(0).getTo().getLabel().equals("a")) {outln("Failed Test 1.15.6");}
		
		ad1 = new AbstractDiagram("0 b ab");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 2) {outln("Failed Test 1.15.7");}
		if(g1.getEdges().size() != 1) {outln("Failed Test 1.15.8");}
		if(!g1.getEdges().get(0).getFrom().getLabel().equals("a")) {outln("Failed Test 1.15.9");}
		if(!g1.getEdges().get(0).getTo().getLabel().equals("b")) {outln("Failed Test 1.15.10");}
		
		ad1 = new AbstractDiagram("0 ab");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 2) {outln("Failed Test 1.15.11");}
		if(g1.getEdges().size() != 2) {outln("Failed Test 1.15.12");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("a")) {
				if(!e.getTo().getLabel().equals("b")) {outln("Failed Test 1.15.13");}
			} else if(e.getFrom().getLabel().equals("b")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.14");}
			} else {
				outln("Failed Test 1.15.15");
			}
		}
		
		ad1 = new AbstractDiagram("0 a b ab ac");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 3) {outln("Failed Test 1.15.16");}
		if(g1.getEdges().size() != 1) {outln("Failed Test 1.15.17");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.18");}
			} else {
				outln("Failed Test 1.15.19");
			}
		}
		
		ad1 = new AbstractDiagram("0 a b ab ac acd ace acde");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 5) {outln("Failed Test 1.15.20");}
		if(g1.getEdges().size() != 5) {outln("Failed Test 1.15.21");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.22");}
			} else if(e.getFrom().getLabel().equals("d")) {
				if(!e.getTo().getLabel().equals("c") && !e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.23");}
			} else if(e.getFrom().getLabel().equals("e")) {
				if(!e.getTo().getLabel().equals("c") && !e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.24");}
			} else {
				outln("Failed Test 1.15.25");
			}
		}
		
		ad1 = new AbstractDiagram("0 a ab ac ad");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 4) {outln("Failed Test 1.15.26");}
		if(g1.getEdges().size() != 3) {outln("Failed Test 1.15.27");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("b")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.28");}
			} else if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.29");}
			} else if(e.getFrom().getLabel().equals("d")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.30");}
			} else {
				outln("Failed Test 1.15.31");
			}
		}
		
		ad1 = new AbstractDiagram("0 a ab ac ad adef adefg adefh adefgh adefi");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 9) {outln("Failed Test 1.15.32");}
		if(g1.getEdges().size() != 21) {outln("Failed Test 1.15.33");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("b")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.34");}
			} else if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.35");}
			} else if(e.getFrom().getLabel().equals("d")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.36");}
			} else if(e.getFrom().getLabel().equals("e")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("d") && !e.getTo().getLabel().equals("f")) {outln("Failed Test 1.15.37");}
			} else if(e.getFrom().getLabel().equals("f")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("d") && !e.getTo().getLabel().equals("e")) {outln("Failed Test 1.15.38");}
			} else if(e.getFrom().getLabel().equals("g")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("d") && !e.getTo().getLabel().equals("e") && !e.getTo().getLabel().equals("f")) {outln("Failed Test 1.15.39");}
			} else if(e.getFrom().getLabel().equals("h")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("d") && !e.getTo().getLabel().equals("e") && !e.getTo().getLabel().equals("f")) {outln("Failed Test 1.15.40");}
			} else if(e.getFrom().getLabel().equals("i")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("d") && !e.getTo().getLabel().equals("e") && !e.getTo().getLabel().equals("f")) {outln("Failed Test 1.15.41");}
			} else {
				outln("Failed Test 1.15.42");
			}
		}
		
		ad1 = new AbstractDiagram("0 b ac ad be bef");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 6) {outln("Failed Test 1.15.43");}
		if(g1.getEdges().size() != 5) {outln("Failed Test 1.15.44");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.45");}
			} else if(e.getFrom().getLabel().equals("d")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.46");}
			} else if(e.getFrom().getLabel().equals("e")) {
				if(!e.getTo().getLabel().equals("b")) {outln("Failed Test 1.15.47");}
			} else if(e.getFrom().getLabel().equals("f")) {
				if(!e.getTo().getLabel().equals("b") && !e.getTo().getLabel().equals("e")) {outln("Failed Test 1.15.48");}
			} else {
				outln("Failed Test 1.15.49");
			}
		}
		
		ad1 = new AbstractDiagram("0 a b ac bc");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 3) {outln("Failed Test 1.15.50");}
		if(g1.getEdges().size() != 0) {outln("Failed Test 1.15.51");}
		
		ad1 = new AbstractDiagram("0 a b ab ac abc abce");
		g1 = ad1.generateContainmentGraph();
		if(g1.getNodes().size() != 4) {outln("Failed Test 1.15.52");}
		if(g1.getEdges().size() != 4) {outln("Failed Test 1.15.53");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("e")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("b") && !e.getTo().getLabel().equals("c")) {outln("Failed Test 1.15.54");}
			} else if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.15.55");}
			} else {
				outln("Failed Test 1.15.56");
			}
		}
		
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","c");
		if(AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.1");}
		
		g1 = new Graph();
		g1.addAdjacencyEdge("a","b");
		e1 = g1.addAdjacencyEdge("a","c");
		g1.addAdjacencyEdge("b","c");
		if(!AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.2");}
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","b");
		g1.addAdjacencyEdge("a","c");
		g1.addAdjacencyEdge("b","c");
		if(AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.3");}
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","b");
		g1.addAdjacencyEdge("b","a");
		g1.addAdjacencyEdge("a","c");
		g1.addAdjacencyEdge("b","c");
		if(AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.4");}
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","b");
		g1.addAdjacencyEdge("a","c");
		g1.addAdjacencyEdge("c","d");
		g1.addAdjacencyEdge("d","b");
		if(!AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.5");}
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","z");
		g1.addAdjacencyEdge("c","a");
		g1.addAdjacencyEdge("c","d");
		g1.addAdjacencyEdge("d","z");
		if(AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.6");}
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","z");
		g1.addAdjacencyEdge("a","c");
		g1.addAdjacencyEdge("a","d");
		g1.addAdjacencyEdge("c","e");
		g1.addAdjacencyEdge("d","f");
		g1.addAdjacencyEdge("d","z");
		if(!AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.7");}
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","z");
		g1.addAdjacencyEdge("a","c");
		g1.addAdjacencyEdge("a","d");
		g1.addAdjacencyEdge("d","a");
		g1.addAdjacencyEdge("c","e");
		g1.addAdjacencyEdge("d","z");
		if(AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.8");}
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","z");
		g1.addAdjacencyEdge("a","c");
		g1.addAdjacencyEdge("a","x");
		g1.addAdjacencyEdge("x","d");
		g1.addAdjacencyEdge("d","a");
		g1.addAdjacencyEdge("c","e");
		g1.addAdjacencyEdge("d","z");
		if(!AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.8a");}
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","z");
		g1.addAdjacencyEdge("a","c");
		g1.addAdjacencyEdge("a","d");
		g1.addAdjacencyEdge("c","a");
		g1.addAdjacencyEdge("d","f");
		g1.addAdjacencyEdge("d","a");
		if(AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.9");}
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","z");
		g1.addAdjacencyEdge("a","b");
		g1.addAdjacencyEdge("b","c");
		g1.addAdjacencyEdge("c","b");
		g1.addAdjacencyEdge("c","e");
		g1.addAdjacencyEdge("e","z");
		if(AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.10");}
		
		g1 = new Graph();
		e1 = g1.addAdjacencyEdge("a","z");
		g1.addAdjacencyEdge("a","b");
		g1.addAdjacencyEdge("b","c");
		g1.addAdjacencyEdge("c","x");
		g1.addAdjacencyEdge("x","y");
		g1.addAdjacencyEdge("y","b");
		g1.addAdjacencyEdge("c","e");
		g1.addAdjacencyEdge("e","z");
		if(!AbstractDiagram.alternativePath(g1, e1)) {outln("Failed Test 1.16.10a");}
		
		ad1 = new AbstractDiagram("0 a b");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 2) {outln("Failed Test 1.17.1");}
		if(g1.getEdges().size() != 0) {outln("Failed Test 1.17.2");}
		
		ad1 = new AbstractDiagram("0 a ab");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 2) {outln("Failed Test 1.17.3");}
		if(g1.getEdges().size() != 1) {outln("Failed Test 1.17.4");}
		if(!g1.getEdges().get(0).getFrom().getLabel().equals("b")) {outln("Failed Test 1.17.5");}
		if(!g1.getEdges().get(0).getTo().getLabel().equals("a")) {outln("Failed Test 1.17.6");}
		
		ad1 = new AbstractDiagram("0 a ab ac abc");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 3) {outln("Failed Test 1.17.7");}
		if(g1.getEdges().size() != 2) {outln("Failed Test 1.17.8");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("b")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.17.9");}
			} else if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.17.10");}
			} else {
				outln("Failed Test 1.17.11");
			}
		}
		
		ad1 = new AbstractDiagram("0 a ab abc");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 3) {outln("Failed Test 1.17.11");}
		if(g1.getEdges().size() != 2) {outln("Failed Test 1.17.12");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("b")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.17.13");}
			} else if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("b")) {outln("Failed Test 1.17.14");}
			} else {
				outln("Failed Test 1.17.15");
			}
		}
		
		ad1 = new AbstractDiagram("0 a b ab abc abcd abce bf bfg");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 7) {outln("Failed Test 1.17.16");}
		if(g1.getEdges().size() != 6) {outln("Failed Test 1.17.17");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("b")) {outln("Failed Test 1.17.18");}
			} else if(e.getFrom().getLabel().equals("d")) {
				if(!e.getTo().getLabel().equals("c")) {outln("Failed Test 1.17.19");}
			} else if(e.getFrom().getLabel().equals("e")) {
				if(!e.getTo().getLabel().equals("c")) {outln("Failed Test 1.17.20");}
			} else if(e.getFrom().getLabel().equals("f")) {
				if(!e.getTo().getLabel().equals("b")) {outln("Failed Test 1.17.21");}
			} else if(e.getFrom().getLabel().equals("g")) {
				if(!e.getTo().getLabel().equals("f")) {outln("Failed Test 1.17.22");}
			} else {
				outln("Failed Test 1.17.23");
			}
		}
		
		ad1 = new AbstractDiagram("0 a b c abc abcd abce abcde");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 5) {outln("Failed Test 1.17.24");}
		if(g1.getEdges().size() != 6) {outln("Failed Test 1.17.25");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("d")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("b") && !e.getTo().getLabel().equals("c")) {outln("Failed Test 1.17.26");}
			} else if(e.getFrom().getLabel().equals("e")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("b") && !e.getTo().getLabel().equals("c")) {outln("Failed Test 1.17.27");}
			} else {
				outln("Failed Test 1.17.28");
			}
		}
		
		ad1 = new AbstractDiagram("0 ab");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 2) {outln("Failed Test 1.17.29");}
		if(g1.getEdges().size() != 2) {outln("Failed Test 1.17.30");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("a")) {
				if(!e.getTo().getLabel().equals("b")) {outln("Failed Test 1.17.31");}
			} else if(e.getFrom().getLabel().equals("b")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.17.32");}
			} else {
				outln("Failed Test 1.17.33");
			}
		}
				
		ad1 = new AbstractDiagram("0 a abc");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 3) {outln("Failed Test 1.17.34");}
		if(g1.getEdges().size() != 4) {outln("Failed Test 1.17.35");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("b")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("c")) {outln("Failed Test 1.17.36");}
			} else if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("a") && !e.getTo().getLabel().equals("b")) {outln("Failed Test 1.17.37");}
			} else {
				outln("Failed Test 1.17.38");
			}
		}
		
		ad1 = new AbstractDiagram("0 a ab abcd");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 4) {outln("Failed Test 1.17.39");}
		if(g1.getEdges().size() != 5) {outln("Failed Test 1.17.40");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("b")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.17.41");}
			} else if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("b") && !e.getTo().getLabel().equals("d")) {outln("Failed Test 1.17.42");}
			} else if(e.getFrom().getLabel().equals("d")) {
				if(!e.getTo().getLabel().equals("b") && !e.getTo().getLabel().equals("c")) {outln("Failed Test 1.17.43");}
			} else {
				outln("Failed Test 1.17.44");
			}
		}
		
		ad1 = new AbstractDiagram("0 a b ab ac abc abce");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 4) {outln("Failed Test 1.17.45");}
		if(g1.getEdges().size() != 3) {outln("Failed Test 1.17.47");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("e")) {
				if(!e.getTo().getLabel().equals("b") && !e.getTo().getLabel().equals("c")) {outln("Failed Test 1.17.48");}
			} else if(e.getFrom().getLabel().equals("c")) {
				if(!e.getTo().getLabel().equals("a")) {outln("Failed Test 1.17.49");}
			} else {
				outln("Failed Test 1.17.50");
			}
		}
		
		ad1 = new AbstractDiagram("0 b d ac bd cd abc acd abcd");
		g1 = ad1.generateImmediateContainmentGraph();
		if(g1.getNodes().size() != 4) {outln("Failed Test 1.17.51");}
		if(g1.getEdges().size() != 1) {outln("Failed Test 1.17.52");}
		for(Edge e : g1.getEdges()) {
			if(e.getFrom().getLabel().equals("a")) {
				if(!e.getTo().getLabel().equals("c")) {outln("Failed Test 1.17.53");}
			} else {
				outln("Failed Test 1.17.54");
			}
		}

		
		

		g1 = new Graph();
		g1.addNode(new Node("a"));
		if(!AbstractDiagram.findAllParents(g1,g1.firstNodeWithLabel("a")).equals("")) {outln("Failed Test 1.18.1");}
		
		g1 = new Graph();
		g1.addAdjacencyEdge("b","a");
		if(!AbstractDiagram.findAllParents(g1,g1.firstNodeWithLabel("a")).equals("")) {outln("Failed Test 1.18.2");}
		if(!AbstractDiagram.findAllParents(g1,g1.firstNodeWithLabel("b")).equals("a")) {outln("Failed Test 1.18.3");}
		
		g1 = new Graph();
		g1.addAdjacencyEdge("b","a");
		g1.addAdjacencyEdge("c","a");
		g1.addAdjacencyEdge("d","b");
		g1.addAdjacencyEdge("d","c");
		if(!AbstractDiagram.findAllParents(g1,g1.firstNodeWithLabel("a")).equals("")) {outln("Failed Test 1.18.4");}
		if(!AbstractDiagram.findAllParents(g1,g1.firstNodeWithLabel("b")).equals("a")) {outln("Failed Test 1.18.5");}
		if(!AbstractDiagram.findAllParents(g1,g1.firstNodeWithLabel("d")).equals("abc")) {outln("Failed Test 1.18.6");}
		
		g1 = new Graph();
		g1.addAdjacencyEdge("b","a");
		g1.addAdjacencyEdge("c","a");
		g1.addAdjacencyEdge("d","b");
		g1.addAdjacencyEdge("d","c");
		g1.addAdjacencyEdge("f","e");
		g1.addAdjacencyEdge("g","f");
		g1.addAdjacencyEdge("h","g");
		g1.addAdjacencyEdge("i","g");
		if(!AbstractDiagram.findAllParents(g1,g1.firstNodeWithLabel("h")).equals("efg")) {outln("Failed Test 1.18.7");}
		if(!AbstractDiagram.findAllParents(g1,g1.firstNodeWithLabel("i")).equals("efg")) {outln("Failed Test 1.18.8");}
		if(!AbstractDiagram.findAllParents(g1,g1.firstNodeWithLabel("g")).equals("ef")) {outln("Failed Test 1.18.9");}

		ad1 = new AbstractDiagram("0");
		list = ad1.findZonesContainingContours("");
		if(list.size() != 0) {outln("Failed Test 1.19.1");}
		
		ad1 = new AbstractDiagram("0");
		list = ad1.findZonesContainingContours("a");
		if(list.size() != 0) {outln("Failed Test 1.19.2");}
		
		ad1 = new AbstractDiagram("0");
		list = ad1.findZonesContainingContours("ab");
		if(list.size() != 0) {outln("Failed Test 1.19.3");}
		
		ad1 = new AbstractDiagram("0 a");
		list = ad1.findZonesContainingContours("");
		if(list.size() != 0) {outln("Failed Test 1.19.4");}
		
		ad1 = new AbstractDiagram("0 a");
		list = ad1.findZonesContainingContours("b");
		if(list.size() != 0) {outln("Failed Test 1.19.5");}
		
		ad1 = new AbstractDiagram("0 a b ab");
		list = ad1.findZonesContainingContours("cd");
		if(list.size() != 0) {outln("Failed Test 1.19.6");}
		
		ad1 = new AbstractDiagram("0 a");
		list = ad1.findZonesContainingContours("a");
		if(list.size() != 1) {outln("Failed Test 1.19.7");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.19.8");}
		
		ad1 = new AbstractDiagram("0 ab");
		list = ad1.findZonesContainingContours("a");
		if(list.size() != 1) {outln("Failed Test 1.19.9");}
		if(!list.get(0).equals("ab")) {outln("Failed Test 1.19.10");}
		
		ad1 = new AbstractDiagram("0 a");
		list = ad1.findZonesContainingContours("ab");
		if(list.size() != 1) {outln("Failed Test 1.19.11");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.19.12");}
		
		ad1 = new AbstractDiagram("0 a b");
		list = ad1.findZonesContainingContours("b");
		if(list.size() != 1) {outln("Failed Test 1.19.13");}
		if(!list.get(0).equals("b")) {outln("Failed Test 1.19.14");}
		
		ad1 = new AbstractDiagram("0 a b ab");
		list = ad1.findZonesContainingContours("a");
		if(list.size() != 2) {outln("Failed Test 1.19.15");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.19.16");}
		if(!list.get(1).equals("ab")) {outln("Failed Test 1.19.17");}
		
		ad1 = new AbstractDiagram("0 a b ab");
		list = ad1.findZonesContainingContours("ab");
		if(list.size() != 3) {outln("Failed Test 1.19.18");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.19.19");}
		if(!list.get(1).equals("b")) {outln("Failed Test 1.19.20");}
		if(!list.get(2).equals("ab")) {outln("Failed Test 1.19.21");}
		
		ad1 = new AbstractDiagram("0 a b ab");
		list = ad1.findZonesContainingContours("abc");
		if(list.size() != 3) {outln("Failed Test 1.19.22");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.19.23");}
		if(!list.get(1).equals("b")) {outln("Failed Test 1.19.24");}
		if(!list.get(2).equals("ab")) {outln("Failed Test 1.19.25");}
		
		ad1 = new AbstractDiagram("0 a b ab xy xyz axy");
		list = ad1.findZonesContainingContours("az");
		if(list.size() != 4) {outln("Failed Test 1.19.26");}
		if(!list.get(0).equals("a")) {outln("Failed Test 1.19.27");}
		if(!list.get(1).equals("ab")) {outln("Failed Test 1.19.28");}
		if(!list.get(2).equals("axy")) {outln("Failed Test 1.19.29");}
		if(!list.get(3).equals("xyz")) {outln("Failed Test 1.19.30");}

		
		ad1 = new AbstractDiagram("0");
		ad2 = ad1.findAtomicDiagram("");
		if(!ad2.toString().equals("0")) {outln("Failed Test 1.20.1");}
				
		ad1 = new AbstractDiagram("0");
		ad2 = ad1.findAtomicDiagram("a");
		if(!ad2.toString().equals("0")) {outln("Failed Test 1.20.2");}

		ad1 = new AbstractDiagram("0");
		ad2 = ad1.findAtomicDiagram("ab");
		if(!ad2.toString().equals("0")) {outln("Failed Test 1.20.3");}

		ad1 = new AbstractDiagram("0 a");
		ad2 = ad1.findAtomicDiagram("");
		if(!ad2.toString().equals("0")) {outln("Failed Test 1.20.4");}
		
		ad1 = new AbstractDiagram("0 a");
		ad2 = ad1.findAtomicDiagram("b");
		if(!ad2.toString().equals("0")) {outln("Failed Test 1.20.5");}

		ad1 = new AbstractDiagram("0 a");
		ad2 = ad1.findAtomicDiagram("a");
		if(!ad2.toString().equals("0 a")) {outln("Failed Test 1.20.6");}

		ad1 = new AbstractDiagram("0 a ab");
		ad2 = ad1.findAtomicDiagram("ab");
		if(!ad2.toString().equals("0 a ab")) {outln("Failed Test 1.20.7");}

		ad1 = new AbstractDiagram("0 a ab");
		ad2 = ad1.findAtomicDiagram("b");
		if(!ad2.toString().equals("0 b")) {outln("Failed Test 1.20.8");}

		ad1 = new AbstractDiagram("0 a ab abc abd abcd");
		ad2 = ad1.findAtomicDiagram("ab");
		if(!ad2.toString().equals("0 a ab")) {outln("Failed Test 1.20.9");}
		ad2 = ad1.findAtomicDiagram("cd");
		if(!ad2.toString().equals("0 c d cd")) {outln("Failed Test 1.20.10");}

		ad1 = new AbstractDiagram("0 a b c cd ce cef");
		ad2 = ad1.findAtomicDiagram("a");
		if(!ad2.toString().equals("0 a")) {outln("Failed Test 1.20.11");}
		ad2 = ad1.findAtomicDiagram("b");
		if(!ad2.toString().equals("0 b")) {outln("Failed Test 1.20.12");}
		ad2 = ad1.findAtomicDiagram("c");
		if(!ad2.toString().equals("0 c")) {outln("Failed Test 1.20.13");}
		ad2 = ad1.findAtomicDiagram("d");
		if(!ad2.toString().equals("0 d")) {outln("Failed Test 1.20.14");}
		ad2 = ad1.findAtomicDiagram("e");
		if(!ad2.toString().equals("0 e")) {outln("Failed Test 1.20.15");}
		ad2 = ad1.findAtomicDiagram("f");
		if(!ad2.toString().equals("0 f")) {outln("Failed Test 1.20.16");}

		ad1 = new AbstractDiagram("0 ab ac acde f");
		ad2 = ad1.findAtomicDiagram("abc");
		if(!ad2.toString().equals("0 ab ac")) {outln("Failed Test 1.20.17");}
		ad2 = ad1.findAtomicDiagram("de");
		if(!ad2.toString().equals("0 de")) {outln("Failed Test 1.20.18");}
		ad2 = ad1.findAtomicDiagram("f");
		if(!ad2.toString().equals("0 f")) {outln("Failed Test 1.20.19");}

		
		
		ad1 = new AbstractDiagram("0");
		aad1 = ad1.generateAtomicDiagrams();
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.1");}
		if(aad1.countAtomicDiagrams() != 0) {outln("Failed Test 1.21.1a");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.1a");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.1");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.2");}
		if(aad1.getChildren().size() != 0) {outln("Failed Test 1.21.3");}
		
		ad1 = new AbstractDiagram("0 a");
		aad1 = ad1.generateAtomicDiagrams();
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.4");}
		if(aad1.countAtomicDiagrams() != 1) {outln("Failed Test 1.21.4a");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.5");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.6");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.7");}
		if(aad1.getChildren().size() != 1) {outln("Failed Test 1.21.8");}
		aad2 = aad1.getChildren().get(0);
		if(!aad2.getParentZone().equals("0")) {outln("Failed Test 1.21.9");}
		if(aad2.getChildren().size() != 0) {outln("Failed Test 1.21.10");}
		
		ad1 = new AbstractDiagram("0 a b");
		aad1 = ad1.generateAtomicDiagrams();
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.11");}
		if(aad1.countAtomicDiagrams() != 2) {outln("Failed Test 1.21.11a");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.12");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.13");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.14");}
		if(aad1.getChildren().size() != 2) {outln("Failed Test 1.21.15");}
		for(AtomicAbstractDiagram aad : aad1.getChildren()) {
			if(aad.getAtomicDiagram().toString().equals("0 a")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.16");}
				if(aad.getChildren().size() != 0) {outln("Failed Test 1.21.17");}
			} else if(aad.getAtomicDiagram().toString().equals("0 b")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.18");}
				if(aad.getChildren().size() != 0) {outln("Failed Test 1.21.19");}
			} else {
				outln("Failed Test 1.21.20");
			}
		}
		
		ad1 = new AbstractDiagram("0 a b c ");
		aad1 = ad1.generateAtomicDiagrams();
	
		if(!aad1.consistentTree()) {outln("-----------");}
		
		ad1 = new AbstractDiagram("0 a b c ab abc e f ef x");
		aad1 = ad1.generateAtomicDiagrams();
	
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.21");}
		if(aad1.countAtomicDiagrams() != 3) {outln("Failed Test 1.21.21a");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.22");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.23");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.24");}
		if(aad1.getChildren().size() != 3) {outln("Failed Test 1.21.25");}
		for(AtomicAbstractDiagram aad : aad1.getChildren()) {
			if(aad.getAtomicDiagram().toString().equals("0 a b c ab abc")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.26");}
				if(aad.getChildren().size() != 0) {outln("Failed Test 1.21.27");}
			} else if(aad.getAtomicDiagram().toString().equals("0 e f ef")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.28");}
				if(aad.getChildren().size() != 0) {outln("Failed Test 1.21.29");}
			} else if(aad.getAtomicDiagram().toString().equals("0 x")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.30");}
				if(aad.getChildren().size() != 0) {outln("Failed Test 1.21.31");}
			} else {
				outln("Failed Test 1.21.32");
			}
		}
		
		ad1 = new AbstractDiagram("0 a b ab ac af abg abc ach abce");
		aad1 = ad1.generateAtomicDiagrams();
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.33");}
		if(aad1.countAtomicDiagrams() != 5) {outln("Failed Test 1.21.33a");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.34");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.35");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.36");}
		if(aad1.getChildren().size() != 1) {outln("Failed Test 1.21.37");}
		for(AtomicAbstractDiagram aad : aad1.getChildren()) {
			if(aad.getAtomicDiagram().toString().equals("0 a b ab ac abc")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.38");}
				if(aad.getChildren().size() != 4) {outln("Failed Test 1.21.39");}
				for(AtomicAbstractDiagram aadSub1 : aad.getChildren()) {
					if(aadSub1.getAtomicDiagram().toString().equals("0 e")) {
						if(!aadSub1.getParentZone().equals("abc")) {outln("Failed Test 1.21.40");}
						if(aadSub1.getChildren().size() != 0) {outln("Failed Test 1.21.41");}
					} else if(aadSub1.getAtomicDiagram().toString().equals("0 f")) {
						if(!aadSub1.getParentZone().equals("a")) {outln("Failed Test 1.21.42");}
						if(aadSub1.getChildren().size() != 0) {outln("Failed Test 1.21.43");}
					} else if(aadSub1.getAtomicDiagram().toString().equals("0 g")) {
						if(!aadSub1.getParentZone().equals("ab")) {outln("Failed Test 1.21.44");}
						if(aadSub1.getChildren().size() != 0) {outln("Failed Test 1.21.45");}
					} else if(aadSub1.getAtomicDiagram().toString().equals("0 h")) {
						if(!aadSub1.getParentZone().equals("ac")) {outln("Failed Test 1.21.46");}
						if(aadSub1.getChildren().size() != 0) {outln("Failed Test 1.21.47");}
					} else {
						outln("Failed Test 1.21.48");
					}
				}
			} else {
				outln("Failed Test 1.21.49");
			}
		}
		
		ad1 = new AbstractDiagram("0 a b ab ac af abg abc ach abce abcemn achxy achxz");
		aad1 = ad1.generateAtomicDiagrams();
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.50");}
		if(aad1.countAtomicDiagrams() != 7) {outln("Failed Test 1.21.50a");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.51");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.52");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.53");}
		if(aad1.getChildren().size() != 1) {outln("Failed Test 1.21.54");}
		for(AtomicAbstractDiagram aad : aad1.getChildren()) {
			if(aad.getAtomicDiagram().toString().equals("0 a b ab ac abc")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.55");}
				if(aad.getChildren().size() != 4) {outln("Failed Test 1.21.56");}
				for(AtomicAbstractDiagram aadSub1 : aad.getChildren()) {
					if(aadSub1.getAtomicDiagram().toString().equals("0 e")) {
						if(!aadSub1.getParentZone().equals("abc")) {outln("Failed Test 1.21.57");}
						if(aadSub1.getChildren().size() != 1) {outln("Failed Test 1.21.58");}
						for(AtomicAbstractDiagram aadSub2 : aadSub1.getChildren()) {
							if(aadSub2.getAtomicDiagram().toString().equals("0 mn")) {
								if(!aadSub2.getParentZone().equals("e")) {outln("Failed Test 1.21.59");}
								if(aadSub2.getChildren().size() != 0) {outln("Failed Test 1.21.60");}
							} else {
								outln("Failed Test 1.21.61");
							}
						}
					} else if(aadSub1.getAtomicDiagram().toString().equals("0 f")) {
						if(!aadSub1.getParentZone().equals("a")) {outln("Failed Test 1.21.62");}
						if(aadSub1.getChildren().size() != 0) {outln("Failed Test 1.21.63");}
					} else if(aadSub1.getAtomicDiagram().toString().equals("0 g")) {
						if(!aadSub1.getParentZone().equals("ab")) {outln("Failed Test 1.21.64");}
						if(aadSub1.getChildren().size() != 0) {outln("Failed Test 1.21.65");}
					} else if(aadSub1.getAtomicDiagram().toString().equals("0 h")) {
						if(!aadSub1.getParentZone().equals("ac")) {outln("Failed Test 1.21.66");}
						if(aadSub1.getChildren().size() != 1) {outln("Failed Test 1.21.67");}
						for(AtomicAbstractDiagram aadSub2 : aadSub1.getChildren()) {
							if(aadSub2.getAtomicDiagram().toString().equals("0 xy xz")) {
								if(!aadSub2.getParentZone().equals("h")) {outln("Failed Test 1.21.68");}
								if(aadSub2.getChildren().size() != 0) {outln("Failed Test 1.21.69");}
							} else {
								outln("Failed Test 1.21.70");
							}
						}
					} else {
						outln("Failed Test 1.21.71");
					}
				}
			} else {
				outln("Failed Test 1.21.72");
			}
		}
		
		ad1 = new AbstractDiagram("0 ab ac acdef acdefg");
		aad1 = ad1.generateAtomicDiagrams();
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.73");}
		if(aad1.countAtomicDiagrams() != 3) {outln("Failed Test 1.21.73a");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.74");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.75");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.76");}
		if(aad1.getChildren().size() != 1) {outln("Failed Test 1.21.77");}
		for(AtomicAbstractDiagram aad : aad1.getChildren()) {
			if(aad.getAtomicDiagram().toString().equals("0 ab ac")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.78");}
				if(aad.getChildren().size() != 1) {outln("Failed Test 1.21.79");}
				for(AtomicAbstractDiagram aadSub1 : aad.getChildren()) {
					if(aadSub1.getAtomicDiagram().toString().equals("0 def")) {
						if(!aadSub1.getParentZone().equals("ac")) {outln("Failed Test 1.21.80");}
						if(aadSub1.getChildren().size() != 1) {outln("Failed Test 1.21.81");}
						for(AtomicAbstractDiagram aadSub2 : aadSub1.getChildren()) {
							if(aadSub2.getAtomicDiagram().toString().equals("0 g")) {
								if(!aadSub2.getParentZone().equals("def")) {outln("Failed Test 1.21.82");}
								if(aadSub2.getChildren().size() != 0) {outln("Failed Test 1.21.83");}
							} else {
								outln("Failed Test 1.21.84");
							}
						}
					} else {
						outln("Failed Test 1.21.85");
					}
				}
			} else {
				outln("Failed Test 1.21.86");
			}
		}
		
		ad1 = new AbstractDiagram("0 a ab b ac ag c bci bi bij bj bd aef aefg aefh");
		aad1 = ad1.generateAtomicDiagrams();
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.87");}
		if(aad1.countAtomicDiagrams() != 4) {outln("Failed Test 1.21.87a");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.88");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.89");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.90");}
		if(aad1.getChildren().size() != 1) {outln("Failed Test 1.21.91");}
		for(AtomicAbstractDiagram aad : aad1.getChildren()) {
			if(aad.getAtomicDiagram().toString().equals("0 a b c ab ac bi bj bci bij")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.92");}
				if(aad.getChildren().size() != 2) {outln("Failed Test 1.21.93");}
				for(AtomicAbstractDiagram aadSub1 : aad.getChildren()) {
					if(aadSub1.getAtomicDiagram().toString().equals("0 g ef efg")) {
						if(!aadSub1.getParentZone().equals("a")) {outln("Failed Test 1.21.94");}
						if(aadSub1.getChildren().size() != 1) {outln("Failed Test 1.21.95");}
						for(AtomicAbstractDiagram aadSub2 : aadSub1.getChildren()) {
							if(aadSub2.getAtomicDiagram().toString().equals("0 h")) {
								if(!aadSub2.getParentZone().equals("ef")) {outln("Failed Test 1.21.96");}
								if(aadSub2.getChildren().size() != 0) {outln("Failed Test 1.21.97");}
							} else {
								outln("Failed Test 1.21.98");
							}
						}
					} else if(aadSub1.getAtomicDiagram().toString().equals("0 d")) {
						if(!aadSub1.getParentZone().equals("b")) {outln("Failed Test 1.21.99");}
						if(aadSub1.getChildren().size() != 0) {outln("Failed Test 1.21.100");}
					} else {
						outln("Failed Test 1.21.101");
					}
				}
			} else {
				outln("Failed Test 1.21.102");
			}
		}
		
		ad1 = new AbstractDiagram("0 b d ac bd cd abc acd abcd");
		aad1 = ad1.generateAtomicDiagrams();
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.03");}
		if(aad1.countAtomicDiagrams() != 1) {outln("Failed Test 1.21.104");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.105");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.106");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.107");}
		if(aad1.getChildren().size() != 1) {outln("Failed Test 1.21.108");}
		for(AtomicAbstractDiagram aad : aad1.getChildren()) {
			if(aad.getAtomicDiagram().toString().equals("0 b d ac bd cd abc acd abcd")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.109");}
				if(aad.getChildren().size() != 0) {outln("Failed Test 1.21.110");}
			} else {
				outln("Failed Test 1.21.111");
			}
		}
		
		ad1 = new AbstractDiagram("0 c bc");
		aad1 = ad1.generateAtomicDiagrams();
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.112");}
		if(aad1.countAtomicDiagrams() != 2) {outln("Failed Test 1.21.113");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.114");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.115");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.116");}
		if(aad1.getChildren().size() != 1) {outln("Failed Test 1.21.117");}
		for(AtomicAbstractDiagram aad : aad1.getChildren()) {
			if(aad.getAtomicDiagram().toString().equals("0 c")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.118");}
				if(aad.getChildren().size() != 1) {outln("Failed Test 1.21.119");}
				for(AtomicAbstractDiagram aadSub1 : aad.getChildren()) {
					if(aadSub1.getAtomicDiagram().toString().equals("0 b")) {
						if(!aadSub1.getParentZone().equals("c")) {outln("Failed Test 1.21.120");}
						if(aadSub1.getChildren().size() != 0) {outln("Failed Test 1.21.121");}
					} else {
						outln("Failed Test 1.21.122");
					}
				}
			} else {
				outln("Failed Test 1.21.123");
			}
		}
		
		ad1 = new AbstractDiagram("0 b c ac bc abc");
		aad1 = ad1.generateAtomicDiagrams();
		if(!aad1.consistentTree()) {outln("Failed Test 1.21.124");}
		if(aad1.countAtomicDiagrams() != 1) {outln("Failed Test 1.21.125");}
		if(!aad1.getAtomicDiagram().toString().equals("0")) {outln("Failed Test 1.21.126");}
		if(aad1.getParentDiagram() != null) {outln("Failed Test 1.21.127");}
		if(aad1.getParentZone() != null) {outln("Failed Test 1.21.128");}
		if(aad1.getChildren().size() != 1) {outln("Failed Test 1.21.129");}
		for(AtomicAbstractDiagram aad : aad1.getChildren()) {
			if(aad.getAtomicDiagram().toString().equals("0 b c ac bc abc")) {
				if(!aad.getParentZone().equals("0")) {outln("Failed Test 1.21.130");}
				if(aad.getChildren().size() != 0) {outln("Failed Test 1.21.131");}
			} else {
				outln("Failed Test 1.21.132");
			}
		}
		
		if(AbstractDiagram.findDuplicateContours("").size() != 0) {outln("Failed Test 1.22.1");}
		if(AbstractDiagram.findDuplicateContours("a").size() != 0) {outln("Failed Test 1.22.2");}
		if(AbstractDiagram.findDuplicateContours("abc").size() != 0) {outln("Failed Test 1.22.3");}

		if(AbstractDiagram.findDuplicateContours("aa").size() != 1) {outln("Failed Test 1.22.4");}
		if(!AbstractDiagram.findDuplicateContours("aa").get(0).equals("a")) {outln("Failed Test 1.22.5");}

		if(AbstractDiagram.findDuplicateContours("aba").size() != 1) {outln("Failed Test 1.22.6");}
		if(!AbstractDiagram.findDuplicateContours("aba").get(0).equals("a")) {outln("Failed Test 1.22.7");}

		if(AbstractDiagram.findDuplicateContours("abaca").size() != 1) {outln("Failed Test 1.22.8");}
		if(!AbstractDiagram.findDuplicateContours("abaca").get(0).equals("a")) {outln("Failed Test 1.22.9");}

		if(AbstractDiagram.findDuplicateContours("aabb").size() != 2) {outln("Failed Test 1.22.10");}
		if(!AbstractDiagram.findDuplicateContours("aabb").get(0).equals("a")) {outln("Failed Test 1.22.11");}
		if(!AbstractDiagram.findDuplicateContours("aabb").get(1).equals("b")) {outln("Failed Test 1.22.12");}

		if(AbstractDiagram.findDuplicateContours("abbaa").size() != 2) {outln("Failed Test 1.22.13");}
		if(!AbstractDiagram.findDuplicateContours("abbaa").get(0).equals("a")) {outln("Failed Test 1.22.14");}
		if(!AbstractDiagram.findDuplicateContours("abbaa").get(1).equals("b")) {outln("Failed Test 1.22.15");}

		if(AbstractDiagram.findDuplicateContours("babaccca").size() != 3) {outln("Failed Test 1.22.16");}
		if(!AbstractDiagram.findDuplicateContours("babaccca").get(0).equals("a")) {outln("Failed Test 1.22.17");}
		if(!AbstractDiagram.findDuplicateContours("babaccca").get(1).equals("b")) {outln("Failed Test 1.22.18");}
		if(!AbstractDiagram.findDuplicateContours("babaccca").get(2).equals("c")) {outln("Failed Test 1.22.19");}

		
		listI1 = new ArrayList<Integer>();
		listI2 = new ArrayList<Integer>();
		if(!AbstractDiagram.compareOccurrences(listI1,listI2)) {outln("Failed Test 1.23.1");}
		if(!AbstractDiagram.compareOccurrences(listI2,listI1)) {outln("Failed Test 1.23.2");}
		
		listI1 = new ArrayList<Integer>();
		listI2 = new ArrayList<Integer>();
		listI2.add(1);
		if(AbstractDiagram.compareOccurrences(listI1,listI2)) {outln("Failed Test 1.23.3");}
		if(AbstractDiagram.compareOccurrences(listI2,listI1)) {outln("Failed Test 1.23.4");}
		
		listI1 = new ArrayList<Integer>();
		listI2 = new ArrayList<Integer>();
		listI1.add(1);
		listI2.add(1);
		if(!AbstractDiagram.compareOccurrences(listI1,listI2)) {outln("Failed Test 1.23.5");}
		if(!AbstractDiagram.compareOccurrences(listI2,listI1)) {outln("Failed Test 1.23.6");}
		
		listI1 = new ArrayList<Integer>();
		listI2 = new ArrayList<Integer>();
		listI1.add(1);
		listI1.add(1);
		listI2.add(1);
		if(AbstractDiagram.compareOccurrences(listI1,listI2)) {outln("Failed Test 1.23.7");}
		if(AbstractDiagram.compareOccurrences(listI2,listI1)) {outln("Failed Test 1.23.8");}
		
		listI1 = new ArrayList<Integer>();
		listI2 = new ArrayList<Integer>();
		listI1.add(2);
		listI2.add(1);
		if(AbstractDiagram.compareOccurrences(listI1,listI2)) {outln("Failed Test 1.23.9");}
		if(AbstractDiagram.compareOccurrences(listI2,listI1)) {outln("Failed Test 1.23.10");}
		
		listI1 = new ArrayList<Integer>();
		listI2 = new ArrayList<Integer>();
		listI1.add(1);
		listI1.add(2);
		listI1.add(3);
		listI2.add(1);
		listI2.add(2);
		listI2.add(3);
		if(!AbstractDiagram.compareOccurrences(listI1,listI2)) {outln("Failed Test 1.23.11");}
		if(!AbstractDiagram.compareOccurrences(listI2,listI1)) {outln("Failed Test 1.23.12");}
		
		listI1 = new ArrayList<Integer>();
		listI2 = new ArrayList<Integer>();
		listI1.add(1);
		listI1.add(2);
		listI1.add(3);
		listI2.add(1);
		listI2.add(3);
		listI2.add(2);
		if(AbstractDiagram.compareOccurrences(listI1,listI2)) {outln("Failed Test 1.23.13");}
		if(AbstractDiagram.compareOccurrences(listI2,listI1)) {outln("Failed Test 1.23.14");}
		
		listI1 = new ArrayList<Integer>();
		listI2 = new ArrayList<Integer>();
		listI1.add(1);
		listI1.add(2);
		listI1.add(3);
		listI2.add(1);
		listI2.add(2);
		listI2.add(2);
		if(AbstractDiagram.compareOccurrences(listI1,listI2)) {outln("Failed Test 1.23.15");}
		if(AbstractDiagram.compareOccurrences(listI2,listI1)) {outln("Failed Test 1.23.16");}
		
		listI1 = new ArrayList<Integer>();
		listI2 = new ArrayList<Integer>();
		listI1.add(1);
		listI1.add(2);
		listI1.add(3);
		listI2.add(4);
		listI2.add(2);
		listI2.add(3);
		if(AbstractDiagram.compareOccurrences(listI1,listI2)) {outln("Failed Test 1.23.17");}
		if(AbstractDiagram.compareOccurrences(listI2,listI1)) {outln("Failed Test 1.23.18");}
		
		
		listI1 = new ArrayList<Integer>();
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != 0) {outln("Failed Test 1.24.1");}
		if(czo2.compareTo(czo1) != 0) {outln("Failed Test 1.24.2");}
		if(!czo1.equals(czo2)) {outln("Failed Test 1.24.2a");}
		if(!czo2.equals(czo1)) {outln("Failed Test 1.24.2b");}
		
		listI1 = new ArrayList<Integer>();
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		czo2 = new ContourZoneOccurrence("b",listI2);
		if(czo1.compareTo(czo2) != -1) {outln("Failed Test 1.24.3");}
		if(czo2.compareTo(czo1) != 1) {outln("Failed Test 1.24.4");}
		if(czo1.equals(czo2)) {outln("Failed Test 1.24.4a");}
		if(czo2.equals(czo1)) {outln("Failed Test 1.24.4b");}
		
		listI1 = new ArrayList<Integer>();
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		czo2 = new ContourZoneOccurrence("b",listI2);
		if(czo1.compareTo(czo2) != -1) {outln("Failed Test 1.24.5");}
		if(czo2.compareTo(czo1) != 1) {outln("Failed Test 1.24.6");}
		if(czo1.equals(czo2)) {outln("Failed Test 1.24.6a");}
		if(czo2.equals(czo1)) {outln("Failed Test 1.24.6b");}
		
		listI1 = new ArrayList<Integer>();
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != 1) {outln("Failed Test 1.24.7");}
		if(czo2.compareTo(czo1) != -1) {outln("Failed Test 1.24.8");}
		if(czo1.equals(czo2)) {outln("Failed Test 1.24.8a");}
		if(czo2.equals(czo1)) {outln("Failed Test 1.24.8b");}
		
		listI1 = new ArrayList<Integer>();
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		listI2.add(1);
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != 0) {outln("Failed Test 1.24.9");}
		if(czo2.compareTo(czo1) != 0) {outln("Failed Test 1.24.10");}
		if(!czo1.equals(czo2)) {outln("Failed Test 1.24.10a");}
		if(!czo2.equals(czo1)) {outln("Failed Test 1.24.10b");}
		
		listI1 = new ArrayList<Integer>();
		listI1.add(2);
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		listI2.add(1);
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != 1) {outln("Failed Test 1.24.11");}
		if(czo2.compareTo(czo1) != -1) {outln("Failed Test 1.24.12");}
		if(czo1.equals(czo2)) {outln("Failed Test 1.24.12a");}
		if(czo2.equals(czo1)) {outln("Failed Test 1.24.12b");}
		
		listI1 = new ArrayList<Integer>();
		listI1.add(2);
		listI1.add(2);
		listI1.add(2);
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		listI2.add(2);
		listI2.add(2);
		listI2.add(2);
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != 0) {outln("Failed Test 1.24.13");}
		if(czo2.compareTo(czo1) != 0) {outln("Failed Test 1.24.14");}
		if(!czo1.equals(czo2)) {outln("Failed Test 1.24.14a");}
		if(!czo2.equals(czo1)) {outln("Failed Test 1.24.14b");}
		
		listI1 = new ArrayList<Integer>();
		listI1.add(2);
		listI1.add(1);
		listI1.add(2);
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		listI2.add(2);
		listI2.add(2);
		listI2.add(2);
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != -1) {outln("Failed Test 1.24.15");}
		if(czo2.compareTo(czo1) != 1) {outln("Failed Test 1.24.16");}
		if(czo1.equals(czo2)) {outln("Failed Test 1.24.16a");}
		if(czo2.equals(czo1)) {outln("Failed Test 1.24.16b");}
		
		listI1 = new ArrayList<Integer>();
		listI1.add(2);
		listI1.add(2);
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		listI2.add(2);
		listI2.add(2);
		listI2.add(2);
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != -1) {outln("Failed Test 1.24.17");}
		if(czo2.compareTo(czo1) != 1) {outln("Failed Test 1.24.18");}
		if(czo1.equals(czo2)) {outln("Failed Test 1.24.17a");}
		if(czo2.equals(czo1)) {outln("Failed Test 1.24.17b");}
		
		listI1 = new ArrayList<Integer>();
		listI1.add(2);
		listI1.add(2);
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		listI2.add(2);
		listI2.add(2);
		listI2.add(3);
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != -1) {outln("Failed Test 1.24.19");}
		if(czo2.compareTo(czo1) != 1) {outln("Failed Test 1.24.20");}
		if(czo1.equals(czo2)) {outln("Failed Test 1.24.20a");}
		if(czo2.equals(czo1)) {outln("Failed Test 1.24.20b");}
		
		listI1 = null;
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = new ArrayList<Integer>();
		listI2.add(2);
		listI2.add(2);
		listI2.add(3);
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != -1) {outln("Failed Test 1.24.21");}
		if(czo2.compareTo(czo1) != 1) {outln("Failed Test 1.24.22");}
		if(czo1.equals(czo2)) {outln("Failed Test 1.24.22a");}
		if(czo2.equals(czo1)) {outln("Failed Test 1.24.22b");}
		
		listI1 = null;
		czo1 = new ContourZoneOccurrence("a",listI1);
		listI2 = null;
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != 0) {outln("Failed Test 1.24.23");}
		if(czo2.compareTo(czo1) != 0) {outln("Failed Test 1.24.24");}
		if(!czo1.equals(czo2)) {outln("Failed Test 1.24.24a");}
		if(!czo2.equals(czo1)) {outln("Failed Test 1.24.24b");}
		
		listI1 = null;
		czo1 = new ContourZoneOccurrence("b",listI1);
		listI2 = null;
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != 1) {outln("Failed Test 1.24.25");}
		if(czo2.compareTo(czo1) != -1) {outln("Failed Test 1.24.26");}
		if(czo1.equals(czo2)) {outln("Failed Test 1.24.26a");}
		if(czo2.equals(czo1)) {outln("Failed Test 1.24.26b");}
		
		
		listI1 = new ArrayList<Integer>();
		listI1.add(2);
		listI1.add(2);
		listI1.add(2);
		czo1 = new ContourZoneOccurrence("b",listI1);
		listI2 = new ArrayList<Integer>();
		listI2.add(2);
		listI2.add(2);
		listI2.add(2);
		czo2 = new ContourZoneOccurrence("a",listI2);
		if(czo1.compareTo(czo2) != 1) {outln("Failed Test 1.24.17");}
		if(czo2.compareTo(czo1) != -1) {outln("Failed Test 1.24.18");}
		
		
		
		ad1 = new AbstractDiagram("");
		mapList = ad1.findContourMaps();
		if(mapList.size() != 0) {outln("Failed Test 1.25.1");}
		
		ad1 = new AbstractDiagram("0");
		mapList = ad1.findContourMaps();
		if(mapList.size() != 0) {outln("Failed Test 1.25.2");}
		
		ad1 = new AbstractDiagram("0 a");
		mapList = ad1.findContourMaps();
		if(mapList.size() != 1) {outln("Failed Test 1.25.3");}
		czoList = mapList.get(0);
		if(czoList.size() != 1) {outln("Failed Test 1.25.4");}
		listI1 = new ArrayList<Integer>();
		listI1.add(0);
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("a",listI1);
		if(!czoList.contains(czo1)) {outln("Failed Test 1.25.5");}
		
		ad1 = new AbstractDiagram("0 a b");
		mapList = ad1.findContourMaps();
		if(mapList.size() != 1) {outln("Failed Test 1.25.6");}
		czoList = mapList.get(0);
		if(czoList.size() != 2) {outln("Failed Test 1.25.7");}
		listI1 = new ArrayList<Integer>();
		listI1.add(0);
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("a",listI1);
		if(!czoList.contains(czo1)) {outln("Failed Test 1.25.8");}
		listI1 = new ArrayList<Integer>();
		listI1.add(0);
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("b",listI1);
		if(!czoList.contains(czo1)) {outln("Failed Test 1.25.9");}
		
		ad1 = new AbstractDiagram("0 a b ab");
		mapList = ad1.findContourMaps();
		if(mapList.size() != 1) {outln("Failed Test 1.25.10");}
		czoList = mapList.get(0);
		if(czoList.size() != 2) {outln("Failed Test 1.25.11");}
		listI1 = new ArrayList<Integer>();
		listI1.add(0);
		listI1.add(1);
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("a",listI1);
		if(!czoList.contains(czo1)) {outln("Failed Test 1.25.12");}
		listI1 = new ArrayList<Integer>();
		listI1.add(0);
		listI1.add(1);
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("b",listI1);
		if(!czoList.contains(czo1)) {outln("Failed Test 1.25.13");}
		
		ad1 = new AbstractDiagram("0 a b c ab ac bc abc");
		mapList = ad1.findContourMaps();
		if(mapList.size() != 1) {outln("Failed Test 1.25.14");}
		czoList = mapList.get(0);
		if(czoList.size() != 3) {outln("Failed Test 1.25.15");}
		listI1 = new ArrayList<Integer>();
		listI1.add(0);
		listI1.add(1);
		listI1.add(2);
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("a",listI1);
		if(!czoList.contains(czo1)) {outln("Failed Test 1.25.16");}
		listI1 = new ArrayList<Integer>();
		listI1.add(0);
		listI1.add(1);
		listI1.add(2);
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("b",listI1);
		if(!czoList.contains(czo1)) {outln("Failed Test 1.25.17");}
		listI1 = new ArrayList<Integer>();
		listI1.add(0);
		listI1.add(1);
		listI1.add(2);
		listI1.add(1);
		czo1 = new ContourZoneOccurrence("c",listI1);
		if(!czoList.contains(czo1)) {outln("Failed Test 1.25.18");}
		
		ad1 = new AbstractDiagram("0 a b ab ac");
		mapList = ad1.findContourMaps();
		if(mapList.size() != 3) {outln("Failed Test 1.25.19");}
		for(ArrayList<ContourZoneOccurrence> czos : mapList) {
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(1);
			listI1.add(2);
			ContourZoneOccurrence czoA = new ContourZoneOccurrence("a",listI1);
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(1);
			listI1.add(1);
			ContourZoneOccurrence czoB = new ContourZoneOccurrence("b",listI1);
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(0);
			listI1.add(1);
			ContourZoneOccurrence czoC = new ContourZoneOccurrence("c",listI1);
			if(czos.contains(czoA)) {
				if(czos.size() != 1) {outln("Failed Test 1.25.20");}
			} else if(czos.contains(czoB)) {
				if(czos.size() != 1) {outln("Failed Test 1.25.21");}
			} else if(czos.contains(czoC)) {
				if(czos.size() != 1) {outln("Failed Test 1.25.22");}
			} else {
				outln("Failed Test 1.25.23");
			}
		}
		
		ad1 = new AbstractDiagram("0 a b d ac ae");
		mapList = ad1.findContourMaps();
		if(mapList.size() != 3) {outln("Failed Test 1.25.24");}
		for(ArrayList<ContourZoneOccurrence> czos : mapList) {
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(1);
			listI1.add(2);
			ContourZoneOccurrence czoA = new ContourZoneOccurrence("a",listI1);
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(1);
			listI1.add(0);
			ContourZoneOccurrence czoB = new ContourZoneOccurrence("b",listI1);
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(0);
			listI1.add(1);
			ContourZoneOccurrence czoC = new ContourZoneOccurrence("c",listI1);
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(1);
			listI1.add(0);
			ContourZoneOccurrence czoD = new ContourZoneOccurrence("d",listI1);
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(0);
			listI1.add(1);
			ContourZoneOccurrence czoE = new ContourZoneOccurrence("e",listI1);
			if(czos.contains(czoA)) {
				if(czos.size() != 1) {outln("Failed Test 1.25.25");}
			} else if(czos.contains(czoB)) {
				if(czos.size() != 2) {outln("Failed Test 1.25.26");}
				if(!czos.contains(czoD)) {outln("Failed Test 1.25.27");}
			} else if(czos.contains(czoC)) {
				if(czos.size() != 2) {outln("Failed Test 1.25.28");}
				if(!czos.contains(czoE)) {outln("Failed Test 1.25.29");}
			} else {
				outln("Failed Test 1.25.30");
			}
		}
		
		ad1 = new AbstractDiagram("a ab acd");
		mapList = ad1.findContourMaps();
		if(mapList.size() != 3) {outln("Failed Test 1.25.30");}
		for(ArrayList<ContourZoneOccurrence> czos : mapList) {
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(1);
			listI1.add(1);
			listI1.add(1);
			ContourZoneOccurrence czoA = new ContourZoneOccurrence("a",listI1);
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(0);
			listI1.add(1);
			listI1.add(0);
			ContourZoneOccurrence czoB = new ContourZoneOccurrence("b",listI1);
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(0);
			listI1.add(0);
			listI1.add(1);
			ContourZoneOccurrence czoC = new ContourZoneOccurrence("c",listI1);
			listI1 = new ArrayList<Integer>();
			listI1.add(0);
			listI1.add(0);
			listI1.add(0);
			listI1.add(1);
			ContourZoneOccurrence czoD = new ContourZoneOccurrence("d",listI1);
			if(czos.contains(czoA)) {
				if(czos.size() != 1) {outln("Failed Test 1.25.31");}
			} else if(czos.contains(czoB)) {
				if(czos.size() != 1) {outln("Failed Test 1.25.32");}
			} else if(czos.contains(czoC)) {
				if(czos.size() != 2) {outln("Failed Test 1.25.33");}
				if(!czos.contains(czoD)) {outln("Failed Test 1.25.34");}
			} else {
				outln("Failed Test 1.25.35");
			}
		}
		
		
		listI1 = new ArrayList<Integer>();
		listGM = AbstractDiagram.firstCombination(listI1);
		if(listGM.size() !=0) {outln("Failed Test 1.26.1");}
		listI1 = new ArrayList<Integer>();
		listGM = AbstractDiagram.firstCombination(listI1);
		if(AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.2");}

		listI1 = new ArrayList<Integer>();
		czoList = new ArrayList<ContourZoneOccurrence>();
		listI1.add(1);
		listGM = AbstractDiagram.firstCombination(listI1);
		if(listGM.size() !=1) {outln("Failed Test 1.26.3");}
		gm1 = listGM.get(0);
		if(gm1.getGroupCount() != 1)  {outln("Failed Test 1.26.4");}
		if(gm1.getMapping()[0] != 0)  {outln("Failed Test 1.26.5");}
		if(AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.6");}

		listI1 = new ArrayList<Integer>();
		listI1.add(2);
		listGM = AbstractDiagram.firstCombination(listI1);
		if(listGM.size() !=1) {outln("Failed Test 1.26.7");}
		gm1 = listGM.get(0);
		if(gm1.getGroupCount() != 2)  {outln("Failed Test 1.26.8");}
		if(gm1.getMapping()[0] != 0)  {outln("Failed Test 1.26.9");}
		if(gm1.getMapping()[1] != 1)  {outln("Failed Test 1.26.10");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.11");}
		gm1 = listGM.get(0);
		if(gm1.getGroupCount() != 2)  {outln("Failed Test 1.26.12");}
		if(gm1.getMapping()[0] != 1)  {outln("Failed Test 1.26.13");}
		if(gm1.getMapping()[1] != 0)  {outln("Failed Test 1.26.14");}
		if(AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.15");}

		listI1 = new ArrayList<Integer>();
		listI1.add(3);
		listI1.add(2);
		listGM = AbstractDiagram.firstCombination(listI1);
		if(listGM.size() != 2) {outln("Failed Test 1.26.16");}
		gm1 = listGM.get(0);
		if(gm1.getGroupCount() != 3)  {outln("Failed Test 1.26.17");}
		if(gm1.getMapping()[0] != 0)  {outln("Failed Test 1.26.18");}
		if(gm1.getMapping()[1] != 1)  {outln("Failed Test 1.26.19");}
		if(gm1.getMapping()[2] != 2)  {outln("Failed Test 1.26.20");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.21");}
		gm1 = listGM.get(0);
		if(gm1.getMapping()[0] != 0)  {outln("Failed Test 1.26.22");}
		if(gm1.getMapping()[1] != 2)  {outln("Failed Test 1.26.23");}
		if(gm1.getMapping()[2] != 1)  {outln("Failed Test 1.26.24");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.25");}
		gm1 = listGM.get(0);
		if(gm1.getMapping()[0] != 1)  {outln("Failed Test 1.26.26");}
		if(gm1.getMapping()[1] != 0)  {outln("Failed Test 1.26.27");}
		if(gm1.getMapping()[2] != 2)  {outln("Failed Test 1.26.28");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.29");}
		gm1 = listGM.get(0);
		if(gm1.getMapping()[0] != 1)  {outln("Failed Test 1.26.30");}
		if(gm1.getMapping()[1] != 2)  {outln("Failed Test 1.26.31");}
		if(gm1.getMapping()[2] != 0)  {outln("Failed Test 1.26.32");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.33");}
		gm1 = listGM.get(0);
		if(gm1.getMapping()[0] != 2)  {outln("Failed Test 1.26.34");}
		if(gm1.getMapping()[1] != 0)  {outln("Failed Test 1.26.35");}
		if(gm1.getMapping()[2] != 1)  {outln("Failed Test 1.26.36");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.37");}
		gm1 = listGM.get(0);
		if(gm1.getMapping()[0] != 2)  {outln("Failed Test 1.26.38");}
		if(gm1.getMapping()[1] != 1)  {outln("Failed Test 1.26.39");}
		if(gm1.getMapping()[2] != 0)  {outln("Failed Test 1.26.40");}
		gm1 = listGM.get(1);
		if(gm1.getGroupCount() != 2)  {outln("Failed Test 1.26.41");}
		if(gm1.getMapping()[0] != 0)  {outln("Failed Test 1.26.42");}
		if(gm1.getMapping()[1] != 1)  {outln("Failed Test 1.26.43");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.44");}
		gm1 = listGM.get(0);
		if(gm1.getMapping()[0] != 0)  {outln("Failed Test 1.26.45");}
		if(gm1.getMapping()[1] != 1)  {outln("Failed Test 1.26.46");}
		if(gm1.getMapping()[2] != 2)  {outln("Failed Test 1.26.47");}
		gm1 = listGM.get(1);
		if(gm1.getMapping()[0] != 1)  {outln("Failed Test 1.26.48");}
		if(gm1.getMapping()[1] != 0)  {outln("Failed Test 1.26.49");}
		
		
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.50");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.51");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.52");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.53");}
		if(!AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.54");}
		gm1 = listGM.get(0);
		if(gm1.getMapping()[0] != 2)  {outln("Failed Test 1.26.56");}
		if(gm1.getMapping()[1] != 1)  {outln("Failed Test 1.26.57");}
		if(gm1.getMapping()[2] != 0)  {outln("Failed Test 1.26.58");}
		gm1 = listGM.get(1);
		if(gm1.getMapping()[0] != 1)  {outln("Failed Test 1.26.59");}
		if(gm1.getMapping()[1] != 0)  {outln("Failed Test 1.26.60");}
		if(AbstractDiagram.nextCombination(listGM)) {outln("Failed Test 1.26.61");}
		
		ad1 = new AbstractDiagram("0");
		ad2 = ad1.complement();
		if(!ad2.toString().equals("")) {outln("Failed Test 1.27.1");}
		
		ad1 = new AbstractDiagram("0 a b ab");
		ad2 = ad1.complement();
		if(!ad2.toString().equals("")) {outln("Failed Test 1.27.2");}
		
		ad1 = new AbstractDiagram("a");
		ad2 = ad1.complement();
		if(!ad2.toString().equals("0")) {outln("Failed Test 1.27.3");}
		
		ad1 = new AbstractDiagram("0 a ab");
		ad2 = ad1.complement();
		if(!ad2.toString().equals("b")) {outln("Failed Test 1.27.4");}
		
		ad1 = new AbstractDiagram("a b c");
		ad2 = ad1.complement();
		if(!ad2.toString().equals("0 ab ac bc abc")) {outln("Failed Test 1.27.5");}
		
		ad1 = new AbstractDiagram("0 x xy xyz");
		ad2 = ad1.complement();
		if(!ad2.toString().equals("y z xz yz")) {outln("Failed Test 1.27.6");}

		ad1 = new AbstractDiagram("a cd");
		ad2 = ad1.complement();
		if(!ad2.toString().equals("0 c d ac ad acd")) {outln("Failed Test 1.27.7");}

		ad1 = new AbstractDiagram("0 b c d bc bd cd bcd");
		ad2 = ad1.complement();
		if(!ad2.toString().equals("")) {outln("Failed Test 1.27.8");}

		
		
		out("Ending method test1");
	}
	

	/**
	 *  Starts with methods connected to diagram normalization and polynomial
	 * isomorphism test
	 */
	protected static void test2() {
		
		outln(" | Starting method test2");
		AbstractDiagram ad1;
		HashMap<String,HashMap<Integer,ArrayList<String>>> allNeighbours;
		HashMap<Integer,ArrayList<String>> neighboursBySize;
		ArrayList<String> neighbours;
		String c1;
		HashMap<String,Integer> cvMap;
		ArrayList<ArrayList<String>> listOfLists;
		ArrayList<String> cList;
		ArrayList<String> resultList;
		DualGraph dg1;
		Node n1,n2;
		Edge e1;

		ad1 = new AbstractDiagram("");
		allNeighbours = ad1.findContourNeighboursByZoneSize();
		if(allNeighbours.keySet().size() != 0) {outln("Failed Test 2.2.1");}
		ad1 = new AbstractDiagram("a b c d ab ac aef");
		allNeighbours = ad1.findContourNeighboursByZoneSize();
		if(allNeighbours.keySet().size() != 6) {outln("Failed Test 2.2.2");}
		neighboursBySize = allNeighbours.get("a");
		if(neighboursBySize.size() != 2) {outln("Failed Test 2.2.3");}
		neighbours = neighboursBySize.get(2);
		if(neighbours.size() != 2) {outln("Failed Test 2.2.4");}
		c1 = neighbours.get(0);
		if(!c1.equals("b")) {outln("Failed Test 2.2.5");}
		c1 = neighbours.get(1);
		if(!c1.equals("c")) {outln("Failed Test 2.2.6");}
		neighbours = neighboursBySize.get(3);
		if(neighbours.size() != 2) {outln("Failed Test 2.2.7");}
		c1 = neighbours.get(0);
		if(!c1.equals("e")) {outln("Failed Test 2.2.8");}
		c1 = neighbours.get(1);
		if(!c1.equals("f")) {outln("Failed Test 2.2.9");}
		neighboursBySize = allNeighbours.get("e");
		if(neighboursBySize.size() != 1) {outln("Failed Test 2.2.10");}
		neighbours = neighboursBySize.get(3);
		if(neighbours.size() != 2) {outln("Failed Test 2.2.11");}
		c1 = neighbours.get(0);
		if(!c1.equals("a")) {outln("Failed Test 2.2.12");}
		c1 = neighbours.get(1);
		if(!c1.equals("f")) {outln("Failed Test 2.2.13");}
		
		cList = new ArrayList<String>();
		cvMap = new HashMap<String,Integer>();
		listOfLists = AbstractDiagram.groupContoursByMap(cList,cvMap);
		if(listOfLists.size() != 0) {outln("Failed Test 2.3.1");}

		cList = new ArrayList<String>();
		cList.add("a");
		cvMap = new HashMap<String,Integer>();
		cvMap.put("a",1);
		listOfLists = AbstractDiagram.groupContoursByMap(cList,cvMap);
		if(listOfLists.size() != 1) {outln("Failed Test 2.3.2");}
		resultList = listOfLists.get(0);
		if(resultList.size() != 1) {outln("Failed Test 2.3.3");}
		if(!resultList.get(0).equals("a")) {outln("Failed Test 2.3.4");}

		
		cList = new ArrayList<String>();
		cList.add("a");
		cList.add("b");
		cList.add("c");
		cvMap = new HashMap<String,Integer>();
		cvMap.put("a",1);
		cvMap.put("b",2);
		cvMap.put("c",3);
		listOfLists = AbstractDiagram.groupContoursByMap(cList,cvMap);
		if(listOfLists.size() != 3) {outln("Failed Test 2.3.5");}
		resultList = listOfLists.get(0);
		if(resultList.size() != 1) {outln("Failed Test 2.3.6");}
		if(!resultList.get(0).equals("a")) {outln("Failed Test 2.3.7");}
		resultList = listOfLists.get(1);
		if(resultList.size() != 1) {outln("Failed Test 2.3.8");}
		if(!resultList.get(0).equals("b")) {outln("Failed Test 2.3.9");}
		resultList = listOfLists.get(2);
		if(resultList.size() != 1) {outln("Failed Test 2.3.10");}
		if(!resultList.get(0).equals("c")) {outln("Failed Test 2.3.11");}
		
		cList = new ArrayList<String>();
		cList.add("a");
		cList.add("b");
		cList.add("c");
		cvMap = new HashMap<String,Integer>();
		cvMap.put("a",1);
		cvMap.put("b",2);
		cvMap.put("c",2);
		listOfLists = AbstractDiagram.groupContoursByMap(cList,cvMap);
		if(listOfLists.size() != 2) {outln("Failed Test 2.3.12");}
		resultList = listOfLists.get(0);
		if(resultList.size() != 1) {outln("Failed Test 2.3.13");}
		if(!resultList.get(0).equals("a")) {outln("Failed Test 2.3.14");}
		resultList = listOfLists.get(1);
		if(resultList.size() != 2) {outln("Failed Test 2.3.15");}
		if(!resultList.get(0).equals("b")) {outln("Failed Test 2.3.16");}
		if(!resultList.get(1).equals("c")) {outln("Failed Test 2.3.17");}
		
		cList = new ArrayList<String>();
		cList.add("a");
		cList.add("b");
		cList.add("c");
		cvMap = new HashMap<String,Integer>();
		cvMap.put("a",1);
		cvMap.put("b",1);
		cvMap.put("c",1);
		listOfLists = AbstractDiagram.groupContoursByMap(cList,cvMap);
		if(listOfLists.size() != 1) {outln("Failed Test 2.3.18");}
		resultList = listOfLists.get(0);
		if(resultList.size() != 3) {outln("Failed Test 2.3.19");}
		if(!resultList.get(0).equals("a")) {outln("Failed Test 2.3.20");}
		if(!resultList.get(1).equals("b")) {outln("Failed Test 2.3.21");}
		if(!resultList.get(2).equals("c")) {outln("Failed Test 2.3.22");}
		
		cList = new ArrayList<String>();
		cList.add("a");
		cList.add("b");
		cList.add("c");
		cList.add("d");
		cList.add("e");
		cList.add("f");
		cvMap = new HashMap<String,Integer>();
		cvMap.put("a",1);
		cvMap.put("b",1);
		cvMap.put("c",1);
		cvMap.put("d",2);
		cvMap.put("e",4);
		cvMap.put("f",4);
		listOfLists = AbstractDiagram.groupContoursByMap(cList,cvMap);
		if(listOfLists.size() != 3) {outln("Failed Test 2.3.23");}
		resultList = listOfLists.get(0);
		if(resultList.size() != 3) {outln("Failed Test 2.3.24");}
		if(!resultList.get(0).equals("a")) {outln("Failed Test 2.3.25");}
		if(!resultList.get(1).equals("b")) {outln("Failed Test 2.3.26");}
		if(!resultList.get(2).equals("c")) {outln("Failed Test 2.3.27");}
		resultList = listOfLists.get(1);
		if(resultList.size() != 1) {outln("Failed Test 2.3.28");}
		if(!resultList.get(0).equals("d")) {outln("Failed Test 2.3.29");}
		resultList = listOfLists.get(2);
		if(resultList.size() != 2) {outln("Failed Test 2.3.30");}
		if(!resultList.get(0).equals("e")) {outln("Failed Test 2.3.31");}
		if(!resultList.get(1).equals("f")) {outln("Failed Test 2.3.32");}
	
		ad1 = new AbstractDiagram("a");
		ad1.normalize();
		if(!ad1.toString().equals("a")) {outln("Failed Test 2.4.1");}

		ad1 = new AbstractDiagram("b");
		ad1.normalize();
		if(!ad1.toString().equals("a")) {outln("Failed Test 2.4.2");}

		ad1 = new AbstractDiagram("a ab");
		ad1.normalize();
		if(!ad1.toString().equals("a ab")) {outln("Failed Test 2.4.3");}

		ad1 = new AbstractDiagram("b ab");
		ad1.normalize();
		if(!ad1.toString().equals("a ab")) {outln("Failed Test 2.4.4");}

		ad1 = new AbstractDiagram("x y z xy xz yz xyz");
		ad1.normalize();
		if(!ad1.toString().equals("a b c ab ac bc abc")) {outln("Failed Test 2.4.5");}

		ad1 = new AbstractDiagram("x y xz xyz xyzq");
		ad1.normalize();
		if(!ad1.toString().equals("a b ac abc abcd")) {outln("Failed Test 2.4.6");}
	
		ad1 = new AbstractDiagram("a b abc abd abce");
		ad1.normalize();
		if(!ad1.toString().equals("a b abc abd abce")) {outln("Failed Test 2.4.7");}

		ad1 = new AbstractDiagram("a b c ab ac abc");
		ad1.normalize();
		if(!ad1.toString().equals("a b c ab ac abc")) {outln("Failed Test 2.4.8");}

		ad1 = new AbstractDiagram("a b c ab bc abc");
		ad1.normalize();
		if(!ad1.toString().equals("a b c ab ac abc")) {outln("Failed Test 2.4.9");}

		ad1 = new AbstractDiagram("a b c d ab ac cd de abc abcde");
		ad1.normalize();
		if(!ad1.toString().equals("a b c d ab ad bc ce abd abcde")) {outln("Failed Test 2.4.10");}
		
		String s;
		ArrayList<String> sList;
		
		s = "";
		sList = AbstractDiagram.findContourList(s);
		if(sList.size() != 0) {outln("Failed Test 2.5.1");}
		
		s = "a";
		sList = AbstractDiagram.findContourList(s);
		if(sList.size() != 1) {outln("Failed Test 2.5.2");}
		if(!sList.get(0).equals("a")) {outln("Failed Test 2.5.3");}
		
		s = "aa";
		sList = AbstractDiagram.findContourList(s);
		if(sList.size() != 2) {outln("Failed Test 2.5.4");}
		if(!sList.get(0).equals("a")) {outln("Failed Test 2.5.5");}
		if(!sList.get(1).equals("a")) {outln("Failed Test 2.5.6");}
		
		s = "abc";
		sList = AbstractDiagram.findContourList(s);
		if(sList.size() != 3) {outln("Failed Test 2.5.7");}
		if(!sList.get(0).equals("a")) {outln("Failed Test 2.5.8");}
		if(!sList.get(1).equals("b")) {outln("Failed Test 2.5.9");}
		if(!sList.get(2).equals("c")) {outln("Failed Test 2.5.10");}
		
		dg1 = new DualGraph();
		n1 = new Node("a");
		n2 = new Node("b");
		if(dg1.addUniqueEdge(n1, n2) != null) {outln("Failed Test 2.6.1");}
		if(dg1.getEdges().size() != 0) {outln("Failed Test 2.6.2");}
		
		dg1.addNode(n1);
		dg1.addNode(n2);
		
		if(dg1.addUniqueEdge(n1, new Node("z")) != null) {outln("Failed Test 2.6.3");}
		if(dg1.getEdges().size() != 0) {outln("Failed Test 2.6.4");}

		if(dg1.addUniqueEdge(new Node("z"), n2) != null) {outln("Failed Test 2.6.5");}
		if(dg1.getEdges().size() != 0) {outln("Failed Test 2.6.6");}

		e1 = dg1.addUniqueEdge(n1, n2);
		if(e1 == null) {outln("Failed Test 2.6.7");}
		if(dg1.getEdges().size() != 1) {outln("Failed Test 2.6.8");}
		if(dg1.getEdges().get(0) != e1) {outln("Failed Test 2.6.9");}
		if(e1.getFrom() != n1) {outln("Failed Test 2.6.10");}
		if(e1.getTo() != n2) {outln("Failed Test 2.6.11");}

		e1 = dg1.addUniqueEdge(n1, n2);
		if(e1 != null) {outln("Failed Test 2.6.8");}
		if(dg1.getEdges().size() != 1) {outln("Failed Test 2.6.9");}

		e1 = dg1.addUniqueEdge(n2, n1);
		if(e1 == null) {outln("Failed Test 2.6.10");}
		if(dg1.getEdges().size() != 2) {outln("Failed Test 2.6.11");}
		if(dg1.getEdges().get(1) != e1) {outln("Failed Test 2.6.12");}
		if(e1.getFrom() != n2) {outln("Failed Test 2.6.13");}
		if(e1.getTo() != n1) {outln("Failed Test 2.6.14");}

		n2 = new Node("c");
		dg1.addNode(n2);
		e1 = dg1.addUniqueEdge(n1, n2);
		if(e1 == null) {outln("Failed Test 2.6.15");}
		if(dg1.getEdges().size() != 3) {outln("Failed Test 2.6.16");}
		if(dg1.getEdges().get(2) != e1) {outln("Failed Test 2.6.17");}
		if(e1.getFrom() != n1) {outln("Failed Test 2.6.18");}
		if(e1.getTo() != n2) {outln("Failed Test 2.6.19");}
		
		e1 = dg1.addUniqueEdge(n1, n2);
		if(e1 != null) {outln("Failed Test 2.6.20");}
		if(dg1.getEdges().size() != 3) {outln("Failed Test 2.6.21");}

		
		out("Ending method test2");
	}

	/**
	 *  | Starts with methods connected to diagram normalization and polynomial
	 * isomorphism test
	 */
	protected static void test3() {
		
		outln(" | Starting method test3");

		if(!DualGraph.findLabelDifferences("","").equals("")) {outln("Failed Test 3.0.1");}
		if(!DualGraph.findLabelDifferences("a","a").equals("")) {outln("Failed Test 3.0.2");}
		if(!DualGraph.findLabelDifferences("ab","ab").equals("")) {outln("Failed Test 3.0.3");}
		if(!DualGraph.findLabelDifferences("ab","ba").equals("")) {outln("Failed Test 3.0.4");}
		if(!DualGraph.findLabelDifferences("abc","bca").equals("")) {outln("Failed Test 3.0.5");}
		if(!DualGraph.findLabelDifferences("a","").equals("a")) {outln("Failed Test 3.0.6");}
		if(!DualGraph.findLabelDifferences("","a").equals("a")) {outln("Failed Test 3.0.7");}
		if(!DualGraph.findLabelDifferences("b","a").equals("ab")) {outln("Failed Test 3.0.8");}
		if(!DualGraph.findLabelDifferences("abc","").equals("abc")) {outln("Failed Test 3.0.9");}
		if(!DualGraph.findLabelDifferences("b","abc").equals("ac")) {outln("Failed Test 3.0.10");}
		if(!DualGraph.findLabelDifferences("ac","bd").equals("abcd")) {outln("Failed Test 3.0.11");}
		if(!DualGraph.findLabelDifferences("abcd","dcb").equals("a")) {outln("Failed Test 3.0.12");}
		
		if(DualGraph.countLabelDifferences("","") != 0) {outln("Failed Test 3.1.1");}
		if(DualGraph.countLabelDifferences("a","") != 1) {outln("Failed Test 3.1.2");}
		if(DualGraph.countLabelDifferences("","a") != 1) {outln("Failed Test 3.1.3");}
		if(DualGraph.countLabelDifferences("ab","") != 2) {outln("Failed Test 3.1.4");}
		if(DualGraph.countLabelDifferences("","ab") != 2) {outln("Failed Test 3.1.5");}
		if(DualGraph.countLabelDifferences("a","ab") != 1) {outln("Failed Test 3.1.6");}
		if(DualGraph.countLabelDifferences("ab","a") != 1) {outln("Failed Test 3.1.7");}
		if(DualGraph.countLabelDifferences("a","b") != 2) {outln("Failed Test 3.1.8");}
		if(DualGraph.countLabelDifferences("b","a") != 2) {outln("Failed Test 3.1.9");}
		if(DualGraph.countLabelDifferences("c","ab") != 3) {outln("Failed Test 3.1.10");}
		if(DualGraph.countLabelDifferences("ab","c") != 3) {outln("Failed Test 3.1.11");}
		if(DualGraph.countLabelDifferences("abcd","bd") != 2) {outln("Failed Test 3.1.12");}
		if(DualGraph.countLabelDifferences("bd","abcd") != 2) {outln("Failed Test 3.1.13");}
		if(DualGraph.countLabelDifferences("abcd","a") != 3) {outln("Failed Test 3.1.14");}
		if(DualGraph.countLabelDifferences("a","abcd") != 3) {outln("Failed Test 3.1.15");}
		if(DualGraph.countLabelDifferences("abcd","e") != 5) {outln("Failed Test 3.1.16");}
		if(DualGraph.countLabelDifferences("e","abcd") != 5) {outln("Failed Test 3.1.17");}
		if(DualGraph.countLabelDifferences("abcd","eacf") != 4) {outln("Failed Test 3.1.18");}
		if(DualGraph.countLabelDifferences("eacf","abcd") != 4) {outln("Failed Test 3.1.19");}
		if(DualGraph.countLabelDifferences("abcd","acd") != 1) {outln("Failed Test 3.1.20");}
		if(DualGraph.countLabelDifferences("acd","abcd") != 1) {outln("Failed Test 3.1.21");}
		
		if(DualGraph.findLabelContainment("","")) {outln("Failed Test 3.2.1");}
		if(DualGraph.findLabelContainment("a","a")) {outln("Failed Test 3.2.2");}
		if(DualGraph.findLabelContainment("a","b")) {outln("Failed Test 3.2.3");}
		if(DualGraph.findLabelContainment("ab","a")) {outln("Failed Test 3.2.4");}
		if(DualGraph.findLabelContainment("a","bc")) {outln("Failed Test 3.2.5");}
		if(DualGraph.findLabelContainment("ab","acd")) {outln("Failed Test 3.2.6");}
		if(DualGraph.findLabelContainment("xy","uvx")) {outln("Failed Test 3.2.7");}
		if(DualGraph.findLabelContainment("ab","xyz")) {outln("Failed Test 3.2.8");}
		if(!DualGraph.findLabelContainment("","a")) {outln("Failed Test 3.2.9");}
		if(!DualGraph.findLabelContainment("a","ab")) {outln("Failed Test 3.2.10");}
		if(!DualGraph.findLabelContainment("b","ab")) {outln("Failed Test 3.2.11");}
		if(!DualGraph.findLabelContainment("y","auxyz")) {outln("Failed Test 3.2.12");}
		if(!DualGraph.findLabelContainment("uxy","auxyz")) {outln("Failed Test 3.2.13");}
		if(!DualGraph.findLabelContainment("xy","axy")) {outln("Failed Test 3.2.14");}
		
		if(!DualGraph.findLabelIntersection("","").equals("")) {outln("Failed Test 3.3.1");}
		if(!DualGraph.findLabelIntersection("a","a").equals("a")) {outln("Failed Test 3.3.2");}
		if(!DualGraph.findLabelIntersection("a","b").equals("")) {outln("Failed Test 3.3.3");}
		if(!DualGraph.findLabelIntersection("ab","b").equals("b")) {outln("Failed Test 3.3.4");}
		if(!DualGraph.findLabelIntersection("ab","a").equals("a")) {outln("Failed Test 3.3.5");}
		if(!DualGraph.findLabelIntersection("b","ab").equals("b")) {outln("Failed Test 3.3.6");}
		if(!DualGraph.findLabelIntersection("a","ab").equals("a")) {outln("Failed Test 3.3.7");}
		if(!DualGraph.findLabelIntersection("ab","ab").equals("ab")) {outln("Failed Test 3.3.8");}
		if(!DualGraph.findLabelIntersection("abcd","bxdz").equals("bd")) {outln("Failed Test 3.3.9");}
		if(!DualGraph.findLabelIntersection("dcb","zdcx").equals("cd")) {outln("Failed Test 3.3.10");}
		if(!DualGraph.findLabelIntersection("xyz","qvw").equals("")) {outln("Failed Test 3.3.11");}
		if(!DualGraph.findLabelIntersection("","qvw").equals("")) {outln("Failed Test 3.3.12");}
		if(!DualGraph.findLabelIntersection("xyz","").equals("")) {outln("Failed Test 3.3.13");}
		
		out("Ending method test3");
	}

	/**
	 * Methods connected to drawing package
	 */
	protected static void test4() {
		
		outln(" | Starting method test4");
/* TODO What happened to RadialLevel????		
		RadialLevel r;

		r = new RadialLevel();
		
		if(r.getNextInd(3) != 0) {outln("Failed Test 4.1.1");}
		if(r.getNextInd(0) != 0) {outln("Failed Test 4.1.2");}
		if(r.getPrevInd(3) != 0) {outln("Failed Test 4.1.3");}
		if(r.getPrevInd(0) != 0) {outln("Failed Test 4.1.4");}
		r.add(new Node("A"));
		if(r.getNextInd(0) != 0) {outln("Failed Test 4.1.5");}
		if(r.getPrevInd(0) != 0) {outln("Failed Test 4.1.6");}
		r.add(new Node("B"));
		if(r.getNextInd(0) != 1) {outln("Failed Test 4.1.7");}
		if(r.getNextInd(1) != 0) {outln("Failed Test 4.1.8");}
		if(r.getPrevInd(0) != 1) {outln("Failed Test 4.1.9");}
		if(r.getPrevInd(1) != 0) {outln("Failed Test 4.1.10");}
		r.add(new Node("D"));
		r.add(new Node("E"));
		if(r.getNextInd(1) != 2) {outln("Failed Test 4.1.11");}
		if(r.getNextInd(3) != 0) {outln("Failed Test 4.1.12");}
		if(r.getNextInd(2) != 3) {outln("Failed Test 4.1.13");}
		if(r.getPrevInd(2) != 1) {outln("Failed Test 4.1.14");}
		if(r.getPrevInd(0) != 3) {outln("Failed Test 4.1.15");}
		if(r.getPrevInd(1) != 0) {outln("Failed Test 4.1.16");}
		
		r = new RadialLevel();
		if(r.distanceBetween(0,0) != 0) {outln("Failed Test 4.2.1");}
		if(r.distanceBetween(0,10) != 0) {outln("Failed Test 4.2.2");}
		r.add(new Node("A"));
		r.add(new Node("B"));
		if(r.distanceBetween(0,0) != 0) {outln("Failed Test 4.2.3");}
		if(r.distanceBetween(0,10) != 0) {outln("Failed Test 4.2.4");}
		if(r.distanceBetween(10,0) != 0) {outln("Failed Test 4.2.5");}
		r.add(new Node("B"));
		if(r.distanceBetween(0,0) != 0) {outln("Failed Test 4.2.6");}
		if(r.distanceBetween(0,10) != 0) {outln("Failed Test 4.2.7");}
		if(r.distanceBetween(10,0) != 0) {outln("Failed Test 4.2.8");}
		if(r.distanceBetween(0,1) != 1) {outln("Failed Test 4.2.9");}
		if(r.distanceBetween(1,0) != 1) {outln("Failed Test 4.2.10");}
		
*/		
		out("Ending method test4");
	}

	/** This tests abstract graph creating and cloning. */
	public static void test5() {
		outln(" | Starting method test5");
		DualGraph dg1;
		DualGraph dg2;
		AbstractDiagram diagram;
		ArrayList<Node> nodes;
		ArrayList<Edge> edges;
		ArrayList<Point> bends;
		Node n1;
		Node n2;
		Node n3;
		Edge e1;
		Edge e2;

		// test a bug when returning a diagram with the empty zone
		dg1 = new DualGraph(new AbstractDiagram("0"));

		if(dg1.findAbstractDiagram().getZoneList().size() != 1) {outln("Test 5.1.0.1a FAIL");}
		if(!dg1.findAbstractDiagram().getZoneList().get(0).equals("")) {outln("Test 5.1.0.1b FAIL");}
		if(dg1.getNodes().size() != 1) {outln("Test 5.1.0.1c FAIL");}
		if(dg1.getEdges().size() != 0) {outln("Test 5.1.0.1d FAIL");}
		if(!dg1.consistent()) {outln("Test 5.1.0.1e FAIL");}
		if(!dg1.consistent()) {outln("Test 5.1.0.1f FAIL");}
		
		dg1 = new DualGraph(new AbstractDiagram("0 a b ab"));
		if(dg1.findAbstractDiagram().getZoneList().size() != 4) {outln("Test 5.1.0.1g FAIL");}
		if(!dg1.findAbstractDiagram().toString().equals("0 a b ab")) {outln("Test 5.1.0.1h FAIL");}
		if(dg1.getNodes().size() != 4) {outln("Test 5.1.0.1i FAIL");}
		if(dg1.getEdges().size() != 4) {outln("Test 5.1.0.1j FAIL");}
		if(!dg1.consistent()) {outln("Test 5.1.0.1k FAIL");}
		if(!dg1.consistent()) {outln("Test 5.1.0.1l FAIL");}
		
		// test a bug in a Graph method
		diagram = new AbstractDiagram("0 a b ab ac az abd acq abde abdf acqr abdef"); 
		dg1 = new DualGraph(diagram);
		n1 = dg1.firstNodeWithLabel("");
		nodes = dg1.connectedUnvisitedNodes(n1);
		if(nodes.size() != 12) {outln("Test 5.1.0.2 FAIL");}
		
		dg1 = new DualGraph();
		dg2 = dg1.clone();
		if(!dg2.getLabel().equals("")) {outln("Test 5.1.1 FAIL");}
		dg1 = new DualGraph();
		dg1.setLabel("fred");
		dg2 = dg1.clone();
		if(!dg2.getLabel().equals("fred")) {outln("Test 5.1.2 FAIL");}
		if(dg2.findAbstractDiagram().getZoneList().size() != 0) {outln("Test 5.1.2a FAIL");}
		dg1.addNode(new Node());
		if(dg2.getNodes().size() != 0) {outln("Test 5.1.3 FAIL");}
		if(dg2.getEdges().size() != 0) {outln("Test 5.1.3a FAIL");}
		if(!dg1.consistent()) {outln("Test 5.1.3b FAIL");}
		if(!dg2.consistent()) {outln("Test 5.1.3c FAIL");}
		
		dg1 = new DualGraph();
		n1 = new Node("n1");
		n1.setType(new NodeType("t2"));
		n1.setVisited(true);
		n1.setScore(5.0);
		n1.setCentre(new Point(6,7));
		dg1.addNode(n1);
		dg2 = dg1.clone();
		nodes = dg2.getNodes();
		if(nodes.size() != 1) {outln("Test 5.1.4 FAIL");}
		if(!nodes.get(0).getLabel().equals("n1")) {outln("Test 5.1.5 FAIL");}
		if(!nodes.get(0).getType().getLabel().equals("t2")) {outln("Test 5.1.16 FAIL");}
		if(nodes.get(0).getVisited() != true) {outln("Test 5.1.7 FAIL");}
		if(nodes.get(0).getScore() != 5.0) {outln("Test 5.1.8 FAIL");}
		if(!nodes.get(0).getCentre().equals(new Point(6,7))) {outln("Test 5.1.9 FAIL");}
	
		n1.setLabel("changed n1");
		n1.setType(new NodeType("t3"));
		n1.setVisited(false);
		n1.setScore(15.0);
		n1.setCentre(new Point(16,17));
		
		nodes = dg2.getNodes();
		if(nodes.size() != 1) {outln("Test 5.1.10 FAIL");}
		if(!nodes.get(0).getLabel().equals("n1")) {outln("Test 5.1.11 FAIL");}
		if(!nodes.get(0).getType().getLabel().equals("t2")) {outln("Test 5.1.12 FAIL");}
		if(nodes.get(0).getVisited() != true) {outln("Test 5.1.13 FAIL");}
		if(nodes.get(0).getScore() != 5.0) {outln("Test 5.1.14 FAIL");}
		if(!nodes.get(0).getCentre().equals(new Point(6,7))) {outln("Test 5.1.15 FAIL");}
		
		dg1 = new DualGraph();
		n1 = new Node("n1");
		n2 = new Node("n2");
		e1 = new Edge(n1,n2);
		dg1.addNode(n1);
		dg1.addNode(n2);
		dg1.addEdge(e1);
		e1.setLabel("e1");
		e1.setScore(1.5);
		bends = new ArrayList<Point>();
		bends.add(new Point(2,3));
		bends.add(new Point(22,33));
		e1.setBends(bends);
		e1.setType(new EdgeType("et2"));
		e1.setVisited(true);
		e1.setWeight(19);
		
		dg2 = dg1.clone();
		if(!dg2.getLabel().equals("")) {outln("Test 5.2.1 FAIL");}
		if(dg2.findAbstractDiagram().getZoneList().size() == 0) {outln("Test 5.2.1a FAIL");}
		edges = dg2.getEdges();
		if(edges.size() != 1) {outln("Test 5.2.2 FAIL");}
		if(!edges.get(0).getLabel().equals("e1")) {outln("Test 5.2.3 FAIL");}
		if(edges.get(0).getScore() != 1.5) {outln("Test 5.2.4 FAIL");}
		if(!edges.get(0).getFrom().getLabel().equals("n1")) {outln("Test 5.2.5 FAIL");}
		if(!edges.get(0).getTo().getLabel().equals("n2")) {outln("Test 5.2.6 FAIL");}
		if(!edges.get(0).getType().getLabel().equals("et2")) {outln("Test 5.2.7 FAIL");}
		if(edges.get(0).getBends().size() != 2) {outln("Test 5.2.8 FAIL");}
		if(edges.get(0).getVisited() != true) {outln("Test 5.2.9 FAIL");}
		if(edges.get(0).getWeight() != 19) {outln("Test 5.2.10 FAIL");}
		if(!dg1.consistent()) {outln("Test 5.2.11 FAIL");}
		if(!dg2.consistent()) {outln("Test 5.2.12 FAIL");}
				
		n1.setLabel("n1 changed");
		n2.setLabel("n2 changed");
		
		dg1.removeEdge(e1);
		edges = dg2.getEdges();
		if(edges.size() != 1) {outln("Test 5.3.1 FAIL");}
		if(!dg1.consistent()) {outln("Test 5.3.2 FAIL");}
		if(!dg2.consistent()) {outln("Test 5.3.3 FAIL");}
		n3 = new Node("n3");
		e1 = new Edge(n1,n2);
		e2 = new Edge(n3,n3);
		dg1.addNode(n3);
		dg1.addEdge(e1);
		dg1.addEdge(e2);
		if(edges.size() != 1) {outln("Test 5.3.4 FAIL");}
		if(!dg1.consistent()) {outln("Test 5.3.5 FAIL");}
		if(!dg2.consistent()) {outln("Test 5.3.6 FAIL");}
	
		dg2= dg1.clone();
		edges = dg2.getEdges();
		if(edges.size() != 2) {outln("Test 5.3.7 FAIL");}
		if(!dg1.consistent()) {outln("Test 5.3.8 FAIL");}
		if(!dg2.consistent()) {outln("Test 5.3.9 FAIL");}
		nodes = dg2.getNodes();
		if(nodes.size() != 3) {outln("Test 5.3.10 FAIL");}

		diagram = new AbstractDiagram("");
		dg1 = new DualGraph(diagram);
		dg2 = dg1.clone();
		if(!dg2.findAbstractDiagram().toString().equals("")) {outln("Test 5.4.1 FAIL");}
		
		diagram.addZone("a");
		if(!dg2.findAbstractDiagram().toString().equals("")) {outln("Test 5.4.2 FAIL");}
		
		diagram = new AbstractDiagram("0 a b c ab ac bc abc");
		dg1 = new DualGraph(diagram);
		dg2 = dg1.clone();
		if(!dg2.findAbstractDiagram().toString().equals("0 a b c ab ac bc abc")) {outln("Test 5.4.3 FAIL");}
		
		diagram.addZone("z");
		if(!dg2.findAbstractDiagram().toString().equals("0 a b c ab ac bc abc")) {outln("Test 5.4.4 FAIL");}
		
		
		out("Ending method test5");
	}
	
	
	
	/**
	 * test the connectivity test function
	 **/	
	public static void test6(){
		
		outln(" | Starting method test 6");	
		DualGraph dg = new DualGraph(new AbstractDiagram("a b c ab ac bc abc"));
		if(dg.checkConnectivity() == false) {outln("Test 6.1.1 FAIL");}

		dg = new DualGraph(new AbstractDiagram("0 d bd bcd abcd abd ab acd ac"));
		if(dg.checkConnectivity() == true) {outln("Test 6.1.1a FAIL");}
		
		Edge e = dg.getEdges().get(0);
		dg.removeEdge(e);
		if(dg.checkConnectivity(e) == true) {outln("Test 6.1.2 FAIL");}
		dg = new DualGraph(new AbstractDiagram("a b ab ac bc abc"));
		Edge e1 = dg.getEdges().get(0);
		if(dg.checkConnectivity(e1) == false ) {outln("Test 6.1.3 FAIL");}
		dg = new DualGraph(new AbstractDiagram("a b c ab ac bc abc"));				
		if(dg.checkConnectivity("a") == false) {outln("Test 6.1.4 FAIL");}		
		dg.removeNode("ac"); 			
		if(dg.checkConnectivity("b") == true) {outln("Test 6.1.5 FAIL");}	
		

		DualGraph ag2 = new DualGraph(new AbstractDiagram("a b c d ac ad bc bd acd"));
		if(ag2.checkConnectivity() == false) {outln("Test 6.2.1 FAIL");}
		Edge e2 = ag2.getEdges().get(0);
		ag2.removeEdge(e2);		
		if(dg.checkConnectivity(e) == true) {outln("Test 6.2.2 FAIL");}
		
		out("Ending method test 6");		
	}	
	
	
	/**
	 * test the planarity test function
	 **/	
	public static void test7(){
		
		outln(" | Starting method test 7");	
		DualGraph ag = new DualGraph(new AbstractDiagram("0 a b c ab ac bc abc"));
		ag.randomizeNodePoints(new Point(50,50),400,400);
		
		DualGraph ag1 = new DualGraph(AbstractDiagram.VennFactory(4));
		ag1.randomizeNodePoints(new Point(50,50),400,400);

 		
		DualGraph ag2 = new DualGraph (new AbstractDiagram("0 a b c ab bc abc"));
		ag2.randomizeNodePoints(new Point(50,50),400,400);
	
		DualGraph ag3 = new DualGraph (new AbstractDiagram("0 a c d e ab ac ae bd de abc abd abe ace bcd bde abce abcde"));
		ag3.randomizeNodePoints(new Point(50,50),400,400);
	
		out("Ending method test 7");	
	}
	
	
	/**
	 * test the face condition test function
	 **/	
	public static void test8(){
		
		outln(" | Starting method test 8");	
		
		DualGraph dg1 = new DualGraph (new AbstractDiagram("0 a b"));
		dg1.randomizeNodePoints(new Point(50,50),400,400);
	
		if(dg1.passFaceConditions() == false){outln("Test 8.1.1 Fail");}
		
		DualGraph dg2 = new DualGraph (new AbstractDiagram("0 a b ab"));
		dg2.randomizeNodePoints(new Point(50,50),400,400);
		
				
		if(dg2.passFaceConditions() == false){outln("Test 8.1.2 Fail");}	
		
		
		DualGraph dg3 = new DualGraph (new AbstractDiagram("0 a b ac bd acd bcd abcd"));
		dg3.randomizeNodePoints(new Point(50,50),400,400);
		
	
		dg3.formFaces();
		if(dg3.passFaceConditions() == true){outln("Test 8.2.1 Fail");}
		
		DualGraph dg4 = new DualGraph (new AbstractDiagram("0 b c ab ac abc"));
		dg4.randomizeNodePoints(new Point(50,50),400,400);
	
		dg4.formFaces();
		if(dg4.passFaceConditions() == true){outln("Test 8.2.2 Fail");}			

		
		
		DualGraph dg;
		ArrayList<Face> faces;
		ArrayList<String> xContours;
		Face f;
		Node n1,n2,n3,n4;
		
		dg = new DualGraph();
		n1 = new Node("", new Point(100,100));
		n2 = new Node("a", new Point(200,200));
		n3 = new Node("ab", new Point(100,300));
		n4 = new Node("b", new Point(100,200));
		dg.addNode(n1);
		dg.addNode(n2);
		dg.addNode(n3);
		dg.addNode(n4);
		dg.addEdge(new Edge(n1,n2));
		dg.addEdge(new Edge(n2,n3));
		dg.addEdge(new Edge(n3,n4));
		dg.addEdge(new Edge(n4,n1));
		faces = dg.formFaces();
		if(faces.size() != 2);
		f = faces.get(0);
		if(f.getCrossingIndex() != 1) {outln("Test 8.3.1 Fail");}
		xContours = f.getCrossingContours();
		if(!xContours.get(0).equals("ab")) {outln("Test 8.3.2 Fail");}
		if(!f.passFaceConditions()) {outln("Test 8.3.3 Fail");}			
		if(f.findContours().size() !=2)  {outln("Test 8.3.3a Fail");}
		if(!f.findContours().get(0).equals("a")){outln("Test 8.3.3b Fail");}			
		if(!f.findContours().get(1).equals("b")){outln("Test 8.3.3c Fail");}			
		f = faces.get(1);
		if(f.getCrossingIndex() != 1) {outln("Test 8.3.4 Fail");}
		xContours = f.getCrossingContours();
		if(!xContours.get(0).equals("ab")) {outln("Test 8.3.5 Fail");}
		if(!f.passFaceConditions()){outln("Test 8.3.6 Fail");}			
		if(f.findContours().size() !=2)  {outln("Test 8.3.6a Fail");}
		if(!f.findContours().get(0).equals("a")){outln("Test 8.3.6b Fail");}			
		if(!f.findContours().get(1).equals("b")){outln("Test 8.3.6c Fail");}			

		dg = new DualGraph(new AbstractDiagram("0 a b ab ac abc"));
		dg.firstNodeWithLabel("").setCentre(new Point(100,100));
		dg.firstNodeWithLabel("a").setCentre(new Point(200,100));
		dg.firstNodeWithLabel("ab").setCentre(new Point(300,100));
		dg.firstNodeWithLabel("abc").setCentre(new Point(300,200));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,200));
		dg.firstNodeWithLabel("b").setCentre(new Point(100,200));
		faces = dg.formFaces();
		if(faces.size() != 3)  {outln("Test 8.3.7 Fail");}

		for(Face face : faces) {
			if(face.getNodeList().size() == 4) { // 2 faces here
				if(face.getCrossingIndex() != 1) {outln("Test 8.3.8 Fail");}
				xContours = face.getCrossingContours();
				if(!xContours.get(0).equals("ab") && !xContours.get(0).equals("bc")) {outln("Test 8.3.9 Fail");}
				if(xContours.get(0).equals("ab")) {
					if(face.findContours().size() !=2)  {outln("Test 8.3.9a Fail");}
					if(!face.findContours().get(0).equals("a")){outln("Test 8.3.9b Fail");}			
					if(!face.findContours().get(1).equals("b")){outln("Test 8.3.9c Fail");}
				} else if(xContours.get(0).equals("bc")) {
					if(face.findContours().size() !=2)  {outln("Test 8.3.9d Fail");}
					if(!face.findContours().get(0).equals("b")){outln("Test 8.3.9e Fail");}			
					if(!face.findContours().get(1).equals("c")){outln("Test 8.3.9f Fail");}
				} else {
					outln("Test 8.3.9g Fail");
				}
			}
			if(face.getNodeList().size() == 6) { // 1 face here
				if(face.getCrossingIndex() != 2) {outln("Test 8.3.10 Fail");}
				xContours = face.getCrossingContours();
				if(!xContours.contains("ab")) {outln("Test 8.3.11 Fail");}
				if(!xContours.contains("bc")) {outln("Test 8.3.12 Fail");}
				if(face.findContours().size() !=3)  {outln("Test 8.3.12a Fail");}
				if(!face.findContours().get(0).equals("a")){outln("Test 8.3.12b Fail");}			
				if(!face.findContours().get(1).equals("b")){outln("Test 8.3.12c Fail");}			
				if(!face.findContours().get(2).equals("c")){outln("Test 8.3.12d Fail");}			
			}
		}
		if(!dg.passFaceConditions()) {outln("Test 8.3.13 Fail");}
		
		dg = new DualGraph (new AbstractDiagram("0 a b c ab ac bc"));
		dg.firstNodeWithLabel("").setCentre(new Point(100,100));
		dg.firstNodeWithLabel("a").setCentre(new Point(200,100));
		dg.firstNodeWithLabel("ab").setCentre(new Point(300,100));
		dg.firstNodeWithLabel("b").setCentre(new Point(300,200));
		dg.firstNodeWithLabel("bc").setCentre(new Point(200,200));
		dg.firstNodeWithLabel("c").setCentre(new Point(100,200));
		dg.firstNodeWithLabel("ac").setCentre(new Point(0,0));
		faces = dg.formFaces();

		faces = dg.formFaces();
		if(faces.size() != 4) {outln("Test 8.3.14 Fail");}
		for(Face face : faces) {
			if(face.getNodeList().size() == 4) { // 2 faces here
				if(face.getCrossingIndex() != 1) {outln("Test 8.3.15 Fail");}
				xContours = face.getCrossingContours();
				if(!xContours.get(0).equals("ab") && !xContours.get(0).equals("ac") && !xContours.get(0).equals("bc")) {outln("Test 8.3.16 Fail");}
				if(xContours.get(0).equals("ab")) {
					if(face.findContours().size() !=2)  {outln("Test 8.3.16a Fail");}
					if(!face.findContours().get(0).equals("a")){outln("Test 8.3.16b Fail");}			
					if(!face.findContours().get(1).equals("b")){outln("Test 8.3.16c Fail");}
				} else if(xContours.get(0).equals("bc")) {
					if(face.findContours().size() !=2)  {outln("Test 8.3.16d Fail");}
					if(!face.findContours().get(0).equals("b")){outln("Test 8.3.16e Fail");}			
					if(!face.findContours().get(1).equals("c")){outln("Test 8.3.16f Fail");}
				}
			}
			if(face.getNodeList().size() == 6) { // one of these
				if(face.getCrossingIndex() != 3) {outln("Test 8.3.17 Fail");}
				xContours = face.getCrossingContours();
				if(!xContours.contains("ab")) {outln("Test 8.3.18 Fail");}
				if(!xContours.contains("ac")) {outln("Test 8.3.19 Fail");}
				if(!xContours.contains("bc")) {outln("Test 8.3.20 Fail");}
				if(face.findContours().size() !=3)  {outln("Test 8.3.20a Fail");}
				if(!face.findContours().get(0).equals("a")){outln("Test 8.3.20b Fail");}			
				if(!face.findContours().get(1).equals("b")){outln("Test 8.3.20c Fail");}			
				if(!face.findContours().get(2).equals("c")){outln("Test 8.3.20d Fail");}			
			} else if(face.getNodeList().size() == 4) { // three of these
				xContours = face.getCrossingContours();
				if(face.findContours().contains("a") && face.findContours().contains("b")) {
					if(!xContours.contains("ab")) {outln("Test 8.3.20e Fail");}
				} else if(face.findContours().contains("a") && face.findContours().contains("c")) {
					if(!xContours.contains("ac")) {outln("Test 8.3.20f Fail");}
				} else if(face.findContours().contains("b") && face.findContours().contains("c")) {
					if(!xContours.contains("bc")) {outln("Test 8.3.20g Fail");}
				} else {
					outln("Test 8.3.20h Fail");
				}
			} else {
				outln("Test 8.3.20i Fail");
			}
		}
		if(dg.passFaceConditions()) {outln("Test 8.3.21 Fail");}

		dg = new DualGraph(new AbstractDiagram("0 ab ac"));
		dg.firstNodeWithLabel("").setCentre(new Point(100,100));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		dg.firstNodeWithLabel("ac").setCentre(new Point(300,100));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("ab"),"ab"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("ac"),"ac"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ab"),dg.firstNodeWithLabel("ac"),"bc"));
		faces = dg.formFaces();
		for(Face face : faces) {
			if(face.getNodeList().size() != 3) {outln("Test 8.3.22 Fail");}
			if(face.findContours().size() !=3)  {outln("Test 8.3.23 Fail");}
			if(!face.findContours().get(0).equals("a")){outln("Test 8.3.24 Fail");}			
			if(!face.findContours().get(1).equals("b")){outln("Test 8.3.25 Fail");}
			if(!face.findContours().get(2).equals("c")){outln("Test 8.3.26 Fail");}
			if(face.getNodeList().size() == 3) { // 2 faces here
				if(face.getCrossingIndex() != 0) {outln("Test 8.3.27 Fail");}
			} else {
				outln("Test 8.3.28 Fail");
			}
		}
		
		dg = new DualGraph();
		dg.addNode(new Node("",new Point(0,0)));
		dg.addNode(new Node("ab",new Point(0,10)));
		dg.addNode(new Node("abde",new Point(0,20)));
		dg.addNode(new Node("ad",new Point(0,30)));
		dg.addNode(new Node("d",new Point(10,0)));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("ab"),"ab"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ab"),dg.firstNodeWithLabel("abde"),"de"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("abde"),dg.firstNodeWithLabel("ad"),"be"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ad"),dg.firstNodeWithLabel("d"),"a"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("d"),dg.firstNodeWithLabel(""),"d"));
		faces = dg.formFaces();
		for(Face face : faces) {
			if(face.getNodeList().size() != 5) {outln("Test 8.3.29 Fail");}
			if(face.findContours().size() !=4)  {outln("Test 8.3.30 Fail");}
			if(!face.findContours().get(0).equals("a")){outln("Test 8.3.31 Fail");}			
			if(!face.findContours().get(1).equals("b")){outln("Test 8.3.32 Fail");}
			if(!face.findContours().get(2).equals("d")){outln("Test 8.3.33 Fail");}
			if(!face.findContours().get(3).equals("e")){outln("Test 8.3.34 Fail");}
			if(face.getNodeList().size() == 5) { // 2 faces here
				if(face.getCrossingIndex() != 2) {outln("Test 8.3.35 Fail");}
				xContours = face.getCrossingContours();
				if(!xContours.contains("ad")) {outln("Test 8.3.36 Fail");}
				if(!xContours.contains("bd")) {outln("Test 8.3.38 Fail");}
			} else {
				outln("Test 8.3.39 Fail");
			}
		}
		
		dg = new DualGraph();
		dg.addNode(new Node("",new Point(0,0)));
		dg.addNode(new Node("abc",new Point(0,10)));
		dg.addNode(new Node("abde",new Point(0,20)));
		dg.addNode(new Node("de",new Point(0,30)));
		dg.addNode(new Node("e",new Point(10,0)));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("abc"),"abc"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("abc"),dg.firstNodeWithLabel("abde"),"cde"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("abde"),dg.firstNodeWithLabel("de"),"ab"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("de"),dg.firstNodeWithLabel("e"),"d"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("e"),dg.firstNodeWithLabel(""),"e"));
		faces = dg.formFaces();
		for(Face face : faces) {
			if(face.getNodeList().size() != 5) {outln("Test 8.3.40 Fail");}
			if(face.findContours().size() !=5)  {outln("Test 8.3.41 Fail");}
			if(!face.findContours().get(0).equals("a")){outln("Test 8.3.42 Fail");}			
			if(!face.findContours().get(1).equals("b")){outln("Test 8.3.43 Fail");}
			if(!face.findContours().get(2).equals("c")){outln("Test 8.3.44 Fail");}
			if(!face.findContours().get(3).equals("d")){outln("Test 8.3.45 Fail");}
			if(!face.findContours().get(4).equals("e")){outln("Test 8.3.46 Fail");}
			if(face.getNodeList().size() == 5) { // 2 faces here
				if(face.getCrossingIndex() != 4) {outln("Test 8.3.47 Fail");}
				xContours = face.getCrossingContours();
				if(!xContours.contains("ad")) {outln("Test 8.3.48 Fail");}
				if(!xContours.contains("ae")) {outln("Test 8.3.49 Fail");}
				if(!xContours.contains("bd")) {outln("Test 8.3.50 Fail");}
				if(!xContours.contains("be")) {outln("Test 8.3.51 Fail");}
			} else {
				outln("Test 8.3.52 Fail");
			}
		}
		
		dg = new DualGraph(new AbstractDiagram("0 a b c ab bc"));
		faces = dg.formFaces();
		if(faces.size() != 3) {outln("Test 8.4.1 Fail");}
		for(Face face : faces) {
			ArrayList<Node> nodeList = face.getNodeList();
			if(nodeList.size() == 6) { // one of these
				Node n = dg.firstNodeWithLabel(face.getFaceSymbols().substring(0,1));
				String word = face.findPartialWord(n, n);
				String reverseWord = pjr.graph.Util.reverseString(word);
				if(!word.equals(face.getFaceSymbols()) && !reverseWord.equals(face.getFaceSymbols())) {outln("Test 8.4.2 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel("ab"), dg.firstNodeWithLabel("bc"));
				if(!word.equals("ca") && !word.equals("bacb")) {outln("Test 8.4.3 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel("bc"), dg.firstNodeWithLabel("ab"));
				if(!word.equals("ca") && !word.equals("bacb")) {outln("Test 8.4.4 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel(""), dg.firstNodeWithLabel("c"));
				if(!word.equals("c") && !word.equals("abacb")) {outln("Test 8.4.5 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel(""), dg.firstNodeWithLabel("c"));
				if(!word.equals("c") && !word.equals("abacb")) {outln("Test 8.4.6 Fail");}
			} else if(nodeList.size() == 4) { // two of these
				Node n = dg.firstNodeWithLabel(face.getFaceSymbols().substring(0,1));
				String word = face.findPartialWord(n, n);
				String reverseWord = pjr.graph.Util.reverseString(word);
				if(!word.equals(face.getFaceSymbols()) && !reverseWord.equals(face.getFaceSymbols())) {outln("Test 8.4.7 Fail");}
			} else {
				outln("Test 8.4.8 Fail");
			}
		}
		
		dg = new DualGraph(new AbstractDiagram("0 ab ac"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("ab")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("ac")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ab"),dg.firstNodeWithLabel("ac")));
		faces = dg.formFaces();
		if(faces.size() != 2) {outln("Test 8.4.9 Fail");}
		for(Face face : faces) {
			ArrayList<Node> nodeList = face.getNodeList();
			if(nodeList.size() == 3) { // two of these
				Node ni = dg.firstNodeWithLabel("");
				Node nj = dg.firstNodeWithLabel("ab");
				Node nk = dg.firstNodeWithLabel("ac");
				String wordi = face.findPartialWord(ni, ni);
				String wordj = face.findPartialWord(nj, nj);
				String wordk = face.findPartialWord(nk, nk);
				if(wordi.length() != 6) {outln("Test 8.4.10 Fail");}
				if(wordj.length() != 6) {outln("Test 8.4.11 Fail");}
				if(wordk.length() != 6) {outln("Test 8.4.12 Fail");}
				String word = face.findPartialWord(dg.firstNodeWithLabel(""), dg.firstNodeWithLabel("ab"));
				if(!word.equals("ab") && !word.equals("acbc")) {outln("Test 8.4.13 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel("ab"), dg.firstNodeWithLabel(""));
				if(!word.equals("ab") && !word.equals("bcac")) {outln("Test 8.4.14 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel("ab"), dg.firstNodeWithLabel("ac"));
				if(!word.equals("bc") && !word.equals("abac")) {outln("Test 8.4.15 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel("ac"), dg.firstNodeWithLabel("ab"));
				if(!word.equals("bc") && !word.equals("acab")) {outln("Test 8.4.16 Fail");}
			} else {
				outln("Test 8.4.17 Fail");
			}
		}
		
		dg = new DualGraph(new AbstractDiagram("xy axy bxy abxy"));
		dg.firstNodeWithLabel("xy").setCentre(new Point(10,10));
		dg.firstNodeWithLabel("axy").setCentre(new Point(20,10));
		dg.firstNodeWithLabel("bxy").setCentre(new Point(0,10));
		dg.firstNodeWithLabel("abxy").setCentre(new Point(20,10));
		
		faces = dg.formFaces();
		if(faces.size() != 2) {outln("Test 8.4.18 Fail");}
		for(Face face : faces) {
			ArrayList<Node> nodeList = face.getNodeList();
			if(nodeList.size() == 4) { // two of these
				Node n = dg.firstNodeWithLabel(face.getFaceSymbols().substring(0,1)+"xy");
				String word = face.findPartialWord(n, n);
				String reverseWord = pjr.graph.Util.reverseString(word);
				if(!word.equals(face.getFaceSymbols()) && !reverseWord.equals(face.getFaceSymbols())) {outln("Test 8.4.19 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel("xy"), dg.firstNodeWithLabel("abxy"));
				if(!word.equals("ba") && !word.equals("ab")) {outln("Test 8.4.20 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel("abxy"), dg.firstNodeWithLabel("xy"));
				if(!word.equals("ba") && !word.equals("ab")) {outln("Test 8.4.21 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel("axy"), dg.firstNodeWithLabel("abxy"));
				if(!word.equals("b") && !word.equals("aba")) {outln("Test 8.4.22 Fail");}
				word = face.findPartialWord(dg.firstNodeWithLabel("abxy"), dg.firstNodeWithLabel("axy"));
				if(!word.equals("b") && !word.equals("aba")) {outln("Test 8.4.23 Fail");}
			} else {
				outln("Test 8.4.24 Fail");
			}
		}
		
 		
 		out("Ending method test 8");

	}
	
	
	
	/**
	 * test the removable/unremovable edges function
	 **/	
	
	public static void test9(){
		
		outln(" | Starting method test 9");
		
		DualGraph ag1 = new DualGraph (new AbstractDiagram("0 a b c ab bc abc"));
		ag1.randomizeNodePoints(new Point(50,50),400,400);
	
		Edge e1 = ag1.getEdges().get(1);
		ag1.removeEdge(e1);
		if(ag1.connected() == false){outln("Test 9.1.1 Fail");}
		ag1.addEdge(e1);
		
		Edge e2 = ag1.getEdges().get(4);
		ag1.removeEdge(e2);
		if(ag1.connected() == false){outln("Test 9.1.2 Fail");}		
		ag1.addEdge(e2);
		
		Edge e3 = ag1.getEdges().get(5);
		ag1.removeEdge(e3);
		if(ag1.connected() == false){outln("Test 9.1.3 Fail");}		
		ag1.addEdge(e3);	
		
		Edge e4 = ag1.getEdges().get(0);
		ag1.removeEdge(e4);
		if(ag1.connected() == true){outln("Test 9.2.1 Fail");}		
		ag1.addEdge(e4);
		
		Edge e5 = ag1.getEdges().get(2);
		ag1.removeEdge(e5);
		if(ag1.connected() == true){outln("Test 9.2.2 Fail");}		
		ag1.addEdge(e5);		
		Edge e6 = ag1.getEdges().get(3);
		ag1.removeEdge(e6);
		if(ag1.connected() == true){outln("Test 9.2.3 Fail");}		
		ag1.addEdge(e6);
		
		
		
		DualGraph dg;
		DualGraph dgRet;
		AbstractDiagram adRet;
		Node n1;
		Node n2;
		Node n3;
		Edge e;
		ArrayList<Node> nList1;
		ArrayList<Node> nList2;
		ArrayList<Node> nodesRet;
		ArrayList<Edge> edgesRet;
		ArrayList<String> stringListRet;
		ZoneStringComparator zComp = new ZoneStringComparator();
		
		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		nodesRet = DualGraph.nodeListIntersection(nList1, nList2);
		if(nodesRet.size() != 0) {outln("Test 9.3.0.1 Fail");}
		nodesRet = DualGraph.nodeListIntersection(nList2, nList1);
		if(nodesRet.size() != 0) {outln("Test 9.3.0.2 Fail");}

		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		n1 = new Node("n1");
		nList1.add(n1);
		nodesRet = DualGraph.nodeListIntersection(nList1, nList2);
		if(nodesRet.size() != 0) {outln("Test 9.3.0.3 Fail");}
		nodesRet = DualGraph.nodeListIntersection(nList2, nList1);
		if(nodesRet.size() != 0) {outln("Test 9.3.0.4 Fail");}

		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		n1 = new Node("n1");
		nList1.add(n1);
		nList2.add(n1);
		nodesRet = DualGraph.nodeListIntersection(nList1, nList2);
		if(nodesRet.size() != 1) {outln("Test 9.3.0.5 Fail");}
		if(nodesRet.get(0) != n1) {outln("Test 9.3.0.6 Fail");}
		nodesRet = DualGraph.nodeListIntersection(nList2, nList1);
		if(nodesRet.size() != 1) {outln("Test 9.3.0.7 Fail");}
		if(nodesRet.get(0) != n1) {outln("Test 9.3.0.8 Fail");}

		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		n1 = new Node("n1");
		nList1.add(new Node("a"));
		nList1.add(new Node("b"));
		nList1.add(n1);
		nList1.add(new Node("b"));
		nList2.add(new Node("x"));
		nList2.add(new Node("y"));
		nList2.add(n1);
		nList2.add(new Node("z"));
		nodesRet = DualGraph.nodeListIntersection(nList1, nList2);
		if(nodesRet.size() != 1) {outln("Test 9.3.0.9 Fail");}
		if(nodesRet.get(0) != n1) {outln("Test 9.3.0.10 Fail");}
		nodesRet = DualGraph.nodeListIntersection(nList2, nList1);
		if(nodesRet.size() != 1) {outln("Test 9.3.0.11 Fail");}
		if(nodesRet.get(0) != n1) {outln("Test 9.3.0.12 Fail");}

		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		n1 = new Node("n1");
		n2 = new Node("n2");
		nList1.add(new Node("a"));
		nList1.add(new Node("b"));
		nList1.add(n1);
		nList1.add(n2);
		nList1.add(new Node("b"));
		nList2.add(new Node("x"));
		nList2.add(new Node("y"));
		nList2.add(n2);
		nList2.add(n1);
		nList2.add(new Node("z"));
		nodesRet = DualGraph.nodeListIntersection(nList1, nList2);
		if(nodesRet.size() != 2) {outln("Test 9.3.0.13 Fail");}
		if(nodesRet.get(0) != n1) {outln("Test 9.3.0.14 Fail");}
		if(nodesRet.get(1) != n2) {outln("Test 9.3.0.15 Fail");}
		nodesRet = DualGraph.nodeListIntersection(nList2, nList1);
		if(nodesRet.size() != 2) {outln("Test 9.3.0.16 Fail");}
		if(nodesRet.get(0) != n2) {outln("Test 9.3.0.17 Fail");}
		if(nodesRet.get(1) != n1) {outln("Test 9.3.0.18 Fail");}

		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		nodesRet = DualGraph.nodeListDifference(nList1, nList2);
		if(nodesRet.size() != 0) {outln("Test 9.3.0.19 Fail");}
		nodesRet = DualGraph.nodeListDifference(nList2, nList1);
		if(nodesRet.size() != 0) {outln("Test 9.3.0.20 Fail");}

		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		n1 = new Node("n1");
		nList1.add(n1);
		nodesRet = DualGraph.nodeListDifference(nList1, nList2);
		if(nodesRet.size() != 1) {outln("Test 9.3.0.21 Fail");}
		if(nodesRet.get(0) != n1) {outln("Test 9.3.0.22 Fail");}
		nodesRet = DualGraph.nodeListDifference(nList2, nList1);
		if(nodesRet.size() != 0) {outln("Test 9.3.0.23 Fail");}

		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		n1 = new Node("n1");
		n2 = new Node("n2");
		nList1.add(n1);
		nList2.add(n2);
		nodesRet = DualGraph.nodeListDifference(nList1, nList2);
		if(nodesRet.size() != 1) {outln("Test 9.3.0.24 Fail");}
		if(nodesRet.get(0) != n1) {outln("Test 9.3.0.15 Fail");}
		nodesRet = DualGraph.nodeListDifference(nList2, nList1);
		if(nodesRet.size() != 1) {outln("Test 9.3.0.26 Fail");}
		if(nodesRet.get(0) != n2) {outln("Test 9.3.0.27 Fail");}

		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		n1 = new Node("n1");
		n2 = new Node("n2");
		nList1.add(n1);
		nList2.add(n1);
		nList2.add(n2);
		nodesRet = DualGraph.nodeListDifference(nList1, nList2);
		if(nodesRet.size() != 0) {outln("Test 9.3.0.28 Fail");}
		nodesRet = DualGraph.nodeListDifference(nList2, nList1);
		if(nodesRet.size() != 1) {outln("Test 9.3.0.29 Fail");}
		if(nodesRet.get(0) != n2) {outln("Test 9.3.0.30 Fail");}

		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		nList1.add(n1);
		nList2.add(n2);
		nList2.add(n3);
		nodesRet = DualGraph.nodeListDifference(nList1, nList2);
		if(nodesRet.size() != 1) {outln("Test 9.3.0.31 Fail");}
		if(nodesRet.get(0) != n1) {outln("Test 9.3.0.32 Fail");}
		nodesRet = DualGraph.nodeListDifference(nList2, nList1);
		if(nodesRet.size() != 2) {outln("Test 9.3.0.33 Fail");}
		if(nodesRet.get(0) != n2) {outln("Test 9.3.0.34 Fail");}
		if(nodesRet.get(1) != n3) {outln("Test 9.3.0.35 Fail");}

		nList1 = new ArrayList<Node>();
		nList2 = new ArrayList<Node>();
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		nList1.add(n1);
		nList1.add(n2);
		nList1.add(n3);
		nList2.add(n1);
		nList2.add(n2);
		nList2.add(n3);
		nodesRet = DualGraph.nodeListDifference(nList1, nList2);
		if(nodesRet.size() != 0) {outln("Test 9.3.0.36 Fail");}
		nodesRet = DualGraph.nodeListDifference(nList2, nList1);
		if(nodesRet.size() != 0) {outln("Test 9.3.0.37 Fail");}
		
		
		
		
		
		dg = new DualGraph(new AbstractDiagram("0 a ab"));
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("ab");
		e = dg.getEdge(n1,n2);
		
		dgRet = dg.findSubdiagram(n1,e);
		
		nodesRet = dgRet.getNodes();
		if(nodesRet.size() != 2) {outln("Test 9.3.1 Fail");}
		stringListRet = new ArrayList<String>();
		for(Node nodeRet :nodesRet) {
			stringListRet.add(nodeRet.getLabel());
		}
		Collections.sort(stringListRet,zComp);
		if(!stringListRet.get(0).equals("a")) {outln("Test 9.3.2 Fail");}
		if(!stringListRet.get(1).equals("ab")) {outln("Test 9.3.3 Fail");}
		if(!dgRet.getLabel().equals("a")) {outln("Test 9.3.3a Fail");}

		edgesRet = dgRet.getEdges();
		if(edgesRet.size() != 1) {outln("Test 9.3.4 Fail");}
		stringListRet = new ArrayList<String>();
		for(Edge edgeRet :edgesRet) {
			stringListRet.add(edgeRet.getLabel());
		}
		Collections.sort(stringListRet,zComp);
		if(!stringListRet.get(0).equals("b")) {outln("Test 9.3.5 Fail");}
		adRet = dgRet.findAbstractDiagram();
		if(!adRet.isomorphicTo(new AbstractDiagram("a ab"))) {outln("Test 9.3.6 Fail");}
		if(!dgRet.getLabel().equals("a")) {outln("Test 9.3.6a Fail");}

		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("ab");
		e = dg.getEdge(n1,n2);
		dgRet = dg.findSubdiagram(n1,e);
		if(dgRet != null) {outln("Test 9.3.7 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab ac"));
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("ab");
		e = dg.getEdge(n1,n2);
		dgRet = dg.findSubdiagram(n1,e);
		if(dgRet != null) {outln("Test 9.3.8 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a ab abc d ad adc abcd"));
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("ab");
		e = dg.getEdge(n1,n2);
		dgRet = dg.findSubdiagram(n1,e);
		if(dgRet != null) {outln("Test 9.3.9 Fail");}

		
		dg = new DualGraph(new AbstractDiagram("0 a ab ac"));
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("ac");
		e = dg.getEdge(n1,n2);
		
		dgRet = dg.findSubdiagram(n1,e);
		
		nodesRet = dgRet.getNodes();
		if(nodesRet.size() != 2) {outln("Test 9.3.10 Fail");}
		stringListRet = new ArrayList<String>();
		for(Node nodeRet :nodesRet) {
			stringListRet.add(nodeRet.getLabel());
		}
		Collections.sort(stringListRet,zComp);
		if(!stringListRet.get(0).equals("a")) {outln("Test 9.3.11 Fail");}
		if(!stringListRet.get(1).equals("ac")) {outln("Test 9.3.12 Fail");}

		edgesRet = dgRet.getEdges();
		if(edgesRet.size() != 1) {outln("Test 9.3.13 Fail");}
		stringListRet = new ArrayList<String>();
		for(Edge edgeRet :edgesRet) {
			stringListRet.add(edgeRet.getLabel());
		}
		Collections.sort(stringListRet,zComp);
		if(!stringListRet.get(0).equals("c")) {outln("Test 9.3.14 Fail");}
		adRet = dgRet.findAbstractDiagram();
		if(!adRet.isomorphicTo(new AbstractDiagram("a ac"))) {outln("Test 9.3.15 Fail");}
		if(!dgRet.getLabel().equals("a")) {outln("Test 9.3.15a Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a ab ac"));
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("ab");
		e = dg.getEdge(n1,n2);
		
		dgRet = dg.findSubdiagram(n1,e);
		
		nodesRet = dgRet.getNodes();
		if(nodesRet.size() != 2) {outln("Test 9.3.15 Fail");}
		stringListRet = new ArrayList<String>();
		for(Node nodeRet :nodesRet) {
			stringListRet.add(nodeRet.getLabel());
		}
		Collections.sort(stringListRet,zComp);
		if(!stringListRet.get(0).equals("a")) {outln("Test 9.3.16 Fail");}
		if(!stringListRet.get(1).equals("ab")) {outln("Test 9.3.17 Fail");}

		edgesRet = dgRet.getEdges();
		if(edgesRet.size() != 1) {outln("Test 9.3.18 Fail");}
		stringListRet = new ArrayList<String>();
		for(Edge edgeRet :edgesRet) {
			stringListRet.add(edgeRet.getLabel());
		}
		Collections.sort(stringListRet,zComp);
		if(!stringListRet.get(0).equals("b")) {outln("Test 9.3.19 Fail");}
		adRet = dgRet.findAbstractDiagram();
		if(!adRet.isomorphicTo(new AbstractDiagram("a ab"))) {outln("Test 9.3.20 Fail");}
		if(!dgRet.getLabel().equals("a")) {outln("Test 9.3.20a Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab abc abd abcd"));
		n1 = dg.firstNodeWithLabel("ab");
		n2 = dg.firstNodeWithLabel("abd");
		e = dg.getEdge(n1,n2);
		
		dgRet = dg.findSubdiagram(n1,e);
		
		nodesRet = dgRet.getNodes();
		if(nodesRet.size() != 4) {outln("Test 9.3.20 Fail");}
		stringListRet = new ArrayList<String>();
		for(Node nodeRet :nodesRet) {
			stringListRet.add(nodeRet.getLabel());
		}
		Collections.sort(stringListRet,zComp);
		if(!stringListRet.get(0).equals("ab")) {outln("Test 9.3.21 Fail");}
		if(!stringListRet.get(1).equals("abc")) {outln("Test 9.3.22 Fail");}
		if(!stringListRet.get(2).equals("abd")) {outln("Test 9.3.23 Fail");}
		if(!stringListRet.get(3).equals("abcd")) {outln("Test 9.3.24 Fail");}

		edgesRet = dgRet.getEdges();
		if(edgesRet.size() != 4) {outln("Test 9.3.25 Fail");}
		stringListRet = new ArrayList<String>();
		for(Edge edgeRet :edgesRet) {
			stringListRet.add(edgeRet.getLabel());
		}
		Collections.sort(stringListRet,zComp);
		if(!stringListRet.get(0).equals("c")) {outln("Test 9.3.26 Fail");}
		if(!stringListRet.get(1).equals("c")) {outln("Test 9.3.27 Fail");}
		if(!stringListRet.get(2).equals("d")) {outln("Test 9.3.28 Fail");}
		if(!stringListRet.get(3).equals("d")) {outln("Test 9.3.29 Fail");}
		adRet = dgRet.findAbstractDiagram();
		if(!adRet.isomorphicTo(new AbstractDiagram("ab abc abd abcd"))) {outln("Test 9.3.30 Fail");}
		if(!dgRet.getLabel().equals("ab")) {outln("Test 9.3.30a Fail");}

		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("ab");
		e = dg.getEdge(n1,n2);
		dgRet = dg.findSubdiagram(n1,e);
		if(dgRet != null) {outln("Test 9.3.31 Fail");}
		
		n1 = dg.firstNodeWithLabel("abc");
		n2 = dg.firstNodeWithLabel("abcd");
		e = dg.getEdge(n1,n2);
		dgRet = dg.findSubdiagram(n1,e);
		if(dgRet != null) {outln("Test 9.3.32 Fail");}

		
		
		ArrayList<DualGraph> dgList;
		AbstractDiagram ad;
		
		dg = new DualGraph(new AbstractDiagram(""));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 0) {outln("Test 9.4.0a Fail");}
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 0) {outln("Test 9.4.0b Fail");}

		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 0) {outln("Test 9.4.1 Fail");}
		ad = dg.findAbstractDiagram();
		if(!ad.isomorphicTo(new AbstractDiagram("0 a b ab"))) {outln("Test 9.4.2 Fail");}
		if(dg.getNodes().size() != 4) {outln("Test 9.4.3 Fail");}
		if(dg.getEdges().size() != 4) {outln("Test 9.4.4 Fail");}
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 0) {outln("Test 9.4.5 Fail");}
		ad = dg.findAbstractDiagram();
		if(!ad.isomorphicTo(new AbstractDiagram("0 a b ab"))) {outln("Test 9.4.6 Fail");}
		if(dg.getNodes().size() != 4) {outln("Test 9.4.7 Fail");}
		if(dg.getEdges().size() != 4) {outln("Test 9.4.8 Fail");}
		
		dg = new DualGraph(new AbstractDiagram("0 a ab"));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 1) {outln("Test 9.4.9 Fail");}
		dgRet = dgList.get(0);
		adRet = dgRet.findAbstractDiagram();
		if(!adRet.isomorphicTo(new AbstractDiagram("a ab"))) {outln("Test 9.4.10 Fail");}
		if(!dgRet.getLabel().equals("a")) {outln("Test 9.4.10a Fail");}
		if(dgRet.getNodes().size() != 2) {outln("Test 9.4.11 Fail");}
		if(dgRet.getEdges().size() != 1) {outln("Test 9.4.12 Fail");}
		ad = dg.findAbstractDiagram();
		if(!ad.isomorphicTo(new AbstractDiagram("0 a ab"))) {outln("Test 9.4.13 Fail");}
		if(dg.getNodes().size() != 3) {outln("Test 9.4.14 Fail");}
		if(dg.getEdges().size() != 2) {outln("Test 9.4.15 Fail");}
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab abc"));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 1) {outln("Test 9.4.16 Fail");}
		dgRet = dgList.get(0);
		adRet = dgRet.findAbstractDiagram();
		if(!adRet.isomorphicTo(new AbstractDiagram("ab abc"))) {outln("Test 9.4.17 Fail");}
		if(!dgRet.getLabel().equals("ab")) {outln("Test 9.4.17a Fail");}
		if(dgRet.getNodes().size() != 2) {outln("Test 9.4.18 Fail");}
		if(dgRet.getEdges().size() != 1) {outln("Test 9.4.19 Fail");}
		ad = dg.findAbstractDiagram();
		if(!ad.isomorphicTo(new AbstractDiagram("0 a b ab abc"))) {outln("Test 9.4.20 Fail");}
		if(dg.getNodes().size() != 5) {outln("Test 9.4.21 Fail");}
		if(dg.getEdges().size() != 5) {outln("Test 9.4.22 Fail");}
		
		dg = new DualGraph(new AbstractDiagram("0 a ab ac"));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 2) {outln("Test 9.4.23 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("a ab"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.24 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.25 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.25a Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("a ac"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.26 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.27 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.27a Fail");}
				continue;
			}
			outln("Test 9.4.28 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.isomorphicTo(new AbstractDiagram("0 a ab ac"))) {outln("Test 9.4.30 Fail");}
		if(dg.getNodes().size() != 4) {outln("Test 9.4.31 Fail");}
		if(dg.getEdges().size() != 3) {outln("Test 9.4.32 Fail");}
		
		dg = new DualGraph(new AbstractDiagram("0 a ab ac abd"));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 2) {outln("Test 9.4.33 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("a ab abd"))) {
				if(dg1.getNodes().size() != 3) {outln("Test 9.4.34 Fail");}
				if(dg1.getEdges().size() != 2) {outln("Test 9.4.35 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.35a Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("a ac"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.38 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.39 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.39a Fail");}
				continue;
			}
			outln("Test 9.4.40 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.isomorphicTo(new AbstractDiagram("0 a ab ac abd")))  {outln("Test 9.4.41 Fail");}
		if(dg.getNodes().size() != 5) {outln("Test 9.4.42 Fail");}
		if(dg.getEdges().size() != 4) {outln("Test 9.4.43 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab ac abd abde abdf abdef acq acqr az"));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 3) {outln("Test 9.4.44 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("a az"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.45 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.46 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.46a Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("ab abd abde abdf abdef"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.47 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.48 Fail");}
				if(!dg1.getLabel().equals("ab")) {outln("Test 9.4.49a Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("a ac acq acqr"))) {
				if(dg1.getNodes().size() != 4) {outln("Test 9.4.49 Fail");}
				if(dg1.getEdges().size() != 3) {outln("Test 9.4.50 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.50a Fail");}
				continue;
			}
			outln("Test 9.4.51 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("0 a b ab ac az abd acq abde abdf acqr abdef")) {outln("Test 9.4.52 Fail");}
		if(dg.getNodes().size() != 12) {outln("Test 9.4.52 Fail");}
		if(dg.getEdges().size() != 13) {outln("Test 9.4.53 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a ab"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 1) {outln("Test 9.4.54 Fail");}
		dgRet = dgList.get(0);
		adRet = dgRet.findAbstractDiagram();
		if(!adRet.isomorphicTo(new AbstractDiagram("a ab"))) {outln("Test 9.4.55 Fail");}
		if(!dgRet.getLabel().equals("a")) {outln("Test 9.4.55a Fail");}
		if(dgRet.getNodes().size() != 2) {outln("Test 9.4.56 Fail");}
		if(dgRet.getEdges().size() != 1) {outln("Test 9.4.57 Fail");}
		ad = dg.findAbstractDiagram();
		if(!ad.isomorphicTo(new AbstractDiagram("0 a"))) {outln("Test 9.4.58 Fail");}
		if(dg.getNodes().size() != 2) {outln("Test 9.4.59 Fail");}
		if(dg.getEdges().size() != 1) {outln("Test 9.4.60 Fail");}
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab abc"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 1) {outln("Test 9.4.61 Fail");}
		dgRet = dgList.get(0);
		adRet = dgRet.findAbstractDiagram();
		if(!adRet.isomorphicTo(new AbstractDiagram("ab abc"))) {outln("Test 9.4.62 Fail");}
		if(!dgRet.getLabel().equals("ab")) {outln("Test 9.4.62a Fail");}
		if(dgRet.getNodes().size() != 2) {outln("Test 9.4.63 Fail");}
		if(dgRet.getEdges().size() != 1) {outln("Test 9.4.64 Fail");}
		ad = dg.findAbstractDiagram();
		if(!ad.isomorphicTo(new AbstractDiagram("0 a b ab"))) {outln("Test 9.4.65 Fail");}
		if(dg.getNodes().size() != 4) {outln("Test 9.4.66 Fail");}
		if(dg.getEdges().size() != 4) {outln("Test 9.4.67 Fail");}
		
		dg = new DualGraph(new AbstractDiagram("0 a ab ac"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 2) {outln("Test 9.4.68 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("a ab"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.69 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.70 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.70a Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("a ac"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.71 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.72 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.72a Fail");}
				continue;
			}
			outln("Test 9.4.73 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.isomorphicTo(new AbstractDiagram("0 a"))) {outln("Test 9.4.74 Fail");}
		if(dg.getNodes().size() != 2) {outln("Test 9.4.75 Fail");}
		if(dg.getEdges().size() != 1) {outln("Test 9.4.76 Fail");}
		
		dg = new DualGraph(new AbstractDiagram("0 a ab ac abd"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 2) {outln("Test 9.4.77 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("a ab abd"))) {
				if(dg1.getNodes().size() != 3) {outln("Test 9.4.78 Fail");}
				if(dg1.getEdges().size() != 2) {outln("Test 9.4.79 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.79b Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("a ac"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.80 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.81 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.81a Fail");}
				continue;
			}
			outln("Test 9.4.81b Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.isomorphicTo(new AbstractDiagram("0 a")))  {outln("Test 9.4.82 Fail");}
		if(dg.getNodes().size() != 2) {outln("Test 9.4.83 Fail");}
		if(dg.getEdges().size() != 1) {outln("Test 9.4.84 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab ac abd abde abdf abdef acq acqr az"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 3) {outln("Test 9.4.85 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("a az"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.86 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.87 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.87a Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("ab abd abde abdf abdef"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.88 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.89 Fail");}
				if(!dg1.getLabel().equals("ab")) {outln("Test 9.4.89a Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("a ac acq acqr"))) {
				if(dg1.getNodes().size() != 4) {outln("Test 9.4.90 Fail");}
				if(dg1.getEdges().size() != 3) {outln("Test 9.4.91 Fail");}
				if(!dg1.getLabel().equals("a")) {outln("Test 9.4.92 Fail");}
				continue;
			}
			outln("Test 9.4.93 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("0 a b ab")) {outln("Test 9.4.94 Fail");}
		if(dg.getNodes().size() != 4) {outln("Test 9.4.95 Fail");}
		if(dg.getEdges().size() != 4) {outln("Test 9.4.95a Fail");}
		

		dg = new DualGraph(new AbstractDiagram("0 a b"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 2) {outln("Test 9.4.96 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("0 a"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.97 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.98 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.99 Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("0 b"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.100 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.101 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.102 Fail");}
				continue;
			}
			outln("Test 9.4.102a Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("0")) {outln("Test 9.4.103 Fail");}
		if(dg.getNodes().size() != 1) {outln("Test 9.4.104 Fail");}
		if(dg.getEdges().size() != 0) {outln("Test 9.4.105 Fail");}


		dg = new DualGraph(new AbstractDiagram("ab abc"));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 1) {outln("Test 9.4.106 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("ab abc"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.107 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.108 Fail");}
				if(!dg1.getLabel().equals("ab")) {outln("Test 9.4.109 Fail");}
				continue;
			}
			outln("Test 9.4.110 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("ab abc")) {outln("Test 9.4.111 Fail");}
		if(dg.getNodes().size() != 2) {outln("Test 9.4.112 Fail");}
		if(dg.getEdges().size() != 1) {outln("Test 9.4.113 Fail");}
		
		dg = new DualGraph(new AbstractDiagram("ab abc"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 1) {outln("Test 9.4.114 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("ab abc"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.115 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.116 Fail");}
				if(!dg1.getLabel().equals("ab")) {outln("Test 9.4.117 Fail");}
				continue;
			}
			outln("Test 9.4.118 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("ab")) {outln("Test 9.4.119 Fail");}
		if(dg.getNodes().size() != 1) {outln("Test 9.4.120 Fail");}
		if(dg.getEdges().size() != 0) {outln("Test 9.4.121 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b"));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 2) {outln("Test 9.4.122 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("0 a"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.123 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.124 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.125 Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("0 b"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.126 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.127 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.128 Fail");}
				continue;
			}
			outln("Test 9.4.129 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("0 a b")) {outln("Test 9.4.130 Fail");}
		if(dg.getNodes().size() != 3) {outln("Test 9.4.131 Fail");}
		if(dg.getEdges().size() != 2) {outln("Test 9.4.132 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b c"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 3) {outln("Test 9.4.133 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("0 a"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.134 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.135 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.136 Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("0 b"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.137 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.138 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.139 Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("0 c"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.140 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.141 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.142 Fail");}
				continue;
			}
			outln("Test 9.4.143 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("0")) {outln("Test 9.4.144 Fail");}
		if(dg.getNodes().size() != 1) {outln("Test 9.4.145 Fail");}
		if(dg.getEdges().size() != 0) {outln("Test 9.4.146 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b c"));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 3) {outln("Test 9.4.147 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("0 a"))) {
				if(dg1.getNodes().size() != 2) {outln("Test 9.4.148 Fail");}
				if(dg1.getEdges().size() != 1) {outln("Test 9.4.149 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.150 Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("0 b"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.151 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.152 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.153 Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("0 c"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.154 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.155 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.156 Fail");}
				continue;
			}
			outln("Test 9.4.157 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("0 a b c")) {outln("Test 9.4.158 Fail");}
		if(dg.getNodes().size() != 4) {outln("Test 9.4.159 Fail");}
		if(dg.getEdges().size() != 3) {outln("Test 9.4.160 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a ab ac abc x xy"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 2) {outln("Test 9.4.161 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("0 a ab ac abc"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.162 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.163 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.164 Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("0 x xy"))) {
				if(dg1.getNodes().size() != 3) {outln("Test 9.4.165 Fail");}
				if(dg1.getEdges().size() != 2) {outln("Test 9.4.166 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.167 Fail");}
				continue;
			}
			outln("Test 9.4.168 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("0")) {outln("Test 9.4.169 Fail");}
		if(dg.getNodes().size() != 1) {outln("Test 9.4.170 Fail");}
		if(dg.getEdges().size() != 0) {outln("Test 9.4.171 Fail");}

		
		dg = new DualGraph(new AbstractDiagram("0 a ab ac abc x xy"));
		dgList = dg.findNestedSubdiagrams(false);
		if(dgList.size() != 2) {outln("Test 9.4.172 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("0 a ab ac abc"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.173 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.174 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.175 Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("0 x xy"))) {
				if(dg1.getNodes().size() != 3) {outln("Test 9.4.176 Fail");}
				if(dg1.getEdges().size() != 2) {outln("Test 9.4.177 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.178 Fail");}
				continue;
			}
			outln("Test 9.4.179 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("0 a x ab ac xy abc")) {outln("Test 9.4.180 Fail");}
		if(dg.getNodes().size() != 7) {outln("Test 9.4.181 Fail");}
		if(dg.getEdges().size() != 7) {outln("Test 9.4.182 Fail");}

		
		dg = new DualGraph(new AbstractDiagram("0 a b ab c cd ce cde"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 2) {outln("Test 9.4.183 Fail");}
		for(DualGraph dg1: dgList) {
			AbstractDiagram ad1 = dg1.findAbstractDiagram();
			if(ad1.isomorphicTo(new AbstractDiagram("0 a b ab"))) {
				if(dg1.getNodes().size() != 4) {outln("Test 9.4.184 Fail");}
				if(dg1.getEdges().size() != 4) {outln("Test 9.4.185 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.186 Fail");}
				continue;
			}
			if(ad1.isomorphicTo(new AbstractDiagram("0 c cd ce cde"))) {
				if(dg1.getNodes().size() != 5) {outln("Test 9.4.187 Fail");}
				if(dg1.getEdges().size() != 5) {outln("Test 9.4.188 Fail");}
				if(!dg1.getLabel().equals("")) {outln("Test 9.4.189 Fail");}
				continue;
			}
			outln("Test 9.4.190 Fail");
		}
		ad = dg.findAbstractDiagram();
		if(!ad.toString().equals("0")) {outln("Test 9.4.191 Fail");}
		if(dg.getNodes().size() != 1) {outln("Test 9.4.192 Fail");}
		if(dg.getEdges().size() != 0) {outln("Test 9.4.193 Fail");}
		
		// bug when subdiagrams are approached from multiple ends
		dg = new DualGraph(new AbstractDiagram("0 a b c d ab ac ad ae af ag ah ai acf acg ade adi afg afh agh acfg afgh"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 0) {outln("Test 9.4.194 Fail");}

		dg = new DualGraph(new AbstractDiagram("0 a d ad ae ai ade adi"));
		dgList = dg.findNestedSubdiagrams(true);
		if(dgList.size() != 0) {outln("Test 9.4.195 Fail");}
		
		out("Ending method test 9");
	}

	
	/**
	 * test AbstractDiagram function containingZones(String contour)
	 * */
	public static void test10(){
		outln(" | Starting method test 10");	
		DualGraph dg = new DualGraph (new AbstractDiagram("0 a b ab abc"));
		AbstractDiagram ad = dg.findAbstractDiagram();
		if(!ad.containingZones("a").equals("")) {outln("Test 10.1.1 Fail");}	
		if(!ad.containingZones("b").equals("")) {outln("Test 10.1.2 Fail");}
		if(!ad.containingZones("c").equals("ab")) {outln("Test 10.1.3 Fail");}		
		
		DualGraph dg1 = new DualGraph (new AbstractDiagram("0 a b ab abd abc abcd"));
		AbstractDiagram ad1 = dg1.findAbstractDiagram();
		if(!ad1.containingZones("a").equals("")) {outln("Test 10.2.1 Fail");}	
		if(!ad1.containingZones("b").equals("")) {outln("Test 10.2.2 Fail");}
		if(!ad1.containingZones("c").equals("ab")) {outln("Test 10.2.3 Fail");}		
		if(!ad1.containingZones("d").equals("ab")) {outln("Test 10.2.4 Fail");}
		
		DualGraph dg2 = new DualGraph (new AbstractDiagram("0 a b ab abd abc abcd abcde abcdef"));
		AbstractDiagram ad2 = dg2.findAbstractDiagram();
		if(!ad2.containingZones("a").equals("")) {outln("Test 10.3.1 Fail");}	
		if(!ad2.containingZones("b").equals("")) {outln("Test 10.3.2 Fail");}
		if(!ad2.containingZones("c").equals("ab")) {outln("Test 10.3.3 Fail");}		
		if(!ad2.containingZones("d").equals("ab")) {outln("Test 10.3.4 Fail");}				
		if(!ad2.containingZones("e").equals("abcd")) {outln("Test 10.3.5 Fail");}	
		if(!ad2.containingZones("f").equals("abcde")) {outln("Test 10.3.5 Fail");}		
		out("Ending method test 10");
	}
	

	
	/**
	 * test triangulation methods
	 * */
	public static void test11(){
		outln(" | Starting method test 11");	
		DualGraph dg, cloneGraph;
		TriangulationEdge te1,te2,te3;
		TriangulationFace tf1,tf2;
		Node n1,n2,n3;
		CutPoint cp;
		ArrayList<String> cList,cList1,cList2;
		ArrayList<CutPoint> cpList;
		ArrayList<TriangulationEdge> teList;
		WellFormedConcreteDiagram cd;
		String contours1, contours2;
		
		
		dg = new DualGraph();
		dg.formFaces();
		dg.triangulate();
		if(dg.getTriangulationFaces().size() != 0)  {outln("Test 11.1.1 Fail");}
		if(dg.findTriangulationEdges().size() != 0)  {outln("Test 11.1.2 Fail");}
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dg.firstNodeWithLabel("").setCentre(new Point(200,0));
		dg.firstNodeWithLabel("a").setCentre(new Point(100,200));
		dg.firstNodeWithLabel("b").setCentre(new Point(300,200));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		dg.formFaces();
		dg.triangulate();
		if(dg.getTriangulationFaces().size() != 2) {outln("Test 11.1.2 Fail");}
		if(dg.findTriangulationEdges().size() != 5) {outln("Test 11.1.3 Fail");}
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("b");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 != null) {outln("Test 11.1.4 Fail");}
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("ab");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 == null) {outln("Test 11.1.5 Fail");}
		if(!te1.getLabel().equals("ab")) {outln("Test 11.1.6 Fail");}
		if(te1.findContourList().size() != 2) {outln("Test 11.1.6a Fail");}
		if(!te1.findContourList().get(0).equals("a")) {outln("Test 11.1.6b Fail");}
		if(!te1.findContourList().get(1).equals("b")) {outln("Test 11.1.6c Fail");}
		if(te1.getEdge() != null) {outln("Test 11.1.7 Fail");}
		if(te1.getFaceList().size() != 1) {outln("Test 11.1.7a Fail");}
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("ab");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 == null) {outln("Test 11.1.8 Fail");}
		if(te1.getEdge() == null) {outln("Test 11.1.9 Fail");}
		if(!te1.getLabel().equals("b")) {outln("Test 11.1.10 Fail");}
		if(te1.findContourList().size() != 1) {outln("Test 11.1.10a Fail");}
		if(!te1.findContourList().get(0).equals("b")) {outln("Test 11.1.10b Fail");}
		if(te1.getFaceList().size() != 1) {outln("Test 11.1.10c Fail");}
		n1 = dg.firstNodeWithLabel("ab");
		n2 = dg.firstNodeWithLabel("b");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 == null) {outln("Test 11.1.11 Fail");}
		if(te1.getEdge() == null) {outln("Test 11.1.12 Fail");}
		if(!te1.getLabel().equals("a")) {outln("Test 11.1.13 Fail");}
		if(te1.findContourList().size() != 1) {outln("Test 11.1.13a Fail");}
		if(!te1.findContourList().get(0).equals("a")) {outln("Test 11.1.13b Fail");}
		if(te1.getFaceList().size() != 1) {outln("Test 11.1.13c Fail");}

		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("a");
		n3 = dg.firstNodeWithLabel("b");
		tf1 = dg.findTriangulationFace(n1,n2,n3);
		if(tf1 != null) {outln("Test 11.1.13c.1 Fail");}
		tf1 = dg.findTriangulationFace(n1,n3,n2);
		if(tf1 != null) {outln("Test 11.1.13c.1a Fail");}
		tf1 = dg.findTriangulationFace(n2,n1,n3);
		if(tf1 != null) {outln("Test 11.1.13c.1b Fail");}
		tf1 = dg.findTriangulationFace(n2,n3,n1);
		if(tf1 != null) {outln("Test 11.1.13c.1c Fail");}
		tf1 = dg.findTriangulationFace(n3,n1,n2);
		if(tf1 != null) {outln("Test 11.1.13c.1d Fail");}
		tf1 = dg.findTriangulationFace(n3,n2,n1);
		if(tf1 != null) {outln("Test 11.1.13c.1e Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("","a","b");
		if(tf1 != null) {outln("Test 11.1.13c.1f Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("","b","a");
		if(tf1 != null) {outln("Test 11.1.13c.1g Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("a","","b");
		if(tf1 != null) {outln("Test 11.1.13c.1h Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("a","b","");
		if(tf1 != null) {outln("Test 11.1.13c.1i Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("b","","a");
		if(tf1 != null) {outln("Test 11.1.13c.1j Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("b","a","");
		if(tf1 != null) {outln("Test 11.1.13c.1k Fail");}
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("a");
		n3 = dg.firstNodeWithLabel("ab");
		tf1 = dg.findTriangulationFace(n1,n2,n3);
		if(tf1 == null) {outln("Test 11.1.13c.2 Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3 Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4 Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5 Fail");}
		tf1 = dg.findTriangulationFace(n1,n3,n2);
		if(tf1 == null) {outln("Test 11.1.13c.2a Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3a Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4a Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5a Fail");}
		tf1 = dg.findTriangulationFace(n2,n1,n3);
		if(tf1 == null) {outln("Test 11.1.13c.2b Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3b Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4b Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5b Fail");}
		tf1 = dg.findTriangulationFace(n2,n3,n1);
		if(tf1 == null) {outln("Test 11.1.13c.2c Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3c Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4c Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5c Fail");}
		tf1 = dg.findTriangulationFace(n3,n1,n2);
		if(tf1 == null) {outln("Test 11.1.13c.2d Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3d Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4d Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5d Fail");}
		tf1 = dg.findTriangulationFace(n3,n2,n1);
		if(tf1 == null) {outln("Test 11.1.13c.2e Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3e Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4e Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5e Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("","ab","a");
		if(tf1 == null) {outln("Test 11.1.13c.2f Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3f Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4f Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5f Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("a","","ab");
		if(tf1 == null) {outln("Test 11.1.13c.2g Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3g Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4g Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5g Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("a","ab","");
		if(tf1 == null) {outln("Test 11.1.13c.2h Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3h Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4h Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5h Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("ab","","a");
		if(tf1 == null) {outln("Test 11.1.13c.2i Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3i Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4i Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5i Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("ab","a","");
		if(tf1 == null) {outln("Test 11.1.13c.2j Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.13c.3j Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.13c.4j Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.13c.5j Fail");}

		
		dg = new DualGraph(new AbstractDiagram("0 a b ab ac abc"));
		dg.firstNodeWithLabel("").setCentre(new Point(50,50));
		dg.firstNodeWithLabel("a").setCentre(new Point(300,50));
		dg.firstNodeWithLabel("b").setCentre(new Point(50,300));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,250));
		dg.firstNodeWithLabel("ac").setCentre(new Point(200,150));
		dg.firstNodeWithLabel("abc").setCentre(new Point(120,180));
		dg.formFaces();
		dg.triangulate();
		if(dg.getTriangulationFaces().size() != 6) {outln("Test 11.1.14 Fail");}
		if(dg.findTriangulationEdges().size() != 11) {outln("Test 11.1.15 Fail");}
		n1 = dg.firstNodeWithLabel("bc");
		n2 = dg.firstNodeWithLabel("ac");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 != null) {outln("Test 11.1.16 Fail");}
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("b");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 != null) {outln("Test 11.1.17 Fail");}
		n1 = dg.firstNodeWithLabel("ac");
		n2 = dg.firstNodeWithLabel("b");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 != null) {outln("Test 11.1.18 Fail");}
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("ab");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 != null) {outln("Test 11.1.19 Fail");}
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("abc");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 == null) {outln("Test 11.1.20 Fail");}
		if(te1.getEdge() != null) {outln("Test 11.1.21 Fail");}
		if(!te1.getLabel().equals("abc")) {outln("Test 11.1.22 Fail");}
		if(te1.findContourList().size() != 3) {outln("Test 11.1.22a Fail");}
		if(!te1.findContourList().get(0).equals("a")) {outln("Test 11.1.22b Fail");}
		if(!te1.findContourList().get(1).equals("b")) {outln("Test 11.1.22c Fail");}
		if(!te1.findContourList().get(2).equals("c")) {outln("Test 11.1.22d Fail");}
		if(te1.getFaceList().size() != 1) {outln("Test 11.1.22e Fail");}
		n1 = dg.firstNodeWithLabel("ab");
		n2 = dg.firstNodeWithLabel("b");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 == null) {outln("Test 11.1.23 Fail");}
		if(te1.getEdge() == null) {outln("Test 11.1.24 Fail");}
		if(!te1.getLabel().equals("a")) {outln("Test 11.1.25 Fail");}
		if(te1.findContourList().size() != 1) {outln("Test 11.1.25a Fail");}
		if(!te1.findContourList().get(0).equals("a")) {outln("Test 11.1.25b Fail");}
		if(te1.getFaceList().size() != 1) {outln("Test 11.1.25c Fail");}
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("b");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 == null) {outln("Test 11.1.26 Fail");}
		if(te1.getEdge() == null) {outln("Test 11.1.27 Fail");}
		if(!te1.getLabel().equals("b")) {outln("Test 11.1.28 Fail");}
		if(te1.getFaceList().size() != 1) {outln("Test 11.1.28a Fail");}
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("ac");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1 == null) {outln("Test 11.1.29 Fail");}
		if(te1.getEdge() == null) {outln("Test 11.1.30 Fail");}
		if(!te1.getLabel().equals("c")) {outln("Test 11.1.31 Fail");}
		if(te1.getFaceList().size() != 2) {outln("Test 11.1.31a Fail");}
		if(te1.findContourList().size() != 1) {outln("Test 11.1.31b Fail");}
		if(!te1.findContourList().get(0).equals("c")) {outln("Test 11.1.31c Fail");}

		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("b");
		n3 = dg.firstNodeWithLabel("ab");
		tf1 = dg.findTriangulationFace(n1,n2,n3); 
		if(tf1 != null) {outln("Test 11.1.32 Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("", "b", "ab"); 
		if(tf1 != null) {outln("Test 11.1.32a Fail");}
		n1 = dg.firstNodeWithLabel("b");
		n2 = dg.firstNodeWithLabel("ab");
		n3 = dg.firstNodeWithLabel("a");
		tf1 = dg.findTriangulationFace(n1,n2,n3); 
		if(tf1 != null) {outln("Test 11.1.33 Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("a", "b", "ab"); 
		if(tf1 != null) {outln("Test 11.1.33a Fail");}
		n1 = dg.firstNodeWithLabel("abc");
		n2 = dg.firstNodeWithLabel("ac");
		n3 = dg.firstNodeWithLabel("ab");
		tf1 = dg.findTriangulationFace(n1,n2,n3); 
		if(tf1 == null) {outln("Test 11.1.34 Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.34a Fail");}
		if(!tf1.findContourList().get(0).equals("b")) {outln("Test 11.1.34b Fail");}
		if(!tf1.findContourList().get(1).equals("c")) {outln("Test 11.1.34c Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("abc","ac","ab"); 
		if(tf1 == null) {outln("Test 11.1.34a Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.34aa Fail");}
		if(!tf1.findContourList().get(0).equals("b")) {outln("Test 11.1.34ba Fail");}
		if(!tf1.findContourList().get(1).equals("c")) {outln("Test 11.1.34ca Fail");}
		n1 = dg.firstNodeWithLabel("abc");
		n2 = dg.firstNodeWithLabel("ac");
		n3 = dg.firstNodeWithLabel("ab");
		tf1 = dg.findTriangulationFace(n1,n2,n3); 
		if(tf1 == null) {outln("Test 11.1.35 Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.35a Fail");}
		if(!tf1.findContourList().get(0).equals("b")) {outln("Test 11.1.35b Fail");}
		if(!tf1.findContourList().get(1).equals("c")) {outln("Test 11.1.35c Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("abc", "ab", "ac"); 
		if(tf1 == null) {outln("Test 11.1.35a Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.35aa Fail");}
		if(!tf1.findContourList().get(0).equals("b")) {outln("Test 11.1.35ba Fail");}
		if(!tf1.findContourList().get(1).equals("c")) {outln("Test 11.1.35ca Fail");}
		n1 = dg.firstNodeWithLabel("abc");
		n2 = dg.firstNodeWithLabel("");
		n3 = dg.firstNodeWithLabel("b");
		tf1 = dg.findTriangulationFace(n1,n2,n3); 
		if(tf1 == null) {outln("Test 11.1.36 Fail");}
		if(tf1.findContourList().size() != 3) {outln("Test 11.1.36a Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.36b Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.36c Fail");}
		if(!tf1.findContourList().get(2).equals("c")) {outln("Test 11.1.36c Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("abc", "", "b"); 
		if(tf1 == null) {outln("Test 11.1.36a Fail");}
		if(tf1.findContourList().size() != 3) {outln("Test 11.1.36aa Fail");}
		if(!tf1.findContourList().get(0).equals("a")) {outln("Test 11.1.36ba Fail");}
		if(!tf1.findContourList().get(1).equals("b")) {outln("Test 11.1.36ca Fail");}
		if(!tf1.findContourList().get(2).equals("c")) {outln("Test 11.1.36ca Fail");}
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("ac");
		n3 = dg.firstNodeWithLabel("ab");
		tf1 = dg.findTriangulationFace(n1,n2,n3); 
		if(tf1 == null) {outln("Test 11.1.37 Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.37a Fail");}
		if(!tf1.findContourList().get(0).equals("b")) {outln("Test 11.1.37b Fail");}
		if(!tf1.findContourList().get(1).equals("c")) {outln("Test 11.1.37c Fail");}
		tf1 = dg.firstTriangulationFaceWithNodeLabels("ac", "ab", "a"); 
		if(tf1 == null) {outln("Test 11.1.37a Fail");}
		if(tf1.findContourList().size() != 2) {outln("Test 11.1.37aa Fail");}
		if(!tf1.findContourList().get(0).equals("b")) {outln("Test 11.1.37ba Fail");}
		if(!tf1.findContourList().get(1).equals("c")) {outln("Test 11.1.37ca Fail");}
		
		
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dg.firstNodeWithLabel("").setCentre(new Point(200,0));
		dg.firstNodeWithLabel("a").setCentre(new Point(100,200));
		dg.firstNodeWithLabel("b").setCentre(new Point(300,200));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		dg.formFaces();
		dg.triangulate();
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("a");
		te1 = dg.findTriangulationEdge(n1,n2);
		cList = new ArrayList<String>();
		te1.assignCutPointsBetweenPoints(cList, new Point(0,0), new Point(100,200));
		cpList = te1.getCutPoints();
		if(cpList.size() != 0) {outln("Test 11.2.1 Fail");}
		
		cList = new ArrayList<String>();
		cList.add(new String("a"));
		te1.assignCutPointsBetweenPoints(cList, new Point(0,0), new Point(100,200));
		cpList = te1.getCutPoints();
		if(cpList.size() != 1) {outln("Test 11.2.2 Fail");}
		cp = cpList.get(0);
		if(!cp.getContourLinks().get(0).getContour().equals("a")) {outln("Test 11.2.3 Fail");}
		if(cp.getCoordinate().x != 50) {outln("Test 11.2.4 Fail");}
		if(cp.getCoordinate().y != 100) {outln("Test 11.2.5 Fail");}
		
		te1.assignCutPointsBetweenPoints(cList, new Point(300,300), new Point(100,200));
		cpList = te1.getCutPoints();
		if(cpList.size() != 1) {outln("Test 11.2.6 Fail");}
		cp = cpList.get(0);
		if(!cp.getContourLinks().get(0).getContour().equals("a")) {outln("Test 11.2.7 Fail");}
		if(cp.getCoordinate().x != 200) {outln("Test 11.2.8 Fail");}
		if(cp.getCoordinate().y != 250) {outln("Test 11.2.9 Fail");}
		
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("ab");
		te1 = dg.findTriangulationEdge(n1,n2);
		cList = new ArrayList<String>();
		cList.add(new String("a"));
		cList.add(new String("b"));
		te1.assignCutPointsBetweenPoints(cList, new Point(100,100), new Point(100,400));
		cpList = te1.getCutPoints();
		if(cpList.size() != 2) {outln("Test 11.2.10 Fail");}
		cp = cpList.get(0);
		if(!cp.getContourLinks().get(0).getContour().equals("a")) {outln("Test 11.2.11 Fail");}
		if(cp.getCoordinate().x != 100) {outln("Test 11.2.12 Fail");}
		if(cp.getCoordinate().y != 200) {outln("Test 11.2.13 Fail");}
		cp = cpList.get(1);
		if(!cp.getContourLinks().get(0).getContour().equals("b")) {outln("Test 11.2.14 Fail");}
		if(cp.getCoordinate().x != 100) {outln("Test 11.2.15 Fail");}
		if(cp.getCoordinate().y != 300) {outln("Test 11.2.16 Fail");}
		
		cList = new ArrayList<String>();
		cList.add(new String("a"));
		cList.add(new String("bc"));
		te1.assignCutPointsBetweenPoints(cList, new Point(400,100), new Point(100,100));
		cpList = te1.getCutPoints();
		if(cpList.size() != 2) {outln("Test 11.2.17 Fail");}
		cp = cpList.get(0);
		if(!cp.getContourLinks().get(0).getContour().equals("a")) {outln("Test 11.2.18 Fail");}
		if(cp.getCoordinate().x != 300) {outln("Test 11.2.19 Fail");}
		if(cp.getCoordinate().y != 100) {outln("Test 11.2.20 Fail");}
		cp = cpList.get(1);
		if(!cp.getContourLinks().get(0).getContour().equals("b")) {outln("Test 11.2.21 Fail");}
		if(cp.getCoordinate().x != 200) {outln("Test 11.2.22 Fail");}
		if(cp.getCoordinate().y != 100) {outln("Test 11.2.23 Fail");}
		
		cList = new ArrayList<String>();
		te1.assignCutPointsBetweenPoints(cList, te1.getFrom().getCentre(),te1.getTo().getCentre());
		cpList = te1.getCutPoints();
		if(cpList.size() != 0) {outln("Test 11.2.24 Fail");}

		cList = new ArrayList<String>();
		cList.add(new String("a"));
		cList.add(new String("b"));
		cList.add(new String("c"));
		cList.add(new String("d"));
		te1.assignCutPointsBetweenNodes(cList);
		cpList = te1.getCutPoints();
		if(cpList.size() != 4) {outln("Test 11.2.25 Fail");}
		
		cp = cpList.get(0);
		if(!cp.getContourLinks().get(0).getContour().equals("a")) {outln("Test 11.2.26 Fail");}
		if(cp.getCoordinate().x != 200) {outln("Test 11.2.27 Fail");}
		if(cp.getCoordinate().y != 20) {outln("Test 11.2.28 Fail");}
		cp = cpList.get(1);
		if(!cp.getContourLinks().get(0).getContour().equals("b")) {outln("Test 11.2.29 Fail");}
		if(cp.getCoordinate().x != 200) {outln("Test 11.2.30 Fail");}
		if(cp.getCoordinate().y != 40) {outln("Test 11.2.31 Fail");}
		cp = cpList.get(2);
		if(!cp.getContourLinks().get(0).getContour().equals("c")) {outln("Test 11.2.32 Fail");}
		if(cp.getCoordinate().x != 200) {outln("Test 11.2.33 Fail");}
		if(cp.getCoordinate().y != 60) {outln("Test 11.2.34 Fail");}
		cp = cpList.get(3);
		if(!cp.getContourLinks().get(0).getContour().equals("d")) {outln("Test 11.2.35 Fail");}
		if(cp.getCoordinate().x != 200) {outln("Test 11.2.36 Fail");}
		if(cp.getCoordinate().y != 80) {outln("Test 11.2.37 Fail");}

		
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dg.firstNodeWithLabel("").setCentre(new Point(200,0));
		dg.firstNodeWithLabel("a").setCentre(new Point(100,200));
		dg.firstNodeWithLabel("b").setCentre(new Point(300,200));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		dg.formFaces();
		dg.triangulate();
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("ab");
		n3 = dg.firstNodeWithLabel("b");
		te1 = dg.findTriangulationEdge(n1,n2);
		te2 = dg.findTriangulationEdge(n1,n3);
		te3 = dg.findTriangulationEdge(n3,n2);
		tf1 = dg.findTriangulationFace(n1,n2,n3);
		
		if(tf1.findOtherConnectingTE(n1,te1) != te2) {outln("Test 11.3.1 Fail");}
		if(tf1.findOtherConnectingTE(n1,te2) != te1) {outln("Test 11.3.2 Fail");}
		if(tf1.findOtherConnectingTE(n2,te1) != te3) {outln("Test 11.3.3 Fail");}
		if(tf1.findOtherConnectingTE(n2,te3) != te1) {outln("Test 11.3.4 Fail");}
		if(tf1.findOtherConnectingTE(n3,te2) != te3) {outln("Test 11.3.5 Fail");}
		if(tf1.findOtherConnectingTE(n3,te3) != te2) {outln("Test 11.3.6 Fail");}
		
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("");
		n3 = dg.firstNodeWithLabel("ab");
		te1 = dg.findTriangulationEdge(n1,n2);
		te2 = dg.findTriangulationEdge(n1,n3);
		te3 = dg.findTriangulationEdge(n3,n2);
		tf1 = dg.findTriangulationFace(n1,n2,n3);
		if(tf1.findOtherConnectingTE(n1,te1) != te2) {outln("Test 11.3.7 Fail");}
		if(tf1.findOtherConnectingTE(n1,te2) != te1) {outln("Test 11.3.8 Fail");}
		if(tf1.findOtherConnectingTE(n2,te1) != te3) {outln("Test 11.3.9 Fail");}
		if(tf1.findOtherConnectingTE(n2,te3) != te1) {outln("Test 11.3.10 Fail");}
		if(tf1.findOtherConnectingTE(n3,te2) != te3) {outln("Test 11.3.11 Fail");}
		if(tf1.findOtherConnectingTE(n3,te3) != te2) {outln("Test 11.3.12 Fail");}

		if(tf1.findOtherConnectingTE(n3,te1) != null) {outln("Test 11.3.13 Fail");}
		if(tf1.findOtherConnectingTE(n2,te2) != null) {outln("Test 11.3.14 Fail");}
		if(tf1.findOtherConnectingTE(n1,te3) != null) {outln("Test 11.3.15 Fail");}
		
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("b");
		n3 = dg.firstNodeWithLabel("ab");
		tf1 = dg.findTriangulationFace(n1,n2,n3);
		if(tf1.findOtherConnectingTE(n2,te1) != null) {outln("Test 11.3.16 Fail");}
		if(tf1.findOtherConnectingTE(n2,te2) != null) {outln("Test 11.3.17 Fail");}
		if(tf1.findOtherConnectingTE(n2,te3) != null) {outln("Test 11.3.18 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dg.firstNodeWithLabel("").setCentre(new Point(200,0));
		dg.firstNodeWithLabel("a").setCentre(new Point(100,200));
		dg.firstNodeWithLabel("b").setCentre(new Point(300,200));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		dg.formFaces();
		dg.triangulate();
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("ab");
		te1 = new TriangulationEdge(n1,n2, new Edge(n1,n2), null);
		te1.sortCutPoints();
		te1 = new TriangulationEdge(n1,n2, new Edge(n1,n2), null);
		te1.addContourPoint("a",new Point(200,190));
		te1.addContourPoint("b",new Point(200,170));
		if(te1.getCutPoints().get(0).getCoordinate().y != 170) {outln("Test 11.4.1 Fail");}
		if(!te1.getCutPoints().get(0).getContourLinks().get(0).getContour().equals("b")) {outln("Test 11.4.2 Fail");}
		if(te1.getCutPoints().get(1).getCoordinate().y != 190) {outln("Test 11.4.3 Fail");}
		if(!te1.getCutPoints().get(1).getContourLinks().get(0).getContour().equals("a")) {outln("Test 11.4.4 Fail");}

		te1 = new TriangulationEdge(n1,n2, new Edge(n1,n2), null);
		te1.addContourPoint("a",new Point(200,190));
		te1.addContourPoint("b",new Point(200,170));
		te1.addContourPoint("c",new Point(200,180));
		if(te1.getCutPoints().get(0).getCoordinate().y != 170) {outln("Test 11.4.5 Fail");}
		if(!te1.getCutPoints().get(0).getContourLinks().get(0).getContour().equals("b")) {outln("Test 11.4.6 Fail");}
		if(te1.getCutPoints().get(1).getCoordinate().y != 180) {outln("Test 11.4.7 Fail");}
		if(!te1.getCutPoints().get(1).getContourLinks().get(0).getContour().equals("c")) {outln("Test 11.4.8 Fail");}
		if(te1.getCutPoints().get(2).getCoordinate().y != 190) {outln("Test 11.4.9 Fail");}
		if(!te1.getCutPoints().get(2).getContourLinks().get(0).getContour().equals("a")) {outln("Test 11.4.10 Fail");}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b c ab bc"));
		dg.firstNodeWithLabel("").setCentre(new Point(300,0));
		dg.firstNodeWithLabel("a").setCentre(new Point(100,100));
		dg.firstNodeWithLabel("b").setCentre(new Point(300,200));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		dg.firstNodeWithLabel("c").setCentre(new Point(500,100));
		dg.firstNodeWithLabel("bc").setCentre(new Point(400,100));
		
		dg.formFaces();
		dg.triangulate();
		int edgeCount = dg.getEdges().size();
		int nodeCount = dg.getNodes().size();
		WellFormedConcreteDiagram.addBoundingNodes(dg,50,dg.getOuterFace().getNodeList());
		if(dg.getEdges().size() != edgeCount+WellFormedConcreteDiagram.getBoundingEdges(dg).size()) {outln("Test 11.5.0a Fail");}
		if(dg.getNodes().size() != nodeCount+WellFormedConcreteDiagram.getBoundingNodes(dg).size()) {outln("Test 11.5.0b Fail");}
		
		dg = new DualGraph(new AbstractDiagram("0 a b c ab bc"));
		dg.firstNodeWithLabel("").setCentre(new Point(300,0));
		dg.firstNodeWithLabel("a").setCentre(new Point(100,100));
		dg.firstNodeWithLabel("b").setCentre(new Point(300,200));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		dg.firstNodeWithLabel("c").setCentre(new Point(500,100));
		dg.firstNodeWithLabel("bc").setCentre(new Point(400,100));
		
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(false);
		cd.generateContours();
		
		cloneGraph = cd.getCloneGraph();

		// Check that the triangulation works
		n1 = cloneGraph.firstNodeWithLabel("");
		n2 = cloneGraph.firstNodeWithLabel("ab");
		n3 = cloneGraph.firstNodeWithLabel("b");
		te1 = cloneGraph.findTriangulationEdge(n1,n2);
		te2 = cloneGraph.findTriangulationEdge(n1,n3);
		te3 = cloneGraph.findTriangulationEdge(n3,n2);
		tf1 = cloneGraph.findTriangulationFace(n1,n2,n3);

		// check that the ContourPoints are present
		teList = tf1.findTEsWithContour("c");
		if(teList.size() != 0) {outln("Test 11.5.1 Fail");}
		
		teList = tf1.findTEsWithContour("a");
		if(teList.size() != 2) {outln("Test 11.5.2 Fail");}
		if(!teList.contains(te1)) {outln("Test 11.5.4 Fail");}
		if(!teList.contains(te3)) {outln("Test 11.5.5 Fail");}
		teList = tf1.findTEsWithContour("b");

		if(teList.size() != 2) {outln("Test 11.5.6 Fail");}
		if(!teList.contains(te1)) {outln("Test 11.5.7 Fail");}
		if(!teList.contains(te2)) {outln("Test 11.5.8 Fail");}
		
		// check the ContourPoint cycles
		for(String contour : cloneGraph.findAbstractDiagram().getContours()) {
			TriangulationEdge te = cloneGraph.firstTriangulationEdgeWithContour(contour);
			int count = 0;
			ContourLink startCL = te.contourLinksWithContour(contour).get(0);
			ContourLink currentCL = null;
			while(startCL != currentCL) {
				if(currentCL == null) {
					currentCL = startCL;
				}
				currentCL = currentCL.getNext();
				count++;
			}

			if(contour.equals("a") && count != 11) {outln("Test 11.5.9 Fail");}
			if(contour.equals("b") && count != 11) {outln("Test 11.5.10 Fail");}
			if(contour.equals("c") && count != 9) {outln("Test 11.5.11 Fail");}
		}
		
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dg.firstNodeWithLabel("").setCentre(new Point(200,0));
		dg.firstNodeWithLabel("a").setCentre(new Point(100,200));
		dg.firstNodeWithLabel("b").setCentre(new Point(300,200));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(true);
		cd.generateContours();
		cloneGraph = cd.getCloneGraph();
		tf1 = cloneGraph.firstTriangulationFaceWithNodeLabels("","a","ab");
		cList1 = tf1.findCycleContours();
		contours1 = "";
		for(String c1 : cList1) {contours1 += c1;}
		tf2 = cloneGraph.firstTriangulationFaceWithNodeLabels("","b","ab");
		cList2 = tf2.findCycleContours();
		contours2 = "";
		for(String c2 : cList2) {contours2 += c2;}
		if(contours1.equals("abab") || contours1.equals("baba")) {
			if(!contours2.equals("baab") && !contours2.equals("abba")) {
				outln("Test 11.6.1 Fail");
			}
		} else {
			if(!contours1.equals("baab") && !contours1.equals("abba")) {
				outln("Test 11.6.2 Fail");
			}
			if(!contours2.equals("abab") && !contours2.equals("baba")) {
				outln("Test 11.6.3 Fail");
			}
		}

		

		out("Ending method test 11");
	}
	
	
	
	/**
	 * test final embedding of wf diagrams against abstract description
	 * */
	public static void test12(){
		out(" | Starting method test 12");	

		AbstractDiagram ad;
		AbstractDiagram adRet;
		ArrayList<String> zoneList;
		DualGraph dg;
		WellFormedConcreteDiagram cd;
		ArrayList<ConcreteContour> ccs;
		ArrayList<Polygon> polygons;
		ArrayList<TriangulationFace> tfList;
		ConcreteContour c1,c2,c3,c4,c5;
		String zones;
		DualGraph cloneGraph;
		TriangulationEdge te1;
		TriangulationFace tf1;
		Polygon p1,p2,p3,p4,p5;
		Point2D point;
		ArrayList<CutPoint> cpList;
		CutPoint cp;


		p1 = new Polygon();
		p1.addPoint(0,0);
		p1.addPoint(0,100);
		p1.addPoint(100,0);
		p2 = new Polygon();
		p2.addPoint(10,10);
		p2.addPoint(10,80);
		p2.addPoint(80,10);
		if(!ConcreteContour.polygonContainment(p1,p2)) {outln("Test 12.0.1 Fail");}
		if(ConcreteContour.polygonContainment(p2,p1)) {outln("Test 12.0.2 Fail");}
		if(!ConcreteContour.polygonContainment(p1,p1)) {outln("Test 12.0.3 Fail");}
		if(!ConcreteContour.polygonContainment(p2,p2)) {outln("Test 12.0.4 Fail");}

		p1 = new Polygon();
		p1.addPoint(0,0);
		p1.addPoint(0,100);
		p1.addPoint(100,0);
		p2 = new Polygon();
		p2.addPoint(10,10);
		p2.addPoint(10,150);
		p2.addPoint(150,10);
		if(ConcreteContour.polygonContainment(p1,p2)) {outln("Test 12.0.5 Fail");}
		if(ConcreteContour.polygonContainment(p2,p1)) {outln("Test 12.0.6 Fail");}
		if(!ConcreteContour.polygonContainment(p1,p1)) {outln("Test 12.0.7 Fail");}
		if(!ConcreteContour.polygonContainment(p2,p2)) {outln("Test 12.0.8 Fail");}

		p1 = new Polygon();
		p1.addPoint(0,0);
		p1.addPoint(0,100);
		p1.addPoint(100,0);
		p2 = new Polygon();
		p2.addPoint(200,200);
		p2.addPoint(200,250);
		p2.addPoint(250,200);
		if(ConcreteContour.polygonContainment(p1,p2)) {outln("Test 12.0.9 Fail");}
		if(ConcreteContour.polygonContainment(p2,p1)) {outln("Test 12.0.10 Fail");}
		if(!ConcreteContour.polygonContainment(p1,p1)) {outln("Test 12.0.11 Fail");}
		if(!ConcreteContour.polygonContainment(p2,p2)) {outln("Test 12.0.12 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(0,0);
		p1.addPoint(0,100);
		p1.addPoint(100,0);
		p2 = new Polygon();
		p2.addPoint(200,200);
		p2.addPoint(200,250);
		p2.addPoint(250,200);
		if(ConcreteContour.polygonContainment(p1,p2)) {outln("Test 12.0.9 Fail");}
		if(ConcreteContour.polygonContainment(p2,p1)) {outln("Test 12.0.10 Fail");}
		if(!ConcreteContour.polygonContainment(p1,p1)) {outln("Test 12.0.11 Fail");}
		if(!ConcreteContour.polygonContainment(p2,p2)) {outln("Test 12.0.12 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(0,0);
		p1.addPoint(100,100);
		p1.addPoint(200,0);
		p1.addPoint(100,200);
		p2 = new Polygon();
		p2.addPoint(80,50);
		p2.addPoint(80,150);
		p2.addPoint(120,150);
		p2.addPoint(120,50);
		if(ConcreteContour.polygonContainment(p1,p2)) {outln("Test 12.0.13 Fail");}
		if(ConcreteContour.polygonContainment(p2,p1)) {outln("Test 12.0.14 Fail");}
		if(!ConcreteContour.polygonContainment(p1,p1)) {outln("Test 12.0.15 Fail");}
		if(!ConcreteContour.polygonContainment(p2,p2)) {outln("Test 12.0.16 Fail");}
		
		
		polygons = new ArrayList<Polygon>();
		if(ConcreteContour.findOuterPolygon(polygons) != null) {outln("Test 12.0a.1 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(50,50);
		p1.addPoint(50,100);
		p1.addPoint(100,50);
		polygons = new ArrayList<Polygon>();
		polygons.add(p1);
		if(ConcreteContour.findOuterPolygon(polygons) != p1) {outln("Test 12.0a.2 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(50,50);
		p1.addPoint(50,100);
		p1.addPoint(100,50);
		p2 = new Polygon();
		p2.addPoint(0,0);
		p2.addPoint(0,200);
		p2.addPoint(200,0);
		polygons = new ArrayList<Polygon>();
		polygons.add(p1);
		polygons.add(p2);
		if(ConcreteContour.findOuterPolygon(polygons) != p2) {outln("Test 12.0a.3 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(50,50);
		p1.addPoint(50,100);
		p1.addPoint(100,50);
		p2 = new Polygon();
		p2.addPoint(0,0);
		p2.addPoint(0,200);
		p2.addPoint(200,0);
		p3 = new Polygon();
		p3.addPoint(40,40);
		p3.addPoint(4,90);
		p3.addPoint(90,40);
		polygons = new ArrayList<Polygon>();
		polygons.add(p1);
		polygons.add(p2);
		polygons.add(p3);
		if(ConcreteContour.findOuterPolygon(polygons) != p2) {outln("Test 12.0a.4 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(50,50);
		p1.addPoint(50,250);
		p1.addPoint(250,50);
		p2 = new Polygon();
		p2.addPoint(0,0);
		p2.addPoint(0,200);
		p2.addPoint(200,0);
		polygons = new ArrayList<Polygon>();
		polygons.add(p1);
		polygons.add(p2);
		polygons.add(p3);
		if(ConcreteContour.findOuterPolygon(polygons) != null) {outln("Test 12.0a.5 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(50,50);
		p1.addPoint(50,100);
		p1.addPoint(100,50);
		p2 = new Polygon();
		p2.addPoint(0,0);
		p2.addPoint(0,100);
		p2.addPoint(100,0);
		polygons = new ArrayList<Polygon>();
		polygons.add(p2);
		polygons.add(p1);
		if(ConcreteContour.findOuterPolygon(polygons) != null) {outln("Test 12.0a.6 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(0,0);
		p1.addPoint(0,100);
		p1.addPoint(100,0);
		p2 = new Polygon();
		p2.addPoint(0,0);
		p2.addPoint(0,100);
		p2.addPoint(100,0);
		polygons = new ArrayList<Polygon>();
		polygons.add(p2);
		polygons.add(p1);
		if(ConcreteContour.findOuterPolygon(polygons) != null) {outln("Test 12.0a.7 Fail");}

		
		p1 = new Polygon();
		p1.addPoint(120,140);
		p1.addPoint(120,187);
		p1.addPoint(157,140);
		point = ConcreteContour.findPointInsidePolygon(p1);
		if(point == null) {outln("Test 12.0b.1 Fail");}
		if(!p1.contains(point)) {outln("Test 12.0b.2 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(100,100);
		p1.addPoint(120,100);
		p1.addPoint(120,120);
		p1.addPoint(110,130);
		p1.addPoint(100,120);
		point = ConcreteContour.findPointInsidePolygon(p1);
		if(point == null) {outln("Test 12.0b.3 Fail");}
		if(!p1.contains(point)) {outln("Test 12.0b.4 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(120,140);
		p1.addPoint(120,187);
		p1.addPoint(120,187);
		p1.addPoint(157,140);
		p1.addPoint(157,140);
		point = ConcreteContour.findPointInsidePolygon(p1);
		if(point == null) {outln("Test 12.0b.5 Fail");}
		if(!p1.contains(point)) {outln("Test 12.0b.6 Fail");}

		p1 = new Polygon();
		p1.addPoint(205,183);
		p1.addPoint(183,205);
		p1.addPoint(183,205);
		p1.addPoint(206,183);
		p1.addPoint(206,183);
		p1.addPoint(206,183);
		p1.addPoint(205,183);
		p1.addPoint(205,183);
		p1.addPoint(205,183);
		point = ConcreteContour.findPointInsidePolygon(p1);
		if(point == null) {outln("Test 12.0b.7 Fail");}
		if(!p1.contains(point)) {outln("Test 12.0b.8 Fail");}

		p1 = new Polygon();
		p1.addPoint(176,212);
		p1.addPoint(164,223);
		p1.addPoint(164,223);
		p1.addPoint(175,213);
		p1.addPoint(176,212);
		p1.addPoint(176,212);
		point = ConcreteContour.findPointInsidePolygon(p1);
		if(point == null) {outln("Test 12.0b.9 Fail");}
		if(!p1.contains(point)) {outln("Test 12.0b.10 Fail");}
		
		
	
		ccs = new ArrayList<ConcreteContour>();
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0")) {outln("Test 12.1.1 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 0) {outln("Test 12.1.2 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(0,0);
		p1.addPoint(0,100);
		p1.addPoint(100,0);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 a")) {outln("Test 12.1.3 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 0) {outln("Test 12.1.4 Fail");}

		p2 = new Polygon();
		p2.addPoint(200,200);
		p2.addPoint(200,300);
		p2.addPoint(300,200);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		ccs.add(c2);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 a b")) {outln("Test 12.1.5 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 0) {outln("Test 12.1.6 Fail");}
		
		p3 = new Polygon();
		p3.addPoint(20,20);
		p3.addPoint(230,230);
		p3.addPoint(100,200);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		ccs.add(c2);
		ccs.add(c3);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 a b c ac bc")) {outln("Test 12.1.7 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 0) {outln("Test 12.1.8 Fail");}

		p4 = new Polygon();
		p4.addPoint(90,30);
		p4.addPoint(200,80);
		p4.addPoint(140,160);
		p4.addPoint(110,80);
		p4.addPoint(100,120);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		c4 = new ConcreteContour("d",p4);
		ccs.add(c4);
		ccs.add(c2);
		ccs.add(c3);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 a b c d ac bc cd")) {outln("Test 12.1.9 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 2) {outln("Test 12.1.10 Fail");}
		if(!zoneList.get(0).equals("")) {outln("Test 12.1.11 Fail");}
		if(!zoneList.get(1).equals("cd")) {outln("Test 12.1.11a Fail");}


		p1 = new Polygon();
		p1.addPoint(100,100);
		p1.addPoint(100,120);
		p1.addPoint(200,120);
		p1.addPoint(200,100);
		p2 = new Polygon();
		p2.addPoint(150,50);
		p2.addPoint(160,50);
		p2.addPoint(160,200);
		p2.addPoint(150,200);
		p3 = new Polygon();
		p3.addPoint(105,105);
		p3.addPoint(105,115);
		p3.addPoint(195,115);
		p3.addPoint(195,105);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		ccs.add(c3);
		ccs.add(c2);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 a b ab ac abc")) {outln("Test 12.1.12 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);

		if(zoneList.size() != 4) {outln("Test 12.1.13 Fail");}
		if(!zoneList.get(0).equals("a")) {outln("Test 12.1.14 Fail");}
		if(!zoneList.get(1).equals("b")) {outln("Test 12.1.15 Fail");}
		if(!zoneList.get(2).equals("ab")) {outln("Test 12.1.16 Fail");}
		if(!zoneList.get(3).equals("ac")) {outln("Test 12.1.17 Fail");}

		p1 = new Polygon();
		p1.addPoint(100,100);
		p1.addPoint(100,120);
		p1.addPoint(200,120);
		p1.addPoint(200,100);
		p2 = new Polygon();
		p2.addPoint(100,110);
		p2.addPoint(120,110);
		p2.addPoint(120,200);
		p2.addPoint(100,200);
		p3 = new Polygon();
		p3.addPoint(180,110);
		p3.addPoint(220,110);
		p3.addPoint(150,200);
		p3.addPoint(110,200);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		ccs.add(c3);
		ccs.add(c2);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 a b c ab ac bc")) {outln("Test 12.1.18 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 1) {outln("Test 12.1.19 Fail");}
		if(!zoneList.get(0).equals("")) {outln("Test 12.1.20 Fail");}

		
		
		p1 = new Polygon();
		p1.addPoint(90,100);
		p1.addPoint(90,140);
		p1.addPoint(200,140);
		p1.addPoint(200,100);
		p2 = new Polygon();
		p2.addPoint(100,110);
		p2.addPoint(120,110);
		p2.addPoint(120,200);
		p2.addPoint(100,200);
		p3 = new Polygon();
		p3.addPoint(180,110);
		p3.addPoint(220,110);
		p3.addPoint(200,200);
		p3.addPoint(110,200);
		p4 = new Polygon();
		p4.addPoint(110,50);
		p4.addPoint(170,50);
		p4.addPoint(170,195);
		p4.addPoint(110,195);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		c4 = new ConcreteContour("d",p4);
		ccs.add(c3);
		ccs.add(c2);
		ccs.add(c4);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 a b c d ab ac ad bc bd cd abd acd bcd")) {outln("Test 12.1.21 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 2) {outln("Test 12.1.22 Fail");}
		if(!zoneList.get(0).equals("a")) {outln("Test 12.1.23 Fail");}
		if(!zoneList.get(1).equals("d")) {outln("Test 12.1.23a Fail");}
		
		
/*
HashMap<String,Area> areaMap = ConcreteContour.generateZoneAreas(ccs);
DiagramPanel.areas = new ArrayList<Area>();
DiagramPanel.polygons = new ArrayList<Polygon>();
DiagramPanel.polygons.add(p1);
DiagramPanel.polygons.add(p2);
DiagramPanel.polygons.add(p3);
DiagramPanel.polygons.add(p4);

Area area = areaMap.get("d");
DiagramPanel.areas.add(area);

DualGraph dg1 = new DualGraph();
euler.display.DualGraphWindow dgw = new euler.display.DualGraphWindow(dg1);
dgw.update(dgw.getGraphics());

PathIterator pi = area.getPathIterator(null);
double[] coords = new double[6];
while(!pi.isDone()) {
	int move = pi.currentSegment(coords);
	if(move == PathIterator.SEG_CLOSE) {out("SEG_CLOSE");};
	if(move == PathIterator.SEG_CUBICTO) {out("SEG_CUBICTO");};
	if(move == PathIterator.SEG_LINETO) {out("SEG_LINETO");};
	if(move == PathIterator.SEG_QUADTO) {out("SEG_QUADTO");};
	if(move == PathIterator.SEG_MOVETO) {out("SEG_MOVETO");};
	outln(" "+coords[0]+","+coords[1]);
	pi.next();
}
*/

		p1 = new Polygon();
		p1.addPoint(200,200);
		p1.addPoint(200,300);
		p1.addPoint(300,200);
		p2 = new Polygon();
		p2.addPoint(200,200);
		p2.addPoint(200,300);
		p2.addPoint(300,200);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		ccs.add(c2);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 ab")) {outln("Test 12.1.24 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 0) {outln("Test 12.1.25 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(100,100);
		p1.addPoint(100,300);
		p1.addPoint(300,300);
		p1.addPoint(300,100);
		p2 = new Polygon();
		p2.addPoint(110,110);
		p2.addPoint(110,130);
		p2.addPoint(130,130);
		p2.addPoint(130,110);
		p3 = new Polygon();
		p3.addPoint(200,200);
		p3.addPoint(200,240);
		p3.addPoint(240,240);
		p3.addPoint(240,200);
		p4 = new Polygon();
		p4.addPoint(210,210);
		p4.addPoint(210,230);
		p4.addPoint(230,230);
		p4.addPoint(230,210);
		p5 = new Polygon();
		p5.addPoint(150,150);
		p5.addPoint(150,170);
		p5.addPoint(170,170);
		p5.addPoint(170,150);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		c4 = new ConcreteContour("d",p4);
		c5 = new ConcreteContour("e",p5);
		ccs.add(c1);
		ccs.add(c2);
		ccs.add(c3);
		ccs.add(c4);
		ccs.add(c5);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 a ab ac ae acd")) {outln("Test 12.1.26 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 0) {outln("Test 12.1.27 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(0,0);
		p1.addPoint(100,0);
		p1.addPoint(100,100);
		p1.addPoint(0,100);
		p2 = new Polygon();
		p2.addPoint(0,0);
		p2.addPoint(50,0);
		p2.addPoint(50,100);
		p2.addPoint(0,100);
		p3 = new Polygon();
		p3.addPoint(50,0);
		p3.addPoint(100,0);
		p3.addPoint(100,100);
		p3.addPoint(50,100);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		ccs.add(c1);
		ccs.add(c2);
		ccs.add(c3);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 ab ac")) {outln("Test 12.1.28 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 0) {outln("Test 12.1.29 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(0,0);
		p1.addPoint(100,0);
		p1.addPoint(100,20);
		p1.addPoint(0,20);
		p2 = new Polygon();
		p2.addPoint(80,0);
		p2.addPoint(100,0);
		p2.addPoint(100,100);
		p2.addPoint(80,100);
		p3 = new Polygon();
		p3.addPoint(0,80);
		p3.addPoint(100,80);
		p3.addPoint(100,100);
		p3.addPoint(0,100);
		p4 = new Polygon();
		p4.addPoint(0,0);
		p4.addPoint(20,0);
		p4.addPoint(20,100);
		p4.addPoint(0,100);
		p5 = new Polygon();
		p5.addPoint(20,20);
		p5.addPoint(20,80);
		p5.addPoint(80,80);
		p5.addPoint(80,20);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		c4 = new ConcreteContour("d",p4);
		c5 = new ConcreteContour("e",p5);
		ccs.add(c1);
		ccs.add(c2);
		ccs.add(c3);
		ccs.add(c4);
		ccs.add(c5);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 a b c d e ab ad bc cd")) {outln("Test 12.1.30 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 0) {outln("Test 12.1.31 Fail");}

		
		p1 = new Polygon();
		p1.addPoint(10,0);
		p1.addPoint(100,0);
		p1.addPoint(100,100);
		p1.addPoint(10,100);
		p2 = new Polygon();
		p2.addPoint(10,0);
		p2.addPoint(50,0);
		p2.addPoint(50,100);
		p2.addPoint(10,100);
		p3 = new Polygon();
		p3.addPoint(50,0);
		p3.addPoint(100,0);
		p3.addPoint(100,100);
		p3.addPoint(50,100);
		p4 = new Polygon();
		p4.addPoint(0,20);
		p4.addPoint(110,20);
		p4.addPoint(110,40);
		p4.addPoint(0,40);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		c4 = new ConcreteContour("d",p4);
		ccs.add(c1);
		ccs.add(c2);
		ccs.add(c3);
		ccs.add(c4);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 d ab ac abd acd")) {outln("Test 12.1.32 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 3) {outln("Test 12.1.33 Fail");}
		if(!zoneList.get(0).equals("d")) {outln("Test 12.1.34 Fail");}
		if(!zoneList.get(1).equals("ab")) {outln("Test 12.1.35 Fail");}
		if(!zoneList.get(2).equals("ac")) {outln("Test 12.1.36 Fail");}

		p1 = new Polygon();
		p1.addPoint(344,164);
		p1.addPoint(314,252);
		p1.addPoint(359,256);
		p1.addPoint(211,476);
		p1.addPoint(234,502);
		p1.addPoint(402,504);
		p1.addPoint(517,344);
		p1.addPoint(517,78);
		p1.addPoint(320,103);
		p1.addPoint(429,150);
		p2 = new Polygon();
		p2.addPoint(366,148);
		p2.addPoint(225,101);
		p2.addPoint(167,125);
		p2.addPoint(147,363);
		p2.addPoint(230,254);
		p2.addPoint(241,323);
		p2.addPoint(440,166);
		p3 = new Polygon();
		p3.addPoint(120,261);
		p3.addPoint(67,236);
		p3.addPoint(67,501);
		p3.addPoint(138,518);
		p3.addPoint(472,520);
		p3.addPoint(411,319);
		p3.addPoint(226,360);
		p3.addPoint(168,394);
		p3.addPoint(161,360);
		p3.addPoint(251,203);
		p3.addPoint(184,193);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		ccs.add(c1);
		ccs.add(c2);
		ccs.add(c3);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		if(!zones.equals("0 a b c ab ac bc")) {outln("Test 12.1.37 Fail");}
		zoneList = ConcreteContour.findDuplicateZones(ccs);
		if(zoneList.size() != 1) {outln("Test 12.1.38 Fail");}
		if(!zoneList.get(0).equals("")) {outln("Test 12.1.39 Fail");}



		// test the triangulation crossings by fixing a couple of TEs in a TF
		ad = new AbstractDiagram("0 a b ab");
		dg = new DualGraph(ad);
		dg.firstNodeWithLabel("").setCentre(new Point(200,0));
		dg.firstNodeWithLabel("a").setCentre(new Point(0,200));
		dg.firstNodeWithLabel("b").setCentre(new Point(400,200));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(false);
		cd.generateContours();
		cloneGraph =cd.getCloneGraph();
		tf1 = cloneGraph.firstTriangulationFaceWithNodeLabels("ab", "a", "");
		if(!tf1.correctContourCrossings()) {outln("Test 12.2.1 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.2.2 Fail");}
		te1 = tf1.getTE1();
		cpList = te1.getCutPoints();
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("a",cp,null,null);
		cpList.add(cp);
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("u",cp,null,null);
		cpList.add(cp);
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("v",cp,null,null);
		cpList.add(cp);
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("x",cp,null,null);
		cpList.add(cp);
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("y",cp,null,null);
		cpList.add(cp);
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("z",cp,null,null);
		cpList.add(cp);
		te1 = tf1.getTE2();
		cpList = te1.getCutPoints();
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("x",cp,null,null);
		cpList.add(cp);
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("v",cp,null,null);
		cpList.add(cp);
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("u",cp,null,null);
		cpList.add(cp);
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("z",cp,null,null);
		cpList.add(cp);
		cp = new CutPoint(te1,new ArrayList<ContourLink>(),new Point(0,0));
		new ContourLink("y",cp,null,null);
		cpList.add(cp);
		if(tf1.correctContourCrossings()) {outln("Test 12.2.3 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() == 0) {outln("Test 12.2.4 Fail");}
		if(!tfList.contains(tf1)) {outln("Test 12.2.5 Fail");}
		
		
		ad = new AbstractDiagram("");
		dg = new DualGraph(ad);
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals("0")) {outln("Test 12.3.1 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.2 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.2a Fail");}
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals("0")) {outln("Test 12.3.2b Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.2c Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.2d Fail");}
		
		ad = new AbstractDiagram("0");
		dg = new DualGraph(ad);
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.3.3 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.4 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.4a Fail");}
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.3.4b Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.4c Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.4d Fail");}
		
		ad = new AbstractDiagram("0 a");
		dg = new DualGraph(ad);
		dg.firstNodeWithLabel("").setCentre(new Point(100,100));
		dg.firstNodeWithLabel("a").setCentre(new Point(50,50));
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.3.5 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.6 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.6a Fail");}
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.3.6b Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.6c Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.6d Fail");}
		
		ad = new AbstractDiagram("0 a b ab");
		dg = new DualGraph(ad);
		dg.firstNodeWithLabel("").setCentre(new Point(100,100));
		dg.firstNodeWithLabel("a").setCentre(new Point(50,50));
		dg.firstNodeWithLabel("b").setCentre(new Point(150,50));
		dg.firstNodeWithLabel("ab").setCentre(new Point(100,50));
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.3.7 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.8 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.8a Fail");}
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.3.8b Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.8c Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.8d Fail");}

		ad = new AbstractDiagram("0 a b ab ac abc");
		dg = new DualGraph(ad);
		dg.firstNodeWithLabel("").setCentre(new Point(200,300));
		dg.firstNodeWithLabel("a").setCentre(new Point(50,200));
		dg.firstNodeWithLabel("b").setCentre(new Point(250,200));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		dg.firstNodeWithLabel("ac").setCentre(new Point(100,200));
		dg.firstNodeWithLabel("abc").setCentre(new Point(150,200));
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.3.9 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.10 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.11 Fail");}
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.3.11a Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.11b Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.11c Fail");}

		
		// duplicate zone bug, now fixed
		ad = new AbstractDiagram("0 a b c d e ab ad bc bd be abd");
		dg = new DualGraph(ad);

		dg.firstNodeWithLabel("").setCentre(new Point(345,132));
		dg.firstNodeWithLabel("a").setCentre(new Point(209,182));
		dg.firstNodeWithLabel("b").setCentre(new Point(537,482));
		dg.firstNodeWithLabel("c").setCentre(new Point(340,291));
		dg.firstNodeWithLabel("d").setCentre(new Point(67,69));
		dg.firstNodeWithLabel("e").setCentre(new Point(529,86));
		dg.firstNodeWithLabel("ab").setCentre(new Point(257,371));
		dg.firstNodeWithLabel("ad").setCentre(new Point(136,273));
		dg.firstNodeWithLabel("bc").setCentre(new Point(403,361));
		dg.firstNodeWithLabel("bd").setCentre(new Point(55,470));
		dg.firstNodeWithLabel("be").setCentre(new Point(522,220));
		dg.firstNodeWithLabel("abd").setCentre(new Point(146,369));
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.3.12 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.13 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.14 Fail");}
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.3.14a Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.3.14b Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.3.14c Fail");}

		

		// Test the whole generation process
		DiagramDrawerPlanar ddp;
		DiagramPanel dp;
		
		ad = new AbstractDiagram("0");
		dg = new DualGraph(ad);
		dp = new DiagramPanel(dg);
		dp.setForceNoRedraw(true);
		dg.randomizeNodePoints(new Point(50,50),400,400);
		ddp = new DiagramDrawerPlanar(dp);
		ddp.layout();
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.1 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.2 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.2a Fail");}
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.2b Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.2c Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.2d Fail");}
		
		ad = new AbstractDiagram("0 a");
		dg = new DualGraph(ad);
		dp = new DiagramPanel(dg);
		dp.setForceNoRedraw(true);
		dg.randomizeNodePoints(new Point(50,50),400,400);
		ddp = new DiagramDrawerPlanar(dp);
		ddp.layout();
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.3 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.4 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.4a Fail");}
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.4b Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.4c Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.4d Fail");}
		
		ad = new AbstractDiagram("0 a b ab");
		dg = new DualGraph(ad);
		dp = new DiagramPanel(dg);
		dp.setForceNoRedraw(true);
		dg.randomizeNodePoints(new Point(50,50),400,400);
		ddp = new DiagramDrawerPlanar(dp);
		ddp.layout();
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.5 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.6 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.6a Fail");}
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.6b Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.6c Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.6d Fail");}

		ad = new AbstractDiagram("0 a b c ab ac bc abc");
		dg = new DualGraph(ad);
		dp = new DiagramPanel(dg);
		dp.setForceNoRedraw(true);
		dg.randomizeNodePoints(new Point(50,50),400,400);
		ddp = new DiagramDrawerPlanar(dp);
		ddp.layout();
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.7 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.8 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.8a Fail");}
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.8b Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.8c Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.8d Fail");}

		ad = new AbstractDiagram("0 a b c d e ab ac ae bd be ce abe ace");
		dg = new DualGraph(ad);
		dp = new DiagramPanel(dg);
		dp.setForceNoRedraw(true);
		dg.randomizeNodePoints(new Point(50,50),400,400);
		ddp = new DiagramDrawerPlanar(dp);
		ddp.layout();
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.9 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.10 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.10a Fail");}
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.10b Fail");}
		zoneList = cd.findDuplicateZones();

		if(zoneList.size() != 0) {outln("Test 12.4.10c Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.10d Fail");}

		ad = new AbstractDiagram("0 a b d f ab af cd de df cde cdf def cdef");
		dg = new DualGraph(ad);
		dp = new DiagramPanel(dg);
		dp.setForceNoRedraw(true);
		dg.randomizeNodePoints(new Point(50,50),400,400);
		ddp = new DiagramDrawerPlanar(dp);
		ddp.layout();
		
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(true);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.11 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.12 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.12a Fail");}
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(true);
		cd.generateContours();
		if(cd.getCloneGraph().findEdgeCrossings().size() != 0) {outln("Test 12.4.12aa Fail");}
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.12b Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.12c Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.12d Fail");}
		

		ad = new AbstractDiagram("0 a b c d e ad bc bd ce cf cef");
		dg = new DualGraph(ad);
		dp = new DiagramPanel(dg);
		dp.setForceNoRedraw(true);
		dg.randomizeNodePoints(new Point(50,50),400,400);
		ddp = new DiagramDrawerPlanar(dp);
		ddp.layout();
		cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.13 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.14 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.15 Fail");}
		cd.setOptimizeContourAngles(false);
		cd.setFitCircles(false);
		cd.generateContours();
		adRet = cd.generateAbstractDiagramFromPolygons();
		if(!adRet.toString().equals(ad.toString())) {outln("Test 12.4.16 Fail");}
		zoneList = cd.findDuplicateZones();
		if(zoneList.size() != 0) {outln("Test 12.4.17 Fail");}
		tfList = cd.findIncorrectTriangulationCrossings();
		if(tfList.size() != 0) {outln("Test 12.4.18 Fail");}
		
	

		out("Ending method test 12");
	}


	/** test routing optimizations */
	public static void test13(){
		
		outln(" | Starting method test 13");
		
		DualGraph dg;
		Node n1,n2;
		ContourLink cl1;
		ArrayList<String> contourList;
		TriangulationEdge te1;
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dg.firstNodeWithLabel("").setCentre(new Point(200,0));
		dg.firstNodeWithLabel("a").setCentre(new Point(230,0));
		dg.firstNodeWithLabel("b").setCentre(new Point(100,0));
		dg.firstNodeWithLabel("ab").setCentre(new Point(200,100));
		dg.formFaces();
		dg.triangulate();
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("a");
		te1 = dg.findTriangulationEdge(n1,n2);
		if(te1.getFrom() == n2) {
			te1.setFrom(n1);
			te1.setTo(n2);
		}
		n1.setLabel("abc");
		n2.setLabel("");
			
		contourList = new ArrayList<String>();
		contourList.add("a");
		te1.assignCutPointsBetweenNodes(contourList);		
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(200,0))) {outln("Test 13.1.0a Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(230,0))) {outln("Test 13.1.0a Fail");}
		
		contourList = new ArrayList<String>();
		contourList.add("a");
		te1.assignCutPointsBetweenNodes(contourList);		
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.2);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(203,0))) {outln("Test 13.1.1 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(227,0))) {outln("Test 13.1.2 Fail");}
		
		contourList = new ArrayList<String>();
		contourList.add("a");
		te1.assignCutPointsBetweenNodes(contourList);		
		cl1 = te1.contourLinksWithContour("a").get(0);
		n1.setCentre(new Point(50,50));
		n2.setCentre(new Point(40,40));
		te1.assignCPRange(cl1.getCutPoint(),0.2);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(49,49))) {outln("Test 13.1.3 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(41,41))) {outln("Test 13.1.4 Fail");}
		
		contourList = new ArrayList<String>();
		contourList.add("a");
		te1.assignCutPointsBetweenNodes(contourList);		
		cl1 = te1.contourLinksWithContour("a").get(0);
		n1.setCentre(new Point(50,50));
		n2.setCentre(new Point(70,30));
		te1.assignCPRange(cl1.getCutPoint(),0.4);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(54,46))) {outln("Test 13.1.5 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(66,34))) {outln("Test 13.1.6 Fail");}
		
		contourList = new ArrayList<String>();
		contourList.add("a");
		te1.assignCutPointsBetweenNodes(contourList);		
		cl1 = te1.contourLinksWithContour("a").get(0);
		n1.setCentre(new Point(50,50));
		n2.setCentre(new Point(30,70));
		te1.assignCPRange(cl1.getCutPoint(),0.4);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(46,54))) {outln("Test 13.1.6a Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(34,66))) {outln("Test 13.1.6b Fail");}
		
		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		contourList.add("c");
		n1.setCentre(new Point(0,0));
		n2.setCentre(new Point(62,62));
		te1.assignCutPointsBetweenNodes(contourList);		
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(8,8))) {outln("Test 13.1.7 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(18,18))) {outln("Test 13.1.8 Fail");}
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(26,26))) {outln("Test 13.1.9 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(36,36))) {outln("Test 13.1.10 Fail");}
		cl1 = te1.contourLinksWithContour("c").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(44,44))) {outln("Test 13.1.11 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(54,54))) {outln("Test 13.1.12 Fail");}

		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		contourList.add("c");
		n1.setCentre(new Point(200,100));
		n2.setCentre(new Point(138,38));
		te1.assignCutPointsBetweenNodes(contourList);
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(192,92))) {outln("Test 13.1.13 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(182,82))) {outln("Test 13.1.14 Fail");}
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(174,74))) {outln("Test 13.1.15 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(164,64))) {outln("Test 13.1.16 Fail");}
		cl1 = te1.contourLinksWithContour("c").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(156,56))) {outln("Test 13.1.17 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(146,46))) {outln("Test 13.1.18 Fail");}

		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		contourList.add("c");
		n1.setCentre(new Point(200,100));
		n2.setCentre(new Point(138,38));
		cl1 = te1.contourLinksWithContour("c").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(156,56))) {outln("Test 13.1.19 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(146,46))) {outln("Test 13.1.20 Fail");}
		te1.assignCutPointsBetweenNodes(contourList);
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(174,74))) {outln("Test 13.1.21 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(164,64))) {outln("Test 13.1.22 Fail");}
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(192,92))) {outln("Test 13.1.23 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(182,82))) {outln("Test 13.1.24 Fail");}

		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		contourList.add("c");
		n1.setCentre(new Point(200,100));
		n2.setCentre(new Point(138,38));
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(174,74))) {outln("Test 13.1.25 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(164,64))) {outln("Test 13.1.26 Fail");}
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(192,92))) {outln("Test 13.1.27 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(182,82))) {outln("Test 13.1.28 Fail");}
		cl1 = te1.contourLinksWithContour("c").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(156,56))) {outln("Test 13.1.29 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(146,46))) {outln("Test 13.1.30 Fail");}
		
		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		n1.setCentre(new Point(100,100));
		n2.setCentre(new Point(400,70));
		te1.setCutPoints(new ArrayList<CutPoint>());
		te1.addContourPoint("a", new Point());
		te1.addContourPoint("b", new Point());
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.1);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(110,99))) {outln("Test 13.1.31 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(245,86))) {outln("Test 13.1.32 Fail");}
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.1);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(255,85))) {outln("Test 13.1.33 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(390,71))) {outln("Test 13.1.34 Fail");}
		te1.assignCutPointsBetweenNodes(contourList);
		
		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		n1.setCentre(new Point(100,100));
		n2.setCentre(new Point(70,400));
		te1.setCutPoints(new ArrayList<CutPoint>());
		te1.addContourPoint("a", new Point());
		te1.addContourPoint("b", new Point());
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.1);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(99,110))) {outln("Test 13.1.35 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(86,245))) {outln("Test 13.1.36 Fail");}
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.1);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(85,255))) {outln("Test 13.1.37 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(71,390))) {outln("Test 13.1.38 Fail");}
		
		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		n1.setCentre(new Point(100,100));
		n2.setCentre(new Point(70,400));
		te1.setCutPoints(new ArrayList<CutPoint>());
		te1.addContourPoint("a", new Point());
		te1.addContourPoint("b", new Point());
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.1);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(99,110))) {outln("Test 13.1.35 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(86,245))) {outln("Test 13.1.36 Fail");}
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.1);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(85,255))) {outln("Test 13.1.37 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(71,390))) {outln("Test 13.1.38 Fail");}
		
		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		n1.setCentre(new Point(100,100));
		n2.setCentre(new Point(200,200));
		te1.setCutPoints(new ArrayList<CutPoint>());
		te1.addContourPoint("a", new Point());
		te1.addContourPoint("b", new Point());
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.2);
		cl1.getCutPoint().setMinLimit(new Point(170,170));
		cl1.getCutPoint().setMaxLimit(new Point(170,170));
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(170,170))) {outln("Test 13.1.39 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(170,170))) {outln("Test 13.1.40 Fail");}
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.2);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(177,177))) {outln("Test 13.1.41 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(193,193))) {outln("Test 13.1.42 Fail");}
		

		n1.setLabel("");
		n2.setLabel("abc");

		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		n1.setCentre(new Point(100,100));
		n2.setCentre(new Point(70,400));
		te1.setCutPoints(new ArrayList<CutPoint>());
		te1.addContourPoint("a", new Point());
		te1.addContourPoint("b", new Point());
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.1);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(86,245))) {outln("Test 13.1.43 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(99,110))) {outln("Test 13.1.44 Fail");}
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.1);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(71,390))) {outln("Test 13.1.45 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(85,255))) {outln("Test 13.1.46 Fail");}
		
		n1.setLabel("");
		n2.setLabel("abc");

		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		n1.setCentre(new Point(100,100));
		n2.setCentre(new Point(70,400));
		te1.setCutPoints(new ArrayList<CutPoint>());
		te1.addContourPoint("a", new Point());
		te1.addContourPoint("b", new Point());
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.1);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(71,390))) {outln("Test 13.1.47 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(84,255))) {outln("Test 13.1.48 Fail");}
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.1);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(85,245))) {outln("Test 13.1.49 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(99,110))) {outln("Test 13.1.50 Fail");}
		
		n1.setLabel("");
		n2.setLabel("abc");

		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		contourList.add("c");
		n1.setCentre(new Point(0,0));
		n2.setCentre(new Point(62,62));
		te1.assignCutPointsBetweenNodes(contourList);		
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(36,36))) {outln("Test 13.1.51 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(26,26))) {outln("Test 13.1.52 Fail");}
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(18,18))) {outln("Test 13.1.53 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(8,8))) {outln("Test 13.1.54 Fail");}
		cl1 = te1.contourLinksWithContour("c").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(54,54))) {outln("Test 13.1.55 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(44,44))) {outln("Test 13.1.56 Fail");}
		
		n1.setLabel("b");
		n2.setLabel("ac");

		contourList = new ArrayList<String>();
		contourList.add("a");
		contourList.add("b");
		contourList.add("c");
		n1.setCentre(new Point(0,0));
		n2.setCentre(new Point(62,62));
		te1.assignCutPointsBetweenNodes(contourList);		
		cl1 = te1.contourLinksWithContour("c").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(54,54))) {outln("Test 13.1.57 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(44,44))) {outln("Test 13.1.58 Fail");}
		cl1 = te1.contourLinksWithContour("b").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(26,26))) {outln("Test 13.1.59 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(36,36))) {outln("Test 13.1.60 Fail");}
		cl1 = te1.contourLinksWithContour("a").get(0);
		te1.assignCPRange(cl1.getCutPoint(),0.5);
		if(!cl1.getCutPoint().getMinLimit().equals(new Point(18,18))) {outln("Test 13.1.61 Fail");}
		if(!cl1.getCutPoint().getMaxLimit().equals(new Point(8,8))) {outln("Test 13.1.62 Fail");}
		
		out("Ending method test 13");
	}
	
	
	
	/** test contour dimension and scaling methods */
	public static void test14(){

		outln(" | Starting method test 14");
		
		ConcreteContour cc1,cc2;
		Polygon p1,p2;
		ArrayList<ConcreteContour> ccs;
		Rectangle r,rRet;
		ArrayList<Rectangle> rs;

		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		cc1 = new ConcreteContour("a",p1);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 100) {outln("Test 14.1.1 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 200) {outln("Test 14.1.2 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(150,200))) {outln("Test 14.1.3 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		p2 = new Polygon();
		p2.addPoint(150, 150);
		p2.addPoint(400, 130);
		p2.addPoint(400, 170);
		cc1 = new ConcreteContour("a",p1);
		cc2 = new ConcreteContour("b",p2);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 300) {outln("Test 14.1.4 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 200) {outln("Test 14.1.5 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(250,200))) {outln("Test 14.1.6 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		p2 = new Polygon();
		p2.addPoint(150, 150);
		p2.addPoint(150, 170);
		p2.addPoint(170, 170);
		p2.addPoint(170, 150);
		cc1 = new ConcreteContour("a",p1);
		cc2 = new ConcreteContour("b",p2);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 100) {outln("Test 14.1.7 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 200) {outln("Test 14.1.8 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(150,200))) {outln("Test 14.1.9 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		p2 = new Polygon();
		p2.addPoint(150, 150);
		p2.addPoint(400, 130);
		p2.addPoint(400, 170);
		cc1 = new ConcreteContour("a",p1);
		cc2 = new ConcreteContour("b",p2);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		WellFormedConcreteDiagram.scaleContours(ccs,0.5);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 150) {outln("Test 14.1.10 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 100) {outln("Test 14.1.11 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(250,200))) {outln("Test 14.1.12 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		p2 = new Polygon();
		p2.addPoint(150, 150);
		p2.addPoint(400, 130);
		p2.addPoint(400, 170);
		cc2 = new ConcreteContour("b",p2);
		cc1 = new ConcreteContour("a",p1);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		WellFormedConcreteDiagram.scaleContours(ccs,2);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 600) {outln("Test 14.1.13 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 400) {outln("Test 14.1.14 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(250,200))) {outln("Test 14.1.15 Fail");}
		
		
		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		p2 = new Polygon();
		p2.addPoint(150, 150);
		p2.addPoint(150, 170);
		p2.addPoint(170, 170);
		p2.addPoint(170, 150);
		cc1 = new ConcreteContour("a",p1);
		cc2 = new ConcreteContour("b",p2);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		WellFormedConcreteDiagram.moveContours(ccs, 50, 50);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 100) {outln("Test 14.1.16 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 200) {outln("Test 14.1.17 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(200,250))) {outln("Test 14.1.18 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		p2 = new Polygon();
		p2.addPoint(150, 150);
		p2.addPoint(150, 170);
		p2.addPoint(170, 170);
		p2.addPoint(170, 150);
		cc1 = new ConcreteContour("a",p1);
		cc2 = new ConcreteContour("b",p2);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		WellFormedConcreteDiagram.moveContours(ccs, -50, -50);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 100) {outln("Test 14.1.19 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 200) {outln("Test 14.1.20 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(100,150))) {outln("Test 14.1.21 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		p2 = new Polygon();
		p2.addPoint(150, 150);
		p2.addPoint(150, 170);
		p2.addPoint(170, 170);
		p2.addPoint(170, 150);
		cc1 = new ConcreteContour("a",p1);
		cc2 = new ConcreteContour("b",p2);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		WellFormedConcreteDiagram.centreContoursOnPoint(ccs, 0, 0);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 100) {outln("Test 14.1.22 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 200) {outln("Test 14.1.23 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(0,0))) {outln("Test 14.1.24 Fail");}
		
		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		p2 = new Polygon();
		p2.addPoint(150, 150);
		p2.addPoint(400, 130);
		p2.addPoint(400, 170);
		cc2 = new ConcreteContour("b",p2);
		cc1 = new ConcreteContour("a",p1);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		WellFormedConcreteDiagram.centreContoursOnPoint(ccs,200,50);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 300) {outln("Test 14.1.25 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 200) {outln("Test 14.1.26 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(200,50))) {outln("Test 14.1.27 Fail");}

		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		p2 = new Polygon();
		p2.addPoint(150, 150);
		p2.addPoint(400, 130);
		p2.addPoint(400, 170);
		cc2 = new ConcreteContour("b",p2);
		cc1 = new ConcreteContour("a",p1);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		WellFormedConcreteDiagram.fitContoursInRectangle(ccs, 0, 200, 700, 300);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 150) {outln("Test 14.1.28 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 100) {outln("Test 14.1.29 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(350,250))) {outln("Test 14.1.30 Fail");}

		p1 = new Polygon();
		p1.addPoint(100, 400);
		p1.addPoint(100, 500);
		p1.addPoint(200, 600);
		p1.addPoint(100, 600);
		cc1 = new ConcreteContour("a",p1);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		if(WellFormedConcreteDiagram.findContoursBounds(ccs).x != 100) {outln("Test 14.1.31 Fail");}
		if(WellFormedConcreteDiagram.findContoursBounds(ccs).y != 400) {outln("Test 14.1.32 Fail");}
		if(WellFormedConcreteDiagram.findContoursBounds(ccs).width != 100) {outln("Test 14.1.33 Fail");}
		if(WellFormedConcreteDiagram.findContoursBounds(ccs).height != 200) {outln("Test 14.1.34 Fail");}

		p1 = new Polygon();
		p1.addPoint(100, 400);
		p1.addPoint(100, 500);
		p1.addPoint(200, 600);
		p1.addPoint(100, 600);
		p2 = new Polygon();
		p2.addPoint(150, 450);
		p2.addPoint(160, 450);
		p2.addPoint(160, 900);
		p2.addPoint(150, 900);
		cc1 = new ConcreteContour("a",p1);
		cc2 = new ConcreteContour("b",p2);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		if(WellFormedConcreteDiagram.findContoursBounds(ccs).x != 100) {outln("Test 14.1.35 Fail");}
		if(WellFormedConcreteDiagram.findContoursBounds(ccs).y != 400) {outln("Test 14.1.36 Fail");}
		if(WellFormedConcreteDiagram.findContoursBounds(ccs).width != 100) {outln("Test 14.1.37 Fail");}
		if(WellFormedConcreteDiagram.findContoursBounds(ccs).height != 500) {outln("Test 14.1.38 Fail");}

		p1 = new Polygon();
		p1.addPoint(100, 100);
		p1.addPoint(100, 200);
		p1.addPoint(200, 300);
		p1.addPoint(100, 300);
		p2 = new Polygon();
		p2.addPoint(150, 150);
		p2.addPoint(400, 130);
		p2.addPoint(400, 170);
		cc2 = new ConcreteContour("b",p2);
		cc1 = new ConcreteContour("a",p1);
		ccs = new ArrayList<ConcreteContour>();
		ccs.add(cc1);
		ccs.add(cc2);
		WellFormedConcreteDiagram.fitContoursInRectangle(ccs, -700, -2200, -100, -200);
		if(WellFormedConcreteDiagram.findContoursWidth(ccs) != 600) {outln("Test 14.1.31 Fail");}
		if(WellFormedConcreteDiagram.findContoursHeight(ccs) != 400) {outln("Test 14.1.32 Fail");}
		if(!WellFormedConcreteDiagram.findContoursCentre(ccs).equals(new Point(-400,-1200))) {outln("Test 14.1.33 Fail");}

		r = new Rectangle(100,300,100,200);
		rs = euler.Util.divideIntoRectangles(r, 1);
		if(rs.size() != 1) {outln("Test 14.2.1 Fail");}
		rRet = rs.get(0);
		if(rRet.x != 100) {outln("Test 14.2.2 Fail");}
		if(rRet.y != 300) {outln("Test 14.2.3 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.4 Fail");}
		if(rRet.height != 200) {outln("Test 14.2.5 Fail");}

		r = new Rectangle(100,300,100,200);
		rs = euler.Util.divideIntoRectangles(r, 2);
		if(rs.size() != 2) {outln("Test 14.2.6 Fail");}
		rRet = rs.get(0);
		if(rRet.x != 100) {outln("Test 14.2.7 Fail");}
		if(rRet.y != 300) {outln("Test 14.2.8 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.9 Fail");}
		if(rRet.height != 100) {outln("Test 14.2.10 Fail");}
		rRet = rs.get(1);
		if(rRet.x != 100) {outln("Test 14.2.7 Fail");}
		if(rRet.y != 400) {outln("Test 14.2.8 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.9 Fail");}
		if(rRet.height != 100) {outln("Test 14.2.10 Fail");}

		r = new Rectangle(100,300,400,100);
		rs = euler.Util.divideIntoRectangles(r, 2);
		if(rs.size() != 2) {outln("Test 14.2.11 Fail");}
		rRet = rs.get(0);
		if(rRet.x != 100) {outln("Test 14.2.12 Fail");}
		if(rRet.y != 300) {outln("Test 14.2.13 Fail");}
		if(rRet.width != 200) {outln("Test 14.2.14 Fail");}
		if(rRet.height != 100) {outln("Test 14.2.15 Fail");}
		rRet = rs.get(1);
		if(rRet.x != 300) {outln("Test 14.2.16 Fail");}
		if(rRet.y != 300) {outln("Test 14.2.17 Fail");}
		if(rRet.width != 200) {outln("Test 14.2.18 Fail");}
		if(rRet.height != 100) {outln("Test 14.2.19 Fail");}
		
		r = new Rectangle(100,300,400,800);
		rs = euler.Util.divideIntoRectangles(r, 16);
		if(rs.size() != 16) {outln("Test 14.2.20 Fail");}
		rRet = rs.get(0);
		if(rRet.x != 100) {outln("Test 14.2.21 Fail");}
		if(rRet.y != 300) {outln("Test 14.2.22 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.23 Fail");}
		if(rRet.height != 200) {outln("Test 14.2.24 Fail");}
		rRet = rs.get(1);
		if(rRet.x != 200) {outln("Test 14.2.25 Fail");}
		if(rRet.y != 300) {outln("Test 14.2.26 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.27 Fail");}
		if(rRet.height != 200) {outln("Test 14.2.28 Fail");}
		rRet = rs.get(4);
		if(rRet.x != 100) {outln("Test 14.2.29 Fail");}
		if(rRet.y != 500) {outln("Test 14.2.30 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.31 Fail");}
		if(rRet.height != 200) {outln("Test 14.2.32 Fail");}
		
		r = new Rectangle(300,100,800,400);
		rs = euler.Util.divideIntoRectangles(r, 13);
		if(rs.size() != 16) {outln("Test 14.2.33 Fail");}
		rRet = rs.get(0);
		if(rRet.x != 300) {outln("Test 14.2.34 Fail");}
		if(rRet.y != 100) {outln("Test 14.2.35 Fail");}
		if(rRet.width != 200) {outln("Test 14.2.36 Fail");}
		if(rRet.height != 100) {outln("Test 14.2.37 Fail");}
		rRet = rs.get(1);
		if(rRet.x != 500) {outln("Test 14.2.38 Fail");}
		if(rRet.y != 100) {outln("Test 14.2.39 Fail");}
		if(rRet.width != 200) {outln("Test 14.2.40 Fail");}
		if(rRet.height != 100) {outln("Test 14.2.41 Fail");}
		rRet = rs.get(4);
		if(rRet.x != 300) {outln("Test 14.2.42 Fail");}
		if(rRet.y != 200) {outln("Test 14.2.43 Fail");}
		if(rRet.width != 200) {outln("Test 14.2.44 Fail");}
		if(rRet.height != 100) {outln("Test 14.2.45 Fail");}
		
		r = new Rectangle(0,0,300,400);
		rs = euler.Util.divideIntoRectangles(r, 12);
		if(rs.size() != 12) {outln("Test 14.2.46 Fail");}
		rRet = rs.get(0);
		if(rRet.x != 0) {outln("Test 14.2.47 Fail");}
		if(rRet.y != 0) {outln("Test 14.2.48 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.49 Fail");}
		if(rRet.height != 100) {outln("Test 14.2.50 Fail");}
		rRet = rs.get(1);
		if(rRet.x != 100) {outln("Test 14.2.51 Fail");}
		if(rRet.y != 0) {outln("Test 14.2.52 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.53 Fail");}
		if(rRet.height != 100) {outln("Test 14.2.54 Fail");}
		rRet = rs.get(3);
		if(rRet.x != 0) {outln("Test 14.2.55 Fail");}
		if(rRet.y != 100) {outln("Test 14.2.56 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.57 Fail");}
		if(rRet.height != 100) {outln("Test 14.2.58 Fail");}
		
		r = new Rectangle(0,0,400,30);
		rs = euler.Util.divideIntoRectangles(r, 12);
		if(rs.size() != 12) {outln("Test 14.2.59 Fail");}
		rRet = rs.get(0);
		if(rRet.x != 0) {outln("Test 14.2.60 Fail");}
		if(rRet.y != 0) {outln("Test 14.2.61 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.61 Fail");}
		if(rRet.height != 10) {outln("Test 14.2.63 Fail");}
		rRet = rs.get(1);
		if(rRet.x != 100) {outln("Test 14.2.64 Fail");}
		if(rRet.y != 0) {outln("Test 14.2.65 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.66 Fail");}
		if(rRet.height != 10) {outln("Test 14.2.67 Fail");}
		rRet = rs.get(4);
		if(rRet.x != 0) {outln("Test 14.2.68 Fail");}
		if(rRet.y != 10) {outln("Test 14.2.69 Fail");}
		if(rRet.width != 100) {outln("Test 14.2.70 Fail");}
		if(rRet.height != 10) {outln("Test 14.2.71 Fail");}
		
		r = new Rectangle(0,0,400,30);
		rs = euler.Util.divideIntoRectangles(r, 0);
		if(rs != null) {outln("Test 14.2.72 Fail");}
		
		r = new Rectangle(0,0,400,30);
		rs = euler.Util.divideIntoRectangles(r, -20);
		if(rs != null) {outln("Test 14.2.73 Fail");}
		
		out("Ending method test 14");
		
	}
	
	
	/** test code for concurrent contours */
	public static void test15(){

		outln(" | Starting method test 15");
		
		GeneralConcreteDiagram cd;
		DualGraph dg,cloneGraph;
		Node n1,n2,n3;
		Edge e1;
		TriangulationFace tf1;
		String contours1;
		ArrayList<TriangulationEdge> teList1;
		ArrayList<String> cList1;
		ArrayList<ContourLink> clList1;
		ArrayList<CutPoint> cpList1;
		ArrayList<Node> nodeList1,nodeList2;
		ArrayList<ArrayList<Node>> nLL1;
		CutPoint cp1;

		
		dg = new DualGraph(new AbstractDiagram("0 ab ac"));
		n1 = dg.firstNodeWithLabel("");
		n2 = dg.firstNodeWithLabel("ab");
		n3 = dg.firstNodeWithLabel("ac");
		n1.setCentre(new Point(200,0));
		n2.setCentre(new Point(100,200));
		n3.setCentre(new Point(200,100));
		dg.addEdge(new Edge(n1,n2,"ab"));
		dg.addEdge(new Edge(n1,n3,"ac"));
		dg.addEdge(new Edge(n2,n3,"bc"));
		cd = new GeneralConcreteDiagram(dg);
		cd.generateContours();
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(true);
		cd.generateContours();
		cloneGraph = cd.getCloneGraph();
		tf1 = cloneGraph.firstTriangulationFaceWithNodeLabels("","ab","ac");
		cpList1 = tf1.findCycleCutPoints();
		if(cpList1.size() != 3) {outln("Test 15.1.1 Fail");}
		if(cpList1.get(0).getContourLinks().size() != 2)  {outln("Test 15.1.2 Fail");}
		clList1 = tf1.findCycleContourLinks();
		if(clList1.size() != 6) {outln("Test 15.1.3 Fail");}
		cList1 = tf1.findCycleContours();
		contours1 = "";
		for(String c1 : cList1) {contours1 += c1;}
		if(!(contours1.equals("abacbc") || contours1.equals("acbcab") || contours1.equals("bcabac"))) {
			outln("Test 15.1.4 Fail");
		}
		teList1 = tf1.findTEsWithContour("a");
		if(teList1.size() != 2) {outln("Test 15.1.5 Fail");}
		teList1 = tf1.findTEsWithContour("b");
		if(teList1.size() != 2) {outln("Test 15.1.6 Fail");}
		teList1 = tf1.findTEsWithContour("c");
		if(teList1.size() != 2) {outln("Test 15.1.7 Fail");}
		
		
		cp1 = new CutPoint(tf1,new ArrayList<ContourLink>(),null);
		if(!cp1.findCommonContourLabels("").equals(""))  {outln("Test 15.2.1 Fail");}
		if(!cp1.findCommonContourLabels("ab").equals(""))  {outln("Test 15.2.2 Fail");}

		cp1 = new CutPoint(tf1,new ArrayList<ContourLink>(),null);
		clList1 = new ArrayList<ContourLink>();
		clList1.add(new ContourLink("a",cp1,null,null));
		cp1 = new CutPoint(tf1,clList1,null);
		if(!cp1.findCommonContourLabels("").equals(""))  {outln("Test 15.2.3 Fail");}
		if(!cp1.findCommonContourLabels("b").equals(""))  {outln("Test 15.2.4 Fail");}
		if(!cp1.findCommonContourLabels("a").equals("a"))  {outln("Test 15.2.5 Fail");}
		if(!cp1.findCommonContourLabels("ab").equals("a"))  {outln("Test 15.2.6 Fail");}

		cp1 = new CutPoint(tf1,new ArrayList<ContourLink>(),null);
		clList1 = new ArrayList<ContourLink>();
		clList1.add(new ContourLink("b",cp1,null,null));
		clList1.add(new ContourLink("a",cp1,null,null));
		cp1 = new CutPoint(tf1,clList1,null);
		if(!cp1.findCommonContourLabels("").equals(""))  {outln("Test 15.2.7 Fail");}
		if(!cp1.findCommonContourLabels("c").equals(""))  {outln("Test 15.2.8 Fail");}
		if(!cp1.findCommonContourLabels("b").equals("b"))  {outln("Test 15.2.9 Fail");}
		if(!cp1.findCommonContourLabels("a").equals("a"))  {outln("Test 15.2.10 Fail");}
		if(!cp1.findCommonContourLabels("ab").equals("ab"))  {outln("Test 15.2.11 Fail");}
		if(!cp1.findCommonContourLabels("ba").equals("ab"))  {outln("Test 15.2.12 Fail");}

		cp1 = new CutPoint(tf1,new ArrayList<ContourLink>(),null);
		clList1 = new ArrayList<ContourLink>();
		clList1.add(new ContourLink("d",cp1,null,null));
		clList1.add(new ContourLink("a",cp1,null,null));
		clList1.add(new ContourLink("b",cp1,null,null));
		clList1.add(new ContourLink("e",cp1,null,null));
		clList1.add(new ContourLink("c",cp1,null,null));
		cp1 = new CutPoint(tf1,clList1,null);
		if(!cp1.findCommonContourLabels("").equals(""))  {outln("Test 15.2.13 Fail");}
		if(!cp1.findCommonContourLabels("xyz").equals(""))  {outln("Test 15.2.14 Fail");}
		if(!cp1.findCommonContourLabels("b").equals("b"))  {outln("Test 15.2.15 Fail");}
		if(!cp1.findCommonContourLabels("aq").equals("a"))  {outln("Test 15.2.16 Fail");}
		if(!cp1.findCommonContourLabels("acxby").equals("abc"))  {outln("Test 15.2.17 Fail");}
		if(!cp1.findCommonContourLabels("xyzeb").equals("be"))  {outln("Test 15.2.18 Fail");}
		if(!cp1.findCommonContourLabels("edacb").equals("abcde"))  {outln("Test 15.2.19 Fail");}

		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dg.firstNodeWithLabel("").setCentre(new Point(20,20));
		dg.firstNodeWithLabel("a").setCentre(new Point(20,30));
		dg.firstNodeWithLabel("b").setCentre(new Point(20,40));
		dg.firstNodeWithLabel("ab").setCentre(new Point(30,30));
		dg.formFaces();
		for(Face f : dg.getFaces()) {
			n1 = f.getNodeList().get(0);
			n2 = f.getNodeList().get(1);
			if(!dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.1 Fail");}
			if(!dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.2 Fail");}
			n1 = f.getNodeList().get(0);
			n2 = f.getNodeList().get(2);
			if(dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.3 Fail");}
			if(dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.4 Fail");}
		}
		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dg.addAdjacencyEdge("", "ab");
		dg.firstNodeWithLabel("").setCentre(new Point(20,20));
		dg.firstNodeWithLabel("a").setCentre(new Point(20,30));
		dg.firstNodeWithLabel("b").setCentre(new Point(20,40));
		dg.firstNodeWithLabel("ab").setCentre(new Point(30,30));
		dg.formFaces();
		for(Face f : dg.getFaces()) {
			if(f.getNodeList().size() == 4) {
				n1 = dg.firstNodeWithLabel("a");
				n2 = dg.firstNodeWithLabel("ab");
				if(!dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.5 Fail");}
				if(!dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.6 Fail");}
				n1 = dg.firstNodeWithLabel("");
				n2 = dg.firstNodeWithLabel("ab");
				if(dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.7 Fail");}
				if(dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.8 Fail");}
			} else if(f.getNodeList().size() == 3) {
				n1 = dg.firstNodeWithLabel("a");
				n2 = dg.firstNodeWithLabel("ab");
				if(!dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.9 Fail");}
				if(!dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.10 Fail");}
				n1 = dg.firstNodeWithLabel("");
				n2 = dg.firstNodeWithLabel("ab");
				if(!dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.11 Fail");}
				if(!dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.12 Fail");}
			} else {
				outln("Test 15.3.12a Fail");
			}
		}

		
		dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dg.firstNodeWithLabel("").setCentre(new Point(20,20));
		dg.firstNodeWithLabel("a").setCentre(new Point(20,30));
		dg.firstNodeWithLabel("b").setCentre(new Point(20,40));
		dg.firstNodeWithLabel("ab").setCentre(new Point(30,30));
		e1 = dg.getEdge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("a"));
		e1.setType(DualGraph.POLY_EDGE_TYPE);
		e1 = dg.getEdge(dg.firstNodeWithLabel("a"),dg.firstNodeWithLabel("ab"));
		e1.setType(DualGraph.POLY_EDGE_TYPE);
		dg.formFaces();
		for(Face f : dg.getFaces()) {
			n1 = dg.firstNodeWithLabel("");
			n2 = dg.firstNodeWithLabel("a");
			if(!dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.13 Fail");}
			if(!dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.14 Fail");}
			n1 = dg.firstNodeWithLabel("");
			n2 = dg.firstNodeWithLabel("ab");
			if(!dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.15 Fail");}
			if(!dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.16 Fail");}
		}
		
		dg = new DualGraph(new AbstractDiagram("0 a b ac bc abc"));
		dg.firstNodeWithLabel("").setCentre(new Point(20,20));
		dg.firstNodeWithLabel("a").setCentre(new Point(20,10));
		dg.firstNodeWithLabel("ac").setCentre(new Point(20,0));
		dg.firstNodeWithLabel("b").setCentre(new Point(20,30));
		dg.firstNodeWithLabel("bc").setCentre(new Point(20,40));
		dg.firstNodeWithLabel("abc").setCentre(new Point(40,20));
		e1 = dg.getEdge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("a"));
		e1.setType(DualGraph.POLY_EDGE_TYPE);
		e1 = dg.getEdge(dg.firstNodeWithLabel("a"),dg.firstNodeWithLabel("ac"));
		e1.setType(DualGraph.POLY_EDGE_TYPE);
		dg.formFaces();
		for(Face f : dg.getFaces()) {
			n1 = dg.firstNodeWithLabel("");
			n2 = dg.firstNodeWithLabel("a");
			if(!dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.17 Fail");}
			if(!dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.18 Fail");}
			n1 = dg.firstNodeWithLabel("");
			n2 = dg.firstNodeWithLabel("ac");
			if(!dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.19 Fail");}
			if(!dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.20 Fail");}
			n1 = dg.firstNodeWithLabel("");
			n2 = dg.firstNodeWithLabel("abc");
			if(dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.21 Fail");}
			if(dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.22 Fail");}
			n1 = dg.firstNodeWithLabel("b");
			n2 = dg.firstNodeWithLabel("abc");
			if(dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.23 Fail");}
			if(dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.24 Fail");}
			n1 = dg.firstNodeWithLabel("");
			n2 = dg.firstNodeWithLabel("bc");
			if(dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.25 Fail");}
			if(dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.26 Fail");}
		}
		
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","c");
		dg.addAdjacencyEdge("c","d");
		dg.addAdjacencyEdge("a","d");
		dg.addAdjacencyEdge("a","x");
		dg.addAdjacencyEdge("y","x");
		dg.addAdjacencyEdge("y","c");
		dg.firstNodeWithLabel("a").setCentre(new Point(20,20));
		dg.firstNodeWithLabel("b").setCentre(new Point(30,30));
		dg.firstNodeWithLabel("c").setCentre(new Point(20,40));
		dg.firstNodeWithLabel("d").setCentre(new Point(10,30));
		dg.firstNodeWithLabel("x").setCentre(new Point(20,25));
		dg.firstNodeWithLabel("y").setCentre(new Point(20,30));
		e1 = dg.getEdge(dg.firstNodeWithLabel("a"),dg.firstNodeWithLabel("x"));
		e1.setType(DualGraph.POLY_EDGE_TYPE);
		e1 = dg.getEdge(dg.firstNodeWithLabel("y"),dg.firstNodeWithLabel("x"));
		e1.setType(DualGraph.POLY_EDGE_TYPE);
		e1 = dg.getEdge(dg.firstNodeWithLabel("y"),dg.firstNodeWithLabel("c"));
		e1.setType(DualGraph.POLY_EDGE_TYPE);
		dg.formFaces();
		for(Face f : dg.getFaces()) {
			if(f.getNodeList().size() == 4) {
				n1 = dg.firstNodeWithLabel("a");
				n2 = dg.firstNodeWithLabel("b");
				if(!dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.27 Fail");}
				if(!dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.28 Fail");}
				n1 = dg.firstNodeWithLabel("a");
				n2 = dg.firstNodeWithLabel("c");
				if(dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.29 Fail");}
				if(dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.30 Fail");}
			} else if(f.getNodeList().size() == 5) {
				n1 = dg.firstNodeWithLabel("a");
				n2 = dg.firstNodeWithLabel("c");
				if(!dg.polylineAdjacencyTestInFace(f,n1,n2)) {outln("Test 15.3.31 Fail");}
				if(!dg.polylineAdjacencyTestInFace(f,n2,n1)) {outln("Test 15.3.32 Fail");}
			} else {
				outln("Test 15.3.19 Fail");
			}
		}
		
		dg = new DualGraph();
		n1 = new Node();
		nodeList1 = new ArrayList<Node>();
		if(dg.findNodesConnectedToNode(n1, nodeList1).size() != 0) {outln("Test 15.4.1 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList1.add(dg.firstNodeWithLabel("a"));
		nodeList2 = dg.findNodesConnectedToNode(n1, nodeList1);
		if(nodeList2.size() != 1) {outln("Test 15.4.2 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.3 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList1.add(dg.firstNodeWithLabel("a"));
		nodeList1.add(dg.firstNodeWithLabel("b"));
		nodeList2 = dg.findNodesConnectedToNode(n1, nodeList1);
		if(nodeList2.size() != 2) {outln("Test 15.4.4 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.5 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.4.6 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList2 = dg.findNodesConnectedToNode(n1, nodeList1);
		if(nodeList2.size() != 1) {outln("Test 15.4.7 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.8 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","ab");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList1.add(dg.firstNodeWithLabel("a"));
		nodeList1.add(dg.firstNodeWithLabel("ab"));
		nodeList2 = dg.findNodesConnectedToNode(n1, nodeList1);
		if(nodeList2.size() != 1) {outln("Test 15.4.9 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.10 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","ab");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList1.add(dg.firstNodeWithLabel("a"));
		nodeList1.add(dg.firstNodeWithLabel("b"));
		nodeList2 = dg.findNodesConnectedToNode(n1,nodeList1);
		if(nodeList2.size() != 2) {outln("Test 15.4.11 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.12 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.4.13 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","ab");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList1.add(dg.firstNodeWithLabel("a"));
		nodeList1.add(dg.firstNodeWithLabel("b"));
		nodeList1.add(dg.firstNodeWithLabel("ab"));
		nodeList2 = dg.findNodesConnectedToNode(n1,nodeList1);
		if(nodeList2.size() != 3) {outln("Test 15.4.14 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.15 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.4.16 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("ab"))) {outln("Test 15.4.17 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","c");
		dg.addAdjacencyEdge("a","c");
		dg.addAdjacencyEdge("b","d");
		dg.addAdjacencyEdge("e","d");
		dg.addAdjacencyEdge("e","f");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList1.add(dg.firstNodeWithLabel("a"));
		nodeList1.add(dg.firstNodeWithLabel("b"));
		nodeList1.add(dg.firstNodeWithLabel("d"));
		nodeList2 = dg.findNodesConnectedToNode(n1,nodeList1);
		if(nodeList2.size() != 3) {outln("Test 15.4.17 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.18 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.4.19 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","c");
		dg.addAdjacencyEdge("a","c");
		dg.addAdjacencyEdge("b","d");
		dg.addAdjacencyEdge("e","d");
		dg.addAdjacencyEdge("e","f");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList1.add(dg.firstNodeWithLabel("a"));
		nodeList1.add(dg.firstNodeWithLabel("e"));
		nodeList1.add(dg.firstNodeWithLabel("d"));
		nodeList2 = dg.findNodesConnectedToNode(n1,nodeList1);
		if(nodeList2.size() != 1) {outln("Test 15.4.20 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.21 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","c");
		dg.addAdjacencyEdge("a","c");
		dg.addAdjacencyEdge("b","d");
		dg.addAdjacencyEdge("e","d");
		dg.addAdjacencyEdge("e","f");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList1.add(dg.firstNodeWithLabel("a"));
		nodeList1.add(dg.firstNodeWithLabel("b"));
		nodeList1.add(dg.firstNodeWithLabel("c"));
		nodeList1.add(dg.firstNodeWithLabel("d"));
		nodeList1.add(dg.firstNodeWithLabel("e"));
		nodeList1.add(dg.firstNodeWithLabel("f"));
		nodeList2 = dg.findNodesConnectedToNode(n1,nodeList1);
		if(nodeList2.size() != 6) {outln("Test 15.4.14 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.22 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("f"))) {outln("Test 15.4.23 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","c");
		dg.addAdjacencyEdge("a","c");
		dg.addAdjacencyEdge("e","d");
		dg.addAdjacencyEdge("e","f");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList1.add(dg.firstNodeWithLabel("a"));
		nodeList1.add(dg.firstNodeWithLabel("b"));
		nodeList1.add(dg.firstNodeWithLabel("c"));
		nodeList1.add(dg.firstNodeWithLabel("d"));
		nodeList1.add(dg.firstNodeWithLabel("e"));
		nodeList1.add(dg.firstNodeWithLabel("f"));
		nodeList2 = dg.findNodesConnectedToNode(n1,nodeList1);
		if(nodeList2.size() != 3) {outln("Test 15.4.24 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.25 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.4.26 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("c"))) {outln("Test 15.4.27 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","ab");
		n1 = dg.firstNodeWithLabel("a");
		nodeList1 = new ArrayList<Node>();
		nodeList1.add(dg.firstNodeWithLabel("a"));
		nodeList1.add(dg.firstNodeWithLabel("ab"));
		nodeList2 = dg.findNodesConnectedToNode(n1,nodeList1);
		if(nodeList2.size() != 2) {outln("Test 15.4.28 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.4.29 Fail");}
		if(!nodeList2.contains(dg.firstNodeWithLabel("ab"))) {outln("Test 15.4.30 Fail");}
		
		
		
		dg = new DualGraph();
		nLL1 = dg.findConnectedSubgraphInc("");
		if(nLL1.size() != 0) {outln("Test 15.5.1 Fail");}
		nLL1 = dg.findConnectedSubgraphInc("a");
		if(nLL1.size() != 0) {outln("Test 15.5.2 Fail");}
		
		dg = new DualGraph();
		dg.addNode(new Node("a"));
		nLL1 = dg.findConnectedSubgraphInc("");
		if(nLL1.size() != 1) {outln("Test 15.5.3 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 1) {outln("Test 15.5.4 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.5.5 Fail");}
		nLL1 = dg.findConnectedSubgraphInc("a");
		if(nLL1.size() != 1) {outln("Test 15.5.6 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 1) {outln("Test 15.5.7 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.5.8 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		nLL1 = dg.findConnectedSubgraphInc("");
		if(nLL1.size() != 1) {outln("Test 15.5.9 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 2) {outln("Test 15.5.10 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.5.11 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.5.12 Fail");}
		nLL1 = dg.findConnectedSubgraphInc("a");
		if(nLL1.size() != 1) {outln("Test 15.5.13 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 1) {outln("Test 15.5.14 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.5.15 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","ab");
		nLL1 = dg.findConnectedSubgraphInc("");
		if(nLL1.size() != 1) {outln("Test 15.5.16 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 2) {outln("Test 15.5.17 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.5.18 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("ab"))) {outln("Test 15.5.19 Fail");}
		nLL1 = dg.findConnectedSubgraphInc("a");
		if(nLL1.size() != 1) {outln("Test 15.5.20 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 2) {outln("Test 15.5.21 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.5.22 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("ab"))) {outln("Test 15.5.23 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","ab");
		nLL1 = dg.findConnectedSubgraphInc("");
		if(nLL1.size() != 1) {outln("Test 15.5.24 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 3) {outln("Test 15.5.25 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.5.26 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.5.27 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("ab"))) {outln("Test 15.5.28 Fail");}
		nLL1 = dg.findConnectedSubgraphInc("a");
		if(nLL1.size() != 2) {outln("Test 15.5.29 Fail");}
		for(ArrayList<Node> nl : nLL1) {
			if(nl.contains(dg.firstNodeWithLabel("a"))) {
				if(nl.size() != 1) {outln("Test 15.5.30 Fail");}
			} else if(nl.contains(dg.firstNodeWithLabel("ab"))) {
				if(nl.size() != 1) {outln("Test 15.5.31 Fail");}
			} else { 
				outln("Test 15.5.32 Fail");
			}
		}
		nLL1 = dg.findConnectedSubgraphInc("b");
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 2) {outln("Test 15.5.33 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.5.34 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("ab"))) {outln("Test 15.5.35 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","ab");
		dg.addAdjacencyEdge("ac","ab");
		dg.addAdjacencyEdge("ac","abc");
		dg.addAdjacencyEdge("ab","abc");
		dg.addAdjacencyEdge("a","az");
		nLL1 = dg.findConnectedSubgraphInc("");
		if(nLL1.size() != 1) {outln("Test 15.5.36 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 6) {outln("Test 15.5.37 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.5.38 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("az"))) {outln("Test 15.5.39 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("abc"))) {outln("Test 15.5.40 Fail");}
		nLL1 = dg.findConnectedSubgraphInc("a");
		if(nLL1.size() != 2) {outln("Test 15.5.41 Fail");}
		for(ArrayList<Node> nl : nLL1) {
			if(nl.contains(dg.firstNodeWithLabel("a"))) {
				if(nl.size() != 2) {outln("Test 15.5.42 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("az"))) {outln("Test 15.5.43 Fail");}
			} else if(nl.contains(dg.firstNodeWithLabel("ab"))) {
				if(nl.size() != 3) {outln("Test 15.5.44 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("ac"))) {outln("Test 15.5.45 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("abc"))) {outln("Test 15.5.46 Fail");}
			} else {
				outln("Test 15.5.47 Fail");
			}
		}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","ab");
		dg.addAdjacencyEdge("ac","ab");
		dg.addAdjacencyEdge("ac","abc");
		dg.addAdjacencyEdge("ab","abc");
		dg.addAdjacencyEdge("a","az");
		dg.addAdjacencyEdge("q","d");
		dg.addAdjacencyEdge("q","ad");
		nLL1 = dg.findConnectedSubgraphInc("");
		if(nLL1.size() != 2) {outln("Test 15.5.48 Fail");}
		for(ArrayList<Node> nl : nLL1) {
			if(nl.contains(dg.firstNodeWithLabel("a"))) {
				if(nl.size() != 6) {outln("Test 15.5.49 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("abc"))) {outln("Test 15.5.50 Fail");}
			} else if(nl.contains(dg.firstNodeWithLabel("q"))) {
				if(nl.size() != 3) {outln("Test 15.5.51 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("d"))) {outln("Test 15.5.52 Fail");}
			} else {
				outln("Test 15.5.53 Fail");
			}
		}
		nLL1 = dg.findConnectedSubgraphInc("a");
		if(nLL1.size() != 3) {outln("Test 15.5.54 Fail");}
		for(ArrayList<Node> nl : nLL1) {
			if(nl.contains(dg.firstNodeWithLabel("a"))) {
				if(nl.size() != 2) {outln("Test 15.5.55 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("az"))) {outln("Test 15.5.56 Fail");}
			} else if(nl.contains(dg.firstNodeWithLabel("ab"))) {
				if(nl.size() != 3) {outln("Test 15.5.57 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("ac"))) {outln("Test 15.5.58 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("abc"))) {outln("Test 15.5.59 Fail");}
			} else if(nl.contains(dg.firstNodeWithLabel("ad"))) {
				if(nl.size() != 1) {outln("Test 15.5.60 Fail");}
			} else {
				outln("Test 15.5.61 Fail");
			}
		}
		
		

		dg = new DualGraph();
		nLL1 = dg.findConnectedSubgraphExc("");
		if(nLL1.size() != 0) {outln("Test 15.6.1 Fail");}
		nLL1 = dg.findConnectedSubgraphExc("a");
		if(nLL1.size() != 0) {outln("Test 15.6.2 Fail");}
		
		dg = new DualGraph();
		dg.addNode(new Node("a"));
		nLL1 = dg.findConnectedSubgraphExc("");
		if(nLL1.size() != 0) {outln("Test 15.6.3 Fail");}
		nLL1 = dg.findConnectedSubgraphExc("a");
		if(nLL1.size() != 0) {outln("Test 15.6.4 Fail");}
		nLL1 = dg.findConnectedSubgraphExc("b");
		if(nLL1.size() != 1) {outln("Test 15.6.5 Fail");}
		nodeList1 = nLL1.get(0);
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.6.6 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		nLL1 = dg.findConnectedSubgraphExc("");
		if(nLL1.size() != 0) {outln("Test 15.6.7 Fail");}
		nLL1 = dg.findConnectedSubgraphExc("a");
		if(nLL1.size() != 1) {outln("Test 15.6.8 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 1) {outln("Test 15.6.9 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.6.10 Fail");}
		nLL1 = dg.findConnectedSubgraphExc("b");
		if(nLL1.size() != 1) {outln("Test 15.6.11 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 1) {outln("Test 15.6.12 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.6.13 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","ab");
		nLL1 = dg.findConnectedSubgraphExc("");
		if(nLL1.size() != 0) {outln("Test 15.6.14 Fail");}
		nLL1 = dg.findConnectedSubgraphExc("c");
		if(nLL1.size() != 1) {outln("Test 15.6.15 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 2) {outln("Test 15.6.16 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.6.17 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("ab"))) {outln("Test 15.6.18 Fail");}
		nLL1 = dg.findConnectedSubgraphExc("a");
		if(nLL1.size() != 0) {outln("Test 15.6.19 Fail");}
		nLL1 = dg.findConnectedSubgraphExc("b");
		if(nLL1.size() != 1) {outln("Test 15.6.20 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 1) {outln("Test 15.6.21 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.6.22 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","ac");
		nLL1 = dg.findConnectedSubgraphExc("");
		if(nLL1.size() != 0) {outln("Test 15.6.23 Fail");}
		nLL1 = dg.findConnectedSubgraphExc("b");
		if(nLL1.size() != 2) {outln("Test 15.6.24 Fail");}
		for(ArrayList<Node> nl : nLL1) {
			if(nl.contains(dg.firstNodeWithLabel("a"))) {
				if(nl.size() != 1) {outln("Test 15.6.25 Fail");}
			} else if(nl.contains(dg.firstNodeWithLabel("ac"))) {
				if(nl.size() != 1) {outln("Test 15.6.26 Fail");}
			} else { 
				outln("Test 15.6.27 Fail");
			}
		}
		nLL1 = dg.findConnectedSubgraphExc("a");
		if(nLL1.size() != 1) {outln("Test 15.6.28 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 1) {outln("Test 15.6.29 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.6.30 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("x","y");
		dg.addAdjacencyEdge("y","a");
		dg.addAdjacencyEdge("a","m");
		dg.addAdjacencyEdge("m","n");
		dg.addAdjacencyEdge("m","q");
		dg.addAdjacencyEdge("n","m");
		nLL1 = dg.findConnectedSubgraphExc("v");
		if(nLL1.size() != 1) {outln("Test 15.6.30 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 6) {outln("Test 15.6.31 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("a"))) {outln("Test 15.6.32 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("x"))) {outln("Test 15.6.33 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("q"))) {outln("Test 15.6.44 Fail");}
		nLL1 = dg.findConnectedSubgraphExc("a");
		if(nLL1.size() != 2) {outln("Test 15.6.35 Fail");}
		for(ArrayList<Node> nl : nLL1) {
			if(nl.contains(dg.firstNodeWithLabel("x"))) {
				if(nl.size() != 2) {outln("Test 15.6.36 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("y"))) {outln("Test 15.6.37 Fail");}
			} else if(nl.contains(dg.firstNodeWithLabel("q"))) {
				if(nl.size() != 3) {outln("Test 15.6.38 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("m"))) {outln("Test 15.6.39 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("n"))) {outln("Test 15.6.40 Fail");}
			} else {
				outln("Test 15.6.41 Fail");
			}
		}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("x","y");
		dg.addAdjacencyEdge("y","a");
		dg.addAdjacencyEdge("a","m");
		dg.addAdjacencyEdge("m","n");
		dg.addAdjacencyEdge("m","q");
		dg.addAdjacencyEdge("n","m");
		dg.addAdjacencyEdge("s","r");
		nLL1 = dg.findConnectedSubgraphExc("v");
		if(nLL1.size() != 2) {outln("Test 15.6.41 Fail");}
		for(ArrayList<Node> nl : nLL1) {
			if(nl.contains(dg.firstNodeWithLabel("x"))) {
				if(nl.size() != 6) {outln("Test 15.6.42 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("m"))) {outln("Test 15.6.43 Fail");}
			} else if(nl.contains(dg.firstNodeWithLabel("s"))) {
				if(nl.size() != 2) {outln("Test 15.6.44 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("r"))) {outln("Test 15.6.45 Fail");}
			} else {
				outln("Test 15.6.46 Fail");
			}
		}
		nLL1 = dg.findConnectedSubgraphExc("a");
		if(nLL1.size() != 3) {outln("Test 15.6.47 Fail");}
		for(ArrayList<Node> nl : nLL1) {
			if(nl.contains(dg.firstNodeWithLabel("x"))) {
				if(nl.size() != 2) {outln("Test 15.6.48 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("y"))) {outln("Test 15.6.49 Fail");}
			} else if(nl.contains(dg.firstNodeWithLabel("q"))) {
				if(nl.size() != 3) {outln("Test 15.6.50 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("m"))) {outln("Test 15.6.51 Fail");}
				if(!nl.contains(dg.firstNodeWithLabel("n"))) {outln("Test 15.6.52 Fail");}
			} else if(nl.contains(dg.firstNodeWithLabel("r"))) {
					if(nl.size() != 2) {outln("Test 15.6.53 Fail");}
					if(!nl.contains(dg.firstNodeWithLabel("s"))) {outln("Test 15.6.54 Fail");}
			} else {
				outln("Test 15.6.55 Fail");
			}
		}
		
		dg = new DualGraph();
		if(dg.findArticulationNode() != null) {outln("Test 15.7.1 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		if(dg.findArticulationNode() != null) {outln("Test 15.7.2 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("c","d");
		if(dg.findArticulationNode() != null) {outln("Test 15.7.3 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","c");
		dg.addAdjacencyEdge("a","c");
		if(dg.findArticulationNode() != null) {outln("Test 15.7.3 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","c");
		dg.addAdjacencyEdge("a","c");
		dg.addAdjacencyEdge("a","d");
		dg.addAdjacencyEdge("d","e");
		dg.addAdjacencyEdge("a","e");
		n1 = dg.firstNodeWithLabel("a");
		if(dg.findArticulationNode() != n1) {outln("Test 15.7.4 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","c");
		dg.addAdjacencyEdge("a","c");
		dg.addAdjacencyEdge("a","z");
		dg.addAdjacencyEdge("z","d");
		dg.addAdjacencyEdge("d","e");
		dg.addAdjacencyEdge("z","e");
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("z");
		if(dg.findArticulationNode() != n1 && dg.findArticulationNode() != n2) {outln("Test 15.7.5 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","c");
		n1 = dg.firstNodeWithLabel("b");
		if(dg.findArticulationNode() != n1) {outln("Test 15.7.6 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("b","c");
		dg.addAdjacencyEdge("c","d");
		n1 = dg.firstNodeWithLabel("b");
		n2 = dg.firstNodeWithLabel("c");
		if(dg.findArticulationNode() != n1 && dg.findArticulationNode() != n2) {outln("Test 15.7.7 Fail");}
		
		
		dg = new DualGraph();
		nLL1 = dg.findDisconnectedSubGraphs(null);
		if(nLL1.size() != 0) {outln("Test 15.8.1 Fail");}
		
		dg = new DualGraph();
		dg.addNode(new Node("a"));
		nLL1 = dg.findDisconnectedSubGraphs(null);
		if(nLL1.size() != 1) {outln("Test 15.8.2 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 1) {outln("Test 15.8.3 Fail");}
		n1 = dg.firstNodeWithLabel("a");
		if(!nodeList1.contains(n1)) {outln("Test 15.8.4 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		nLL1 = dg.findDisconnectedSubGraphs(null);
		if(nLL1.size() != 1) {outln("Test 15.8.5 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 2) {outln("Test 15.8.6 Fail");}
		n1 = dg.firstNodeWithLabel("a");
		n2 = dg.firstNodeWithLabel("b");
		if(!nodeList1.contains(n1) || !nodeList1.contains(n2)) {outln("Test 15.8.7 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("c","d");
		nLL1 = dg.findDisconnectedSubGraphs(null);
		if(nLL1.size() != 2) {outln("Test 15.8.8 Fail");}
		for(ArrayList<Node> nL : nLL1) {
			if(nL.contains(dg.firstNodeWithLabel("a"))) {
				if(nL.size() != 2) {outln("Test 15.8.9 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.8.10 Fail");}
			} else if(nL.contains(dg.firstNodeWithLabel("c"))) {
				if(nL.size() != 2) {outln("Test 15.8.11 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("d"))) {outln("Test 15.8.12 Fail");}
			} else {
				outln("Test 15.8.13 Fail");
			}
		}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("c","d");
		dg.addAdjacencyEdge("x","y");
		dg.addAdjacencyEdge("x","z");
		dg.addAdjacencyEdge("z","y");
		nLL1 = dg.findDisconnectedSubGraphs(null);
		if(nLL1.size() != 3) {outln("Test 15.8.14 Fail");}
		for(ArrayList<Node> nL : nLL1) {
			if(nL.contains(dg.firstNodeWithLabel("a"))) {
				if(nL.size() != 2) {outln("Test 15.8.15 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.8.16 Fail");}
			} else if(nL.contains(dg.firstNodeWithLabel("c"))) {
				if(nL.size() != 2) {outln("Test 15.8.11 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("d"))) {outln("Test 15.8.17 Fail");}
			} else if(nL.contains(dg.firstNodeWithLabel("z"))) {
				if(nL.size() != 3) {outln("Test 15.8.11 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("x"))) {outln("Test 15.8.18 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("y"))) {outln("Test 15.8.19 Fail");}
			} else {
				outln("Test 15.8.20 Fail");
			}
		}
		
		dg = new DualGraph();
		dg.addNode(new Node("a"));
		nLL1 = dg.findDisconnectedSubGraphs(dg.firstNodeWithLabel("a"));
		if(nLL1.size() != 0) {outln("Test 15.8.20 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		nLL1 = dg.findDisconnectedSubGraphs(dg.firstNodeWithLabel("a"));
		if(nLL1.size() != 1) {outln("Test 15.8.21 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 1) {outln("Test 15.8.22 Fail");}
		n1 = dg.firstNodeWithLabel("b");
		if(!nodeList1.contains(n1)) {outln("Test 15.8.23 Fail");}

		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("c","b");
		dg.addAdjacencyEdge("c","d");
		dg.addAdjacencyEdge("b","d");
		nLL1 = dg.findDisconnectedSubGraphs(dg.firstNodeWithLabel("a"));
		if(nLL1.size() != 1) {outln("Test 15.8.24 Fail");}
		nodeList1 = nLL1.get(0);
		if(nodeList1.size() != 3) {outln("Test 15.8.25 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.8.26 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("c"))) {outln("Test 15.8.27 Fail");}
		if(!nodeList1.contains(dg.firstNodeWithLabel("d"))) {outln("Test 15.8.28 Fail");}
		
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("c","d");
		dg.addAdjacencyEdge("a","z");
		dg.addAdjacencyEdge("d","z");
		nLL1 = dg.findDisconnectedSubGraphs(dg.firstNodeWithLabel("z"));
		if(nLL1.size() != 2) {outln("Test 15.8.29 Fail");}
		for(ArrayList<Node> nL : nLL1) {
			if(nL.contains(dg.firstNodeWithLabel("a"))) {
				if(nL.size() != 2) {outln("Test 15.8.30 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.8.31 Fail");}
			} else if(nL.contains(dg.firstNodeWithLabel("c"))) {
				if(nL.size() != 2) {outln("Test 15.8.32 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("d"))) {outln("Test 15.8.33 Fail");}
			} else {
				outln("Test 15.8.34 Fail");
			}
		}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("a","b");
		dg.addAdjacencyEdge("c","d");
		dg.addAdjacencyEdge("x","y");
		dg.addAdjacencyEdge("x","z");
		dg.addAdjacencyEdge("z","y");
		dg.addAdjacencyEdge("a","q");
		dg.addAdjacencyEdge("d","q");
		dg.addAdjacencyEdge("y","q");
		nLL1 = dg.findDisconnectedSubGraphs(dg.firstNodeWithLabel("q"));
		if(nLL1.size() != 3) {outln("Test 15.8.35 Fail");}
		for(ArrayList<Node> nL : nLL1) {
			if(nL.contains(dg.firstNodeWithLabel("a"))) {
				if(nL.size() != 2) {outln("Test 15.8.36 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("b"))) {outln("Test 15.8.37 Fail");}
			} else if(nL.contains(dg.firstNodeWithLabel("c"))) {
				if(nL.size() != 2) {outln("Test 15.8.38 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("d"))) {outln("Test 15.8.39 Fail");}
			} else if(nL.contains(dg.firstNodeWithLabel("z"))) {
				if(nL.size() != 3) {outln("Test 15.8.40 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("x"))) {outln("Test 15.8.41 Fail");}
				if(!nL.contains(dg.firstNodeWithLabel("y"))) {outln("Test 15.8.42 Fail");}
			} else {
				outln("Test 15.8.43 Fail");
			}
		}
		
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("ac", "ab");
		dg.addAdjacencyEdge("ab", "abc");
		dg.connectDisconnectedComponents();
		n1 = dg.firstNodeWithLabel("ac");
		n2 = dg.firstNodeWithLabel("abc");
		if(!n1.connectingNodes().contains(n2)) {outln("Test 15.9.1 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("ab", "ax");
		dg.addAdjacencyEdge("ab", "ay");
		dg.addAdjacencyEdge("ab", "bzq");
		dg.addAdjacencyEdge("ac", "aq");
		dg.addAdjacencyEdge("ac", "ay");
		dg.addAdjacencyEdge("ac", "bzq");
		dg.addAdjacencyEdge("adq", "ax");
		dg.addAdjacencyEdge("adq", "ay");
		dg.connectDisconnectedComponents();
		n1 = dg.firstNodeWithLabel("bzq");
		n2 = dg.firstNodeWithLabel("adq");
		if(n1.connectingNodes().contains(n2)) {outln("Test 15.9.2 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("ab", "ax");
		dg.addAdjacencyEdge("ab", "ay");
		dg.addAdjacencyEdge("ab", "bzq");
		dg.addAdjacencyEdge("ac", "aq");
		dg.addAdjacencyEdge("ac", "ay");
		dg.addAdjacencyEdge("ac", "bzq");
		dg.addAdjacencyEdge("adq", "ax");
		dg.addAdjacencyEdge("adq", "ay");
		dg.addAdjacencyEdge("adq", "br");
		dg.connectDisconnectedComponents();
		n1 = dg.firstNodeWithLabel("bzq");
		n2 = dg.firstNodeWithLabel("adq");
		if(n1.connectingNodes().contains(n2)) {outln("Test 15.9.3 Fail");}
		n1 = dg.firstNodeWithLabel("ab");
		n2 = dg.firstNodeWithLabel("br");
		if(!n1.connectingNodes().contains(n2)) {outln("Test 15.9.4 Fail");}
		dg = new DualGraph();
		n1 = new Node("");
		n2 = new Node("a");
		dg.addNode(n1);
		dg.addNode(n2);
		dg.addEdge(new Edge(n1, n2));
		dg.addAdjacencyEdge("a", "ab");
		dg.addAdjacencyEdge("ab", "abc");
		dg.addAdjacencyEdge("abc", "bc");
		dg.connectDisconnectedComponents();
		n2 = dg.firstNodeWithLabel("bc");
		if(!n1.connectingNodes().contains(n2)) {outln("Test 15.9.5 Fail");}
		
		dg = new DualGraph();
		dg.addAdjacencyEdge("e", "abce");
		dg.addAdjacencyEdge("e", "ace");
		dg.addAdjacencyEdge("e", "bd");
		dg.addAdjacencyEdge("bce", "abce");
		dg.addAdjacencyEdge("bce", "ace");
		dg.addAdjacencyEdge("bce", "bd");
		dg.addAdjacencyEdge("ac", "abce");
		dg.addAdjacencyEdge("ac", "ace");
		dg.connectDisconnectedComponents();
		n1 = dg.firstNodeContainingLabel("ac");
		n2 = dg.firstNodeContainingLabel("bd");
		if(n1.connectingNodes().contains(n2)) {outln("Test 15.9.6 Fail");}
		
		
		dg = new DualGraph();
		if(dg.findNewChar('a') != 'A') {outln("Test 15.10.1 Fail");}
		
		dg = new DualGraph();
		if(dg.findNewChar('c') != 'C') {outln("Test 15.10.2 Fail");}
		
		dg = new DualGraph();
		dg.setContourHoleLabelMap(new HashMap<String, String>());
		if(dg.findNewChar('a') != 'A') {outln("Test 15.10.3 Fail");}
		
		dg = new DualGraph();
		dg.setContourDuplicateLabelMap(new HashMap<String, String>());
		if(dg.findNewChar('a') != 'A') {outln("Test 15.10.4 Fail");}
		
		dg = new DualGraph();
		dg.setContourDuplicateLabelMap(new HashMap<String, String>());
		dg.setContourHoleLabelMap(new HashMap<String, String>());
		if(dg.findNewChar('Q') != 'A') {outln("Test 15.10.5 Fail");}
		
		dg = new DualGraph();
		dg.setContourDuplicateLabelMap(new HashMap<String, String>());
		dg.setContourHoleLabelMap(new HashMap<String, String>());
		if(dg.findNewChar('!') != 'A') {outln("Test 15.10.6 Fail");}
		
		dg = new DualGraph();
		dg.setContourDuplicateLabelMap(new HashMap<String, String>());
		dg.setContourHoleLabelMap(new HashMap<String, String>());
		if(dg.findNewChar('z') != 'Z') {outln("Test 15.10.7 Fail");}
		
		dg = new DualGraph();
		dg.setContourDuplicateLabelMap(new HashMap<String, String>());
		dg.getContourDuplicateLabelMap().put("A", "a");
		dg.setContourHoleLabelMap(new HashMap<String, String>());
		dg.getContourHoleLabelMap().put("B", "a");
		if(dg.findNewChar('a') != 'C') {outln("Test 15.10.8 Fail");}
		
		dg = new DualGraph();
		dg.setContourDuplicateLabelMap(new HashMap<String, String>());
		dg.getContourDuplicateLabelMap().put("A", "a");
		dg.setContourHoleLabelMap(new HashMap<String, String>());
		dg.getContourHoleLabelMap().put("B", "a");
		if(dg.findNewChar('c') != 'C') {outln("Test 15.10.9 Fail");}

		dg = new DualGraph();
		dg.setContourDuplicateLabelMap(new HashMap<String, String>());
		dg.getContourDuplicateLabelMap().put("A", "a");
		dg.setContourHoleLabelMap(new HashMap<String, String>());
		dg.getContourHoleLabelMap().put("D", "a");
		if(dg.findNewChar('b') != 'B') {outln("Test 15.10.10 Fail");}

		dg = new DualGraph();
		dg.setContourDuplicateLabelMap(new HashMap<String, String>());
		dg.getContourDuplicateLabelMap().put("D", "a");
		dg.setContourHoleLabelMap(new HashMap<String, String>());
		dg.getContourHoleLabelMap().put("A", "a");
		if(dg.findNewChar('a') != 'B') {outln("Test 15.10.11 Fail");}

		dg = new DualGraph();
		dg.setContourDuplicateLabelMap(new HashMap<String, String>());
		for(int i = 0; i< 40; i++) {
			char c = (char)(i+(int)'A');
			dg.getContourDuplicateLabelMap().put(""+c, "a");
		}
		if(dg.findNewChar('a') != '{') {outln("Test 15.10.12 Fail");}

		dg = new DualGraph();
		dg.setContourDuplicateLabelMap(new HashMap<String, String>());
		for(int i = 0; i< 40; i++) {
			char c = (char)(i+(int)'A');
			dg.getContourDuplicateLabelMap().put(""+c, "a");
		}

		dg.setContourHoleLabelMap(new HashMap<String, String>());
		dg.getContourHoleLabelMap().put("{", "b");
		if(dg.findNewChar('d') != '|') {outln("Test 15.10.13 Fail");}


		out("Ending method test 15");
		
	}
	
	
	
	
	protected static void test16(){
		outln(" | Starting method test 16");
		ArrayList<ConcreteContour> contours = new ArrayList<ConcreteContour>();
		Polygon pol1 = new Polygon();
		Node nA1, nA2;
		Edge eA;
		

		
		pol1.addPoint(10, 10);
		pol1.addPoint(150, 10);
		pol1.addPoint(150, 150);
		pol1.addPoint(5,150);		
		ConcreteContour cc1 = new ConcreteContour("a", pol1);
		contours.add(cc1);
		
		Polygon pol2 = new Polygon();
		pol2.addPoint(50, 50);		
		pol2.addPoint(200,50);
		pol2.addPoint(200,200);
		pol2.addPoint(50, 200);
		ConcreteContour cc2 = new ConcreteContour("b",pol2);
		contours.add(cc2);	
		
		Polygon pol3 = new Polygon();
		pol3.addPoint(100, 100);
		pol3.addPoint(300, 100);
		pol3.addPoint(300,300);
		pol3.addPoint(100, 300);		
		ConcreteContour cc3 = new ConcreteContour("c",pol3);			
		contours.add(cc3);
		
		
		Polygon pol4 = new Polygon();
		pol4.addPoint(300,20);
		pol4.addPoint(450, 20);
		pol4.addPoint(450,250);	
		ConcreteContour cc4 = new ConcreteContour("d", pol4);
		contours.add(cc4);
		
		Polygon pol5 = new Polygon();
		pol5.addPoint(300,20);
		pol5.addPoint(450, 20);
		pol5.addPoint(350,250);
		pol5.translate(100, 100);
		ConcreteContour cc5 = new ConcreteContour("e", pol5);
		contours.add(cc5);
	
		Polygon pol6 = new Polygon();
		pol6.addPoint(10, 10);
		pol6.addPoint(110, 10);
		pol6.addPoint(50, 50);
		pol6.translate(350, 350);
	
		ConcreteContour cc6 = new ConcreteContour("f", pol6);
		contours.add(cc6);
		
		Polygon pol7 = new Polygon();
		pol7.addPoint(120, 10);		
		pol7.addPoint(220,10);
		pol7.addPoint(160,50);
		pol7.translate(350,350);
		ConcreteContour cc7 = new ConcreteContour("g",pol7);
		contours.add(cc7);	
		
		Polygon pol8 = new Polygon();
		pol8.addPoint(0, 30);
		pol8.addPoint(250, 30);
		pol8.addPoint(115,100);
		pol8.translate(350,350);	
		ConcreteContour cc8 = new ConcreteContour("h",pol8);			
		contours.add(cc8);
		
		DualGraph dg = ConstructedConcreteDiagram.generateEulerGraph(contours);
		if(dg.firstNodeAtPoint(new Point(150,50))== null)
		outln("Test 16.1.1 Fail");
		if(dg.firstNodeAtPoint(new Point(150,100))== null)
		outln("Test 16.1.2 Fail");	
		if(dg.firstNodeAtPoint(new Point(50,150))== null)
		outln("Test 16.1.3 Fail");
		if(dg.firstNodeAtPoint(new Point(100,150))== null)
		outln("Test 16.1.4 Fail");	
		if(dg.firstNodeAtPoint(new Point(200,100))== null)
		outln("Test 16.1.5 Fail");
		if(dg.firstNodeAtPoint(new Point(100,200))== null)
		outln("Test 16.1.6 Fail");			
		if(dg.firstNodeAtPoint(new Point(450,120))== null)
		outln("Test 16.1.7 Fail");
		if(dg.firstNodeAtPoint(new Point(417,200))== null)
		outln("Test 16.1.8 Fail");			
		if(dg.firstNodeAtPoint(new Point(430,380))== null)
		outln("Test 16.1.9 Fail");
		if(dg.firstNodeAtPoint(new Point(380,380))== null)
		outln("Test 16.1.10 Fail");			
		if(dg.firstNodeAtPoint(new Point(540,380))== null)
		outln("Test 16.1.11 Fail");
		if(dg.firstNodeAtPoint(new Point(490,380))== null)
		outln("Test 16.1.12 Fail");			
											
		for(Edge e : dg.getEdges()){
			Node n1 = e.getFrom();
			Node n2 = e.getTo();
			if(n1.getLabel().compareTo("ab")==0 && n2.getLabel().compareTo("ab")==0){
				if(e.getBends().size()!=1&&e.getBends().size()!=3){
					outln("Test 16.2.1 Fail");
					if(e.getBends().size()==1){
						if(e.getBends().get(0).getX()!=50 ||e.getBends().get(0).getY()!=50){
							outln("Test 16.2.2 Fail");
						}
					}
					if(e.getBends().size()==3){
						if(e.getBends().get(0).getX()!=5 ||e.getBends().get(0).getY()!=150){
							outln("Test 16.2.3 Fail");
						}
						if(e.getBends().get(1).getX()!=10 ||e.getBends().get(1).getY()!=10){
							outln("Test 16.2.4 Fail");
						}
						if(e.getBends().get(2).getX()!=150 ||e.getBends().get(2).getY()!=10){
							outln("Test 16.2.5 Fail");
						}
					}
				}
			
			}
			if(n1.getLabel().compareTo("bc")==0 && n2.getLabel().compareTo("bc")==0){
				if(e.getBends().size()!=1&&e.getBends().size()!=3){
					outln("Test 16.3.1 Fail");
					if(e.getBends().size()==1){
						if(e.getBends().get(0).getX()!=200 ||e.getBends().get(0).getY()!=200){
							outln("Test 16.3.2 Fail");
						}
					}
					if(e.getBends().size()==3){
						if(e.getBends().get(0).getX()!=300 ||e.getBends().get(0).getY()!=100){
							outln("Test 16.3.3 Fail");
						}
						if(e.getBends().get(1).getX()!=300 ||e.getBends().get(1).getY()!=300){
							outln("Test 16.3.4 Fail");
						}
						if(e.getBends().get(2).getX()!=100 ||e.getBends().get(2).getY()!=300){
							outln("Test 16.3.5 Fail");
						}
					}
				}
			
			}
			if(n1.getLabel().compareTo("ac")==0 && n2.getLabel().compareTo("ac")==0){
				if(e.getBends().size()!=1){
					outln("Test 16.4.1 Fail");
				}
				if((e.getBends().get(0).getX()!= 150 &&  e.getBends().get(0).getX()!= 100)
						|| ( e.getBends().get(0).getY()!=150&&e.getBends().get(0).getY()!=100) ){
							outln("Test 16.4.2 Fail");
				}			
			}
			if(n1.getLabel().compareTo("ab")==0 && n2.getLabel().compareTo("bc")==0){
				if(e.getBends().size()!=1){
					outln("Test 16.5.1 Fail");
				}
				if(e.getBends().get(0).getX()!=200 ||e.getBends().get(0).getY()!=50){
					outln("Test 16.5.2 Fail");
				}
			}
			if(n1.getLabel().compareTo("bc")==0 && n2.getLabel().compareTo("ab")==0){
				if(e.getBends().size()!=1){
					outln("Test 16.6.1 Fail");
				}
				if(e.getBends().get(0).getX()!=50 ||e.getBends().get(0).getY()!=200){
					outln("Test 16.6.2 Fail");
				}
			}
			if(n1.getLabel().compareTo("ac")==0 && n2.getLabel().compareTo("bc")==0){
				if(e.getBends().size()!=0){
					outln("Test 16.7.1 Fail");
				}				
			}
			
			if(n1.getLabel().compareTo("ab")==0 && n2.getLabel().compareTo("de")==0){
				outln("Test 16.8.1 Fail");						
			}
			if(n1.getLabel().compareTo("ab")==0 && n2.getLabel().compareTo("fh")==0){
				outln("Test 16.8.2 Fail");						
			}
			if(n1.getLabel().compareTo("ab")==0 && n2.getLabel().compareTo("gh")==0){
				outln("Test 16.8.3 Fail");						
			}
			if(n1.getLabel().compareTo("de")==0 && n2.getLabel().compareTo("fh")==0){
				outln("Test 16.8.4 Fail");						
			}
			
			if(n1.getLabel().compareTo("ac")==0 && n2.getLabel().compareTo("de")==0){
				outln("Test 16.8.5 Fail");						
			}
			if(n1.getLabel().compareTo("ac")==0 && n2.getLabel().compareTo("fh")==0){
				outln("Test 16.8.6 Fail");						
			}
			if(n1.getLabel().compareTo("ac")==0 && n2.getLabel().compareTo("gh")==0){
				outln("Test 16.8.7 Fail");						
			}
			if(n1.getLabel().compareTo("dc")==0 && n2.getLabel().compareTo("fh")==0){
				outln("Test 16.8.8 Fail");						
			}
			if(n1.getLabel().compareTo("bc")==0 && n2.getLabel().compareTo("de")==0){
				outln("Test 16.8.1 Fail");						
			}
			if(n1.getLabel().compareTo("bc")==0 && n2.getLabel().compareTo("fh")==0){
				outln("Test 16.8.2 Fail");						
			}
			if(n1.getLabel().compareTo("bc")==0 && n2.getLabel().compareTo("gh")==0){
				outln("Test 16.8.3 Fail");						
			}
			if(n1.getLabel().compareTo("bc")==0 && n2.getLabel().compareTo("fh")==0){
				outln("Test 16.8.4 Fail");						
			}
			if(n1.getLabel().compareTo("de")==0 && n2.getLabel().compareTo("de")==0){
				if(e.getBends().size()!=1&&e.getBends().size()!=2){
					outln("Test 16.9.1 Fail");
				}
				if(e.getBends().size()==1){
					if(e.getBends().get(0).getX()!=450&& e.getBends().get(0).getX()!=400
							||e.getBends().get(0).getY()!=250&&e.getBends().get(0).getY()!=120){
						outln("Test 16.9.2 Fail");
					}
				}
				if(e.getBends().size()==2){
					if(e.getBends().get(0).getX()!=300&& e.getBends().get(0).getX()!=550
							||e.getBends().get(0).getY()!=20&&e.getBends().get(0).getY()!=120){
						outln("Test 16.9.3 Fail");
					}
					if(e.getBends().get(1).getX()!=450&& e.getBends().get(1).getX()!=450
							||e.getBends().get(1).getY()!=20&&e.getBends().get(1).getY()!=350){
						outln("Test 16.9.4 Fail");
					}
				}
				
				if(n1.getLabel().compareTo("gh")==0 && n2.getLabel().compareTo("fh")==0){
					if(e.getBends().size()== 3){
						if(e.getBends().get(0).getX()!= 600 ||e.getBends().get(0).getY()!= 380){
							outln("Test 16.10.1 Fail");
						}
						if(e.getBends().get(1).getX()!= 465 ||e.getBends().get(1).getY()!= 450){
							outln("Test 16.10.2 Fail");
						}
						if(e.getBends().get(2).getX()!= 350 ||e.getBends().get(2).getY()!= 380){
							outln("Test 16.10.3 Fail");
						}
					}				
				}
				if(n1.getLabel().compareTo("fh")==0 && n2.getLabel().compareTo("fh")==0){
					if(e.getBends().size()== 2){
						if(e.getBends().get(0).getX()!= 360 ||e.getBends().get(0).getY()!= 360){
							outln("Test 16.10.4 Fail");
						}
						if(e.getBends().get(1).getX()!= 460 ||e.getBends().get(1).getY()!= 360){
							outln("Test 16.10.5 Fail");
						}			
					}
					if(e.getBends().size()== 1){
						if(e.getBends().get(0).getX()!= 400 ||e.getBends().get(0).getY()!= 400){
							outln("Test 16.10.6 Fail");
						}
					}
				}
				if(n1.getLabel().compareTo("gh")==0 && n2.getLabel().compareTo("gh")==0){
					if(e.getBends().size()== 2){
						if(e.getBends().get(0).getX()!= 470 ||e.getBends().get(0).getY()!= 360){
							outln("Test 16.10.7 Fail");
						}
						if(e.getBends().get(1).getX()!= 570 ||e.getBends().get(1).getY()!= 360){
							outln("Test 16.10.8 Fail");
						}			
					}
					if(e.getBends().size()== 1){
						if(e.getBends().get(0).getX()!= 510 ||e.getBends().get(0).getY()!= 400){
							outln("Test 16.10.9 Fail");
						}
					}
				}
							
				
				
			}						
			
		}		

		nA1= new Node(new Point(100,200));
		nA2 = new Node(new Point(100,300));
		eA = new Edge(nA1,nA2);
		if(!ConstructedConcreteDiagram.edgeCrossLineSegment(new Point(50,250), new Point(150,250), eA)){
			outln("Test 16.11.1 Fail");
		}
		
		nA1= new Node(new Point(100,200));
		nA2 = new Node(new Point(100,300));
		eA = new Edge(nA1,nA2);
		if(ConstructedConcreteDiagram.edgeCrossLineSegment(new Point(50,250), new Point(50,290), eA)){
			outln("Test 16.11.2 Fail");
		}
		
		nA1= new Node(new Point(100,200));
		nA2 = new Node(new Point(100,300));
		eA = new Edge(nA1,nA2);
		eA.addBend(new Point(200,250));
		if(!ConstructedConcreteDiagram.edgeCrossLineSegment(new Point(150,200), new Point(150,250), eA)){
			outln("Test 16.11.3 Fail");
		}
		
		nA1= new Node(new Point(100,200));
		nA2 = new Node(new Point(100,300));
		eA = new Edge(nA1,nA2);
		eA.addBend(new Point(200,250));
		if(ConstructedConcreteDiagram.edgeCrossLineSegment(new Point(150,20), new Point(150,50), eA)){
			outln("Test 16.11.4 Fail");
		}
		
		nA1= new Node(new Point(100,200));
		nA2 = new Node(new Point(100,300));
		eA = new Edge(nA1,nA2);
		eA.addBend(new Point(110,210));
		eA.addBend(new Point(200,250));
		eA.addBend(new Point(110,290));
		if(!ConstructedConcreteDiagram.edgeCrossLineSegment(new Point(150,200), new Point(150,250), eA)){
			outln("Test 16.11.5 Fail");
		}
		
		nA1= new Node(new Point(100,200));
		nA2 = new Node(new Point(100,300));
		eA = new Edge(nA1,nA2);
		eA.addBend(new Point(110,210));
		eA.addBend(new Point(200,250));
		eA.addBend(new Point(110,290));
		if(ConstructedConcreteDiagram.edgeCrossLineSegment(new Point(50,-200), new Point(50,-250), eA)){
			outln("Test 16.11.6 Fail");
		}
		
		
		
		dg = new DualGraph();
		if(UnremovableContoursSearch.unremovableContourTest(dg,"c")) {outln("Test 16.12.1 Fail");}
		
		dg = new DualGraph();
		dg.addNode(new Node("ab"));
		dg.addNode(new Node("a"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ab"),dg.firstNodeWithLabel("a")));
		if(UnremovableContoursSearch.unremovableContourTest(dg,"a")) {outln("Test 16.12.2 Fail");}
		if(UnremovableContoursSearch.unremovableContourTest(dg,"b")) {outln("Test 16.12.3 Fail");}
		
		dg = new DualGraph();
		dg.addNode(new Node("ab"));
		dg.addNode(new Node("a"));
		if(UnremovableContoursSearch.unremovableContourTest(dg,"a")) {outln("Test 16.12.4 Fail");}
		if(!UnremovableContoursSearch.unremovableContourTest(dg,"b")) {outln("Test 16.12.6 Fail");}
		if(UnremovableContoursSearch.unremovableContourTest(dg,"c")) {outln("Test 16.12.5 Fail");}
		
		dg = new DualGraph();
		dg.addNode(new Node(""));
		dg.addNode(new Node("a"));
		dg.addNode(new Node("b"));
		dg.addNode(new Node("c"));
		dg.addNode(new Node("d"));
		dg.addNode(new Node("ab"));
		dg.addNode(new Node("ac"));
		dg.addNode(new Node("ad"));
		dg.addNode(new Node("bc"));
		dg.addNode(new Node("bd"));
		dg.addNode(new Node("cd"));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("a")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("b")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("c")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("d")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("a"),dg.firstNodeWithLabel("ab")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("a"),dg.firstNodeWithLabel("ad")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("b"),dg.firstNodeWithLabel("ab")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("b"),dg.firstNodeWithLabel("ac")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("c"),dg.firstNodeWithLabel("ac")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("c"),dg.firstNodeWithLabel("bc")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("c"),dg.firstNodeWithLabel("cd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("d"),dg.firstNodeWithLabel("ad")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("d"),dg.firstNodeWithLabel("bd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("d"),dg.firstNodeWithLabel("cd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ab"),dg.firstNodeWithLabel("abc")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ab"),dg.firstNodeWithLabel("abd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ac"),dg.firstNodeWithLabel("abc")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ac"),dg.firstNodeWithLabel("acd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ad"),dg.firstNodeWithLabel("abd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("bc"),dg.firstNodeWithLabel("abc")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("bd"),dg.firstNodeWithLabel("abd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("bd"),dg.firstNodeWithLabel("bcd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("cd"),dg.firstNodeWithLabel("acd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("cd"),dg.firstNodeWithLabel("bcd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("abc"),dg.firstNodeWithLabel("abcd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("abd"),dg.firstNodeWithLabel("abcd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("acd"),dg.firstNodeWithLabel("abcd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("bcd"),dg.firstNodeWithLabel("abcd")));
		if(UnremovableContoursSearch.unremovableContourTest(dg,"a")) {outln("Test 16.12.9 Fail");}
		if(UnremovableContoursSearch.unremovableContourTest(dg,"b")) {outln("Test 16.12.10 Fail");}
		if(!UnremovableContoursSearch.unremovableContourTest(dg,"c")) {outln("Test 16.12.11 Fail");}
		if(!UnremovableContoursSearch.unremovableContourTest(dg,"d")) {outln("Test 16.12.12 Fail");}


		
		out("Ending method test 16");
		
	}
	
	
	
	protected static void test17(){
		outln(" | Starting method test 17");
/*		ArrayList<ConcreteContour> contours = new ArrayList<ConcreteContour>();
			
		Polygon pol1 = new Polygon();
		pol1.addPoint(20, 20);
		pol1.addPoint(300, 20);
		pol1.addPoint(300, 300);
		pol1.addPoint(20,300);		
		ConcreteContour cc1 = new ConcreteContour("a", pol1);
		contours.add(cc1);
			
		Polygon pol2 = new Polygon();
		pol2.addPoint(140, 140);		
		pol2.addPoint(400,140);
		pol2.addPoint(400,400);
		pol2.addPoint(140, 400);
		ConcreteContour cc2 = new ConcreteContour("b",pol2);
		contours.add(cc2);	
			
		Polygon pol3 = new Polygon();
		pol3.addPoint(60, 240);
		pol3.addPoint(240, 240);
		pol3.addPoint(240,450);
		pol3.addPoint(60,450);	
		ConcreteContour cc3 = new ConcreteContour("c",pol3);			
		contours.add(cc3);
			
		Polygon pol9 = new Polygon();
		pol9.addPoint(80, 80);
		pol9.addPoint(200,80);
		pol9.addPoint(200,200);
		
		ConcreteContour cc9 = new ConcreteContour("d",pol9);			
		contours.add(cc9);
			
		Polygon pol10 = new Polygon();
		pol10.addPoint(220, 260);
		pol10.addPoint(280, 260);
		pol10.addPoint(220,350);	
		ConcreteContour cc10 = new ConcreteContour("e",pol10);			
		contours.add(cc10);
		
		
		ConstructedConcreteDiagram ccd = new ConstructedConcreteDiagram();
		DualGraph dg1 = ccd.getStraightLineDualOfGraph(contours);
		DualGraph dg2 = dg1.clone();
		dg2.removeNode("");
		dg2.allPossibleCycles();

		ArrayList<String> nodeList = new ArrayList<String>();
		nodeList.add("ac");
		nodeList.add("c");
		nodeList.add("bc");
		nodeList.add("abc");		

		ArrayList<String> nodeList1 = new ArrayList<String>();
		nodeList1.add("a");
		nodeList1.add("ac");
		nodeList1.add("abc");
		nodeList1.add("ab");			
		nodeList1.add("abd");
		nodeList1.add("ad");		

		ArrayList<String> nodeList2 = new ArrayList<String>();
		nodeList2.add("abc");
		nodeList2.add("bc");
		nodeList2.add("bce");
		nodeList2.add("be");			
		nodeList2.add("b");
		nodeList2.add("ab");			
		nodeList2.add("abe");
		nodeList2.add("abce");	
		
		ArrayList<String> nodeList3 = new ArrayList<String>();
		nodeList3.add("a");
		nodeList3.add("ad");
		nodeList3.add("abd");
		nodeList3.add("ab");			
		nodeList3.add("b");
		nodeList3.add("be");			
		nodeList3.add("abe");
		nodeList3.add("abce");	
		nodeList3.add("abc");
		nodeList3.add("ac");
		
		ArrayList<String> nodeList4 = new ArrayList<String>();
		nodeList4.add("a");
		nodeList4.add("ad");
		nodeList4.add("abd");
		nodeList4.add("ab");			
		nodeList4.add("abe");
		nodeList4.add("abce");			
		nodeList4.add("abc");
		nodeList4.add("ac");	
		
		ArrayList<String> nodeList5 = new ArrayList<String>();
		nodeList5.add("abc");
		nodeList5.add("ab");
		nodeList5.add("abe");
		nodeList5.add("be");			
		nodeList5.add("bce");
		nodeList5.add("bc");			
		nodeList5.add("c");
		nodeList5.add("ac");	

		ArrayList<String> nodeList6 = new ArrayList<String>();
		nodeList6.add("ad");
		nodeList6.add("abd");
		nodeList6.add("ab");
		nodeList6.add("abe");			
		nodeList6.add("be");
		nodeList6.add("bce");			
		nodeList6.add("bc");
		nodeList6.add("c");		
		nodeList6.add("ac");
		nodeList6.add("a");	
*/	
/*		ArrayList<Face> faces = dg2.allPossibleCycles();
		for(Face f: faces){
			if(dg2.containsFace(f, nodeList)){
				System.out.println("0. correct!");
			}
			if(dg2.containsFace(f, nodeList1)){
				System.out.println("1. correct!");
			}
			if(dg2.containsFace(f, nodeList2)){
				System.out.println("2. correct!");
			}
			if(dg2.containsFace(f, nodeList3)){
				System.out.println("3. correct!");
			}
			if(dg2.containsFace(f, nodeList4)){
				System.out.println("4. correct!");
			}
			if(dg2.containsFace(f, nodeList5)){
				System.out.println("5. correct!");
			}
			if(dg2.containsFace(f, nodeList6)){
				System.out.println("6. correct!");
			}

		}
*/
		
		
		out("Ending method test 17");

	}
	
	
	protected static void test18() {
		outln("| Starting method test 18");
		
		
		Graph g;
		Node n1,n2,n3;
		Edge e1,e2,e3,e4;
		
		g = new Graph();
		g.addNode(new Node("a",new Point(50,50)));
		g.addNode(new Node("b",new Point(100,50)));
		g.addNode(new Node("c",new Point(100,100)));
		e1 = g.addAdjacencyEdge("a","b");
		e2 = g.addAdjacencyEdge("c","b");
		n1 = g.firstNodeWithLabel("a");
		n2 = g.firstNodeWithLabel("b");
		n3 = g.firstNodeWithLabel("c");
		e3 = HybridGraph.replaceEdges(g,e1,e2,n2);
		if(g.getNodes().size() != 2) {outln("Test 18.1.1 Fail");}
		if(g.getEdges().size() != 1) {outln("Test 18.1.2 Fail");}
		e4 = g.getEdges().get(0);
		if(e4.getFrom() != n1) {outln("Test 18.1.3 Fail");}
		if(e4.getTo() != n3) {outln("Test 18.1.4 Fail");}
		if(e4.getBends().size() != 1) {outln("Test 18.1.5 Fail");}
		if(!e4.getBends().get(0).equals(new Point(100,50))) {outln("Test 18.1.6 Fail");}
		
		g = new Graph();
		g.addNode(new Node("a",new Point(50,50)));
		g.addNode(new Node("b",new Point(100,50)));
		g.addNode(new Node("c",new Point(100,100)));
		e1 = g.addAdjacencyEdge("a","b");
		e2 = g.addAdjacencyEdge("c","b");
		n1 = g.firstNodeWithLabel("a");
		n2 = g.firstNodeWithLabel("b");
		n3 = g.firstNodeWithLabel("c");
		e3 = HybridGraph.replaceEdges(g,e2,e1,n2);
		if(g.getNodes().size() != 2) {outln("Test 18.1.7 Fail");}
		if(g.getEdges().size() != 1) {outln("Test 18.1.8 Fail");}
		e4 = g.getEdges().get(0);
		if(e4.getFrom() != n3) {outln("Test 18.1.9 Fail");}
		if(e4.getTo() != n1) {outln("Test 18.1.10 Fail");}
		if(e4.getBends().size() != 1) {outln("Test 18.1.11 Fail");}
		if(!e4.getBends().get(0).equals(new Point(100,50))) {outln("Test 18.1.12 Fail");}
		
		g = new Graph();
		g.addNode(new Node("a",new Point(50,50)));
		g.addNode(new Node("b",new Point(100,50)));
		g.addNode(new Node("c",new Point(100,100)));
		e1 = g.addAdjacencyEdge("a","b");
		e1.addBend(new Point(60,40));
		e1.addBend(new Point(70,40));
		e2 = g.addAdjacencyEdge("b","c");
		e2.addBend(new Point(110,60));
		e2.addBend(new Point(110,70));
		n1 = g.firstNodeWithLabel("a");
		n2 = g.firstNodeWithLabel("b");
		n3 = g.firstNodeWithLabel("c");
		e3 = HybridGraph.replaceEdges(g,e1,e2,n2);
		if(g.getNodes().size() != 2) {outln("Test 18.1.13 Fail");}
		if(g.getEdges().size() != 1) {outln("Test 18.1.14 Fail");}
		e4 = g.getEdges().get(0);
		if(e4.getFrom() != n1) {outln("Test 18.1.15 Fail");}
		if(e4.getTo() != n3) {outln("Test 18.1.16 Fail");}
		if(e4.getBends().size() != 5) {outln("Test 18.1.17 Fail");}
		if(!e4.getBends().get(0).equals(new Point(60,40))) {outln("Test 18.1.18 Fail");}
		if(!e4.getBends().get(1).equals(new Point(70,40))) {outln("Test 18.1.19 Fail");}
		if(!e4.getBends().get(2).equals(new Point(100,50))) {outln("Test 18.1.20 Fail");}
		if(!e4.getBends().get(3).equals(new Point(110,60))) {outln("Test 18.1.21 Fail");}
		if(!e4.getBends().get(4).equals(new Point(110,70))) {outln("Test 18.1.22 Fail");}
		
		g = new Graph();
		g.addNode(new Node("a",new Point(50,50)));
		g.addNode(new Node("b",new Point(100,50)));
		g.addNode(new Node("c",new Point(100,100)));
		e1 = g.addAdjacencyEdge("a","b");
		e1.addBend(new Point(60,40));
		e1.addBend(new Point(70,40));
		e2 = g.addAdjacencyEdge("b","c");
		e2.addBend(new Point(110,60));
		e2.addBend(new Point(110,70));
		n1 = g.firstNodeWithLabel("a");
		n2 = g.firstNodeWithLabel("b");
		n3 = g.firstNodeWithLabel("c");
		e3 = HybridGraph.replaceEdges(g,e2,e1,n2);
		if(g.getNodes().size() != 2) {outln("Test 18.1.23 Fail");}
		if(g.getEdges().size() != 1) {outln("Test 18.1.24 Fail");}
		e4 = g.getEdges().get(0);
		if(e4.getFrom() != n3) {outln("Test 18.1.25 Fail");}
		if(e4.getTo() != n1) {outln("Test 18.1.26 Fail");}
		if(e4.getBends().size() != 5) {outln("Test 18.1.27 Fail");}
		if(!e4.getBends().get(0).equals(new Point(110,70))) {outln("Test 18.1.28 Fail");}
		if(!e4.getBends().get(1).equals(new Point(110,60))) {outln("Test 18.1.29 Fail");}
		if(!e4.getBends().get(2).equals(new Point(100,50))) {outln("Test 18.1.30 Fail");}
		if(!e4.getBends().get(3).equals(new Point(70,40))) {outln("Test 18.1.31 Fail");}
		if(!e4.getBends().get(4).equals(new Point(60,40))) {outln("Test 18.1.32 Fail");}
		
		g = new Graph();
		g.addNode(new Node("a",new Point(50,50)));
		g.addNode(new Node("b",new Point(100,50)));
		g.addNode(new Node("c",new Point(100,100)));
		e1 = g.addAdjacencyEdge("b","a");
		e1.addBend(new Point(70,40));
		e1.addBend(new Point(60,40));
		e2 = g.addAdjacencyEdge("c","b");
		e2.addBend(new Point(110,70));
		e2.addBend(new Point(110,60));
		n1 = g.firstNodeWithLabel("a");
		n2 = g.firstNodeWithLabel("b");
		n3 = g.firstNodeWithLabel("c");
		e3 = HybridGraph.replaceEdges(g,e1,e2,n2);
		if(g.getNodes().size() != 2) {outln("Test 18.1.43 Fail");}
		if(g.getEdges().size() != 1) {outln("Test 18.1.44 Fail");}
		e4 = g.getEdges().get(0);
		if(e4.getFrom() != n1) {outln("Test 18.1.45 Fail");}
		if(e4.getTo() != n3) {outln("Test 18.1.46 Fail");}
		if(e4.getBends().size() != 5) {outln("Test 18.1.47 Fail");}
		if(!e4.getBends().get(0).equals(new Point(60,40))) {outln("Test 18.1.48 Fail");}
		if(!e4.getBends().get(1).equals(new Point(70,40))) {outln("Test 18.1.49 Fail");}
		if(!e4.getBends().get(2).equals(new Point(100,50))) {outln("Test 18.1.50 Fail");}
		if(!e4.getBends().get(3).equals(new Point(110,60))) {outln("Test 18.1.51 Fail");}
		if(!e4.getBends().get(4).equals(new Point(110,70))) {outln("Test 18.1.52 Fail");}
		
		g = new Graph();
		g.addNode(new Node("a",new Point(50,50)));
		g.addNode(new Node("b",new Point(100,50)));
		g.addNode(new Node("c",new Point(100,100)));
		e1 = g.addAdjacencyEdge("b","a");
		e1.addBend(new Point(70,40));
		e1.addBend(new Point(60,40));
		e2 = g.addAdjacencyEdge("c","b");
		e2.addBend(new Point(110,70));
		e2.addBend(new Point(110,60));
		n1 = g.firstNodeWithLabel("a");
		n2 = g.firstNodeWithLabel("b");
		n3 = g.firstNodeWithLabel("c");
		e3 = HybridGraph.replaceEdges(g,e2,e1,n2);
		if(g.getNodes().size() != 2) {outln("Test 18.1.63 Fail");}
		if(g.getEdges().size() != 1) {outln("Test 18.1.64 Fail");}
		e4 = g.getEdges().get(0);
		if(e4.getFrom() != n3) {outln("Test 18.1.65 Fail");}
		if(e4.getTo() != n1) {outln("Test 18.1.66 Fail");}
		if(e4.getBends().size() != 5) {outln("Test 18.1.67 Fail");}
		if(!e4.getBends().get(0).equals(new Point(110,70))) {outln("Test 18.1.68 Fail");}
		if(!e4.getBends().get(1).equals(new Point(110,60))) {outln("Test 18.1.69 Fail");}
		if(!e4.getBends().get(2).equals(new Point(100,50))) {outln("Test 18.1.70 Fail");}
		if(!e4.getBends().get(3).equals(new Point(70,40))) {outln("Test 18.1.71 Fail");}
		if(!e4.getBends().get(4).equals(new Point(60,40))) {outln("Test 18.1.72 Fail");}
		
		
		
		
		out("Ending method test 18");

	}
	
	
	
	protected static void test19() {
		outln("| Starting method test 19");
		
		
		Graph g;
		GraphPrecision gp;
		Node n1,n2,n3,n4;
		Edge e1,e2;
		ArrayList<Point2D.Double> bends;
		
		g = new Graph();
		gp = new GraphPrecision(g);
		if(!gp.consistent()) {outln("Test 19.1.1 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		gp = new GraphPrecision(g);
		if(!gp.consistent()) {outln("Test 19.1.2 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		gp = new GraphPrecision(g);
		g.removeNode(n1);
		if(gp.consistent()) {outln("Test 19.1.3 Fail");}
		
		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		if(gp.consistent()) {outln("Test 19.1.4 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		gp = new GraphPrecision(g);
		if(!gp.consistent()) {outln("Test 19.1.5 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		gp = new GraphPrecision(g);
		g.removeEdge(e1);
		if(gp.consistent()) {outln("Test 19.1.6 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		gp = new GraphPrecision(g);
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		if(gp.consistent()) {outln("Test 19.1.7 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		gp = new GraphPrecision(g);
		if(!gp.consistent()) {outln("Test 19.1.8 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		gp = new GraphPrecision(g);
		g.removeNode(n3);
		if(gp.consistent()) {outln("Test 19.1.8 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		gp = new GraphPrecision(g);
		g.removeEdge(e2);
		if(gp.consistent()) {outln("Test 19.1.9 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		gp = new GraphPrecision(g);
		n4 = new Node("d",new Point(0,0));
		g.addNode(n4);
		if(gp.consistent()) {outln("Test 19.1.10 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		gp = new GraphPrecision(g);
		g.addEdge(e2);
		if(gp.consistent()) {outln("Test 19.1.11 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		gp = new GraphPrecision(g);
		n1.setCentre(new Point(3,6));
		if(gp.consistent()) {outln("Test 19.1.12 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		gp = new GraphPrecision(g);
		n1.setCentre(new Point(5,5));
		if(gp.consistent()) {outln("Test 19.1.13 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(1,2));
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		e2.addBend(new Point(4,5));
		e2.addBend(new Point(8,9));
		gp = new GraphPrecision(g);
		if(!gp.consistent()) {outln("Test 19.1.14 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(1,2));
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		e2.addBend(new Point(4,5));
		gp = new GraphPrecision(g);
		e2.addBend(new Point(8,9));
		if(gp.consistent()) {outln("Test 19.1.15 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(1,2));
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		e2.addBend(new Point(4,5));
		gp = new GraphPrecision(g);
		e2.setBends(new ArrayList<Point>());
		if(gp.consistent()) {outln("Test 19.1.16 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(1,2));
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		e2.addBend(new Point(4,5));
		gp = new GraphPrecision(g);
		e1.setBends(new ArrayList<Point>());
		e1.addBend(new Point(1,3));
		if(gp.consistent()) {outln("Test 19.1.17 Fail");}
		
		g = new Graph();
		n1 = new Node("a",new Point(5,6));
		g.addNode(n1);
		n2 = new Node("b",new Point(2,7));
		g.addNode(n2);
		n3 = new Node("c",new Point(2,7));
		g.addNode(n3);
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(1,2));
		g.addEdge(e1);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		e2.addBend(new Point(4,5));
		gp = new GraphPrecision(g);
		e1.setBends(new ArrayList<Point>());
		e1.addBend(new Point(0,2));
		if(gp.consistent()) {outln("Test 19.1.17 Fail");}
		

		
		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		if(!gp.consistent()) {outln("Test 19.2.1 Fail");}
		
		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		n2 = new Node("b");
		e1 = new Edge(n1,n2);
		bends = new ArrayList<Point2D.Double>();
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		gp.addNode(n2,new Point2D.Double(5.1,6.2));
		gp.addEdge(e1,bends);
		if(!gp.consistent()) {outln("Test 19.2.2 Fail");}
		
		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		n2 = new Node("b");
		e1 = new Edge(n1,n2);
		bends = new ArrayList<Point2D.Double>();
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		gp.addNode(n2,new Point2D.Double(5.1,6.2));
		gp.addEdge(e1,bends);
		g.getNodes().remove(0);
		if(gp.consistent()) {outln("Test 19.2.3 Fail");}

		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		n2 = new Node("b");
		e1 = new Edge(n1,n2);
		bends = new ArrayList<Point2D.Double>();
		bends.add(new Point2D.Double(1.2,5.6));
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		gp.addNode(n2,new Point2D.Double(5.1,6.2));
		gp.addEdge(e1,bends);
		if(!gp.consistent()) {outln("Test 19.2.4 Fail");}

		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		n2 = new Node("b");
		e1 = new Edge(n1,n2);
		bends = new ArrayList<Point2D.Double>();
		bends.add(new Point2D.Double(1.2,5.6));
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		gp.addNode(n2,new Point2D.Double(5.1,6.2));
		n1.setCentre(new Point(2,6));
		gp.addEdge(e1,bends);
		if(gp.consistent()) {outln("Test 19.2.5 Fail");}

		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		n2 = new Node("b");
		e1 = new Edge(n1,n2);
		bends = new ArrayList<Point2D.Double>();
		bends.add(new Point2D.Double(1.2,5.6));
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		gp.addNode(n2,new Point2D.Double(5.1,6.2));
		gp.addEdge(e1,bends);
		e1.setBends(new ArrayList<Point>());
		if(gp.consistent()) {outln("Test 19.2.6 Fail");}

		
		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		if(gp.removeNode(n1) == false)  {outln("Test 19.3.1 Fail");}
		if(!gp.consistent()) {outln("Test 19.3.2 Fail");}

		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		n2 = new Node("b");
		e1 = new Edge(n1,n2);
		bends = new ArrayList<Point2D.Double>();
		bends.add(new Point2D.Double(1.2,5.6));
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		gp.addNode(n2,new Point2D.Double(5.1,6.2));
		gp.addEdge(e1,bends);
		if(gp.removeEdge(e1) == false)  {outln("Test 19.3.3 Fail");}
		if(!gp.consistent()) {outln("Test 19.3.4 Fail");}

		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		n2 = new Node("b");
		e1 = new Edge(n1,n2);
		bends = new ArrayList<Point2D.Double>();
		bends.add(new Point2D.Double(1.2,5.6));
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		gp.addNode(n2,new Point2D.Double(5.1,6.2));
		gp.addEdge(e1,bends);
		if(gp.removeNode(n1) == false)  {outln("Test 19.3.5 Fail");}
		if(gp.removeNode(n1) == true)  {outln("Test 19.3.6 Fail");}
		if(gp.removeEdge(e1) == true)  {outln("Test 19.3.7 Fail");}
		if(!gp.consistent()) {outln("Test 19.3.8 Fail");}

		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		n2 = new Node("b");
		e1 = new Edge(n1,n2);
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		gp.addNode(n2,new Point2D.Double(7.1,8.0));
		bends = new ArrayList<Point2D.Double>();
		bends.add(new Point2D.Double(4.2,5.6));
		bends.add(new Point2D.Double(1.2,9.1));
		gp.addEdge(e1,bends);
		if(!gp.consistent()) {outln("Test 19.4.1 Fail");}
		if(gp.findNodeCentre(n1).x != 5.1) {outln("Test 19.4.2 Fail");}
		if(gp.findNodeCentre(n1).y != 6.2) {outln("Test 19.4.3 Fail");}
		bends = gp.findEdgeBends(e1);
		if(bends.size() != 2) {outln("Test 19.4.4 Fail");}
		if(bends.get(1).x != 1.2) {outln("Test 19.4.5 Fail");}
		if(bends.get(0).y != 5.6) {outln("Test 19.4.6 Fail");}
		n3 = new Node("c");
		if(gp.findNodeCentre(n3) != null) {outln("Test 19.4.7 Fail");}
		e2 = new Edge(n1,n2);
		if(gp.findEdgeBends(e2) != null) {outln("Test 19.4.8 Fail");}
		
		g = new Graph();
		gp = new GraphPrecision(g);
		n1 = new Node("a");
		n2 = new Node("b");
		e1 = new Edge(n1,n2);
		gp.addNode(n1,new Point2D.Double(5.1,6.2));
		gp.addNode(n2,new Point2D.Double(7.1,8.0));
		gp.addEdge(e1,bends);
		if(!gp.consistent()) {outln("Test 19.5.1 Fail");}
		gp.addEdgeBend(e1, new Point2D.Double(1.3,-0.6));
		if(!gp.consistent()) {outln("Test 19.5.2 Fail");}
		gp.addEdgeBend(e1, new Point2D.Double(3.9,1.5));
		if(!gp.consistent()) {outln("Test 19.5.3 Fail");}

		
		
		out("Ending method test 19");

	}
	
	
	
	
	

	
	
}
