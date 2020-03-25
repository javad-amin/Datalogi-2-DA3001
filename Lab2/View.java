import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

/**
 * The panel which contaions the simulation.
 */
public class View extends JPanel {
	private Model model;  // link to the model which will be displayed.
	
	/**
    * View's construction which recieves the model as a parameter.
    */
	public View( Model m) {
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
				g.setColor( this.model.defaultV.movingColor );
			} else {
				g.setColor( this.model.defaultV.stuckColor );
			}
			
			// Painting each particle.
			g.fillRect(x, y, this.model.defaultV.particleSize, this.model.defaultV.particleSize);
			
			
        }
	}

}
