package custom.mesh;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.AbstractBox;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import mygame.SelectionArea;

/**
 * @author Lohnn
 */
public class SelectionBox extends AbstractBox
{
    private Geometry geo;
    private static final short[] GEOMETRY_INDICES_DATA =
    {
        2, 1, 0, 3, 2, 0, // back
        6, 5, 4, 7, 6, 4, // right
        10, 9, 8, 11, 10, 8, // front
        14, 13, 12, 15, 14, 12, // left
        18, 17, 16, 19, 18, 16 // top
    };
    private static final float[] GEOMETRY_NORMALS_DATA =
    {
        0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, // back
        1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, // right
        0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, // front
        -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, // left
        0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0 // top
    };
    private static final float[] GEOMETRY_TEXTURE_DATA =
    {
        1, 0, 0, 0, 0, 1, 1, 1, // back
        1, 0, 0, 0, 0, 1, 1, 1, // right
        1, 0, 0, 0, 0, 1, 1, 1, // front
        1, 0, 0, 0, 0, 1, 1, 1, // left
        1, 0, 0, 0, 0, 1, 1, 1 // top
    };

    /**
     * Creates a new box.
     * <p>
     * The box has a center of 0,0,0 and extends in the out from the center by
     * the given amount in <em>each</em> direction. So, for example, a box
     * with extent of 0.5 would be the unit cube.
     *
     * @param name the name of the box.
     * @param x the size of the box along the x axis, in both directions.
     * @param y the size of the box along the y axis, in both directions.
     * @param z the size of the box along the z axis, in both directions.
     */
    public SelectionBox(float x, float y, float z)
    {
        super();
        updateGeometry(Vector3f.ZERO, x, y, z);
        geo = new Geometry("SelectionBox", this);
    }

    /**
     * Creates a new box.
     * <p>
     * The box has the given center and extends in the out from the center by
     * the given amount in <em>each</em> direction. So, for example, a box
     * with extent of 0.5 would be the unit cube.
     * 
     * @param name the name of the box.
     * @param center the center of the box.
     * @param x the size of the box along the x axis, in both directions.
     * @param y the size of the box along the y axis, in both directions.
     * @param z the size of the box along the z axis, in both directions.
     */
    public SelectionBox(Vector3f center, float x, float y, float z)
    {
        super();
        updateGeometry(center, x, y, z);
        geo = new Geometry("SelectionBox", this);
    }

    /**
     * Constructor instantiates a new <code>Box</code> object.
     * <p>
     * The minimum and maximum point are provided, these two points define the
     * shape and size of the box but not itâ€™s orientation or position. You should
     * use the {@link #setLocalTranslation()} and {@link #setLocalRotation()}
     * methods to define those properties.
     * 
     * @param name the name of the box.
     * @param min the minimum point that defines the box.
     * @param max the maximum point that defines the box.
     */
    public SelectionBox(Vector3f min, Vector3f max)
    {
        super();
        updateGeometry(min, max);
        geo = new Geometry("SelectionBox", this);
    }

    /**
     * Empty constructor for serialization only. Do not use.
     */
    public SelectionBox()
    {
        super();
    }

    /**
     * Creates a clone of this box.
     * <p>
     * The cloned box will have â€˜_cloneâ€™ appended to itâ€™s name, but all other
     * properties will be the same as this box.
     */
    @Override
    public SelectionBox clone()
    {
        return new SelectionBox(center.clone(), xExtent, yExtent, zExtent);
    }

    protected void duUpdateGeometryIndices()
    {
        if(getBuffer(Type.Index) == null)
        {
            setBuffer(Type.Index, 3, BufferUtils.createShortBuffer(GEOMETRY_INDICES_DATA));
        }
    }

