package infproj.game.entities;
import infproj.game.level.Level;
import infproj.game.level.tiles.Tile;

public abstract class Mob extends Entity
{
	protected String name;
	protected int speed=1;
	protected int numSteps=0;
	protected boolean isMoving = false;
	protected int movingDir = 1;
	protected int scale = 1;
	
	public int healthPoints=25;
	
	public Mob(Level level, String name, int x, int y, int speed) {
		super(level);
		this.name=name;
		this.x=x;
		this.y=y;
		this.speed=speed;
	}
	
	public void move(int xa, int ya)
	{
		if ( xa!=0 && ya!=0 )
		{
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}
		numSteps++;
		if (!hasCollided(xa,ya))
		{
			x+=xa*speed;
			y+=ya*speed;
		}
	}
	
	public int getMovingDirection()
	{
		return this.movingDir;
	}
	
	public boolean hasCollided(int xa, int ya)
	{
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;
		
		for (int x = xMin; x < xMax; x++){if (isSolidTile(xa, ya, x, yMin)) return true;}
		for (int x = xMin; x < xMax; x++){if (isSolidTile(xa, ya, x, yMax)) return true;}
		for (int y = yMin; y < yMax; y++){if (isSolidTile(xa, ya, xMin, y)) return true;}
		for (int y = yMin; y < yMax; y++){if (isSolidTile(xa, ya, xMax, y)) return true;}
		return false;
	}
	
	protected boolean isSolidTile(int xa, int ya, int x, int y)
	{
		if (level==null) { return false; }
		Tile lastTile = level.getTile((this.x + x)>>3, (this.y +y)>>3);
		Tile newTile = level.getTile((this.x + x + xa)>>3, (this.y + y + ya)>>3);
		if (newTile.isSolid() && !lastTile.equals(newTile)) { return true; }
		return false;
	}
	
	public String getName()
	{
		return name;
	}
	
	public abstract void die();
}
