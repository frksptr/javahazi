package szoftechtutor;

import java.awt.Color;
import java.util.stream.IntStream;

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
	int shipElements, shotElements, enemyShotElements;
	int[] maximumShips = {0,5,4,3,2,1}; 
	
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
			shipElements = gs.serverGameSpace.ownShips.shipElements;
			shotElements = gs.serverGameSpace.ownShips.shotShipElements;
			enemyShotShips = gs.clientGameSpace.ownShips.shotShips;
			enemyShotElements = gs.clientGameSpace.ownShips.shotShipElements;
		}
		else{
			placedShips = gs.clientGameSpace.ownShips.placedShips;
			shotShips = gs.clientGameSpace.ownShips.shotShips;
			shipElements = gs.clientGameSpace.ownShips.shipElements;
			shotElements = gs.clientGameSpace.ownShips.shotShipElements;
			enemyShotShips = gs.serverGameSpace.ownShips.shotShips;
			enemyShotElements = gs.serverGameSpace.ownShips.shotShipElements;
		}
//		for(int i=0;i<6;i++){
//			enemyShotShips[i] = maximumShips[i]-enemyShotShips[i];
//		}
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
		        		,placedShips[1],placedShips[2],placedShips[3],placedShips[4],placedShips[5],shipElements,15-IntStream.of(placedShips).sum());
		    }
		    else {
		        status_text = String.format("Kilőtt hajóid száma:\n"
		        		+ "█ %d darab\n"
		        		+ "██ %d darab\n"
		        		+ "███ %d darab\n"
		        		+ "████ %d darab\n"
		        		+ "█████ %d darab\n\n"
		        		+ "azaz [%d] darab hajóelem és\n"
		        		+ "[%d] hajó a 15-ből."
		        		,shotShips[1],shotShips[2],shotShips[3],shotShips[4],shotShips[5],35-shotElements, IntStream.of(shotShips).sum());  	
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
		        		,5-enemyShotShips[1],4-enemyShotShips[2],3-enemyShotShips[3],2-enemyShotShips[4],1-enemyShotShips[5], enemyShotElements);   	
		    }
		}
		return status_text;
	}
}