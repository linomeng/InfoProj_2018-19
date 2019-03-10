package infproj.game.entities;

import infproj.game.gfx.Colors;
import infproj.game.gfx.Screen;
import infproj.game.level.Level;
import infproj.game.gfx.Colors;

public abstract class Projectile extends Mob
{
	public int damageTotal;
	public int damagePerc;
	private int color = Colors.get(-1, 111, 145, 543);
	
	public Projectile(Level level, String name, int x, int y, int speed)
	{
		super(level, name, x, y, speed);
		this.name=name;
		this.x=x;
		this.y=y;
		this.speed=speed;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen);

}
