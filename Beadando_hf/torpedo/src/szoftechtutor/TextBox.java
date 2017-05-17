package szoftechtutor;

import java.awt.Color;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

import szoftechtutor.Control.NetworkType;

/**
 * Szoveg kiirasara szolgalo elem.
 */
public class TextBox extends JTextArea {
	
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

	/**
	 * Kiirja a lerakando/kilovendo hajokhoz tartozo informaciot.
	 * @param form Mely helyre irunk informaciot.
	 * @param placement	Lerakas fazis van-e, vagy loves.
	 * @param networkType	Az alkalmazas kliens-e, vagy szerver.
	 * @param gs	A jatek aktualis allapota.
	 * @return	A kiirando szoveg.
	 */
	public String textCreator(boolean form, boolean placement, NetworkType networkType, GameState gs){
		if(networkType == NetworkType.Server){
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

		if(form==true){ //format == 1 -> textarea
		    if(placement) {
				status_text = String.format("Meg lepakolhato hajoid:\n"
		        		+ "█ %d darab\n"
		        		+ "██ %d darab\n"
		        		+ "███ %d darab\n"
		        		+ "████ %d darab\n"
		        		+ "█████ %d darab\n\n"
		        		+ "[%d] darab hajoelem\n"
		        		+ "[%d] ossz lerakott hajod"
		        		,placedShips[1],placedShips[2],placedShips[3],placedShips[4],placedShips[5],shipElements,15-IntStream.of(placedShips).sum());
		    }
		    else {
		        status_text = String.format("Kilott hajoid szama:\n"
		        		+ "█ %d darab\n"
		        		+ "██ %d darab\n"
		        		+ "███ %d darab\n"
		        		+ "████ %d darab\n"
		        		+ "█████ %d darab\n\n"
		        		+ "azaz [%d] darab hajoelem es\n"
		        		+ "[%d] hajo a 15-bol."
		        		,shotShips[1],shotShips[2],shotShips[3],shotShips[4],shotShips[5],35-shotElements, IntStream.of(shotShips).sum());  	
		    }
		}
		else if(form == false){ //format == 0 -> textbar
		    if(placement) {
		        status_text = String.format("Az ellenfel meg nem vegzett a hajoinak lerakasaval.");
		    }
		    else {
		        status_text = String.format(
		        		"Ellenfel elsullyesztendo hajoi:\n"
		        		+ "  █ %d  -  "
		        		+ "██ %d  -  " 
		        		+ "███ %d  -  "
		        		+ "████ %d  -  "
		        		+ "█████ %d - "
		        		+ "[%d] darab hajoelemet kell elsullyesztened meg"
		        		,5-enemyShotShips[1],4-enemyShotShips[2],3-enemyShotShips[3],2-enemyShotShips[4],1-enemyShotShips[5], enemyShotElements);   	
		    }
		}
		return status_text;
	}
}