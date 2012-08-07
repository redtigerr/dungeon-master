package custom.mesh;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/**
 *
 * @author lohnn
 */
public class CustomMeshes
{
    public Geometry selectionBox(Vector2f position)
    {
        Mesh m = new Mesh();

        Geometry geom = new Geometry("OurMesh", m);

        return geom;
    }

    public Mesh quad(Vector3f start, Vector3f end)
    {
        Mesh m = new Mesh();

        Vector3f[] vertices = new Vector3f[4];
        vertices[0] = start;
        vertices[1] = new Vector3f(end.x, start.y, end.z);
        vertices[2] = new Vector3f(start.x, end.y, start.z);
        vertices[3] = end;

        Vector2f[] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0, 0);
        texCoord[1] = new Vector2f(1, 0);
        texCoord[2] = new Vector2f(0, 1);
        texCoord[3] = new Vector2f(1, 1);

        int[] indexes =
        {
            2, 0, 1, 1, 3, 2
        };

        m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        m.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
        m.updateBound();

        return m;
    }
}
