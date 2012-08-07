package mygame;

import com.jme3.material.Material;

/**
 * This is the definition of a certain type of floor to easier save all data about the specific types of floors
 * 
 * @author lohnn
 */
public class TileType
{
    private String tileType;
    private int height;
    private Material material;

    /**
     * 
     * @param floorType
     * @param material
     * @param height Height where 0 = pit/water, 1 = normal/ground, 2 = walls
     */
    public TileType(String floorType, Material material, int height)
    {
        this.tileType = floorType;
        this.material = material;
        this.height = height;
    }

    /**
     * Floor types:
     * Dirt - (dirt to be taken over by imps)
     * Unused - Unused floor owned by a player
     * 
     * @return the floorType
     */
    public String getTileType()
    {
        return tileType;
    }

    /**
     * @param floorType the floorType to set
     */
    public void setTileType(String floorType)
    {
        this.tileType = floorType;
    }

    /**
     * @return the material
     */
    public Material getMaterial()
    {
        return material;
    }

    /**
     * @param material the material to set
     */
    public void setMaterial(Material material)
    {
        this.material = material;
    }

    /**
     * @return the height
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height)
    {
        this.height = height;
    }
}
