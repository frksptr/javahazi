package szoftechtutor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Board extends JPanel {

	private boolean enemy = false;
	public int ships = 5;
	private JButton[][] buttonGrid = new JButton[10][10];
	private Color waterColor = Color.BLUE;
	private Color shipColor = Color.BLACK;
	private Color shotShipColor = Color.RED;
	private Color shotWaterColor = Color.GRAY;

	public Board(int posx, int posy, int width, int height, boolean enemy, ActionListener handler) {

		this.setBounds(posx, posy, width, height);
		this.enemy = enemy;

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.ipadx = 0;
		c.ipady = 0;

		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		/*for
		JTextArea j;
		.
		.
		.
		.
		c.gridx = valami
		c.gridy = vlami
		this.add(j,c);
		*/
		
		for (int col = 1; col < 11; col++) {
			for (int row = 1; row < 11; row++) {
				JButton button = new JButton();
				

				button.setBackground(waterColor);

				button.setCursor(new Cursor(Cursor.HAND_CURSOR));
				button.setPreferredSize(new java.awt.Dimension(height / 10, width / 10));
				button.addActionListener(handler);

				c.gridx = col;
				c.gridy = row;

				this.add(button, c);
				buttonGrid[row-1][col-1] = button;
			}
		}

	}

	public boolean placeShip(Ship ship, JButton button) {
		button.setBackground(shipColor);
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
		if (canPlaceShip(ship, x, y)) {
			// int length = ship.type;
			//
			// if (ship.vertical) {
			// for (int i = y; i < y + length; i++) {
			// Cell cell = getCell(x, i);
			// cell.ship = ship;
			// if (!enemy) {
			// cell.setFill(Color.WHITE);
			// cell.setStroke(Color.GREEN);
			// }
			// }
			// } else {
			// for (int i = x; i < x + length; i++) {
			// Cell cell = getCell(i, y);
			// cell.ship = ship;
			// if (!enemy) {
			// cell.setFill(Color.WHITE);
			// cell.setStroke(Color.GREEN);
			// }
			// }
			// }
			//
			// return true;
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
	
	public CellType checkShoot(Point p) {
		JButton button = buttonGrid[p.x][p.y];
		CellType cellType = null;
		
		if (button.getBackground() == waterColor) {
			button.setBackground(shotWaterColor);
			cellType = CellType.WaterShot;
		} else if (button.getBackground() == shipColor) {
			button.setBackground(shotShipColor);
			cellType = CellType.ShipShot;
		}
		
		return cellType;
	}
	
	public void showShot(JButton button) {
		// TODO: GameState alapján kirajzolni megint az egészet
		button.setText(".");		
	}

	// private Cell[] getNeighbors(int x, int y) {
	// Point2D[] points = new Point2D[] {
	// new Point2D(x - 1, y),
	// new Point2D(x + 1, y),
	// new Point2D(x, y - 1),
	// new Point2D(x, y + 1)
	// };
	//
	// List<Cell> neighbors = new ArrayList<Cell>();
	//
	// for (Point2D p : points) {
	// if (isValidPoint(p)) {
	// neighbors.add(getCell((int)p.getX(), (int)p.getY()));
	// }
	// }
	//
	// return neighbors.toArray(new Cell[0]);
	// return null;
	// }

	private boolean canPlaceShip(Ship ship, int x, int y) {

		// int length = ship.type;
		//
		// if (ship.vertical) {
		// for (int i = y; i < y + length; i++) {
		// if (!isValidPoint(x, i))
		// return false;
		//
		// Cell cell = getCell(x, i);
		// if (cell.ship != null)
		// return false;
		//
		// for (Cell neighbor : getNeighbors(x, i)) {
		// if (!isValidPoint(x, i))
		// return false;
		//
		// if (neighbor.ship != null)
		// return false;
		// }
		// }
		// } else {
		// for (int i = x; i < x + length; i++) {
		// if (!isValidPoint(i, y))
		// return false;
		//
		// Cell cell = getCell(i, y);
		// if (cell.ship != null)
		// return false;
		//
		// for (Cell neighbor : getNeighbors(i, y)) {
		// if (!isValidPoint(i, y))
		// return false;
		//
		// if (neighbor.ship != null)
		// return false;
		// }
		// }
		// }

		return true;
	}
}