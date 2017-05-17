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
 * A játékteret leíró osztály, a lőhető cellák gombokként kerülnek megvalósításra
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
	 * A játékteret felépíti és inicializálja a mezőket.
	 * @param posx	A tartalmazó UI elemen belüli x koordináta
	 * @param posy	A tartalmazó UI elemen belüli y koordináta
	 * @param width	A játékteret tartalmazó elem szélessége
	 * @param height	A játékteret tartalmazó elem magassága
	 * @param handler	Az egyes mezőkön történő kattintáshoz tartozó event handler-e
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
	 * Megadja az adott cellának megfelelő gomb pozícióját,(sor,oszlop) formátumban.
	 * @param button	A cellához tartozó gomb, melynek (sor,oszlop) pozíciójára kíváncsiak vagyunk.
	 * @return	A cellának megfelelő (sor,oszlop) pozíció.
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
	 * Visszaadja a (sor,oszlop) formátumú pozícióhoz tartozó cellának megflelő gombot.
	 * @param p	A keresett cella (sor,oszlop) pozíciója.
	 * @return	A megadott pozícióban található cellának megfelelő gomb.
	 */
	public JButton getButton(Point p) {
		return buttonGrid[p.x][p.y];
		
	}
		
	/** A játékteret frissíti az új állás alapján. A cellák új állapota alapján az azoknak megfelelő színezést alkalmazza.
	 * @param A frissítendő cellák aktuális állapota mátrix formátumban.
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