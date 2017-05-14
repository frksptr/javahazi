package szoftechtutor;

import javax.swing.JButton;
import javafx.scene.Parent;

public class Ship {
    public int size; 
    public int elements_no; //mennyi elemu az adott azonositoju hajo
    public int id; // 1-15-ig azonositja a shipeket
    public boolean scuttled = false;
    public boolean completed = false;
	public Ship[][] cellsIsShipArray = new Ship[10][10];
    
    public Ship(int posx, int posy, boolean isNew) {
    		if(isNew)
    		{
    			cellsIsShipArray[posx][posy]=null;
    		}
    	}
    }