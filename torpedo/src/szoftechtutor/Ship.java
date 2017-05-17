package szoftechtutor;

import java.io.Serializable;

/**
 * Az egyes hajók állapotát leíró osztály.
 */
public class Ship  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * A hajó maximális mérete.
     */
    public int size; 
    
    /**
     * A hajó aktuális elemeinek száma.
     */
    public int elements_no;
    /**
     * A hajó azonosítója
     */
    public int id;
    /**
     * Elsüllyedt-e a hajó.
     */
    public boolean scuttled;
    /**
     * A hajó rendelkezik-e minden elemével.
     */
    public boolean completed;
    
    public Ship() {
		    size=0; 
		    elements_no=0;
		    id=0;
		    scuttled = false;
		    completed = false;
    	}
    }