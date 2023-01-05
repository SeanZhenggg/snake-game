import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Fruit {
    private int x;
    private int y;

    public Fruit() {
        this.x = calculatePosition(Main.column);
        this.y = calculatePosition(Main.row);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void drawFruit(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(this.x, this.y, Main.CELL_SIZE, Main.CELL_SIZE);
    }

    public void setNewLocation(Snake s) {
        int new_x;
        int new_y;
        boolean overlapping;

        do {
            new_x = calculatePosition(Main.column);
            new_y = calculatePosition(Main.row);
            overlapping = checkOverlap(new_x, new_y, s);
        } while (overlapping);

        this.x = new_x;
        this.y = new_y;
    }

    private int calculatePosition(int unit) {
        return (int) (Math.floor(Math.random() * unit) * Main.CELL_SIZE);
    }

    private boolean checkOverlap(int x, int y, Snake s) {
        ArrayList<Node> snake_body = s.getSnakeBody();
        for(int j = 0; j < snake_body.size(); j++) {
            if(x == snake_body.get(j).x && y == snake_body.get(j).y) {
                return true;
            }
        }
        return false;
    }
}
