/**
 * Simulation class which is a thread which starts the simulation.
*/
class Simulation extends Thread {
	Model model;
	View view;
	defaultValues defaultV = new defaultValues();

	/**
    * Creates and starts the simulation.
    */	
	Simulation( Model m, View v ) {
		this.model = m;
		this.view = v;
		this.start();
	}
	
	/**
    * Run function for the thread which makes sure the simulation keeps running.
    */
	public void run() {
		while( true ) { 
			model.moveAll(); // Move all the particles in the model.
            view.repaint(); // repaint the view.
			try {
				sleep( model.defaultV.timestep ); // Delays between each movement of particles.
			}
			catch( InterruptedException e ) {
			}
			finally { }
		}
	}
}