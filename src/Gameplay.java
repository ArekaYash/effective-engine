import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballposX = 120;
    private int ballposY = 350;
    int ballXdir = -2;
    int ballYdir = -4;

    private MapGen map;

    public Gameplay() {
        map = new MapGen(3, 7);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        randomizeBallDirection();
    }

    private void randomizeBallDirection() {
        Random rand = new Random();
        int xDir = rand.nextInt(3) + 1; // Random number between 1 and 3
        int yDir = rand.nextInt(3) + 1; // Random number between 1 and 3

        ballXdir = rand.nextBoolean() ? xDir : -xDir; // Randomize direction
        ballYdir = rand.nextBoolean() ? yDir : -yDir; // Randomize direction
    }

    public void paint(Graphics g) {
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        //map
        map.draw((Graphics2D) g);

        //paddle
        g.setColor(Color.white);
        g.fillRect(playerX, 550, 100, 8);

        //ball
        g.setColor(Color.white);
        g.fillOval(ballposX, ballposY, 20, 20);

        //score
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.BOLD, 25));
        g.drawString("Score: " + score, 520, 35);

        if(totalBricks == 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.drawString("YOU WON", 260, 300);

            g.setFont(new Font("TimesRoman", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        if(ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.drawString("GAME OVER :( Score: " + score, 190, 300);

            g.setFont(new Font("TimesRoman", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(play) {
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            A: for(int i = 0; i < map.map.length; i++) {
                for(int j = 0; j < map.map[0].length; j++) {
                    if(map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(i, j, 0);
                            totalBricks--;
                            score += 5;

                            randomizeBallDirection();

//                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
//                                ballXdir = -ballXdir;
//                            } else {
//                                ballYdir = -ballYdir;
//                            }

                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            if(ballposX < 0) ballXdir = -ballXdir;
            if(ballposY < 0) ballYdir = -ballYdir;
            if(ballposX > 670) ballXdir = -ballXdir;
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 600) playerX = 600;
            else moveRight();
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX < 10) playerX = 10;
            else moveLeft();
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -2;
                ballYdir = -4;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGen(3, 7);
                randomizeBallDirection();

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
