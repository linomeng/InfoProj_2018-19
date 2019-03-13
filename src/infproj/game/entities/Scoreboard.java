package infproj.game.entities;

import infproj.game.Game;
import infproj.game.entities.Player;
import infproj.game.gfx.Colors;
import infproj.game.gfx.Font;
import infproj.game.gfx.Screen;
import infproj.game.level.Level;

public class Scoreboard extends Entity
{
	
	public Screen screen;
	public String name;
	public int x;
	public int y;
	private Player player = Game.player;
	private int heartColor = Colors.get(-1, 111, 411, 555);
	
	private int fullHearts=0;
	private int halfHearts=0;
	private int emptyHearts=0;
	
	public Scoreboard(Level level, Screen screen, String name, int x, int y)
	{
		super(level);
		this.screen=screen;
		this.name=name;
		this.x=x;
		this.y=y;
		this.zIndex=1;
	}
	
	public void tick()
	{
		fullHearts=0;
		halfHearts=0;
		emptyHearts=0;
		
		int hp = player.healthPoints;
		fullHearts=hp/10;
		if (hp%10==5) halfHearts++;
		emptyHearts=10-(fullHearts+halfHearts);
	}
	
	public void gameOver()
	{
		String message="game over";
		int scale=2;
		Font.render(message, screen, player.x-((message.length()*(8*scale))/2), player.y, Colors.get(-1, -1, -1, 000), scale);
		//Game.isRunning=false;
	}

	public void render(Screen screen)
	{
		Font.render("SCORE: "+player.score, screen, x, y+16, Colors.get(-1, -1, -1, 555), 1);
		Font.render("KILLS: "+player.monstersKilled, screen, x, y+32, Colors.get(-1, -1, -1, 555), 1);
		
		double accRatio=0;
		if (Bullet.bulletsMissed==0) accRatio=1;
		else{accRatio=1-((double)Bullet.bulletsMissed/(double)Bullet.bulletsShot);}
		if (accRatio<0) accRatio=0;
		if (accRatio>1) accRatio=1;
		player.accuracy=accRatio;
		Font.render("ACC: "+(int)Math.floor(accRatio*100)+"%", screen, x, y+48, Colors.get(-1, -1, -1, 555), 1);
		
		for (int i=0; i<fullHearts;i++)
		{
			screen.renderField((x*i)+16, y, heartColor, 0, 2, 20, 21, 1);
			/*
			screen.render(x+(i*16), y+16, 0 + 20 * 32, heartColor, 0, 1);
			screen.render(x+(i*16)+8, y+16, 1 + 20 * 32, heartColor, 0, 1);
			screen.render(x+(i*16), y+24, 0 + 21 * 32, heartColor, 0, 1);
			screen.render(x+(i*16)+8, y+24, 1 + 21 * 32, heartColor, 0, 1);
			*/
		}
		
		if (halfHearts==1)
		{
			screen.renderField((x*fullHearts)+16, y, heartColor, 2, 4, 20, 21, 1);
			/*
			screen.render(x+(fullHearts*16), y+16, 2 + 20 * 32, heartColor, 0, 1);
			screen.render(x+(fullHearts*16)+8, y+16, 3 + 20 * 32, heartColor, 0, 1);
			screen.render(x+(fullHearts*16), y+24, 2 + 21 * 32, heartColor, 0, 1);
			screen.render(x+(fullHearts*16)+8, y+24, 3 + 21 * 32, heartColor, 0, 1);
			*/
		}
		
		for (int i=0; i<emptyHearts;i++)
		{
			screen.renderField((x*(fullHearts+halfHearts)+i*16)+16, y, heartColor, 4, 6, 20, 21, 1);
			/*
			screen.render(x+(i*16)+((fullHearts+halfHearts)*16), y+16, 4 + 20 * 32, heartColor, 0, 1);
			screen.render(x+(i*16)+((fullHearts+halfHearts)*16)+8, y+16, 5 + 20 * 32, heartColor, 0, 1);
			screen.render(x+(i*16)+((fullHearts+halfHearts)*16), y+24, 4 + 21 * 32, heartColor, 0, 1);
			screen.render(x+(i*16)+((fullHearts+halfHearts)*16)+8, y+24, 5 + 21 * 32, heartColor, 0, 1);
			*/
		}
		
		screen.renderField(x+175, y, heartColor, 0, 6, 9, 13, 2);
		
		if (player.healthPoints<=0) gameOver();
	}
}
