package szoftechtutor;

import java.io.Serializable;

/**
 * Az egyes hajok allapotat leiro osztaly.
 */
public class Ship  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * A hajo maximalis merete.
     */
    public int size; 
    
    /**
     * A hajo aktualis elemeinek szama.
     */
    public int elements_no;
    /**
     * A hajo azonositoja
     */
    public int id;
    /**
     * Elsullyedt-e a hajo.
     */
    public boolean scuttled;
    /**
     * A hajo rendelkezik-e minden elemevel.
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