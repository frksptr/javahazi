package szoftechtutor;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import szoftechtutor.Logic;

public class TextBox extends JTextArea{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String status_text;
	JTextArea textArea = new JTextArea();
	JTextArea statusBar = new JTextArea();
	
	int i=0;
	
	public TextBox(int x,int y,int w,int h) {
		
		this.setSize(w,h);
		this.setLocation(x,y);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setEditable(false);
	}

	public String textCreator(boolean format, boolean placement, GameState gs){
		i= i+1;
		int[] placedShips = gs.serverGameSpace.ownShips.placedShips;
		if(format==true){ //format == 1 -> textarea
		    if(placement) {
		        status_text = String.format("Még lepakolható hajóid:\n"
		        		+ "█ %d darab\n"
		        		+ "██ %d darab\n"
		        		+ "███ %d darab\n"
		        		+ "████ %d darab\n"
		        		+ "█████ %d darab\n\n"
		        		+ "[%d] darab hajóelem eddig\n"
		        		+ "[%d] össz lerakott hajóid"
		        		,placedShips[1],placedShips[2],placedShips[3],placedShips[4],placedShips[5],10,23,34);
		    }
		    else {
		        status_text = String.format("Kilőtt hajóid száma:\n"
		        		+ "█ %d darab\n"
		        		+ "██ %d darab\n"
		        		+ "███ %d darab\n"
		        		+ "████ %d darab\n"
		        		+ "█████ %d darab\n\n"
		        		+ "azaz [%d] darab hajóelem\n"
		        		+ "[%d] a(a) 15-ből."
		        		,i,3,5,1,5,1,34);  	
		    }
		}
		else if(format == false){ //format == 0 -> textbar
		    if(placement) {
		        status_text = String.format("Az ellenfél még nem végzett a hajóinak lerakásával. %d",i);
		    }
		    else {
		        status_text = String.format(
		        		"Ellenfél elsüllyesztendő hajói:\n"
		        		+ "  █ %d  -  "
		        		+ "██ %d  -  " 
		        		+ "███ %d  -  "
		        		+ "████ %d  -  "
		        		+ "█████ %d - "
		        		+ "[%d] darab hajót kell elsüllyesztened még"
		        		,1,1,1,1,1, 34);   	
		    }
		}
		return status_text;
	}
}