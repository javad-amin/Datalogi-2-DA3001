import javax.swing.*;
import java.awt.*;

/**
 * The main Brownian animation class which creates the main frame and the controllers.
 *
 * @author Javad Amin
 * @version 2017-02-12
 */
public class BrownAnimation extends JFrame {
	defaultValues defaultV = new defaultValues();  // Link to the default values.
	Model model = new Model();                    // Model used for simulation purpose.
	View view = new View(model);                 // Simulations display area.
	
	
	public BrownAnimation () {
		super();
		
		// Settings for the simulation view panel.
		view.setPreferredSize(new Dimension(defaultV.xPanelSize, defaultV.yPanelSize));
		
		// Creating the controllers.
		Manipulation man = new Manipulation( this );
		
		// Setting for the main frame.
		setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		setSize(400, 600);
		setLocation(0, 0); 
		setTitle("Brown animation!");
		setVisible(true);
		
		// Adding the view Panel.
		c.gridx = 0;
		c.gridy = 0;
		add( view, c );
		
		// Adding the controllers.
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 10;
		c.ipady = 10;
		add( man, c );
		
		pack();
		
		// Running the simulation.
		Simulation simulation = new Simulation( model, view );
		
		// Closing the window = Exiting the programs process.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	/**
    * Creates the Brown Animation.
    */
    public static void main (String [] x) {
		BrownAnimation f = new BrownAnimation();
		// Trace t = new Trace(f);   TODO maybe remove
		
		SwingUtilities.invokeLater(new Runnable() {

		@Override
		public void run() {
			new Tracking(f);
			}
		});
    }
}
