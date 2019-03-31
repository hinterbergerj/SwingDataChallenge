
package Main;

import java.io.File;
import java.util.ArrayList;		// import relevant classes for the data structure
import java.util.Scanner;

	public class SwingDimension {	// data structure to read and analyze data given on all axes
		
		ArrayList<Double> ax = new ArrayList<Double>();
		ArrayList<Double> ay = new ArrayList<Double>();
		ArrayList<Double> az = new ArrayList<Double>();		// create all arrayLists to fill with data provided
		ArrayList<Double> wx = new ArrayList<Double>();
		ArrayList<Double> wy = new ArrayList<Double>();
		ArrayList<Double> wz = new ArrayList<Double>();
	
		// method to read in different data files to the structure in the appropriate axes
		
		public void readIn(String filename) {
			String [] points = null;
			File swingData = new File(filename);	// file based on filename given
		
			try {
				Scanner sc = new Scanner(swingData);			// read file
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
				sc.close();		// close scanner
			}
			catch (Exception e) {
				System.out.println(e.getMessage());			// file not found exception
			}
		}
	
		// method to print a specific axis given in the args
		
		public void printAxis(String axis) {
			switch (axis) {
				case "ax": System.out.println(ax.toString()); break;
				case "ay": System.out.println(ay.toString()); break;
				case "az": System.out.println(az.toString()); break;
				case "wx": System.out.println(wx.toString()); break;	// prints axis depending on input
				case "wy": System.out.println(wy.toString()); break;
				case "wz": System.out.println(wz.toString()); break;
			}
		}
	
		// method to print all axes
		
		public void printAll() {
			System.out.println(ax.toString());
			System.out.println(ay.toString());
			System.out.println(az.toString());		// prints all
			System.out.println(wx.toString());
			System.out.println(wy.toString());
			System.out.println(wz.toString());
		}
		
		// method used in data structure to get appropriate axis array called in main
		
		private ArrayList<Double> getAxisArray(String axisName) {
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
		
		// method iterates through the axis array denoted by dataName and returns the first index at which
		// value are greater than threshold for at least winLength samples in a row
		
		public int searchContinuityAboveValue(String dataName, int indexBegin, int indexEnd, double threshold, int winLength) {
			ArrayList<Double> data = getAxisArray(dataName);	// get proper axis array
			int targetIndex = -1;		// target index is the value to be returned
			int count = 0;				// count to compare to winLength
			for (int i = indexBegin; i <= indexEnd; i++) {		// index end is inclusive
				if (data.get(i) > threshold && count == 0) {	// if data at given index is greater than threshold for the first time
					System.out.println("The value at " + i + " which is " + data.get(i) + " is greater than " + threshold);
					targetIndex = i;	// temporarily store first index of success
					count++;
				}
				else if (data.get(i) > threshold && count > 0) {	// if there has been more than one success consecutively
					System.out.println("The value at " + i + " which is " + data.get(i) + " is greater than " + threshold);
					count++;
					if (count == winLength) {	// once number of successes has reached winLength
						return targetIndex;		// return the index of the first success
					}
					else {
						continue;
					}
				}
				else {					// if no match is found, or a streak of success is broken before winLength,
					targetIndex = -1;	// reset count and targetIndex
					count = 0;
				}	
			}
			return targetIndex;		// return -1 if no match is found
		}
		
		// method iterates through axis array denoted by dataName from a higher index to a lower index,
		// returning the first index of a value between thresholdLo and thresholdHi for at least
		// winLength samples in a row
		
		public int backSearchContinuityWithinRange(String dataName, int indexBegin, int indexEnd, double thresholdLo, double thresholdHi, int winLength) {
			ArrayList<Double> data = getAxisArray(dataName);	// get proper axis array
			int targetIndex = -1;	// instantiate targetIndex and count
			int count = 0;
			for (int i = indexBegin; i >= indexEnd; i--) {		// from higher indexBegin to lower indexEnd (inclusive), going backwards
				if (data.get(i) > thresholdLo && data.get(i) < thresholdHi && count == 0) {	// if data at given index is greater than threshold and first success
					System.out.println("The value at " + i + " which is " + data.get(i) + " is greater than " + thresholdLo + " and less than " + thresholdHi);
					targetIndex = i;	// store index of first success
					count++;
				}
				else if (data.get(i) > thresholdLo && data.get(i) < thresholdHi && count > 0) { // if more than one success is found consecutively
					System.out.println("The value at " + i + " which is " + data.get(i) + " is greater than " + thresholdLo + " and less than " + thresholdHi);
					count++;
					if (count == winLength) {	// once number of successes has reached winLength
						return targetIndex;		// return first index of success
					}
					else {
						continue;
					}
				}
				else {
					targetIndex = -1;		// if streak is broken or no success is found
					count = 0;				// reset count and targetIndex
				}	
			}
			return targetIndex;		// return -1 if no match is found
		}
		
		// method to check both axis arrays given in args and return the first index of successes in
		// both arrays for at least winLength samples in a row
		
		public int searchContinuityAboveValueTwoSignals(String dataName1, String dataName2, int indexBegin, int indexEnd, double threshold1, double threshold2, int winLength) {
			ArrayList<Double> data1 = getAxisArray(dataName1);
			ArrayList<Double> data2 = getAxisArray(dataName2);	// get the two axis arrays
			int targetIndex = -1;
			int count = 0;
			for (int i = indexBegin; i <= indexEnd; i++) {		// iterate between indexes given
				if (data1.get(i) > threshold1 && data2.get(i) > threshold2 && count == 0) {	// if a success is found in both data sets for the first time
					targetIndex = i;	// store index of first success
					count++;
				}
				else if (data1.get(i) > threshold1 && data2.get(i) > threshold2 && count > 0) {	// if a second or more success is found check if winLength is satisfied
					count++;
					if (count == winLength) {	// if number of successes is equal to winLength
						return targetIndex;
					}
					else {
						continue;
					}
				}
				else {						// if no match is found or a streak is broken
					targetIndex = -1;		// reset targetIndex and count
					count = 0;
				}
			}
			
			return targetIndex;    // return -1 if no success is found
		}
		
		// method iterates through array given by dataName and returns an arrayList of Pairs
		// Pairs contain starting and ending index of each streak of values in between thresholdHi and thresholdLo for
		// at least winLength samples in a row
		// arrayList contains each pair that contained a success
		
		public ArrayList<Pair> searchMultiContinuityWithinRange(String dataName, int indexBegin, int indexEnd, double thresholdLo, double thresholdHi, int winLength) {
			ArrayList<Double> data = getAxisArray(dataName);	// get proper axis array
			ArrayList<Pair> indexes = new ArrayList<Pair>();	// create arrayList of indexes of success
			Pair targetRange = new Pair(0, 0);		// create pair to fill when a success is found
			int firstIndex = 0;
			int lastIndex = 0;		// create temporary values
			int count = 0;
			for (int i = indexBegin; i <= indexEnd; i++) {		// for each element within range of indexes given
				if (data.get(i) > thresholdLo && data.get(i) < thresholdHi && count == 0) {	// if data at given index is greater than threshold and this is the first success
					System.out.println("The value at " + i + " which is " + data.get(i) + " is greater than " + thresholdLo + " and less than " + thresholdHi);
					firstIndex = i;		// store first index of success
					count++;
				}
				else if (data.get(i) > thresholdLo && data.get(i) < thresholdHi && count > 0) {		// if more than one success in a row is found
					System.out.println("The value at " + i + " which is " + data.get(i) + " is greater than " + thresholdLo + " and less than " + thresholdHi);
					count++;
					if (count >= winLength) {	// if number of successes matches or exceeds winLength
						if (data.get(i+1) > thresholdLo && data.get(i+1) < thresholdHi) {	// if the next element is a match, continue the loop
							continue;
						}
						else {								// if end of streak
							lastIndex = i;					// store last index
							targetRange.first = firstIndex;	// add starting an ending index to success pair
							targetRange.last = lastIndex;
							System.out.println("I am adding.");
							indexes.add(targetRange);		// add pair to arrayList to be returned
							count = 0;
						}
					}
					else {
						continue;
					}
				}
				else {
					count = 0;		// if a streak is broken early or no match is found, ensure that count is reset
				}	
			}
			return indexes;		// return array of successful indexes
		}
		
	}

