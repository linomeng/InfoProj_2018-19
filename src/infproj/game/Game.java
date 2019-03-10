/*
 * Klausurersatzleistung: Informatik Projekt von Lino Mengers und Jona Böck 2018/2019
 * Fertigstellung am 10.03.19 (->Version)
 * Marie-Curie-Gymnasium
 * Informatik GK - Fricke
 * 
 * Trello: https://trello.com/b/AgBXZF6l/informatik-projekt
 * GitHub: https://github.com/linomeng/InfoProj_2018-19/
 */



package infproj.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;

import infproj.game.entities.Bullet;
import infproj.game.entities.Monster;
import infproj.game.entities.Player;
import infproj.game.entities.Scoreboard;
import infproj.game.gfx.Colors;
import infproj.game.gfx.Font;
import infproj.game.gfx.Screen;
import infproj.game.gfx.SpriteSheet;
import infproj.game.level.Level;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH=320;
	public static final int HEIGHT=WIDTH/10*14;
	public static final int SCALE = 3;
	public static final String NAME="Game";
	
	public static final String version="100319";
	
	public double nsPerTick = 1000000000D/60D;
	
	private JFrame frame;
	
	public boolean running=false;
	
	public int tickCount = 0;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private int[] colors = new int[6*6*6];
	
	public Screen screen;
	public InputHandler input;
	public Level level;
	public static Player player;
	public static Scoreboard scoreboard;
	public static boolean isRunning=true;
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		
		frame = new JFrame(NAME);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this,BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		frame.setTitle("InfoProj Version 100319");
	}
	
	public void init()
	{
		int index = 0;
		for (int r = 0; r < 6; r++)
		{
			for (int g = 0; g < 6; g++)
			{
				for (int b = 0; b < 6; b++)
				{
					int rr = (r * 255/5);
					int gg = (g * 255/5);
					int bb = (b * 255/5);
					
					colors[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}
					
		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);
		level = new Level(WIDTH/8,HEIGHT/8, 0);
		player = new Player(level, 8, 8, input);
		new Monster(level, "monstör", 300, 300, 1);
		scoreboard = new Scoreboard(level, screen, "Scoreboard", 16, level.width*8+4);
		level.addEntity(player);
		level.addEntity(scoreboard);
	}
	
	public void run()
	{
		long lastTime = System.nanoTime();
		
		int frames= 0;
		int ticks=0;
		
		long lastTimer= System.currentTimeMillis();
		double delta = 0;
		
		init();
		
		while(running)
		{
			long now = System.nanoTime();
			delta += (now-lastTime)/nsPerTick;
			lastTime=now;
			boolean shouldRender = true;
			
			while(delta >= 1)
			{
				ticks++;
				tick();
				delta--;
				shouldRender = true;
			}
			
			try {Thread.sleep(4);} catch (InterruptedException e) {e.printStackTrace();}
			
			if (shouldRender)
			{
				frames++;
				render();
			}
			
			if (System.currentTimeMillis() - lastTimer >= 1000)
			{
				lastTimer += 1000;
				//System.out.println(frames+", "+ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public void tick()
	{
		if (isRunning)
		{
			tickCount++;
			level.tick();
		}
	}
	
	public void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if (bs==null)
		{
			createBufferStrategy(3);
			return;
		}
		
		int xOffset = player.x - (screen.width/2);
		int yOffset = player.y - (screen.height/2);
		
		level.renderTiles(screen, xOffset, yOffset);
		Font.render("P", screen, player.x, player.y, Colors.get(-1, -1, -1, 000), 1);
	
		/*
		 * for (int x = 0; x < level.width; x++ )
		{	
			int color = Colors.get(-1, -1, -1, 000);
			if (x % 10 == 0 && x!=0) color = Colors.get(-1, -1, -1, 500);
			Font.render((x%10)+"", screen, 0+(x*8), 0, color, 0);
		}
		
		for (int y = 0; y < level.height; y++ )
		{	
			int color = Colors.get(-1, -1, -1, 000);
			if (y % 10 == 0 && y!=0) color = Colors.get(-1, -1, -1, 500);
			Font.render((y%10)+"", screen, 0+(y*8), 0, color, 1);
		}
		
		*/
		
		//String message="HELLO WORLD! 0138";
		//Font.render(message, screen, screen.xOffset+screen.width/2-(message.length()*8/2), screen.yOffset+screen.height/2, Colors.get(-1, -1, -1, 000), 1);
		
		level.renderEntities(screen);
		
		for (int y = 0; y < screen.height; y++ )
		{	for (int x = 0; x < screen.width; x++ )
			{
				int colorCode = screen.pixels[x+y*screen.width];
				if (colorCode < 255) pixels[x+y*WIDTH] = colors[colorCode];
			}
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	public void updateTitleBarText()
	{
		changeTitleBarText(designTitleBarText());
	}
	
	public String designTitleBarText()
	{
		String prod="";
		int hp=player.healthPoints;
		int roundedHP=hp;
		if (hp%10>=5) roundedHP+=hp%10;
		else { roundedHP-=10-hp%10; }
		int cap=roundedHP/10;
		prod="InfoProjekt | HP: "+hp+" [";
		for (int i=0; i<cap-1; i++) { prod+="#"; }
		
		if (hp%10<3) prod+="-";
		if (hp%10>2&&hp%10<7) prod+="/";
		for (int i=0; i<10-cap; i++) { prod+="-"; }
		
		prod+="] SCORE: "+player.score;
		return prod;
	}
	
	public void changeTitleBarText(String text)
	{
		frame.setTitle(text);
	}
	
	public synchronized void start()
	{
		running=true;
		new Thread(this).start();
	}
	
	public synchronized void stop()
	{
		 running = false;
	}
	
	public static void main(String[] args)
	{
		new Game().start();
	}
	
	public void over()
	{
		System.out.println("lol");
	}
}
