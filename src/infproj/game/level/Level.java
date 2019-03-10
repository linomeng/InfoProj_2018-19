package infproj.game.level;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import infproj.game.entities.Entity;
import infproj.game.gfx.Screen;
import infproj.game.level.tiles.Tile;

public class Level {
	private byte[] tiles;
	public int width;
	public int height;
	public List<Entity> entities = new ArrayList<Entity>();
	public int id=0;
	
	public String levelData="";
	
	public Level(int width, int height, int id)
	{
			tiles = new byte[width*height];
			this.width = width;
			this.height = height;
			this.generateLevel();
			this.id=id;
	}
	
	public void generateLevel()
	{
		try {setLevelData(id);} catch (IOException e) {e.printStackTrace();}
		if (getCurrentLevelData().length()==0)
		{
			loadLevel(processRawLevelData(getCurrentLevelData()));
		}
		else
		{
			for (int y=0; y < height; y++)
			{	for (int x=0; x < width; x++)
				{
					if (y==0||x==0||x==width-1||y==height-1) tiles[x+y*width] = Tile.STONE.getId();
					else if (y>=width) tiles[x+y*width] = Tile.STONE.getId();
					else tiles[x+y*width] = Tile.GRASS.getId();
				}
			}
		}
	}
	
	public void addLevelData(int x, int y, int id)
	{
		int xLength=(x+"").length();
		int yLength=(y+"").length();
		int idLength=(id+"").length();
		
		for (int i=0; i<(4-xLength); i++) { levelData+="0"; }
		levelData+=x+",";
		for (int i=0; i<(4-yLength); i++) { levelData+="0"; }
		levelData+=y+",";
		for (int i=0; i<(4-idLength); i++) { levelData+="0"; }
		levelData+=id+".";
	}
	
	public void loadLevel(ArrayList<int[]> processedLevelData)
	{
		System.out.println(processedLevelData.size());
		for (int i=0; i < processedLevelData.size(); i++)
		{	
			int[] currentTile=processedLevelData.get(i);
			int x = processedLevelData.get(i)[0];
			int y = processedLevelData.get(i)[1];
			int id = processedLevelData.get(i)[2];
			if (currentTile!=null) tiles[x+y*width] = (byte)id;
		}
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	int modX=1;
	int modY=1;
	int modId=1;
	
	public ArrayList<int[]> processRawLevelData(String rawLevelData)
	{
		ArrayList<int[]> processedLevelData = new ArrayList<int[]>();
		for (int i=0; i<rawLevelData.length();i+=15)
		{
			int[] temp = new int[3];
			temp[0]+=(Integer.parseInt(rawLevelData.substring(i,i+4)));
			temp[1]+=(Integer.parseInt(rawLevelData.substring(i+5,i+9)));
			temp[2]+=(Integer.parseInt(rawLevelData.substring(i+10,i+14)));
			processedLevelData.add(temp);
		}
		return processedLevelData;
	}
	
	public void setLevelData(int levelID) throws IOException
	{
		String content = new String(Files.readAllBytes(Paths.get("res/level"+levelID+".txt")));
		levelData=content;
	}
	
	public String getCurrentLevelData()
	{
		return levelData;
	}

	
	public void tick()
	{
		addEntities(entitiesToAdd);
		entitiesToAdd.removeAll(entitiesToAdd);
		for (Entity e : entities)
		{
			e.tick();
		}
		removeEntities(entitiesToRemove);
		entitiesToRemove.removeAll(entitiesToRemove);
	}
	
	public void renderTiles(Screen screen, int xOffset, int yOffset)
	{
		if (xOffset < 0) xOffset = 0;
		if (xOffset > ((width<<3)-screen.width)) xOffset = ((width<<3)-screen.width);
		if (yOffset < 0) yOffset = 0;
		if (yOffset > ((height<<3)-screen.height)) yOffset = ((height<<3)-screen.height);
		
		screen.setOffset(xOffset, yOffset);
		
		for (int y=0; y < height; y++)
		{	for (int x=0; x < width; x++)
			{
				getTile(x,y).render(screen, this, x<<3, y<<3);
			}
		}
	}
	
	public void renderEntities(Screen screen)
	{
		for (Entity e : entities)
		{
			if (e.zIndex==0) e.render(screen);
		}
		for (Entity e : entities)
		{
			if (e.zIndex==1) e.render(screen);
		}
	}

	public Tile getTile(int x, int y) {
		if (0 > x || x >= width || 0 > y || y >= height) return Tile.VOID;
		return Tile.tiles[tiles[x+y*width]];
	}
	
	public void addEntity(Entity entity)
	{
		this.entities.add(entity);
	}
	
	public void addEntities(ArrayList<Entity> entities)
	{
		for (Entity e : entities)
		{
			addEntity(e);
		}
	}
	
	ArrayList<Entity> entitiesToAdd = new ArrayList<Entity>();
	
	public void addToEntityWaitlist(Entity entity)
	{
		entitiesToAdd.add(entity);
	}
	
	public void removeEntity(Entity entity)
	{
		for (int i=0; i<entities.size(); i++)
		{
			if (entities.get(i).equals(entity)) entities.remove(i);
		}
	}
	
	public void removeEntities(ArrayList<Entity> entities)
	{
		for (Entity e : entities)
		{
			removeEntity(e);
		}
	}
	
	ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();
	
	public void addToEntityRemovelist(Entity entity)
	{
		entitiesToRemove.add(entity);
	}
}
