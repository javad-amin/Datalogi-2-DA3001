import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Simulations controllers class.
 */
public class Manipulation extends JPanel implements AdjustmentListener {
	BrownAnimation parent;                          // Link to the parent window/main window.
	JScrollBar speedBar, stepBar, sizeBar;         // Declaring the scrollbars.
	defaultValues defaultV = new defaultValues(); // Link to the default values.
	
	/**
	* Our manipulation constrator which takes the main class as a parameter. 
	*/
	public Manipulation( BrownAnimation p ) {
		super();
		this.parent = p;

		// Settings for the controllers panel.
		setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		setSize( 400, 450 );

		// Create the scrollbar to change the speed of simulation.
		speedBar = new JScrollBar( JScrollBar.HORIZONTAL, defaultV.timestep, 30, 1, 100 );
		speedBar.addAdjustmentListener( this );
		speedBar.setPreferredSize( new Dimension( defaultV.scrollBarsX, defaultV.scrollBarsY ) );
		c.gridx = 0;
		c.gridy = 0;
		add( new JLabel( "Faster" ), c );


		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add( speedBar, c );

		c.gridx = 2;
		c.gridy = 0;
		add( new JLabel( "Slower" ), c );
	  
		// Create the scrollbar to change the step length of simulation.
		stepBar = new JScrollBar( JScrollBar.HORIZONTAL, defaultV.L, 10, 1, 25 );
		stepBar.addAdjustmentListener( this );
		stepBar.setPreferredSize( new Dimension( defaultV.scrollBarsX, defaultV.scrollBarsY ) );
		c.gridx = 0;
		c.gridy = 1;
		add( new JLabel( "Shorter Step" ), c );

		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add( stepBar, c );

		c.gridx = 2;
		c.gridy = 1;
		add( new JLabel( "Longer Step" ), c );

		setVisible( true );
	}

	/**
	* Listener function for our scrollbars.
	*/
	public void adjustmentValueChanged( AdjustmentEvent e ) {
		Object source = e.getSource();

		// Making the changes accordingly when a scrollbar is used.
		if( source == this.speedBar )
			this.parent.model.defaultV.timestep = e.getValue();
		else if( source == this.stepBar )
			this.parent.model.defaultV.L = e.getValue();
	}
}

