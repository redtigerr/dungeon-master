package custom.mesh;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * TODO: move the tile-geometry making in here
 * @author lohnn
 */
public class CreateGeoms
{
    private Material m;
    private Box b;

    public CreateGeoms(SimpleApplication simpleApplication)
    {
        m = new Material(simpleApplication.getAssetManager(), "Commom/MatDefs/Misc/Unshaded.j3md");
        b = new Box(Vector3f.ZERO, 1, 1, 1);
    }

    public Geometry createBox()
    {
        Geometry box = new Geometry("box", b);
        box.setMaterial(m);
        return box;
    }
}
