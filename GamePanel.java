import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JLabel implements Runnable{

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (GAME_WIDTH*5)/9;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 25;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    Thread gameThread;
    Graphics graphics;
    Image image;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    ScoreBoard score;

    GamePanel(){
        this.setPreferredSize(SCREEN_SIZE);
        newPaddle();
        newBall();
        this.setFocusable(true);
        score = new ScoreBoard(GAME_WIDTH,GAME_HEIGHT);
        this.addKeyListener(new AL());
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void newBall(){ // when game start new ball is to be paint by paint method.
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),(GAME_HEIGHT/2)-(BALL_DIAMETER/2),BALL_DIAMETER, BALL_DIAMETER);
    }
    public void newPaddle(){// same as Ball;
        paddle1 = new Paddle(0, (GAME_HEIGHT/2 - PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH, GAME_HEIGHT/2 - PADDLE_HEIGHT/2,PADDLE_WIDTH, PADDLE_HEIGHT,2);

    }
    public void paint(Graphics g){//paint is a method which can be used by any component like panel , label etc
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0,0,this);

    }
    public void draw(Graphics g){//using paint method 2D animation create by drawing or repaint the component.
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }
    public void move(){// it declared how the ball and paddle move.
        paddle1.move();
        paddle2.move();
        ball.move();
    }
    public void checkCollision(){
        if(paddle1.y<=0){
            paddle1.y = 0;
        }
        if(paddle1.y>= GAME_HEIGHT-PADDLE_HEIGHT)
            paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;

        if(paddle2.y<=0){
            paddle2.y = 0;
        }
        if(paddle2.y>= GAME_HEIGHT-PADDLE_HEIGHT)
            paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;

        // ball bounce back through top and bottom walls
        if(ball.y<= 0)
            ball.setYDirection(-ball.yVelocity);
        if(ball.y>= GAME_HEIGHT-BALL_DIAMETER)
            ball.setYDirection(-ball.yVelocity);

        // ball only bounce back when collide with paddles
        if(ball.intersects(paddle1) || ball.intersects(paddle2) ){
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if(ball.yVelocity<0)
                ball.yVelocity--;
            else
                ball.yVelocity++;
            if(ball.intersects(paddle1)){
                ball.setXDirection(ball.xVelocity);
            }
            else
                ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        // if ball collide with the side walls instead of paddle then opponent player get a score.
        if(ball.x<=0){
            score.player2++;
            newBall();
            newPaddle();
        }
        if(ball.x>=GAME_WIDTH-BALL_DIAMETER){
            score.player1++;
            newBall();
            newPaddle();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;
        while(true){
            long now = System.nanoTime();
            delta +=(now-lastTime)/ns;
            lastTime = now;
            if(delta>=1){
                move();
                checkCollision();
                repaint();
                delta--;
//                System.out.println("test");
            }
        }
    }

    public class AL extends KeyAdapter{// keyAdapter is a Abstract Class where we can use any method as per
        // use but KeyListener is an interface which have to implement all the 3 methods into the class.
        public void keyPressed(KeyEvent e){// react as per the key event during key pressed.
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e){// and what happened when key released
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
