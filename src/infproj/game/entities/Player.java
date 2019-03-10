package infproj.game.entities;

import infproj.game.Game;
import infproj.game.InputHandler;
import infproj.game.gfx.Colors;
import infproj.game.gfx.Font;
import infproj.game.gfx.Screen;
import infproj.game.level.Level;

public class Player extends Mob
{
	private InputHandler input;
	private int defaultSpeed=2;
	private int scale=1;
	private String name="Player";
	private int color = Colors.get(-1, 111, 145, 543);
	int canShootTimer=0;
	boolean canShoot=false;
	private int xSpeed=1;
	private int ySpeed=1;
	public boolean isRunning=false;
	public int healthPoints=100;
	public int score=0;
	public int monstersKilled=0;

	public Player(Level level, int x, int y, InputHandler input) {
		super(level, "Player", x, y, 1);
		this.input=input;
		this.x=x;
		this.y=y;
		this.zIndex=1;
	}

	public void tick() {
		if (healthPoints<=0) die();
		randomGenerateMonster();
		int fastSpeed=1;
		isRunning=false;
		int xa = 0;
		int ya = 0;
		if (input.up.isPressed()) { ya--; }
		if (input.left.isPressed()) { xa--; }
		if (input.down.isPressed()) { ya++; }
		if (input.right.isPressed()) { xa++; }
		if (input.run.isPressed()) { isRunning=true; fastSpeed++; }
		
		if (!((input.up.isPressed() && input.left.isPressed() | input.right.isPressed()) ||
			(input.down.isPressed() && input.left.isPressed() | input.right.isPressed())))
		{ 		
			xa=(xa*(fastSpeed));
			ya=(ya*(fastSpeed)); 
		}
		
		if (xa!=0 || ya!=0)
		{
			if ( ya < 0 && xa == 0) movingDir = 0;
			if ( ya > 0 && xa == 0) movingDir = 1;
			if ( xa < 0 && ya == 0) movingDir = 2;
			if ( xa > 0 && ya == 0) movingDir = 3;
			if ( xa > 0 && ya < 0 ) movingDir = 4;
			if ( xa < 0 && ya > 0 ) movingDir = 5;
			if ( xa < 0 && ya < 0 ) movingDir = 6;
			if ( xa > 0 && ya > 0 ) movingDir = 7;
			move (xa*xSpeed, ya*ySpeed); isMoving = true;
		}
		else {
			isMoving = false;
		}
		
		if (canShootTimer>15) canShoot=true;
		else { canShootTimer+=1; }
		
		if (canShoot && input.attack.isPressed()) { canShootTimer=0; shoot();}
		canShoot=false;
		
	}
	
	private void shoot()
	{
		if (!isRunning) { new Bullet(level, "lastShot", x, y, defaultSpeed+1, movingDir); }
	}

	public Screen currentScreen;
	
	public void render(Screen screen) {
		currentScreen = screen;
		int xTile = 0;
		int yTile = 28;
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
	
	public void changeHP(int damage)
	{
		if (!(this.healthPoints<=0)) 
		this.healthPoints-=damage;
	}
	
	public void changeScore(int scorePoints)
	{
		changeTotalScore(scorePoints*monstersKilled);
	}
	
	double currentChance=1;
	
	public void updateCurrentChance()
	{
		currentChance++;
		currentChance-=(Monster.monsterArray.size())/currentChance;
		
		if (currentChance<1.5) currentChance=1.5;
		System.out.println("yuh"+currentChance);
	}
	
	public void randomGenerateMonster()
	{
		if ((double)generateRandomNumber(0,499/(int)currentChance)<currentChance)
		{
			new Monster(level, "Monsta!", generateRandomNumber(8,300), generateRandomNumber(8,300), 1);
		}
	}
	
	public void changeTotalScore(int change)
	{
		if (!(score+change+1<=0)) score+=change;
	}
	
	public void die()
	{
		level.addToEntityRemovelist(this);
	}

}
