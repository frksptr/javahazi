package szoftechtutor;

import javax.swing.JButton;
import javafx.scene.Parent;

public class Ship {
    public int size;
    public int elements_no;
    public int id;
    public boolean shooted = false;
    public boolean complete = false;
    private int health;
    
    // TODO: eltárolni hogy melyik gomb tartozik hozzá kattintáskor, vagy inkább koordinátákat
    
    public Ship(int type) {
       // this.type =0;
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