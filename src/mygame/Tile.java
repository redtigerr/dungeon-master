package mygame;

import com.jme3.scene.Geometry;

/**
 * This is the definition of a floor piece/tile
 * 
 * @author lohnn
 */
public class Tile
{
    private int owner;
    private TileType tileType;
    private Geometry geo;

    public Tile(int owner, Geometry geo, TileType tileType)
    {
        this.owner = owner;
        this.tileType = tileType;
        this.geo = geo;
    }
    
    public void replaceTile(Tile tile)
    {
        owner = tile.owner;
        geo = tile.geo;
        tileType = tile.getTileType();
    }

    public void replaceTile(int owner, Geometry geo, TileType tileType)
    {
        this.owner = owner;
        this.tileType = tileType;
        this.geo = geo;
    }

    /**
     * @return the owner
     */
    public int getOwner()
    {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(int owner)
    {
        this.owner = owner;
    }

    /**
     * @return the TileType
     */
    public TileType getTileType()
    {
        return tileType;
    }

    /**
     * @param floortype the TileType to set
     */
    public void setTiletypesetTileType(TileType tileType)
    {
        this.tileType = tileType;
    }

    /**
     * @return the geo
     */
    public Geometry getGeometry()
    {
        return geo;
    }

    /**
     * @param geo the geo to set
     */
    public void setGeometry(Geometry geo)
    {
        this.geo = geo;
    }
}
