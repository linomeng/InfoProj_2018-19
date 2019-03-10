package infproj.game.entities;

import infproj.game.Game;
import infproj.game.gfx.Screen;
import infproj.game.level.Level;

public abstract class Entity
{
	public int x, y;
	protected Level level;
	
	public int zIndex=0;
	
	public Entity(Level level)
	{
		init(level);
	}

	public final void init(Level level)
	{
		this.level = level;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen);
	
	public double calculateDist(int x1, int x2, int y1, int y2)
	{
		return (Math.sqrt(
				((x2-x1)*(x2-x1)) +
				((y2-y1)*(y2-y1))
				));
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public int generateRandomNumber(int min, int max)
	{
		return (int)((Math.random()*max)+min);
	}
}
