import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

/***
 * The Gnomes store their ID number, their current village, their destination village, 
 * all the villages they have visited so far, the path they need to take next, 
 * and a JPanel that displays their ID number.
 * @author Mnemosyne
 *
 */
//these are the people
public class Gnome extends Thread {
	static int number = 1;
	int ID;
	Village current;
	Village destiny;
	ArrayList<Village> passport = new ArrayList<Village>();
	ArrayList<Road> upcomingpath = new ArrayList<Road>();
	JPanel image = new JPanel();
	boolean adj = false;
	boolean nonAdj = false;

	// gnome image stuff
	private static final String IMG_PATH = "src/weeee (1).png";

	/**
	 * Returns the village the gnome is currently in.
	 * @return
	 */
	public Village getCurrent() {
		return current;
	}
	/**
	 * Sets a given village as a gnome's current.  Note: does not actually move gnome.
	 * @param current Village to be set as current.
	 */

	public void setCurrent(Village current) {
		this.current = current;
	}
	/**
	 * Gnome constructor.  Stores ID number, tells it to stay at current village,
	 * starts the Gnome's thread, creates the label for the gnome to display while traveling
	 * @param birthplace The village the Gnome will begin it's life at.
	 */

	public Gnome(Village birthplace) {
		this.ID = number++;
		stayAtVillage(birthplace);
		new Thread(this).start();
		
		image.setOpaque(false);
		try{
			BufferedImage img = ImageIO.read(new File(IMG_PATH));
			ImageIcon icon = new ImageIcon(img);
			JLabel label = new JLabel(icon);
			image.add(label);
		}catch(IOException e){
			e.printStackTrace();
		}
		image.setSize(30, 65);
		JLabel nameID = new JLabel(""+ this.ID);
		nameID.setFont(new Font("Serif", Font.BOLD, 9));
		image.add(nameID);
		image.setBackground(new Color(102, 178, 255));

	}

