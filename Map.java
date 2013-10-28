import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * Basically creates a graph.  
 */
public class Map {
	static ArrayList<Village> allVillages = new ArrayList<Village>(); // field to hold all the villages
	static ArrayList<Road> allRoads = new ArrayList<Road>(); // field to hold all the roads
	static ArrayList<Gnome> allGnomes = new ArrayList<Gnome>(); // field to hold all the gnomes
	static int turn = 500;

	//constructor for Map
	public Map() {
		
		  for (int i = 0; i < 5; i++) { allVillages.add(new
				  Village(View.graphPanel)); } 
		  for (int i = 0; i < 3; i++) {
			allRoads.add(new Road(allVillages.get(i), allVillages.get(i + 1), i + 1)); } 
		  	allRoads.add(new Road(allVillages.get(0), allVillages.get(2),4)); 
		  	allRoads.add(new Road(allVillages.get(2), allVillages.get(4), 1)); 
		  	allRoads.add(new Road(allVillages.get(4), allVillages.get(1), 7));
		 
		findInRoads();
		findOutRoads();
		findAdjRoads();

	}// end of constructor

	
	/**
	 * This method finds and returns a Village, given its ID number.
	 */
	public static Village findVillage(int idNum) {
		Village find = null;
		for (Village v : Map.allVillages) {
			if (v.getID() == idNum) {
				find = v;
			}
		}
		return find;
	}// end of method findVillage

	
	/**
	 * This method finds and returns a Gnome, given its ID number.
	 */
	public static Gnome findGnome(int idNum) {
		Gnome find = null;
		for (Gnome g : Map.allGnomes) {
			if (g.getID() == idNum) {
				find = g;
			}
		}
		return find;
	}// end of method findGnome

	
	/**
	 * When called by the user, a new Village is added.
	 */
	public static void addVillage(Color c, ArrayList<Village> passportStamps) {
		allVillages.add(new Village(View.graphPanel, c, passportStamps));
	}// end of method addVillage

	
	/**
	 * This method is used to delete Villages, as well as any Roads associated
	 * with them.
	 */

	public static void nukeVillage(Village nuked) {
		nuked.setCapacity(0);
		findInRoads();
		findOutRoads();
		
		for (Gnome g :allGnomes){
			if(g.current.equals(nuked)){
			if (nuked.outgoingRoads.size() != 0) {
				g.visitAdjVillage();
			} else {
				g.dieGnomeDIE();
			}}
		}
		ArrayList<Road> villageRoads = new ArrayList<Road>();
		villageRoads.addAll(nuked.incomingRoads);
		villageRoads.addAll(nuked.outgoingRoads);
		for (Road r : villageRoads) {
			deleteRoad(r);
		}
		allVillages.remove(nuked);
		

	}// end of method nukeVillage

	
	/**
	 * This method is used to delete a Village and its associated Roads.
	 * However, this method rebuilds roads that had led to this intermediary
	 * Village and links them to the roads that went out of this Village.
	 * 
	 * @param razed
	 */
	public static void razeVillage(Village razed) {
		razed.setCapacity(0);// to prevent gnomes from coming to this village
		findInRoads();
		findOutRoads();
		for (Gnome g :allGnomes){
			if(g.current.equals(razed)){
			if (razed.outgoingRoads.size() != 0) {
				g.visitAdjVillage();
			} else {
				g.dieGnomeDIE();
			}}
		}

		for (Road from : razed.incomingRoads) {
			for (Road to : razed.outgoingRoads) {
				if (from.getLeave() != to.getArrive()) {
					if (findRoad(from.getLeave(), to.getArrive()) == null) {
						allRoads.add(new Road(from.getLeave(), to.getArrive(),
								from.getToll() + to.getToll()));
					}
				}
			}
		}// end of for each loops
		for (Road start : razed.incomingRoads) {
			deleteRoad(start);
		}
		for (Road end : razed.outgoingRoads) {
			deleteRoad(end);
		}
		allVillages.remove(razed);
	}// end of method razeVilalge

	
	/**
	 * Add a road to the list of allRoads. 
	 * @param from
	 * @param to
	 * @param toll
	 */
	public void addRoad(Village from, Village to, int toll) {
		allRoads.add(new Road(from, to, toll));
	}// end of addRoad

	
	/**
	 * Deletes a road from the list of allRoads. 
	 * @param r
	 */
	public static void deleteRoad(Road r) {
		allRoads.remove(r);
	}// end of deleteRoad

	
	/**
	 * This method goes through all the current Villages in the Map, and finds
	 * all the Roads that lead into each Village.
	 */

