import java.util.ArrayList;

/**
 * Creating and making changes to the praticles.
 */
public class Model {
	defaultValues defaultV = new defaultValues(); // Link to the default values.
	ArrayList<Particle> particleArray = new ArrayList<Particle>(); //Our particles container.
	ArrayList<Particle> stickyArray = new ArrayList<Particle>(); //Our sticky area map.
	
	// Map of pixels on the screen which are sticky.
	
	// If it crashes add defaultV.L
	int xPixels = defaultV.xPanelSize + defaultV.particleSize ;
	int yPixels = defaultV.yPanelSize + defaultV.particleSize ;
	int[ ][ ] stickyMap = new int[xPixels][yPixels];
	
	/**
	* Creates the particles adding them to the container/list above.
	*/
	Model() {
		for(int i=0; i<defaultV.particleAmount; i++) {
			Particle particle = new Particle();
			this.particleArray.add(particle);
		}
	}
	
	/*
	* Changes the position of all of the particles.
	*/
	void moveAll() {
		// Utför slumpflyttning av alla partiklar
		for(int i=0; i<defaultV.particleAmount; i++) {
			Particle p = (Particle) particleArray.get(i);
			
			// if particle is inside sticky area make it stop.
			// if it's already stuck, add it's area to the stickyMap.
			for(int rowP=0; rowP<=defaultV.particleSize; rowP++) {
				for(int colomnP=0; colomnP<=defaultV.particleSize; colomnP++) {
					if(stickyMap[(int) p.x + rowP][(int) p.y + colomnP] == 1){
						p.isMoving = false;
					} if(!p.isMoving) {
						stickyMap[(int) p.x + rowP][(int) p.y + colomnP] = 1;
					}
					
				}
			}
				
			
			// In case the particle is already stuck ignore it.
			// Otherwise move it.
			if(!p.isMoving) {
				continue;
			} else {
				p.randomMove(defaultV.L);
			}
		}
	}
	
}