    protected void duUpdateGeometryNormals()
    {
        if(getBuffer(Type.Normal) == null)
        {
            setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(GEOMETRY_NORMALS_DATA));
        }
    }

    protected void duUpdateGeometryTextures()
    {
        if(getBuffer(Type.TexCoord) == null)
        {
            setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(GEOMETRY_TEXTURE_DATA));
        }
    }

    protected void duUpdateGeometryVertices()
    {
        FloatBuffer fpb = BufferUtils.createVector3Buffer(20);
        Vector3f[] v = computeVertices();
        fpb.put(new float[]
                {
                    v[0].x, v[0].y, v[0].z, v[1].x, v[1].y, v[1].z, v[2].x, v[2].y, v[2].z, v[3].x, v[3].y, v[3].z, // back
                    v[1].x, v[1].y, v[1].z, v[4].x, v[4].y, v[4].z, v[6].x, v[6].y, v[6].z, v[2].x, v[2].y, v[2].z, // right
                    v[4].x, v[4].y, v[4].z, v[5].x, v[5].y, v[5].z, v[7].x, v[7].y, v[7].z, v[6].x, v[6].y, v[6].z, // front
                    v[5].x, v[5].y, v[5].z, v[0].x, v[0].y, v[0].z, v[3].x, v[3].y, v[3].z, v[7].x, v[7].y, v[7].z, // left
                    v[2].x, v[2].y, v[2].z, v[6].x, v[6].y, v[6].z, v[7].x, v[7].y, v[7].z, v[3].x, v[3].y, v[3].z // top
                });
        setBuffer(Type.Position, 3, fpb);
        updateBound();
    }

    /**
     * @return the geo
     */
    public Geometry getGeo()
    {
        return geo;
    }

    /**
     * @param geo the geo to set
     */
    public void setGeo(Geometry geo)
    {
        this.geo = geo;
    }
    
    public void updateSelectionBoxVertices(SelectionArea selectionArea)
    {
        if(selectionArea != null) {
            selectionArea.start.x = selectionArea.start.x - 0.5f;
            selectionArea.start.y = selectionArea.start.y - 0.5f;
            selectionArea.end.x = selectionArea.end.x + 0.5f;
            selectionArea.end.y = selectionArea.end.y + 0.5f;
        }else {
            selectionArea = new SelectionArea(new Vector2f(0, 0), new Vector2f(0, 0));
        }
        VertexBuffer buffer = geo.getMesh().getBuffer(Type.Position);
        float[] vertexArray = BufferUtils.getFloatArray((FloatBuffer) buffer.getData());
        //System.out.println(selectionArea.start + " : " + selectionArea.end);

        //Update vertexArray postitions

        ////////////////////////
        // Corners: (check Box reference.png for corners)
        // 1: (15,16,17),   (24,25,26)
        // 2: (18,19,20),   (33,34,35), (51,52,53)
        // 3: (3,4,5),      (12,13,14)
        // 4: (6,7,8),      (21,22,23), (48,49,50)
        // 5: (30,31,32),   (45,46,47), (54,55,56)
        // 6: (27,28,29),   (36,37,38)
        // 7: (9,10,11),    (42,43,44), (57,58,59)
        // 8: (0,1,2),      (39,40,41)
        ////////////////////////

        //Left side
        //8
        vertexArray[0] = selectionArea.start.x;
        vertexArray[2] = selectionArea.start.y;
        vertexArray[39] = selectionArea.start.x;
        vertexArray[41] = selectionArea.start.y;
        //7
        vertexArray[9] = selectionArea.start.x;
        vertexArray[11] = selectionArea.start.y;
        vertexArray[42] = selectionArea.start.x;
        vertexArray[44] = selectionArea.start.y;
        vertexArray[57] = selectionArea.start.x;
        vertexArray[59] = selectionArea.start.y;
        //6
        vertexArray[27] = selectionArea.start.x;
        vertexArray[29] = selectionArea.end.y;
        vertexArray[36] = selectionArea.start.x;
        vertexArray[38] = selectionArea.end.y;
        //5
        vertexArray[30] = selectionArea.start.x;
        vertexArray[32] = selectionArea.end.y;
        vertexArray[45] = selectionArea.start.x;
        vertexArray[47] = selectionArea.end.y;
        vertexArray[54] = selectionArea.start.x;
        vertexArray[56] = selectionArea.end.y;
        //Right side
        //4
        vertexArray[6] = selectionArea.end.x;
        vertexArray[8] = selectionArea.start.y;
        vertexArray[21] = selectionArea.end.x;
        vertexArray[23] = selectionArea.start.y;
        vertexArray[48] = selectionArea.end.x;
        vertexArray[50] = selectionArea.start.y;
        //3
        vertexArray[3] = selectionArea.end.x;
        vertexArray[5] = selectionArea.start.y;
        vertexArray[12] = selectionArea.end.x;
        vertexArray[14] = selectionArea.start.y;
        //2
        vertexArray[18] = selectionArea.end.x;
        vertexArray[20] = selectionArea.end.y;
        vertexArray[33] = selectionArea.end.x;
        vertexArray[35] = selectionArea.end.y;
        vertexArray[51] = selectionArea.end.x;
        vertexArray[53] = selectionArea.end.y;
        //1
        vertexArray[15] = selectionArea.end.x;
        vertexArray[17] = selectionArea.end.y;
        vertexArray[24] = selectionArea.end.x;
        vertexArray[26] = selectionArea.end.y;

        buffer.updateData(BufferUtils.createFloatBuffer(vertexArray));

        geo.updateModelBound();
    }
}