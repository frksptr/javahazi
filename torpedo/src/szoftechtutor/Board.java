package szoftechtutor;

import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.Font;

public class Board extends JPanel {

	private boolean enemy = false;
	public int ships = 35;
	public int shipToPlace = 15;
	public int shootedShip = 35;
	private JButton[][] buttonGrid = new JButton[11][11];
	private Color waterColor = Color.CYAN;
	private Color shipColor = Color.BLACK;
	private Color shotShipColor = Color.RED;
	private Color shotWaterColor = Color.blue;
	private Color unknownColor = Color.LIGHT_GRAY;
			
	public Board(int posx, int posy, int width, int height, boolean enemy, ActionListener handler) {

		this.setBounds(posx, posy, width, height);
		this.enemy = enemy;

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
	

	public boolean placeShip(Ship ship, JButton button) {
		if(button.getBackground() == waterColor){
			if(ships == 0)
				System.out.println("Nem rakhatsz le több hajót!!!\n");
				//TODO: Le kell kérdeznie a GUI-nak, hogy milyen értékekkel vannak.
				//TODO: Át kell adni commandba. A "Ready" csak az összes hajó lerakása után mehet.
			else
				{
					button.setBackground(shipColor);
					ships--;
				}
			}
		else{
			button.setBackground(waterColor);
			ships++;
		}
		System.out.println("Még " + (ships) +" hajót kell leraknod!\n");
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
		return false;
	}

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
	
	public JButton getButton(Point p) {
		return buttonGrid[p.x][p.y];
		
	}
	
	///////////////////////////Ez a függvény nem hívódik meg a Logicban már lekezelődik. Kell-e akkor
	//ill gondoljuk át hogy hol kéne lennie
	public CellType checkShoot(Point p) {
		JButton button = buttonGrid[p.x][p.y];
		CellType cellType = null;
		
		if (button.getBackground() == waterColor) {
			button.setBackground(shotWaterColor);
			cellType = CellType.WaterShot;
		} else if (button.getBackground() == shipColor) {
			button.setBackground(shotShipColor);
			cellType = CellType.ShipShot;
			shootedShip--;
			System.out.println("Blablabla!\n");
			if (shootedShip == 0) System.out.println("Nyertél\n");
			else System.out.println("Még " + shootedShip + " darab hajót kell kilőnöd!\n");
		}
		
		return cellType;
	}
	
	public void redrawFromNewGameState(CellType[][] cellTypes) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				CellType cellType = cellTypes[i][j];
				JButton button = buttonGrid[i][j];
				switch (cellType){
					case Ship:
						button.setBackground(shipColor);
						break;
					case ShipShot:
						button.setBackground(shotShipColor);
						break;
					case Water:
						button.setBackground(waterColor);
						break;
					case WaterShot:
						button.setBackground(shotWaterColor);
						break;
					case Unknown:
						button.setBackground(unknownColor);
						break;
				}
			}
		}
	}
}