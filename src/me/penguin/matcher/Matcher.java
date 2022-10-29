package me.penguin.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Matcher {
	
	public static String findBestMatch(String word, List<String> checks) {
		HashMap<String, Integer> scores = new HashMap<>();
		word = word.toLowerCase().strip();
		for (int i = 0 ; i < checks.size() ; i++) checks.set(i, checks.get(i).toLowerCase());
		for (String x : checks) scores.put(x, compare(word, x));
		return getMaxKey(scores);		
	}
	
	private static int compare(String in, String b) {
		String longer = in.length() > b.length() ? in : b;
		String shorter = in.length() > b.length() ? b : in;
		int consecutive = 1;
		char[] clong = longer.toCharArray();
		char[] cshort = shorter.toCharArray();
		int dif = longer.length()-shorter.length();
		int shift = 0-(shorter.length()-1);
		int totalpoints = 0;
		for (int s = shift ; s <= dif+(shorter.length()-1) ; s++) {
			int points = 0;
			for (int i = 0 ; i < cshort.length ; i++) {
				try {
					if (cshort[i] == clong[i+s]) points += consecutive++;
					else consecutive = 1;
				} catch (ArrayIndexOutOfBoundsException e) { continue; }
			}
			totalpoints += (s != 0 ? points : 2*points);
		}		
		
		totalpoints += getWordBonus(in, b);
		totalpoints *= getLengthAdjustment(in, b);
		return (totalpoints);
	}
	
	private static double getLengthAdjustment(String a, String b) {
		double lshort = Math.min(a.length(), b.length());
		double llong = Math.max(a.length(), b.length());
		double removing = (1.0-(lshort/llong))*0.25;
		return 1.0-removing;
	}
	
	private static int getWordBonus(String in, String check) {
		List<String> eatenwords = new ArrayList<>();
		int points = 0;
		for (String w : in.split(" ")) {
			if (check.contains(w) && !eatenwords.contains(w) && w.length() > 2) {
				points += getPossiblePoints(w)*2;
				eatenwords.add(w);
			}
		}
		return points;
	}
	
	private static int getPossiblePoints(String word) {
		int total = 0;
		for (int i = 1 ; i <= word.length() ; i++) total+=i;
		return total;
	}
	
	private static <T> T getMaxKey(HashMap<T, Integer> map) {
		T maxkey = null;
		for (Entry<T, Integer> ent : map.entrySet()) {
			if (maxkey == null || ent.getValue() > map.get(maxkey)) maxkey = ent.getKey();
		}
		return maxkey;
	}

}
