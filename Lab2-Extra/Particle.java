import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.awt.Color;

/**
 * Particle class, creating a particle, moving it, keeping trace of it's movement option.
 */
public class Particle {
	double x, y; // particles position
	defaultValues defaultV = new defaultValues();  // Link to the default values.
	int middleX = defaultV.xPanelSize/2; // The x value for the middle of the panel.
	int middleY = defaultV.yPanelSize/2; // The y value for the middle of the panel.
	int spread = defaultV.spread;  // The value for the starting spread of the particles.
	boolean isMoving; // Moving or not, starting condition = moving
	boolean isTracking; // Should the particle be tracked or not.
	ArrayList<Particle> trackingPath = new ArrayList<Particle>();  // The path which a particle moves in.
	Color color;        // The color of each particle.
	String ColorName;
	
	/**
	* Random particle between min and max.
	*/
	Particle () {
		
		this.x = ThreadLocalRandom.current().nextDouble(middleX-spread, middleX+spread);
		this.y = ThreadLocalRandom.current().nextDouble(middleY-spread, middleY+spread);
		this.isMoving = true;
		this.isTracking = false;
		this.color = defaultV.movingColor;
		this.ColorName = "yellow";
	}

	/**
	* Particle with custom start position.
	*/
	Particle (double xs, double ys) {
		this();
		this.x = xs;
		this.y = ys;
	}

	/**
	* Setting tracking option of a particle.
	*/
	void setTracking (boolean set) {
		if (set) {
			this.isTracking = true;
		} else {
			this.isTracking = false;
		}
	}
	
	/**
	* Setting tracking option of a particle.
	*/
	void setColor (Color c) {
		this.color = c;
	}
	
	/**
	*  Changes the particles position randomly.
	*/
	void randomMove(int L) {

		Double angle = Math.random() * 2 * Math.PI;     // Choose a random angle.
		this.x += L * Math.cos( angle );               // Set the new position.
		this.y += L * Math.sin( angle );  
		
		// If the particle reaches the Panel's corners make it stuck there.
		if( this.x <= 0.0 ) {
			this.isMoving = false;
			this.x = 0.0;
		}
		else if( this.x >= defaultV.xPanelSize ) {
			this.isMoving = false;
			this.x = defaultV.xPanelSize - defaultV.particleSize;
		}

		if( this.y <= 0.0 ) {
			this.isMoving = false;
			this.y = 0.0;
		}
		else if( this.y >= defaultV.yPanelSize ) {
			this.isMoving = false;
			this.y = defaultV.yPanelSize - defaultV.particleSize;
		}
		
		if (this.isTracking) {
			// Add new possition to our trackingPath on every simulation step over.
			this.trackingPath.add(new Particle(this.x, this.y));
		}
	}
}