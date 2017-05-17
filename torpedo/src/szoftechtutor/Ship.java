package szoftechtutor;

import java.io.Serializable;

/**
 * Az egyes haj�k �llapot�t le�r� oszt�ly.
 */
public class Ship  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * A haj� maxim�lis m�rete.
     */
    public int size; 
    
    /**
     * A haj� aktu�lis elemeinek sz�ma.
     */
    public int elements_no;
    /**
     * A haj� azonos�t�ja
     */
    public int id;
    /**
     * Els�llyedt-e a haj�.
     */
    public boolean scuttled;
    /**
     * A haj� rendelkezik-e minden elem�vel.
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