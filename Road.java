
/*
 * Roads represent the edges in the graph. 
 */
public class Road implements Comparable<Road>{
	Village leave; // village of origin
	Village arrive; // village that road goes to
	int toll; // toll on the road
	
	/**
	 * returns the village that the road originates from. 
	 **/
	public Village getLeave() {
		return leave;
	}

	/**
	 * sets the village that the road originates from. 
	 **/
	public void setLeave(Village leave) {
		this.leave = leave;
	}

	/**
	 * returns the village that the road goes to. 
	 **/
	public Village getArrive() {
		return arrive;
	}

	/**
	 * sets the village the road goes to. 
	 **/
	public void setArrive(Village arrive) {
		this.arrive = arrive;
	}

	/**
	 * returns the toll on the road.
	 * @return
	 */
	public int getToll() {
		return toll;
	}
	
	/**
	 * sets the toll on the road. 
	 * @param toll
	 */
	public void setToll(int toll) {
		this.toll = toll;
	}
	
	
	/**
	 * Returns true if an Edge has identified the Vertex that it leaves from and the Vertex that it
	 * arrives at.
	 * @return
	 */
	public boolean areVillagesKnown() {
		return (leave.isKnown() & arrive.isKnown());
	}// end of method areVillagesKnown
	
	
	/**
	 * If an edge has both a leave and arrive Vertex, then the vertices are known and thus set to 
	 * true.
	 */
	public void knowVillages() {
		leave.setKnown(true);
		arrive.setKnown(true);
	}// end of method knowVillages


	/**
	 * Overrides the original toString() method. 
	 */
	public String toString() {
		return "Road from " + getLeave() + " to " + getArrive() + " with toll " + getToll();
	}// end of method toString()
	
	
	/**
	 * Allows for to Roads to be compared based off their toll. Returns 0 if the two tolls are equal, 
	 * a value less than 0 if this instance's toll is less than the other's toll, and a value 
	 * greater than 0 if other's toll is greater than this instance's toll. 
	 */
	@Override
	public int compareTo(Road other) {
		return Integer.compare(this.toll, other.toll);
	}// end of method compareTo
	

	// constructor for a Road
	public Road(Village start, Village end, int cost) {
		leave = start;
		arrive = end;
		toll = cost;
	}// end of constructor
		
}// end of class Road
