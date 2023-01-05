import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JPanel implements KeyListener {

    public static final int CELL_SIZE = 20;
    public static int width = 400;
    public static int height = 400;
    public static int row = height / CELL_SIZE;
    public static int column = width / CELL_SIZE;
    private Timer t;
    private int speed = 100;
    private static String direction;
    private boolean allowKeyPress;
    private int score = 0;
    private int highestScore;
    private String desktopDir = System.getProperty("user.home") + "/Desktop/";
    private String myFileName = desktopDir + "highestScore.txt";
    private Snake snake;
    private Fruit fruit;

    public Main() {
        reset();
        readHighestScore();
        addKeyListener(this);
    }

    @Override public void paintComponent(Graphics g) {
        // check if the snake bites itself
        ArrayList<Node> snake_body = snake.getSnakeBody();
        Node snake_head = snake_body.get(0);
        for(int i = 1; i < snake_body.size(); i++) {
            if(snake_body.get(i).x == snake_head.x && snake_body.get(i).y == snake_head.y) {
                allowKeyPress = false;
                t.cancel();
                t.purge();
                int response = JOptionPane.showOptionDialog(
                        this,
                        "Game Over!! Your score is " + score + ",\nThe highest score was " + highestScore + ",\nWould you like to start over?",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        null,
                        JOptionPane.YES_OPTION
                );

                recordHighestScore(score);

                switch (response) {
                    case JOptionPane.CLOSED_OPTION:
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return;
                }
            }
        }
        // draw a black background
        g.fillRect(0, 0, width, height);
        fruit.drawFruit(g);
        snake.drawSnake(g);

        // remove snake tail and put it in head
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;
        if(direction.equals("left")) {
            snakeX -= CELL_SIZE;
        } else if(direction.equals("right")) {
            snakeX += CELL_SIZE;
        } else if(direction.equals("up")) {
            snakeY -= CELL_SIZE;
        } else if(direction.equals("down")) {
            snakeY += CELL_SIZE;
        }

        Node newHead = new Node(snakeX, snakeY);
        // check if the snake eat the fruit
        if(snake.getSnakeBody().get(0).x == fruit.getX() && snake.getSnakeBody().get(0).y == fruit.getY()) {
            // 1. set fruit to a new location
            fruit.setNewLocation(snake);
            // 2. drawFruit
            fruit.drawFruit(g);
            // 3. score++
            score ++;
        } else {
            snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
        }
        snake.getSnakeBody().add(0, newHead);

        allowKeyPress = true;
        requestFocusInWindow();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setContentPane(new Main());
        window.setSize(500, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(allowKeyPress) {
            if(e.getKeyCode() == 37 && !direction.equals("right")) {
                direction = "left";
            } else if(e.getKeyCode() == 38 && !direction.equals("down")) {
                direction = "up";
            } else if(e.getKeyCode() == 39 && !direction.equals("left")) {
                direction = "right";
            } else if(e.getKeyCode() == 40 && !direction.equals("up")) {
                direction = "down";
            }
        }
        allowKeyPress = false;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private void setTimer() {
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, speed);
    }

    private void reset() {
        score = 0;
        allowKeyPress = true;
        direction = "right";
        snake = new Snake();
        fruit = new Fruit();
        setTimer();
    }

    public void readHighestScore() {
        try {
            File myObj = new File(myFileName);
            Scanner myReader = new Scanner(myObj);
            highestScore = myReader.nextInt();
            myReader.close();
        } catch (FileNotFoundException e) {
            highestScore = 0;
            try {
                File myObj = new File(myFileName);
                if(myObj.createNewFile()) {
                    System.out.println("File created : " + myObj.getName());
                }

                FileWriter myWriter = new FileWriter(myObj.getName());
                myWriter.write("" + highestScore);
            } catch (IOException err) {
                System.out.println("An Error occurred");
                err.printStackTrace();
            }
        }
    }

    public void recordHighestScore(int score) {
        try {
            FileWriter myWriter = new FileWriter(myFileName);
            if(score > highestScore) {
                System.out.println("Rewriting score...");
                myWriter.write("" + score);
                highestScore = score;
            } else {
                myWriter.write("" + highestScore);
            }
            myWriter.close();
        } catch (IOException err) {
            err.printStackTrace();
        }

    }
}
