/* This program was written
 * by Jessica Devlin
 * on 03/29/19
 * last modified on 03/31/19
 * for Diamond Kinetics
 * This is a program written to analyze data given by a sample swing using different methods of scrutiny
 * This program implements a data structure called SwingDimension which contains the data provided in a sample csv
 * SwingDimension also contains the completed methods as defined by the coding challenge provided
 * A Pair class has also been created to return the appropriate information for the last method in SwingDimension
 */

package Main;

import java.util.ArrayList;		// imports relevant classes for main

import swingStructure.Pair;
import swingStructure.SwingDimension;		// imports data structure classes for use in main

public class SwingDataMain {

	public static void main (String[] args) {
		
		SwingDimension swing = new SwingDimension();
		String filename = "src\\data\\latestSwing.csv";
		swing.readIn(filename);
		
		System.out.println(swing.searchContinuityAboveValue("ax", 0, 20, 3000.0, 5));
		System.out.println(swing.backSearchContinuityWithinRange("ax", 7, 2, 3000.0, 6000.0, 2));
		ArrayList<Pair> rslt = swing.searchMultiContinuityWithinRange("ay", 0, 1000, 0, 6, 3);
		System.out.println(rslt.size());
		for (int i = 0; i < rslt.size(); i++) {
			System.out.println(rslt.get(i).toString());
		}
	} 
}	
