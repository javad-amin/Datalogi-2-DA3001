import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.lang.Iterable;

/**
 * The panel which contaions the simulation.
 */
public class View extends JPanel {
	private Model model;  // link to the model which will be displayed.
	
	/**
    * View's construction which recieves the model as a parameter.
    */
	public View(Model m) {
		super();
		setBackground(Color.black); // Setting background color.
		this.model = m;
	}
	
	/**
    * Drawings inside the view.
    */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Setting drawings the global color.
		g.setColor( this.model.defaultV.movingColor ); 

		//Painting all of the particles.
		for(int i = 0; i < this.model.particleArray.size(); i++) {
			Particle p = (Particle) this.model.particleArray.get(i);
			int x = (int) p.x;
			int y = (int) p.y;
			
			// Changing the color accordingly.
			if( p.isMoving ) {
				g.setColor( p.color );
			} else {
				g.setColor( this.model.defaultV.stuckColor );
			}
			
			// Painting each particle.
			g.fillRect(x, y, this.model.defaultV.particleSize, this.model.defaultV.particleSize);
			
			if (p.isTracking && p.trackingPath.size() > 1) {
				Particle firstPos = p.trackingPath.get(0);  // Our very first particle position.
				for(int j = 1; j < p.trackingPath.size() - 1; j++) {
					Particle secondPos = p.trackingPath.get(j);
					// draw line from first to second.
					Graphics2D g2 = (Graphics2D) g;
					Line2D lin = new Line2D.Float((int) firstPos.x, (int) firstPos.y, (int) secondPos.x, (int) secondPos.y);
					g.setColor( p.color );
					g2.draw(lin);
					// Set our first to the next.
					firstPos = secondPos;
				}
			}
        }
	}

}
