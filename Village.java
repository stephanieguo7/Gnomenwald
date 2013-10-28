import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Represents a Village in the Graph. 
 */
@SuppressWarnings("serial")
public class Village extends JPanel implements Comparable<Village> {

	// Holds all the Villages this Village is connected to
	ArrayList<Village> adjacentVillages = new ArrayList<Village>();

	static int number = 1;
	int ID;

	// Reference to the panel that is displaying these nodes
	DisplayPanel displayPanel;

	// Holds the Gnome population of the village
	ArrayList<Gnome> currentResidents = new ArrayList<Gnome>();
	
	//Holds color of villages
	Color color = new Color(204, 255, 204);
	
	int capacity;
	int allottedStay;
	int dist; 
	boolean known;
	boolean passportRequired;
	Village next;
	ArrayList<Village> stampsRequired = new ArrayList<Village>();
	ArrayList<Gnome> leavingGnomes = new ArrayList<Gnome>();
	ArrayList<Road> incomingRoads = new ArrayList<Road>();
	ArrayList<Road> outgoingRoads = new ArrayList<Road>();
	JLabel nameLabel = new JLabel();
	
	
	/**
	 * returns the ID of the village.
	 * @return
	 */
	public int getID() {
		return ID;
	}
	
	
	/**
	 * sets the ID of the Village. 
	 * @param iD
	 */
	public void setID(int iD) {
		ID = iD;
	}
	
	
	/**
	 * returns the capacity of the village. 
	 * @return
	 */
	public int getCapacity() {
		return capacity;
	}
	
	
	/**
	 * sets the capacity of the village. 
	 * @param capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * gets the alloted stay for the village.
	 * @return
	 */
	public int getAllottedStay() {
		return allottedStay;
	}

	/**
	 * sets the alloted stay for the village. 
	 * @param allottedStay
	 */
	public void setAllottedStay(int allottedStay) {
		this.allottedStay = allottedStay;
	}
	
	
	/**
	 * returns the distance to the village from an origin village. 
	 * @return
	 */
	public int getDist() {
		return dist;
	}
	
	/**
	 * sets the distance to the village from an origin village. 
	 * @param dist
	 */
	public void setDist(int dist) {
		this.dist = dist;
	}
	
	
	/**
	 * returns whether the village is known. 
	 * @return
	 */
	public boolean isKnown() {
		return known;
	}
	
	/**
	 * sets whether the village is known.
	 * @param known
	 */
	public void setKnown(boolean known) {
		this.known = known;
	}
	
	
	/**
	 * returns whether a passport is required to visit the village. 
	 * @return
	 */
	public boolean isPassportRequired() {
		return passportRequired;
	}

	
	/**
	 * sets whether a passport is required to visit the village. 
	 * @param passportRequired
	 */
	public void setPassportRequired(boolean passportRequired) {
		this.passportRequired = passportRequired;
	}
	
	
	/**
	 * returns the village's next
	 * @return
	 */
	public Village getNext() {
		return next;
	}
	
	/**
	 * sets the village's next.
	 * @param next
	 */
	public void setNext(Village next) {
		this.next = next;
	}
	
	/**
	 * Overrides the default toString method. 
	 */
	public String toString() {
		return "Village: " + getID();
	}// end of toString method
	
	
	/**
	 * Using a common destination village to compare the distances to it from different villages.  
	 */
	public int compareTo(Village other) {
		return Integer.compare(this.dist, other.dist);	
	}
	

	// constructor for Village
	public Village(final DisplayPanel displayPanel) {

		this.ID = number++;
		Random generator = new Random();
		int i = generator.nextInt(3) + 3;
		capacity = i;
		allottedStay = i;
		known = false;
		passportRequired = false;
		setBackground(color);
			
		this.displayPanel = displayPanel;
		nameLabel.setText(ID + " |" + " Gnomes: " + currentResidents.size());
		add(nameLabel);
		/*nameLabel.setVisible(true);*/
		
		

		// makes the nodes draggable
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation(e.getX() + getLocation().x, e.getY()
						+ getLocation().y);
				displayPanel.repaint();
			}
		});

		setSize(90, 30);
		repaint();
	}//end of default constructor
	
	
	/**
	 * The constructor that is called when user adds a Village
	 * @param displayPanel
	 * @param country
	 * @param passportStamps
	 */
	public Village (final DisplayPanel displayPanel, Color c, ArrayList<Village> passportStamps) {
		this(displayPanel);
		setBackground(c);
		if (passportStamps.size() > 0) {
			passportRequired = true;
			stampsRequired = passportStamps;
		}
	}
	
}