package szoftechtutor;

import javax.swing.JButton;

import javafx.scene.Parent;

public class Ship {
    public int type;
    public boolean vertical = true;

    private int health;
    
    // TODO: elt�rolni hogy melyik gomb tartozik hozz� kattint�skor, vagy ink�bb koordin�t�kat
    
    private JButton button;

    public Ship(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        health = type;

        /*VBox vbox = new VBox();
        for (int i = 0; i < type; i++) {
            Rectangle square = new Rectangle(30, 30);
            square.setFill(null);
            square.setStroke(Color.BLACK);
            vbox.getChildren().add(square);
        }

        getChildren().add(vbox);*/
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }
}