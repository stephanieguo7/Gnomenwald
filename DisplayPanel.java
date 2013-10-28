import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;



import javax.swing.JPanel;

/**
 * Displays the nodes and connections in the graph.  Redraws the roads continuously
 * through a while loop in a thread.
 * @author dvargo
 */
@SuppressWarnings("serial")
public class DisplayPanel extends JPanel
{
    

    public DisplayPanel()
    {
        setBackground(Color.white);
        repaint();
        setLayout(null);

        //will redraw and new lines in  the nodes automatically for you
        new Thread(new Runnable()
        {
            public void run()
            {
                while (true)
                {
                	drawRoads();
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (Exception e)
                    {

                    }// end of catch block
                }// end of while loop
            }// end of run() method
        // end of Runnable() followed by end of Thread() then start the thread
        }).start();
    }// end of DisplayPanel constructor

    /**
     * Adds a node to the graph
     * @param newVillage The village to add
     * @param xPosition The x Position in the graph to add it
     * @param yPosition The y Position in the graph to add it
     */
    public void add(Village newVillage, int xPosition, int yPosition)
    {
        add(newVillage);
        newVillage.setLocation(xPosition, yPosition);
 
    }
    
    /**
     * Draws all roads on the map, calculates the angle of where the 
     * arrow should point, and then draws the arrow.
     */

    public void drawRoads() {
		Graphics g = getGraphics();
		for (Road r : Map.allRoads) {
			g.drawLine(r.getLeave().getX(), r.getLeave().getY(),
					r.getArrive().getX(), r.getArrive().getY());
			double theta = Math.atan2(r.getArrive().getY() - r.getLeave().getY(), 
					r.getArrive().getX() - r.getLeave().getX());  
	        Arrow.drawArrow((Graphics2D) g, theta, r.getArrive().getX(), r.getArrive().getY()); 
		}
	}//end of drawRoads()
    
    

}
