package graficosProfissional;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener {

  public static JFrame frame;
  private Thread thread;
  private boolean isRunning = false;
  private final int WIDTH = 160;
  private final int HEIGHT = 120;
  private final int SCALE = 3;

  private BufferedImage image;
  private Spritesheet sheet;
  private BufferedImage[] player;
  private int frames = 0;
  private int maxFrames = 10;
  private int curAnimation = 0;
  private int maxAnimation = 3;
  private int x=0;

  public Game() {
    sheet = new Spritesheet("/spritesheet.png");
    player = new BufferedImage[4];
    player[0] = sheet.getSprite(0,0,16,16);
    player[1] = sheet.getSprite(16,0,16,16);
    player[2] = sheet.getSprite(32,0,16,16);
    player[3] = sheet.getSprite(48,0,16,16);

    //System.out.println("Jogo iniciado");

    setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
    initFrame();

    image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
  }

  public void initFrame() {
    frame = new JFrame("Game #1");
    frame.add(this);
    frame.setResizable(false);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public synchronized void start() {
    thread = new Thread(this);
    isRunning = true;
    thread.start();
  }

  public synchronized void stop() {
    isRunning = false;
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Game game = new Game();
    game.start();
  }

  public void tick() {
    x++;
    frames++;
    if(frames>maxFrames){
      frames=0;
      curAnimation++;
      if(curAnimation>maxAnimation){
        curAnimation=0;
      }
    }
  }

  public void render() {
    BufferStrategy bs = this.getBufferStrategy();
    if (bs == null) {
      this.createBufferStrategy(3);
      return;
    }

    Graphics g = image.getGraphics();
    g.setColor(new Color(255, 50, 100));
    g.fillRect(0, 0, WIDTH, HEIGHT);

    //colocando retangulos
    /*g.setColor(Color.BLUE);
    g.fillRect(20, 20, 80, 80);*/

    //escrevendo
    /*g.setFont(new Font("Arial",Font.BOLD,20));
    g.setColor(Color.white);
    g.drawString("Olá Mundo!",19,19);*/

    /*Renderizacao do jogo*/
    //ROTACIONANDO
    Graphics2D g2 = (Graphics2D) g;
    //g2.setColor(new Color(0,0,0,80)); //colocando fundo com alpha channel para clarear ou escurecer
    //g2.fillRect(0,0,WIDTH,HEIGHT); //camada para o alpha channel
    //g2.rotate(Math.toRadians(x),20,20); //rotacao
    //FIM ROTACIONANDO

    g.drawImage(player[curAnimation],20,20,null);

    //alguns testes animando
    /*g.drawImage(player,x,20,null);
    g.drawImage(player,x,50,null);
    if(x==(WIDTH-player.getWidth())){
      x=0;
    }*/

    /***/

    g.dispose();
    g = bs.getDrawGraphics();
    g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
    bs.show();
  }

  public void run() {
    long lastTime = System.nanoTime();
    double amountOfTicks = 60.0;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;
    int frames = 0;
    double timer = System.currentTimeMillis();

    while (isRunning) {
      //System.out.println("Meu jogo está rodando!");
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;
      if (delta >= 1) {
        tick();
        render();
        frames++;
        delta--;
      }

      if (System.currentTimeMillis() - timer >= 1000) {
        System.out.println("FPS: " + frames);
        frames = 0;
        timer += 1000;
      }
    }

    stop();
  }

  public void keyTyped(KeyEvent e) {
    System.out.println("AQUI1");
    e.getKeyCode();
  }

  public void keyPressed(KeyEvent e) {
    System.out.println("AQUI2");
  }

  public void keyReleased(KeyEvent e) {
    System.out.println("AQUI3");
  }
}
