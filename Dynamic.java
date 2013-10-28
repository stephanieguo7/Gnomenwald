import javax.swing.*;
/**
 * Draws Gnomes and villages to the graph.
 * Stores the width and height of the village panels
 * @author Mnemosyne
 *
 */

public class Dynamic extends JPanel{

	public final int WIDTH = 660;
	public final int HEIGHT = 480;

	/**
	 * Adds the preset Villages (if there are any) to the view of the GUI.
	 * @param graphPanel
	 */
	public void addVillagetoGraph(DisplayPanel graphPanel) {
		// create Village and add to array list that contains all Villages
		for (Village v : Map.allVillages) {
			v.setVisible(true);

			// add Village to position on graph
			if (v.getID() == 0) {
				View.graphPanel.add(v, 20, HEIGHT / 3);
			}
			else if (v.getID() == 1) {
				View.graphPanel.add(v, 20, 2 * (HEIGHT / 3));
			}
			else if (v.getID() == 2) {
				View.graphPanel.add(v, WIDTH / 6, 20);
			}
			else if (v.getID() == 3) {
				View.graphPanel.add(v, 2 * (WIDTH / 6), 20);
			}
			else if (v.getID() == 4) {
				View.graphPanel.add(v, 3 * (WIDTH / 6), 2 * (HEIGHT / 3));
			}
			else if (v.getID() == 5) {
				View.graphPanel.add(v, 3 * (WIDTH / 6), HEIGHT / 3);
			}
			
		}
		
	}//end of addVillagetoGraph
	
	/**
	 * This adds a Village that the user creates to the GUI's view
	 * @param graphPanel
	 * @param v
	 */
	public void addAdditionalVillage(DisplayPanel graphPanel, Village v) {
		View.graphPanel.add(v, View.WINDOW_WIDTH/2, View.WINDOW_HEIGHT/2);
		graphPanel.repaint();
		
	}
	/**
	 * Draws the gnomes along the road they are taking
	 * @param path The road the gnome is on
	 * @param g The gnome that is moving along the path.
	 */
	
	public void drawGnomes(Road path, Gnome g) {
		int xdiff = (path.getLeave().getX() - path.getArrive().getX()) / 3;
		int x = path.getLeave().getX() - xdiff;
		int ydiff = (path.getLeave().getY() - path.getArrive().getY()) / 3;
		int y = path.getLeave().getY() - ydiff;
		
		View.graphPanel.add(g.image);
		g.image.setLocation(x, y);
		g.image.setVisible(true);
		g.image.setOpaque(true);
	}

	
    
}
