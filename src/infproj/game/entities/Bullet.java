package infproj.game.entities;

import java.util.ArrayList;

import infproj.game.Game;
import infproj.game.gfx.Colors;
import infproj.game.gfx.Screen;
import infproj.game.level.Level;

public class Bullet extends Projectile {

	public int moveDir=0;
	public int damage=10;
	private int color = Colors.get(-1, 111, 430, 555);
	private int scale=1;
	public int scorePoints=2;
	Player player = Game.player;
	public ArrayList<Object> monsterArray = Monster.monsterArray;
	public int range=10;
	public static int bulletsShot=0;
	public static int bulletsMissed=0;
	
	public Bullet(Level level, String name, int x, int y, int speed, int moveDir)
	{
		super(level, name, x, y, speed);
		this.level=level;
		this.name=name;
		this.x=x;
		this.y=y;
		this.speed=speed;
		this.moveDir=moveDir;

		level.addToEntityWaitlist(this);
	}

	public void tick() 
	{
		int xa=0; int ya=0;
		if (moveDir==0) { xa = 0; ya-=1;}
		if (moveDir==1) { xa = 0; ya =1;}
		if (moveDir==2) { xa-= 1; ya =0;}
		if (moveDir==3) { xa = 1; ya =0;}
		if (moveDir==4) { xa +=1; ya-=1;}
		if (moveDir==5) { xa -=1; ya =1;}
		if (moveDir==6) { xa -=1; ya-=1;}
		if (moveDir==7) { xa  =1; ya =1;}
		
		checkForEntityCollision();
		
		if (!hasCollided(xa,ya))
		{
			move(xa, ya);
		}
		else
		{
			die(false);
			// Unfertig: An Wand abprallen
			//if (moveDir%2==0) moveDir++;
			//else { moveDir--; }
			//if (moveDir==(4 | 5 | 6 | 7)) moveDir=7;
		}
	}
	
	public void checkForEntityCollision()
	{
		for (int i=0; i<monsterArray.size(); i++)
		{
			Object current = monsterArray.get(i);
			if (calculateDist(x, ((Monster) current).getX(), y,((Monster)current).getY())<=range)
			{
				((Monster)current).changeHP(damage);
				die(true);
			}
		}
	}
	
	public void render(Screen screen) {
		int xTile = moveDir;
		int yTile = 27;
		screen.render(x, y, xTile + yTile * 32, color, 0, scale);
	}

	public boolean hasCollided(int xa, int ya) {
		int xMin = 2;
		int xMax = 5;
		int yMin = 2;
		int yMax = 5;
		
		for (int x = xMin; x < xMax; x++){if (isSolidTile(xa, ya, x, yMin)) return true;}
		for (int x = xMin; x < xMax; x++){if (isSolidTile(xa, ya, x, yMax)) return true;}
		for (int y = yMin; y < yMax; y++){if (isSolidTile(xa, ya, xMin, y)) return true;}
		for (int y = yMin; y < yMax; y++){if (isSolidTile(xa, ya, xMax, y)) return true;}
		return false;
	}
	
	public void die(boolean cause)
	{
		if (!cause) bulletsMissed++;
		else bulletsShot++;
		die();
	}

	@Override
	public void die() {
		level.addToEntityRemovelist(this);
		
	}

}
