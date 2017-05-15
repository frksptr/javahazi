package szoftechtutor;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;

import szoftechtutor.Control.NetworkType;

public class TextBox extends JTextArea{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String status_text;
	JTextArea textArea = new JTextArea();
	JTextArea statusBar = new JTextArea();
	int[] placedShips;
	int[] shotShips;
	int[] enemyShotShips;
	
	public TextBox(int x,int y,int w,int h) {
		
		this.setSize(w,h);
		this.setLocation(x,y);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setEditable(false);
	}

	public String textCreator(boolean form, boolean placement, Control ctrl, GameState gs){
		if(ctrl.networkType == NetworkType.Server){
			placedShips = gs.serverGameSpace.ownShips.placedShips;
			shotShips = gs.serverGameSpace.ownShips.shotShips;
			enemyShotShips = gs.clientGameSpace.enemyShips.shotShips;
		}
		else{
			placedShips = gs.clientGameSpace.ownShips.placedShips;
			shotShips = gs.clientGameSpace.ownShips.shotShips;
			enemyShotShips = gs.serverGameSpace.enemyShips.shotShips;
		}
		if(form==true){ //format == 1 -> textarea
		    if(placement) {
				status_text = String.format("Még lepakolható hajóid:\n"
		        		+ "█ %d darab\n"
		        		+ "██ %d darab\n"
		        		+ "███ %d darab\n"
		        		+ "████ %d darab\n"
		        		+ "█████ %d darab\n\n"
		        		+ "[%d] darab hajóelem\n"
		        		+ "[%d] össz lerakott hajód"
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
		        		,enemyShotShips[1],enemyShotShips[2],enemyShotShips[3],enemyShotShips[4],enemyShotShips[5],10,23,34);  	
		    }
		}
		else if(form == false){ //format == 0 -> textbar
		    if(placement) {
		        status_text = String.format("Az ellenfél még nem végzett a hajóinak lerakásával.");
		    }
		    else {
		        status_text = String.format(
		        		"Ellenfél elsüllyesztendő hajói:\n"
		        		+ "  █ %d  -  "
		        		+ "██ %d  -  " 
		        		+ "███ %d  -  "
		        		+ "████ %d  -  "
		        		+ "█████ %d - "
		        		+ "[%d] darab hajóelemet kell elsüllyesztened még"
		        		,shotShips[1],shotShips[2],shotShips[3],shotShips[4],shotShips[5], 34);   	
		    }
		}
		return status_text;
	}
}