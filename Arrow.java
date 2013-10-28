import java.awt.*;  
import java.awt.geom.*;  
 
   /**
    * Calculates and draws an arrow shape given a graphics component, the direction the head should 
    * be in, and the x and y position of it's point.
    * 
    * @author Mnemosyne
    * @param g2 The Graphics2D component to draw the arrow head on
    * @param theta The angle the arrow will be pointing in
    * @param x0 The x position of the arrow's point
    * @param y0 The y position of the arrow's point
    *
    */
public class Arrow {
	static int barb = 20;
	static double phi = Math.PI/6;

    public static void drawArrow(Graphics2D g2, double theta, double x0, double y0) {
     
        double x = x0 - barb * Math.cos(theta + phi);  
        double y = y0 - barb * Math.sin(theta + phi);  
        g2.draw(new Line2D.Double(x0, y0, x, y));  
        x = x0 - barb * Math.cos(theta - phi);  
        y = y0 - barb * Math.sin(theta - phi);  
        g2.draw(new Line2D.Double(x0, y0, x, y));  
    }  
}  