	public static void findInRoads() {
		for (Village v : allVillages) {
			v.incomingRoads.clear();
		}
		for (Road r : allRoads) {
			r.getArrive().incomingRoads.add(r);
		}
	}// end of method findInRoads

	
	/**
	 * This method goes through all the current Villages in the Map, and finds
	 * all the Roads that lead out of each Village.
	 */

	public static void findOutRoads() {
		for (Village v : allVillages) {
			v.outgoingRoads.clear();
		}
		for (Road r : allRoads) {
			r.getLeave().outgoingRoads.add(r);
		}
	}// end of method findOutRoads

	
	/**
	 * For all the Villages in this Map, this method finds all its adjacent
	 * Villages and adds them into the respective Villages' adjacency list.
	 * 
	 */

	public static void findAdjRoads() {
		for (Village v : allVillages) {
			v.adjacentVillages.clear();
			for (Road r : allRoads) {
				if (r.getLeave().equals(v)) {
					v.adjacentVillages.add(r.getArrive());
				}
			}
		}
	}// end of method findAdjRoads()

	/**
	 * This is only called when the destination is a Village that requires
	 * passport stamps. Returns a path that will show all the roads a Gnome has
	 * to take in order to acquire all the necessary passport stamps it needs to
	 * get to the destination. If a Gnome has already acquired some of the
	 * stamps, it will attempt to find a shorter path that excludes the already
	 * visited villages. Check for Villages are known then adds, to avoid
	 * cycles. Returns a list of ordered Villages.
	 * 
	 * @param destination
	 * @return
	 */

	public static ArrayList<Village> topoSort(Village destination) {
		findInRoads();
		findOutRoads();
		for (Village v : allVillages) {
			v.setKnown(false);
		}
		int[] indeg = new int[allVillages.size()];
		ArrayList<Village> zeroDegrees = new ArrayList<Village>();
		ArrayList<Village> orderedVillages = new ArrayList<Village>();
		for (Village v : allVillages) { // adds Village v if there are no roads
			// leading to it
			indeg[v.ID] = v.incomingRoads.size();
			if (indeg[v.ID] == 0) {
				zeroDegrees.add(v);
			}
		}
		int counter = 0;
		while (zeroDegrees.size() > 0) {
			Village u = zeroDegrees.iterator().next();
			zeroDegrees.remove(u); // remove a Village
			orderedVillages.add(u); // and add into the sorted ArrayList

			for (Iterator<Road> it = u.outgoingRoads.iterator(); it.hasNext();) {
				Road r = it.next();
				Village end = r.getArrive();
				indeg[end.ID]--;
				if (indeg[end.ID] == 0) {
					zeroDegrees.add(end);
				}
			}
			counter++;

		}
		if (counter < allVillages.size()) {
			System.out.println("cycle!");
		}

		return orderedVillages;
	}// end of method topoSort

	
	/**
	 * When given a starting Village to an End village, this method finds the
	 * road connecting the two.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */

	public static Road findRoad(Village start, Village end) {
		for (Road r : allRoads) {
			if (r.getLeave().equals(start) & r.getArrive().equals(end)) {
				return r;
			}
		}
		return null;
	}// end of method findRoad

	
	/**
	 * This method finds the shortest path from a given Village to all other
	 * Villages that exist in the Map, using Dijkstra's Algorithm. This method
	 * will continue until all Villages in the graph have been known.
	 * 
	 * @param start
	 */

