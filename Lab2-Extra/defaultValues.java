import java.awt.Color;

/**
 * A class which contains all of our dufault values making it easier to make changes.
 */
public class defaultValues {

	int particleAmount = 1000;                     // The amount of particles.
	Integer timestep = 30;                        // The time between rerunning the simulation.
	int xPanelSize = 400;                        // The width size of the view panel.
	int yPanelSize = 400;                       // The height size of the view panel.
	int spread = 50;                           // The spread of the particles as the program starts.
	int L = 4;                                // The distance which a particle can move each time.
	Color movingColor = Color.yellow;        // The color of the moving particles.
	Color stuckColor = Color.green;         // The color of the stuck particles.
	int particleSize = 2;                  // The size of particles.
	int scrollBarsX = 200;                // The width of scrollbars.
	int scrollBarsY = 19;                // The height of scrollbars.
	
	Color[] colorCode = {Color.blue, Color.red, Color.orange, Color.darkGray, Color.cyan, 
	Color.gray, Color.lightGray, Color.magenta, Color.pink, Color.white};
	
	String[] colorName = {"blue", "red", "orange", "darkGray", "cyan",
	"gray", "lightGray", "magenta", "pink", "white"};
}
