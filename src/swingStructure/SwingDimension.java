
package swingStructure;

import java.io.File;
import java.util.ArrayList;		// import relevant classes for the data structure
import java.util.Collections;
import java.util.Scanner;
import java.util.function.Predicate;

	public class SwingDimension {	// data structure to read and analyze data given on all axes
		
		private ArrayList<Double> ax;
		private ArrayList<Double> ay;
		private ArrayList<Double> az;		// create all arrayLists to fill with data provided
		private ArrayList<Double> wx;
		private ArrayList<Double> wy;
		private ArrayList<Double> wz;
		
		public enum Axis {
			AX, AY, AZ, WX, WY, WZ			// declare axis enumerations for print method
		}
		
		public SwingDimension(String filename) {		// constructor takes filename rather than making readIn a separate method
			ax = new ArrayList<Double>();				// to ensure that structure is filled
			ay = new ArrayList<Double>();
			az = new ArrayList<Double>();		// create all arrayLists to fill with data provided
			wx = new ArrayList<Double>();
			wy = new ArrayList<Double>();
			wz = new ArrayList<Double>();
			this.readIn(filename);
		}
	
		// method to read in different data files to the structure in the appropriate axes
		
		private void readIn(String filename) {			// read file is now private
			String [] points = null;
			File swingData = new File(filename);	// file based on filename given
		
			try (Scanner sc = new Scanner(swingData)) {		    // read file using try with resources
				while (sc.hasNext()) {							// while there is another line of data
					String data = sc.next();					// read line into a temporary string
					points = data.split(",");					// split the string by column
					ax.add(Double.parseDouble(points[0]));		
					ay.add(Double.parseDouble(points[1]));
					az.add(Double.parseDouble(points[2]));		// parse each index to a Double
					wx.add(Double.parseDouble(points[3]));		// and add to corresponding axis array
					wy.add(Double.parseDouble(points[4]));
					wz.add(Double.parseDouble(points[5]));
				}
			}
			catch (Exception e) {
				System.out.println(e.getMessage());			// file not found exception
			}
		}
	
		// method to print a specific axis given in the args
		
		public void printAxis(Axis input) {
			
			switch(input) {
				case AX:
					System.out.println(ax); break;
				case AZ:
					System.out.println(az); break;
				case AY:
					System.out.println(ay); break;
				case WX:
					System.out.println(wx); break;
				case WZ:
					System.out.println(wz); break;
				case WY:
					System.out.println(wy); break;
			}
		}
	
		// method to print all axes
		
		public void printAll() {
			System.out.println(ax);
			System.out.println(ay);
			System.out.println(az);		// prints all
			System.out.println(wx);
			System.out.println(wy);
			System.out.println(wz);
		}
		
		// method used in data structure to get appropriate axis array called in main
		
		public ArrayList<Double> getAxisArray(String axisName) {
			ArrayList<Double> data = new ArrayList<Double>();
			switch (axisName) {					// switch array based on string given in args
				case "ax": data = ax; break;
				case "ay": data = ay; break;
				case "az": data = az; break;
				case "wx": data = wx; break;
				case "wy": data = wy; break;
				case "wz": data = wz; break;
			}
			return data;						// returns axis arrayList
		}
		
		private ArrayList<Pair> helperMethod(ArrayList<Double> data, int indexBegin, int indexEnd, Predicate<Double> check, int winLength) {

			ArrayList<Pair> indexes = new ArrayList<Pair>();	// create arrayList of indexes of success
			int firstIndex = 0;
			int lastIndex = 0;		// create temporary values
			int count = 0;
			for (int i = indexBegin; i <= indexEnd; i++) {		// for each element within range of indexes given
				if (check.test(data.get(i)) && count == 0) {	// if data at given index is greater than threshold and this is the first success
					// System.out.println("The value at " + i + " which is " + data.get(i) + " is greater than " + thresholdLo + " and less than " + thresholdHi);
					firstIndex = i;		// store first index of success
					count++;
				}
				else if (check.test(data.get(i)) && count > 0) {		// if more than one success in a row is found
					// System.out.println("The value at " + i + " which is " + data.get(i) + " is greater than " + thresholdLo + " and less than " + thresholdHi);
					count++;
					if (count >= winLength) {	// if number of successes matches or exceeds winLength
						count++;
						if(check.test(data.get(i+1))) {
							continue;
						}
						else {			// if a match is found and count does not equal winLength
							lastIndex = i;					// store last index
							// System.out.println("about to add index: "+firstIndex+","+lastIndex);
							Pair targetRange = new Pair(firstIndex, lastIndex);		// create pair to fill when a success is found
							indexes.add(targetRange);		// add pair to arrayList to be returned
							count = 0;			// reset count when a success has been stored
					
						}
					}
				}
				else {
					count = 0;		// if a streak is broken early or no match is found, ensure that count is reset
				}	
			}
			return indexes;		// return array of all successful indexes
		}
		
		// method iterates through the axis array denoted by dataName and returns the first index at which
		// value are greater than threshold for at least winLength samples in a row
		
		public int searchContinuityAboveValue(String dataName, int indexBegin, int indexEnd, double threshold, int winLength) {
			Predicate<Double> check = n -> n > threshold;
			ArrayList<Double> data = getAxisArray(dataName);	// get proper axis array
			ArrayList<Pair> successes = helperMethod(data, indexBegin, indexEnd, check, winLength);
			if (successes.size() > 0) {				// if successes occur
				return successes.get(0).first; 		// return first index of first success
			}
			else { 
				return -1;		// if no successes
			}
		}
		
		// method iterates through axis array denoted by dataName from a higher index to a lower index,
		// returning the first index of a value between thresholdLo and thresholdHi for at least
		// winLength samples in a row
		
		public int backSearchContinuityWithinRange(String dataName, int indexBegin, int indexEnd, double thresholdLo, double thresholdHi, int winLength) {
			ArrayList<Double> data = getAxisArray(dataName);	// get proper axis array
			Predicate<Double> check = n -> thresholdLo < n && n < thresholdHi;
			Collections.reverse(data);			// flip data to be able to search backwards
			indexBegin = data.size() - 1 - indexBegin;		// adjust indexes accordingly
			indexEnd = data.size() - 1 - indexEnd;
			ArrayList<Pair> successes = helperMethod(data, indexBegin, indexEnd, check, winLength);
			Collections.reverse(data);			// reset data to what it was
			if (successes.size() > 0) {
				return data.size() - successes.get(0).first - 1;	// return adjusted index of smallest index of result
			}
			else {
				return -1;			// if no successes
			}
		}
		
		// method to check both axis arrays given in args and return the first index of successes in
		// both arrays for at least winLength samples in a row
		
		// I could not find a way to implement the helper method on this one in a way that made sense
		
		public int searchContinuityAboveValueTwoSignals(String dataName1, String dataName2, int indexBegin, int indexEnd, double threshold1, double threshold2, int winLength) {
			ArrayList<Double> data1 = getAxisArray(dataName1);
			ArrayList<Double> data2 = getAxisArray(dataName2);	// get the two axis arrays
			Predicate<Double> check1 = n -> n > threshold1;
			Predicate<Double> check2 = n -> n > threshold2;
			ArrayList<Pair> successes1 = helperMethod(data1, indexBegin, indexEnd, check1, winLength);
			ArrayList<Pair> successes2 = helperMethod(data2, indexBegin, indexEnd, check2, winLength);
			// check if the successes overlap and return the first point at which they overlap for at least winLength
			return -1;
		}
		
		// method iterates through array given by dataName and returns an arrayList of Pairs
		// Pairs contain starting and ending index of each streak of values in between thresholdHi and thresholdLo for
		// at least winLength samples in a row
		// arrayList contains each pair that contained a success
		
		public ArrayList<Pair> searchMultiContinuityWithinRange(String dataName, int indexBegin, int indexEnd, double thresholdLo, double thresholdHi, int winLength) {
			ArrayList<Double> data = getAxisArray(dataName);	// get proper axis array
			Predicate<Double> check = n -> thresholdLo < n && n < thresholdHi;
			ArrayList<Pair> successes = helperMethod(data, indexBegin, indexEnd, check, winLength);
			ArrayList<Pair> rslt = new ArrayList<Pair>();
			int tempFirst = 0;
			int tempLast = 0;
			if (successes.size() > 0) { 		// if one of more successes exist
				// System.out.println("adding first element");
				rslt.add(successes.get(0));
				successes.remove(0);	// if any successes, add the first to final successes to be compared
				for (int i = 0; i < successes.size(); i++) {		// for all successes
					// test: System.out.println("looped: "+i);
					if (rslt.get(rslt.size()-1).last+1 == successes.get(i).first) {			// compare last element of rslt to see if its consecutive
						tempFirst = rslt.get(rslt.size()-1).first;							// to next element in successes
						tempLast = successes.get(i).last;
						rslt.remove(rslt.size()-1);							// delete old pair
						Pair modified = new Pair(tempFirst, tempLast);		// and if so, combine them
						rslt.add(modified);									// add modified pair
					}
					else {
						rslt.add(successes.get(i));
					}
				}
			}
			else { 
				return null;		// if no successes
			}
			return rslt;	// return modified results
		}
		
	}

