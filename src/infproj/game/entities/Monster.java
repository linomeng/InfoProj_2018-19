package infproj.game.entities;

import java.util.ArrayList;

import infproj.game.Game;
import infproj.game.gfx.Colors;
import infproj.game.gfx.Screen;
import infproj.game.level.Level;

public class Monster extends Mob {
	
	public int range=10;
	protected Player player = Game.player; 
	protected int damage = 5;
	private int color = Colors.get(-1, 111, 555, 311);
	public boolean isMoving=false;
	public int healthPoints=10;
	public int scorePoints=20;
	
	public static ArrayList<Object> monsterArray = new ArrayList<Object>(); 

	public Monster(Level level, String name, int x, int y, int speed) {
		super(level, name, x, y, speed);
		this.level=level;
		this.name=name;
		this.x=x;
		this.y=y;
		this.speed=speed;
		monsterArray.add(this);
		level.addToEntityWaitlist(this);
	}
	
		int canAttackTimer=0;
		boolean canAttack=false;
		boolean canMove=true;
	
	public void tick()
	{
		if (healthPoints<=0) die();
		
		isMoving=false;
		int xa = 0;
		int ya = 0;
		xa = compareCoords(x, player.x);
		ya = compareCoords(y, player.y);
		if (canMove) move(xa+(speed-1), ya+(speed-1));
		if (xa!=0 || ya!=0)
		{
			isMoving=true;
			if ( ya < 0 && xa == 0) movingDir = 0;
			if ( ya > 0 && xa == 0) movingDir = 1;
			if ( xa < 0 && ya == 0) movingDir = 2;
			if ( xa > 0 && ya == 0) movingDir = 3;
			if ( xa > 0 && ya < 0 ) movingDir = 4;
			if ( xa < 0 && ya > 0 ) movingDir = 5;
			if ( xa < 0 && ya < 0 ) movingDir = 6;
			if ( xa > 0 && ya > 0 ) movingDir = 7;
		}
		
		if (canAttackTimer>20) canAttack=true; else { canAttackTimer+=1; }
		if (canAttack && calculateDist(x, player.x, y, player.y)<=range) 
		{	
			attack(player);
			canAttackTimer=0;
		}
		canAttack=false;
		canMove=!canMove;
	}
	
	public int compareCoords(int x1, int x2)
	{
		if (x1>x2) return -speed;
		if (x1<x2) return speed;
		return 0;
	}
	
	public void render(Screen screen)
	{
		int xTile = 0;
		int yTile = 22;
		int walkingSpeed=3;
		int flipTop =		(numSteps >> walkingSpeed) & 1;
		int flipBottom =	(numSteps >> walkingSpeed) & 1;
		
		if (movingDir%4 == 1) xTile+=2;
		else if (movingDir%4 > 1)
				{	xTile+=4+((numSteps >> walkingSpeed)&1)*2;
					flipTop = (movingDir%4 - 1) % 2;
				}
		
		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 4;
		
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, color, flipTop, scale);
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile+1) + yTile * 32, color, flipTop, scale);
		screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile+1) * 32, color, flipBottom, scale);
		screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile+1) + (yTile+1) * 32, color, flipBottom, scale);
	}
	
	public void attack(Player player)
	{
		player.changeHP(damage);
	}
	
	public void changeHP(int damage)
	{
		if (!(this.healthPoints<=0)) 
		this.healthPoints-=damage;
	}

	public void die() 
	{
		player.monstersKilled++;
		player.updateCurrentChance();
		player.changeScore(scorePoints);
		for (int i=0; i<monsterArray.size(); i++)
		{
			if (this.equals(monsterArray.get(i)))
			{
				monsterArray.remove(i);
				break;
			}
		}
		level.addToEntityRemovelist(this);
	}
}
