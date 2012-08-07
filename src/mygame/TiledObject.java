package mygame;

import com.jme3.scene.Spatial;
import javax.vecmath.Vector2d;

/**
 *
 * @author lohnn
 */
public class TiledObject
{
    private Vector2d size, position;
    private Spatial geo;

    public TiledObject(Spatial geomery, Vector2d size)
    {
        this.size = size;
        geo = geomery;
    }

    /**
     * @return the size
     */
    public Vector2d getSize()
    {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Vector2d size)
    {
        this.size = size;
    }

    /**
     * @return the geo
     */
    public Spatial getGeo()
    {
        return geo;
    }

    /**
     * @param geo the geo to set
     */
    public void setGeo(Spatial geo)
    {
        this.geo = geo;
    }

    /**
     * @return the position
     */
    public Vector2d getPosition()
    {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Vector2d position)
    {
        this.position = position;
    }
}
