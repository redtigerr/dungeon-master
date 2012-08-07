package custom.mesh;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/**
 * Creates the custom walls for the game
 * TODO: have to find a good way to know which vertex are which to being able to reuse the positions for the next wall piece
 *
 * @author lohnn
 */
public class Wall
{
    Geometry geo;

    /**
     * Creates a wall piece
     * Sides = true when there's *not* a connected wall piece
     * 0 = back
     * 1 = front
     * 2 = left
     * 3 = right
     * 
     * @param position The position of the wall piece
     * @param sides What walls it is *not* connected to
     */
    public Wall(Vector2f position, boolean[] sides)
    {
        createWall(position, sides);
    }

    /**
     * Creates a wall piece with all sides set to the same
     * 
     * @param position
     * @param sides 
     */
    public Wall(Vector2f position, boolean side)
    {
        boolean[] sides = {side, side, side, side};
        createWall(position, sides);
    }

    private void createWall(Vector2f position, boolean[] sides)
    {
        Mesh m = new Mesh();

        //top
        Vector3f[] vertices = new Vector3f[20];
        vertices[0] = new Vector3f(position.x - 0.5f, 1.1f, position.y - 0.5f); //--
        vertices[1] = new Vector3f(position.x - 0.5f, 1.1f, position.y + 0.5f); //-+
        vertices[2] = new Vector3f(position.x + 0.5f, 1.1f, position.y - 0.5f); //+-
        vertices[3] = new Vector3f(position.x + 0.5f, 1.1f, position.y + 0.5f); //++*

        float[] texCoord = {
            0, 0, 1, 0, 0, 1, 1, 1, // top
            0, 0, 1, 0, 0, 1, 1, 1, // left
            0, 0, 1, 0, 0, 1, 1, 1, // right
            0, 0, 1, 0, 0, 1, 1, 1, // back
            0, 0, 1, 0, 0, 1, 1, 1 // front
        };
        Vector3f[] normals = new Vector3f[20];
        normals[0] = new Vector3f(0, 1, 0);
        normals[1] = new Vector3f(0, 1, 0);
        normals[2] = new Vector3f(0, 1, 0);
        normals[3] = new Vector3f(0, 1, 0);

        int[] indexes = new int[30];
        indexes[0] = 2;
        indexes[1] = 0;
        indexes[2] = 1;
        indexes[3] = 1;
        indexes[4] = 3;
        indexes[5] = 2;

        int i = 4;
        int j = 6;

        //front
        if(sides[0] == true) {
            vertices[i] = new Vector3f(position.x + 0.5f, 1.1f, position.y + 0.5f); //--
            vertices[i + 1] = new Vector3f(position.x - 0.5f, 1.1f, position.y + 0.5f); //-+
            vertices[i + 2] = new Vector3f(position.x + 0.5f, 0.1f, position.y + 0.5f); //+-
            vertices[i + 3] = new Vector3f(position.x - 0.5f, 0.1f, position.y + 0.5f); //++

            indexes[j] = i + 2;
            indexes[j + 1] = i;
            indexes[j + 2] = i + 1;
            indexes[j + 3] = i + 1;
            indexes[j + 4] = i + 3;
            indexes[j + 5] = i + 2;

            normals[i] = new Vector3f(0, 0, 1);
            normals[i + 1] = new Vector3f(0, 0, 1);
            normals[i + 2] = new Vector3f(0, 0, 1);
            normals[i + 3] = new Vector3f(0, 0, 1);

            i += 4;
            j += 6;
        }
        //back

        if(sides[1] == true) {
            vertices[i] = new Vector3f(position.x - 0.5f, 1.1f, position.y - 0.5f); //--
            vertices[i + 1] = new Vector3f(position.x + 0.5f, 1.1f, position.y - 0.5f); //-+
            vertices[i + 2] = new Vector3f(position.x - 0.5f, 0.1f, position.y - 0.5f); //+-
            vertices[i + 3] = new Vector3f(position.x + 0.5f, 0.1f, position.y - 0.5f); //++

            indexes[j] = i + 2;
            indexes[j + 1] = i;
            indexes[j + 2] = i + 1;
            indexes[j + 3] = i + 1;
            indexes[j + 4] = i + 3;
            indexes[j + 5] = i + 2;

            normals[i] = new Vector3f(0, 0, -1);
            normals[i + 1] = new Vector3f(0, 0, -1);
            normals[i + 2] = new Vector3f(0, 0, -1);
            normals[i + 3] = new Vector3f(0, 0, -1);

            i += 4;
            j += 6;
        }
        //left

        if(sides[2] == true) {
            vertices[i] = new Vector3f(position.x - 0.5f, 1.1f, position.y + 0.5f); //--
            vertices[i + 1] = new Vector3f(position.x - 0.5f, 1.1f, position.y - 0.5f); //-+
            vertices[i + 2] = new Vector3f(position.x - 0.5f, 0.1f, position.y + 0.5f); //+-
            vertices[i + 3] = new Vector3f(position.x - 0.5f, 0.1f, position.y - 0.5f); //++

            indexes[j] = i + 2;
            indexes[j + 1] = i;
            indexes[j + 2] = i + 1;
            indexes[j + 3] = i + 1;
            indexes[j + 4] = i + 3;
            indexes[j + 5] = i + 2;

            normals[i] = new Vector3f(-1, 0, 0);
            normals[i + 1] = new Vector3f(-1, 0, 0);
            normals[i + 2] = new Vector3f(-1, 0, 0);
            normals[i + 3] = new Vector3f(-1, 0, 0);

            i += 4;
            j += 6;
        }
        //right

        if(sides[3] == true) {
            vertices[i] = new Vector3f(position.x + 0.5f, 1.1f, position.y - 0.5f); //--
            vertices[i + 1] = new Vector3f(position.x + 0.5f, 1.1f, position.y + 0.5f); //-+
            vertices[i + 2] = new Vector3f(position.x + 0.5f, 0.1f, position.y - 0.5f); //+-
            vertices[i + 3] = new Vector3f(position.x + 0.5f, 0.1f, position.y + 0.5f); //++

            indexes[j] = i + 2;
            indexes[j + 1] = i;
            indexes[j + 2] = i + 1;
            indexes[j + 3] = i + 1;
            indexes[j + 4] = i + 3;
            indexes[j + 5] = i + 2;

            normals[i] = new Vector3f(1, 0, 0);
            normals[i + 1] = new Vector3f(1, 0, 0);
            normals[i + 2] = new Vector3f(1, 0, 0);
            normals[i + 3] = new Vector3f(1, 0, 0);
        }

        m.setBuffer(Type.Position,
                3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(Type.Normal,
                3, BufferUtils.createFloatBuffer(normals));
        m.setBuffer(Type.TexCoord,
                2, BufferUtils.createFloatBuffer(texCoord));
        m.setBuffer(Type.Index,
                1, BufferUtils.createIntBuffer(indexes));
        m.updateBound();
        geo = new Geometry("OurMesh", m);
//        geo.setMaterial(dirt.getMaterial());
//        tile[x][y] = new Tile(0, geo);
//
//        mapParts.attachChild(geo);
    }

    public Geometry getGeo()
    {
        return geo;
    }
}