	/**
	 * While the thread runs, the gnome will stay where it is, move about randomly, or 
	 * visit a specific village given the state of the booleans adj and nonAdj.  
	 */
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (adj) {
				visitAdjVillage();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (nonAdj) {
				ArrayList<Road> path = this.visitNotAdj(destiny);
				travel(path);
				nonAdj = false;
			
				}
			

		}
	}

	/**
	 * This method allows a Gnome to stay at a particular Village.
	 * If the village still has the capacity for it, the gnome will stay 
	 * at the village for the allowed number of nights.  The gnome is added to
	 * the villages current residents list and updates the village's label to 
	 * reflect its presence.
	 * 
	 * @param stay Village the gnome is chilling at.
	 */
	public void stayAtVillage(Village stay) {
		if (stay.capacity > 0) {

			View.graphPanel.repaint();
			stay.capacity--;
			this.setCurrent(stay);
			try {
				Thread.sleep(stay.allottedStay * Map.turn);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stay.currentResidents.add(this);
			for (Village v : Map.allVillages) {
				v.nameLabel.setText(v.ID + " |" + " Gnomes: "
						+ v.currentResidents.size());
			}
			stampPassport();
			image.setVisible(false);
			View.graphPanel.repaint();

		}
	}

	/**
	 * This is called when the user wants the Gnome to randomly visit adjacent
	 * villages. When a Gnome visits a village, it first checks the village's
	 * capacity. If the village can hold him, he will stay there awhile before
	 * moving on. Else he will move the next turn. Also random picks a number of
	 * turns to stay at a village. Regardless of whether the Gnome stays or not,
	 * it gets its passport stamped.
	 */
	public Road visitAdjVillage() {
		while (current != null) {
			Map.findAdjRoads();
			ArrayList<Village> possibleNexts = current.adjacentVillages;
			Random generator = new Random();
			if (possibleNexts.size() > 0) {
				int i = generator.nextInt(possibleNexts.size()) + 0;
				Village next = possibleNexts.get(i);
				if (next.capacity > 0) {
					Road path = Map.findRoad(current, next);
					current.currentResidents.remove(this);
					current.capacity++;
					Dynamic dyno = new Dynamic();
					try{
					dyno.drawGnomes(path, this);
					}catch(Exception E){
						this.dieGnomeDIE();
						//Map.allGnomes.remove(this);
					}
					
					this.stayAtVillage(next);
					return path;
				} else {
					return null;
				}
			} else {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				visitAdjVillage();
			}
		}
		return null;
	}

	/**
	 * This method checks to see if this Gnome already has a stamp from this
	 * village, and if it doesn't, he gets a stamp from this village.
	 */
	private void stampPassport() {
		// checks to see if current village is in passport, if not, adds to
		// ArrayList
		if (!passport.contains(current)) {
			passport.add(current);
		}

	}// end of method StampPassport

	/**
	 * This is called when the user wants the Gnome to go to a village that is
	 * not adjacent to the village the Gnome is currently in. If this village
	 * requires a passport, uses topological sort. Otherwise, it just looks for
	 * the shortest path to the destination.
	 */
	public ArrayList<Road> visitNotAdj(Village goal) {

		JFrame frame = new JFrame();
		ArrayList<Road> path = new ArrayList<Road>();
		ArrayList<Village> locations = new ArrayList<Village>();
		locations.add(this.current);
		if (goal.passportRequired) {
			// ArrayList<Village> visits = Map.topoSort(goal);
			for (Village v : goal.stampsRequired) {
				/*
				 * if (visits.indexOf(v) > visits.indexOf(goal)) {
				 * JOptionPane.showMessageDialog(frame,
				 * "Cannot get to destination because in order to reach " + goal
				 * + ", you must first reach " + v +
				 * ".  This is not currently possible. " +
				 * "Would you like to add a Road to fix this?");
				 */

				locations.add(v);

			}// end of for loop
			locations.add(goal);
			for (int i = 0; i < locations.size() - 1; i++) {
				Map.shortestPath(locations.get(i));
				path.addAll(Map.getShortestPath(locations.get(i + 1)));

			}
			// }//end of using toposort
		} else {
			Map.shortestPath(current);
			path = Map.getShortestPath(goal);
		}
		return path;
	}

	/**
	 * When a Gnome leaves a village, this method is called. Shows the user
	 * which Gnomes are leaving a particular village.
	 */
	public void leaveVillage() {
		current.currentResidents.remove(this);
		current.capacity++;

	}

	/**
	 * This method takes in an ArrayList of Roads, or a pathway that the Gnome
	 * must travel to reach a destination. Can only travel to one village per
	 * turn. After he reaches the destination, the Gnome can stay for the
	 * allotted time to stay.
	 */
	public void travel(ArrayList<Road> pathway) {
		Village destination = null;
		if (pathway == null) {
				JFrame frame = new JFrame();
				JOptionPane
				.showMessageDialog(frame,
						"Cannot get to destination. How about adding more Roads first?");
				return;
		}
		else if (pathway.size() == 1) {
			current.currentResidents.remove(this);
			Dynamic dyno = new Dynamic();
			dyno.drawGnomes(pathway.get(0), this);
			destination = pathway.get(0).getArrive();
		} else if (pathway.size() > 1) {
			for (int i = 0; i < pathway.size(); i++) {
				current.currentResidents.remove(this);
				Village next = pathway.get(i).getArrive();
				Road path = Map.findRoad(current, next);
				this.transition(next);
				Dynamic dyno = new Dynamic();
				dyno.drawGnomes(path, this);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			destination = pathway.get(pathway.size() - 1).getArrive();
		}

		this.stayAtVillage(destination);
	}
/**
 * When the gnome is moving to a specific place, it is supposed to stay at the village
 * without effecting the village's counter.
 * @param stay
 */
	public void transition(Village stay) {
		View.graphPanel.repaint();
		this.setCurrent(stay);
		try {
			Thread.sleep(Map.turn);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stampPassport();
		image.setVisible(false);
		View.graphPanel.repaint();

	}

	/**
	 * This method is called when a Gnome needs to passby a Village in order to
	 * reach its overall destination. The Gnome only stays at the Village for
	 * the current turn and does not decrease the Village's capacity level.
	 * 
	 * @param passby
	 */
	public void passby(Village passby) {
		this.setCurrent(passby);
		try {
			Thread.sleep(Map.turn);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Kills the gnome when the village it is in has died.
	 */
	public void dieGnomeDIE() {
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame,
				"A gnome has perished, along with the village. "
						+ "There was no way to excape!");
		this.current.currentResidents.remove(this);
		this.setCurrent(null);
		this.interrupt();
		
	}
	/**
	 * Returns a string with the gnome and its id
	 */

	public String toString() {
		return "Gnome: " + getID();
	}

	/**
	 * Gnome's ID
	 * @return the ID of the Gnome
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Stops the Gnome from moving to adjacent villages by setting the
	 * adj boolean to false
	 * @throws InterruptedException
	 */
	public void stopGnome() throws InterruptedException {
		this.adj = false;

	}
	/**
	 * Starts the Gnome moving to adjacent villages by setting the adj 
	 * boolean to true.
	 * @throws InterruptedException
	 */

	public void startGnome() throws InterruptedException {
		this.adj = true;
	}
}
