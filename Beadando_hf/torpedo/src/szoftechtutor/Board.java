package szoftechtutor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Font;

/**
 * A jatekteret leiro osztaly, a loheto cellak gombokkent kerulnek megvalositasra
 *
 */
/**
 * @author nempeter
 *
 */
/**
 * @author nempeter
 *
 */
/**
 * @author nempeter
 *
 */
public class Board extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton[][] buttonGrid = new JButton[11][11];
	private Color waterColor = Color.CYAN;
	private Color shipColor = Color.BLACK;
	private Color shotShipColor = Color.RED;
	private Color shotWaterColor = Color.blue;
	private Color unknownColor = Color.LIGHT_GRAY;
			
	/**
	 * A jatekteret felepiti es inicializalja a mezoket.
	 * @param posx	A tartalmazo UI elemen beluli x koordinata
	 * @param posy	A tartalmazo UI elemen beluli y koordinata
	 * @param width	A jatekteret tartalmazo elem szelessege
	 * @param height	A jatekteret tartalmazo elem magassaga
	 * @param handler	Az egyes mezokon torteno kattintashoz tartozo event handler-e
	 */
	public Board(int posx, int posy, int width, int height, ActionListener handler) {

		this.setBounds(posx, posy, width, height);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.ipadx = 0;
		c.ipady = 0;

		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		
		char a = 'A';

		for (int col = 1; col < 12; col++) {
			for (int row = 1; row < 12; row++) {
				JButton button = new JButton();
				if((col > 10) || (row > 10)){
					button.setEnabled(false);
					button.setPreferredSize(new java.awt.Dimension(height / 11, width / 11));
					if(col > 10 && row <= 10){
						button.setText(""+a);
						a +=1;
					}
					if(row > 10 && col <= 10){
						button.setText(""+col);
					}
					
					
					button.setFont(new Font("Arial", Font.PLAIN, 12));
					button.setBorder(null);
				}
				else{
					button.setBackground(waterColor);
					button.setCursor(new Cursor(Cursor.HAND_CURSOR));
					button.setPreferredSize(new java.awt.Dimension(height / 11, width / 11));
					button.addActionListener(handler);
					//button.setBorder(new LineBorder(Color.BLACK));
				}
				c.gridx = col;
				c.gridy = row;

				this.add(button, c);
				buttonGrid[row-1][col-1] = button;
			}
		}

	}
	
 
	/**
	 * Megadja az adott cellanak megfelelo gomb poziciojat,(sor,oszlop) formatumban.
	 * @param button	A cellahoz tartozo gomb, melynek (sor,oszlop) poziciojara kivancsiak vagyunk.
	 * @return	A cellanak megfelelo (sor,oszlop) pozicio.
	 */
	public Point getPosition(JButton button) {
		int x = 0;
		int y = 0;
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (buttonGrid[row][col] == button) {
					x = row;
					y = col;
				}
			}
		}
		return new Point(x,y);
	}
	
	
	/**
	 * Visszaadja a (sor,oszlop) formatumu poziciohoz tartozo cellanak megflelo gombot.
	 * @param p	A keresett cella (sor,oszlop) pozicioja.
	 * @return	A megadott pozicioban talalhato cellanak megfelelo gomb.
	 */
	public JButton getButton(Point p) {
		return buttonGrid[p.x][p.y];
		
	}
		
	/** A jatekteret frissiti az uj allas alapjan. A cellak uj allapota alapjan az azoknak megfelelo szinezest alkalmazza.
	 * @param cellTypes A frissitendo cellak aktualis allapota matrix formatumban.
	 */
	public void redrawFromNewGameState(CellType[][] cellTypes) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				switch (cellTypes[i][j]){
					case Ship:
						buttonGrid[i][j].setBackground(shipColor);
						break;
					case ShipShot:
						buttonGrid[i][j].setBackground(shotShipColor);
						break;
					case Water:
						buttonGrid[i][j].setBackground(waterColor);
						break;
					case WaterShot:
						buttonGrid[i][j].setBackground(shotWaterColor);
						break;
					case Unknown:
						buttonGrid[i][j].setBackground(unknownColor);
						break;
				}
			}
		}
	}
}