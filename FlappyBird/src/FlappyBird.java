import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener
{
    int frameWidth = 360;
    int frameHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;
    Image gameOverImage; // Tambahkan game over image

    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    boolean gameOver = false;
    int score = 0;
    JLabel scoreLabel;

    public FlappyBird()
    {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        setLayout(null);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(10, 10, 150, 30);
        add(scoreLabel);

        // Load game images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        startGame();
    }

    public void startGame()
    {
        gameOver = false;
        score = 0;
        updateScoreLabel();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

        if (pipesCooldown != null && pipesCooldown.isRunning()) {
            pipesCooldown.stop();
        }

        if (gameLoop != null && gameLoop.isRunning()) {
            gameLoop.stop();
        }

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    System.out.println("pipa");
                    placePipes();
                }
            }
        });

        pipesCooldown.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    private void updateScoreLabel()
    {
        scoreLabel.setText("Score: " + score);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        // background
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

        // pipes
        for(int i = 0; i < pipes.size(); i++)
        {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }

        // player
        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        if (gameOver)
        {
            g.setColor(new Color(0, 0, 0, 120));
            g.fillRect(0, 0, frameWidth, frameHeight);

            g.setColor(new Color(255, 165, 0));
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String gameOverText = "GAME OVER";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(gameOverText);


            g.setColor(Color.BLACK);
            for(int xOffset = -2; xOffset <= 2; xOffset++) {
                for(int yOffset = -2; yOffset <= 2; yOffset++) {
                    if(Math.abs(xOffset) + Math.abs(yOffset) > 0) {
                        g.drawString(gameOverText, (frameWidth - textWidth) / 2 + xOffset,
                                frameHeight / 2 - 40 + yOffset);
                    }
                }
            }

            g.setColor(new Color(255, 165, 0));
            g.drawString(gameOverText, (frameWidth - textWidth) / 2, frameHeight / 2 - 40);

            g.setFont(new Font("Arial", Font.BOLD, 24));
            String finalScoreText = "SCORE: " + score;
            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(finalScoreText);

            g.setColor(Color.BLACK);
            for(int xOffset = -1; xOffset <= 1; xOffset++) {
                for(int yOffset = -1; yOffset <= 1; yOffset++) {
                    if(Math.abs(xOffset) + Math.abs(yOffset) > 0) {
                        g.drawString(finalScoreText, (frameWidth - textWidth) / 2 + xOffset,
                                frameHeight / 2 + 20 + yOffset);
                    }
                }
            }

            g.setColor(Color.WHITE);
            g.drawString(finalScoreText, (frameWidth - textWidth) / 2, frameHeight / 2 + 20);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            String restartText = "PRESS R TO RESTART";
            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(restartText);

            g.setColor(Color.BLACK);
            for(int xOffset = -1; xOffset <= 1; xOffset++) {
                for(int yOffset = -1; yOffset <= 1; yOffset++) {
                    if(Math.abs(xOffset) + Math.abs(yOffset) > 0) {
                        g.drawString(restartText, (frameWidth - textWidth) / 2 + xOffset,
                                frameHeight / 2 + 60 + yOffset);
                    }
                }
            }

            g.setColor(Color.YELLOW);
            g.drawString(restartText, (frameWidth - textWidth) / 2, frameHeight / 2 + 60);
        }
    }

    public void move()
    {
        if (gameOver)
        {
            return;
        }

        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        // Mengecek apakah player keluar dari layar
        if (player.getPosY() + player.getHeight() >= frameHeight)
        {
            gameOver = true;
            return;
        }

        for(int i = 0; i < pipes.size(); i++)
        {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

            // Mengecek tabrakan pipa
            if (checkCollision(player, pipe)) {
                gameOver = true;
                return;
            }

            // Jika pipa terlewati maka skor bertambah
            if (!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                // Menghitung dengan menggunakan upper pipe saja
                if (i % 2 == 0) {
                    pipe.setPassed(true);
                    score++;
                    updateScoreLabel();
                    System.out.println("Score: " + score);
                }
            }

            if (pipe.getPosX() + pipe.getWidth() < 0)
            {
                pipes.remove(i);
                i--;
            }
        }
    }

    private boolean checkCollision(Player player, Pipe pipe)
    {
        return player.getPosX() < pipe.getPosX() + pipe.getWidth() &&
                player.getPosX() + player.getWidth() > pipe.getPosX() &&
                player.getPosY() < pipe.getPosY() + pipe.getHeight() &&
                player.getPosY() + player.getHeight() > pipe.getPosY();
    }

    public void placePipes()
    {
        int randomPosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        upperPipe.setVelocityX(-3);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + pipeHeight + openingSpace, pipeWidth, pipeHeight, lowerPipeImage);
        lowerPipe.setVelocityX(-3);
        pipes.add(lowerPipe);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver)
        {
            player.setVelocityY(-10);
        }
        else if (e.getKeyCode() == KeyEvent.VK_R && gameOver)
        {
            System.out.println("Restarting game...");
            startGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}