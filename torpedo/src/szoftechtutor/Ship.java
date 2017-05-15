package szoftechtutor;

import javax.swing.JButton;
import javafx.scene.Parent;

public class Ship {
    public int size; 
    public int elements_no; //mennyi elemu az adott azonositoju hajo
    public int id; // 1-15-ig azonositja a shipeket
    public boolean scuttled;
    public boolean completed;
//	public Ship[][] cellsIsShipArray = new Ship[10][10];
    
    public Ship() {
		    size=0; 
		    elements_no=0; //mennyi elemu az adott azonositoju hajo
		    id=0; // 1-15-ig azonositja a shipeket
		    scuttled = false;
		    completed = false;
    	}
    }