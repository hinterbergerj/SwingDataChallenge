package swingStructure;

public class Pair {	// pair class
	
	int first;
	int last;
	
	public Pair(int first, int last) {	// pair constructor without children
		this.first = first;
		this.last = last;
	}
	
	public String toString() { 					// to string
		return "(" + first + "," + last + ")"; 
		}

}