	public static void shortestPath(Village start) {
		findInRoads();
		findOutRoads();
		for (int i = 0; i < allVillages.size(); i++) { // first initialize all
														// the Villages'
														// distances to infinity
			allVillages.get(i).setDist(Integer.MAX_VALUE);
			allVillages.get(i).setNext(null);
		}
		start.dist = 0;

		PriorityQueue<Village> villageQ = new PriorityQueue<Village>();
		villageQ.add(start);
		while (!villageQ.isEmpty()) {
			Village u = villageQ.poll();
			for (Road r : u.outgoingRoads) {
				Village adjacent = r.getArrive();
				int toll = r.getToll();
				int distThruU = u.dist + toll;
				if (distThruU < adjacent.dist) {
					villageQ.remove(adjacent);
					adjacent.dist = distThruU;
					adjacent.next = u;
					villageQ.add(adjacent);
				}
			}
		}

	}// end of shortestPath

	
	/**
	 * This method displays the calculations of Dijkstra's algorithm, by
	 * returning the shortest path to a Village desired by the user. The path is
	 * represented by an ArrayList of Roads.
	 * 
	 * @param destination
	 * @return
	 */

	public static ArrayList<Road> getShortestPath(Village destination) {
		ArrayList<Village> successiveVillages = new ArrayList<Village>();
		for (Village previous = destination; previous != null; previous = previous.next) {
			successiveVillages.add(previous);
		}
		Collections.reverse(successiveVillages);
		if (successiveVillages.size() == 1) {
			return null;
		}
		ArrayList<Road> path = new ArrayList<Road>();
		for (int i = 0; i < successiveVillages.size() - 1; i++) {
			path.add(findRoad(successiveVillages.get(i),
					successiveVillages.get(i + 1)));
		}
		return path;
	}

	
	/**
	 * This method takes the road proposals, and adds them to the map in order
	 * to find the minimum spanning tree. However, these proposals will not be
	 * added to the map view. If a road proposal is on the minimum spanning
	 * tree, it will be accepted and added to the map view. Else, it will be
	 * rejected and deleted.
	 * 
	 * @param proposal
	 * @return
	 */

	public static void minSpanTree(Road proposal) {
		ArrayList<Road> copyAllRoads = new ArrayList<Road>();
		copyAllRoads.addAll(allRoads);
		copyAllRoads.add(proposal);

		for (int i = 0; i < allVillages.size(); i++) { // first initialize all
														// the Villages' knowns
														// to be false
			allVillages.get(i).setKnown(false);
		}
		PriorityQueue<Road> sortedRoads = new PriorityQueue<Road>();
		sortedRoads.addAll(copyAllRoads);
		ArrayList<Road> minspantree = new ArrayList<Road>();

		while (sortedRoads.size() > 0) {
			Road temp = sortedRoads.poll(); // removes the edge with the minimum
											// weight and stores in temp
			if (!temp.areVillagesKnown()) {
				minspantree.add(temp);
				temp.knowVillages();
			}
		}
		addToRoadSystem(minspantree);

	}// end of method minSpanTree

	/**
	 * Adds road to the system if it is in the minimum spanning tree. Else,
	 * displays a message saying that the road could not be added. 
	 * @param minspantree
	 */
	
	public static void addToRoadSystem(ArrayList<Road> minspantree) {
		Road addition = null;
		for (Road r : minspantree) {
			if (!allRoads.contains(r)) {
				addition = r;
			}
		}
		if (addition != null) {
			allRoads.add(addition);
		} else {
			JFrame frame = new JFrame();
			JOptionPane
					.showMessageDialog(
							frame,
							"This road is not in the minimum spanning tree. "
									+ "In order for the government to save some money, "
									+ "this road cannot be constructed.");
		}

	}// end of method addToRoadSystem

}// end of class Map
