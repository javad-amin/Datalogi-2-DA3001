import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class Tracking extends JFrame {
	BrownAnimation parent;                          // Link to the parent window/main window.
	defaultValues defaultV = new defaultValues(); // Link to the default values.
	JTable table;
	
	public Tracking( BrownAnimation p ) {
		super();
		this.parent = p;

		setLocation(400, 0);

		//create table with data
		table = new JTable(new MyTableModel());

		//add the table to the frame
		this.add(new JScrollPane(table));
		table.setPreferredScrollableViewportSize(new Dimension(400, 170));
		this.setTitle("Tracking options.");
		this.pack();
		this.setVisible(true);
		
		/*
		* A timer for updating the table.
		*/
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				updateAll();
			}
		};
		Timer timer = new Timer(100 ,taskPerformer);
		timer.setRepeats(true);
		timer.start();
	}

    class MyTableModel extends AbstractTableModel {
		
		//headers for the table
		String[] columns = new String[] {
			"Particle", "Coordinates", "State", "Color", "Tracking"
		};
		
		// Get the length of columns.
		public int getColumnCount() {
			return columns.length;
		}
		
		// Get the length of rows.
		public int getRowCount() {
			return Math.min(10, parent.model.particleArray.size());
			// The length is 10 in case the amount of particles more.
		}

		public String getColumnName(int col) {
			return columns[col];
		}

		/*
		* Determine the default renderer/editor for each cell.
		*/
		@Override
		public Class<?> getColumnClass(int c) {
			switch(c) {
			case 3: // color
				return Color.class;
			case 4: // tracking
				return Boolean.class;
			}
			return super.getColumnClass(c);
		}

		/*
		* Making checkboxes editable.
		*/
		public boolean isCellEditable(int row, int col) {
			if (col == 3 || col == 4) {
			  return true;
			} else {
			  return false;
			}
		}

		/*
		* Setting the tracking value of the particle on and off as a boolean.
		*/
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Particle p = parent.model.particleArray.get(rowIndex);
		switch(columnIndex) {
			case 3:
				p.color = (Color)aValue;
				break;
			case 4:
				p.isTracking = (Boolean)aValue;
				break;
			}
		}
		
		/*
		* Getting each value from the particle and constructing the table.
		*/
		@Override
		public Object getValueAt(int row, int col) {
			Particle p = parent.model.particleArray.get(row);
			switch(col) {
			case 0: // # number
				return Long.valueOf(row+1);
			case 1:// coord 
				return "x: " + (int) p.x + ", y: " + (int) p.y;
			case 2: // state
				return p.isMoving ? "moving" : "stuck";
			case 3: // color
				return p.ColorName;
			case 4: // tracking
				return new Boolean(p.isTracking);
			}
			return "undefined";
		}
		  
		}
	
	/*
	* Updating the entire table.
	*/
	public void updateAll() {
		((MyTableModel) table.getModel()).fireTableDataChanged();
	}
} 
