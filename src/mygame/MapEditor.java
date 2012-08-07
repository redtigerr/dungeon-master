package mygame;

import custom.mesh.SelectionBox;
import custom.mesh.Wall;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.util.BufferUtils;
import com.jme3.water.WaterFilter;
import custom.camera.DungeonMasterCamera;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import globals.TeamColors;
import globals.Triggers;
import java.nio.FloatBuffer;

/**
 * This is the map editor
 *
 * @author Lohnn
 */
public class MapEditor extends SimpleApplication
{
    private int mapWidth = 30, mapHeight = 25;
    private Tile[][] tile = new Tile[mapWidth][mapHeight];
    private boolean actionIsPressed = false, cancelIsPressed = false;
    private Vector2f selectionStart = new Vector2f(Vector2f.ZERO);
    DungeonMasterCamera dmCam;
    //////////////////////////
    //Flor types
    private TileType dirt,
            unused_p1,
            unused_p2,
            unused_p3,
            unused_p4,
            currentAddTile,
            currentDeleteTile,
            currentUnusued;//TODO: DELETE LATER
    //////////////////////////
    //Wall types
    private TileType stoneWall;
    //Nifty stuff
    NiftyImage niftyDirtImage, niftyUnusedImage;
    Nifty nifty;

    public void NiftySetup()
    {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/tempUI.xml", "QuickSlotList");
        guiViewPort.addProcessor(niftyDisplay);
        niftyDirtImage = nifty.getRenderEngine().createImage("Interface/Images/Dirt.jpg", false);
        niftyUnusedImage = nifty.getRenderEngine().createImage("Interface/Images/UnusedFloor.png", false);
    }

    @Override
    public void simpleInitApp()
    {
//        Logger.getLogger("").setLevel(Level.OFF);
        initNodes();
        initTileTypes();
        loadMap();
        initLights();
        initCamera();
        initKeys();
        initWater();
        createSelectionBox();
        placeSelectionBox(0, 0);
        NiftySetup();
    }
    private FilterPostProcessor fpp;
    private WaterFilter water;
    private Vector3f lightDir = new Vector3f(-4.9f, -1.3f, 5.9f);

    private void initWater()
    {
        fpp = new FilterPostProcessor(assetManager);
        water = new WaterFilter(rootNode, lightDir);
        water.setWaterHeight(0);
        water.setMaxAmplitude(0.1f);
//        water.setWaveScale(0.001f);
        fpp.addFilter(water);
        viewPort.addProcessor(fpp);
    }

    private void initTileTypes()
    {
        //TODO: Load what players and playercolors for the floors
        //Floors
        dirt = new TileType("Dirt", assetManager.loadMaterial("Materials/Floors/Dirt.j3m"), 1);
        unused_p1 = new TileType("Unused", assetManager.loadMaterial("Materials/Floors/Unused_tile.j3m"), 1);
        unused_p1.getMaterial().setColor("KeyColor", TeamColors.getTeamColor(1));
        unused_p2 = new TileType("Unused", assetManager.loadMaterial("Materials/Floors/Unused_tile.j3m"), 1);
        unused_p2.getMaterial().setColor("KeyColor", TeamColors.getTeamColor(2));
        unused_p3 = new TileType("Unused", assetManager.loadMaterial("Materials/Floors/Unused_tile.j3m"), 1);
        unused_p3.getMaterial().setColor("KeyColor", TeamColors.getTeamColor(3));
        unused_p4 = new TileType("Unused", assetManager.loadMaterial("Materials/Floors/Unused_tile.j3m"), 1);
        unused_p4.getMaterial().setColor("KeyColor", TeamColors.getTeamColor(4));
        //Walls
        stoneWall = new TileType("Stone", assetManager.loadMaterial("Materials/Walls/StoneWalls.j3m"), 2);
        currentAddTile = stoneWall;
        currentDeleteTile = dirt;
        currentUnusued = unused_p1; //TODO: DELETE LATER!
    }

    @Override
    public void simpleUpdate(float tpf)
    {
//        fpsText.setText("DIN MAMMA");
        dl.setDirection(cam.getDirection());
        //collidableBox.setLocalTranslation(getRounded2dMousePos().x, collidableBox.getLocalTranslation().y, getRounded2dMousePos().getY());
        if(getSelectionArea() != null && !dmCam.isRotating()) {
            pl.setPosition(new Vector3f(getRounded2dMousePos().x, 2f, getRounded2dMousePos().y));
            if(!actionIsPressed && !cancelIsPressed) {
                placeSelectionBox(getRounded2dMousePos().x, getRounded2dMousePos().y);
            }
            updateSelectionBox();
        }else {
            actionIsPressed = false;
            cancelIsPressed = false;
        }
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
    }
    Node selectableFloor, selectionBoxNode, mapParts;

    private void initNodes()
    {
        selectableFloor = new Node();
        rootNode.attachChild(selectableFloor);

        selectionBoxNode = new Node();
        rootNode.attachChild(selectionBoxNode);

        mapParts = new Node();
        rootNode.attachChild(mapParts);
    }
    Geometry collidableBox;

    private Geometry createFloorPiece(Vector2f position)
    {
        Mesh m = new Mesh();

        Vector3f[] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(position.x - 0.5f, 0.1f, position.y - 0.5f); //--
        vertices[1] = new Vector3f(position.x - 0.5f, 0.1f, position.y + 0.5f); //-+
        vertices[2] = new Vector3f(position.x + 0.5f, 0.1f, position.y - 0.5f); //+-
        vertices[3] = new Vector3f(position.x + 0.5f, 0.1f, position.y + 0.5f); //++

        Vector3f[] normals = new Vector3f[4];
        normals[0] = new Vector3f(0, 1, 0);
        normals[1] = new Vector3f(0, 1, 0);
        normals[2] = new Vector3f(0, 1, 0);
        normals[3] = new Vector3f(0, 1, 0);

        Vector2f[] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0, 0);
        texCoord[1] = new Vector2f(1, 0);
        texCoord[2] = new Vector2f(0, 1);
        texCoord[3] = new Vector2f(1, 1);

        int[] indexes = {
            2, 0, 1, 1, 3, 2
        };

        m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        m.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        m.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
        m.updateBound();

        Geometry geom = new Geometry("FloorPiece", m);
        return geom;
    }

    private void loadMap()
    {
        //TODO: Load all floor types and place them down
        Box b = new Box(new Vector3f(0, -1.5f, 0), mapWidth / 2, 1, mapHeight / 2);
//        Box b = new Box(new Vector3f(-mapWidth / 2, -0.5f, -mapHeight / 2), new Vector3f(mapWidth / 2, 0, mapHeight / 2 - 1));
        collidableBox = new Geometry("Box", b);

        Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        boxMat.getAdditionalRenderState().setWireframe(true);
        boxMat.setColor("Color", ColorRGBA.Blue);
        boxMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.FrontAndBack);
        collidableBox.setMaterial(boxMat);
        selectableFloor.attachChild(collidableBox);

        for(int x = 0; x < mapWidth; x++) {
            for(int y = 0; y < mapHeight; y++) {
                //TODO: add numbers for testing
                Vector2f position = new Vector2f(-mapWidth / 2 + x, -mapHeight / 2 + y);
                Geometry geo;
                if(x == 0 || x == mapWidth - 1 || y == 0 || y == mapHeight - 1) {
                    /**
                     * Sides = true when there's *not* a connected wall piece
                     * 0 = back
                     * 1 = front
                     * 2 = left
                     * 3 = right
                     */
//                    boolean[] tempBool = {false, false, false, false};
                    Wall tempWall2 = new Wall(new Vector2f(position.x, position.y), true);
                    geo = tempWall2.getGeo();

                    geo.setMaterial(stoneWall.getMaterial());
                    tile[x][y] = new Tile(0, geo, stoneWall);

                    mapParts.attachChild(geo);
                }else {
                    geo = createFloorPiece(position);

                    geo.setMaterial(dirt.getMaterial());
                    tile[x][y] = new Tile(0, geo, dirt);

                    mapParts.attachChild(geo);
                }
            }
        }
    }
    Geometry selectionBoxGeo;
    SelectionBox selectionBox;

    private void createSelectionBox()
    {
        selectionBox = new SelectionBox(new Vector3f(0, 0.6f, 0), 0.51f, 0.51f, 0.51f);
        selectionBox.setDynamic();

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Red);

        selectionBox.getGeo().setMaterial(mat);
        selectionBoxNode.attachChild(selectionBox.getGeo());
    }
    DirectionalLight dl;
    PointLight pl;

    /**
     * Initializes the lights and shadows
     */
    private void initLights()
    {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.multLocal(0.8f));
        rootNode.addLight(al);

        pl = new PointLight();
        pl.setColor(ColorRGBA.White);
        rootNode.addLight(pl);
        dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White.multLocal(0.2f));
        rootNode.addLight(dl);

//        BasicShadowRenderer bsr = new BasicShadowRenderer(assetManager, 256);
//        bsr.setDirection(new Vector3f(0, -.5f, -.5f).normalizeLocal()); // light direction
//        viewPort.addProcessor(bsr);
    }

    private void initCamera()
    {
        flyCam.setEnabled(false);
        dmCam = new DungeonMasterCamera(cam, inputManager, rootNode, rootNode);
    }

    private void initKeys()
    {
        inputManager.addMapping("Action", Triggers.actionButton);
        inputManager.addListener(buttonActionListener, "Action");
        inputManager.addMapping("Cancel", Triggers.secondButton);
        inputManager.addListener(buttonActionListener, "Cancel");
        inputManager.addMapping("Rotate", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        inputManager.addListener(buttonActionListener, "Rotate");

        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(buttonActionListener, "Space");
        inputManager.addMapping("Plus", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addListener(buttonActionListener, "Plus");
        inputManager.addMapping("Minus", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addListener(buttonActionListener, "Minus");
        inputManager.addMapping("Reset Camera", new KeyTrigger(KeyInput.KEY_LCONTROL));
        inputManager.addListener(buttonActionListener, "Reset Camera");
        inputManager.addMapping("Ask For Position", new KeyTrigger(keyInput.KEY_TAB));
        inputManager.addListener(buttonActionListener, "Ask For Position");
        inputManager.addMapping("QueueButton", Triggers.queueButton);
        inputManager.addListener(buttonActionListener, "Toggle Rotation");
        inputManager.addMapping("Toggle Rotation", Triggers.toggleRotate);
        inputManager.addListener(buttonActionListener, "Team1");
        inputManager.addMapping("Team1", new KeyTrigger(keyInput.KEY_NUMPAD1));
        inputManager.addMapping("Team1", new KeyTrigger(keyInput.KEY_1));
        inputManager.addListener(buttonActionListener, "Team2");
        inputManager.addMapping("Team2", new KeyTrigger(keyInput.KEY_NUMPAD2));
        inputManager.addMapping("Team2", new KeyTrigger(keyInput.KEY_2));
        inputManager.addListener(buttonActionListener, "Team3");
        inputManager.addMapping("Team3", new KeyTrigger(keyInput.KEY_NUMPAD3));
        inputManager.addMapping("Team3", new KeyTrigger(keyInput.KEY_3));
        inputManager.addListener(buttonActionListener, "Team4");
        inputManager.addMapping("Team4", new KeyTrigger(keyInput.KEY_NUMPAD4));
        inputManager.addMapping("Team4", new KeyTrigger(keyInput.KEY_4));
    }
    boolean isDirt = true; //TODO: DELETE LATER

    /**
     * TODO: DELETE LATER!
     * This is temporary for updating between the teams and stuff
     * @param temp 
     */
    private void updateDeleteTile(TileType temp)
    {
        currentUnusued = temp;
        if(!isDirt) {
            currentDeleteTile = temp;
        }
    }
    private ActionListener buttonActionListener = new ActionListener()
    {
        public void onAction(String name, boolean isPressed, float tpf)
        {
            if(name.equals("Space") && isPressed) {
                if(isDirt) {
                    isDirt = false;
                    updateDeleteTile(currentUnusued);
                    Element niftyElement = nifty.getCurrentScreen().findElementByName("rightImage");
                    niftyElement.getRenderer(ImageRenderer.class).setImage(niftyUnusedImage);
                }else {
                    isDirt = true;
                    currentDeleteTile = dirt;
                    Element niftyElement = nifty.getCurrentScreen().findElementByName("rightImage");
                    niftyElement.getRenderer(ImageRenderer.class).setImage(niftyDirtImage);
                }
            }else if(name.equals("Team1")) {
                updateDeleteTile(unused_p1);
            }else if(name.equals("Team2")) {
                updateDeleteTile(unused_p2);
            }else if(name.equals("Team3")) {
                updateDeleteTile(unused_p3);
            }else if(name.equals("Team4")) {
                updateDeleteTile(unused_p4);
            }else if(name.equals("Plus")) {
                if(!isPressed) {
//                    dmCam.setCamMoveSpeed(dmCam.getCamMoveSpeed() + 10);
//                    System.out.println(dmCam.getCamMoveSpeed());
                }
            }else if(name.equals("Minus")) {
                if(!isPressed) {
//                    dmCam.setCamMoveSpeed(dmCam.getCamMoveSpeed() - 10);
//                    System.out.println(dmCam.getCamMoveSpeed());
                }
            }else if(name.equals("Reset Camera")) {
                cam.setLocation(new Vector3f(50, 100, 0));
            }else if(name.equals("Ask For Position") && isPressed) {
//                VertexBuffer buffer = selectionBoxGeo.getMesh().getBuffer(Type.Position);
                VertexBuffer buffer = selectionBox.getGeo().getMesh().getBuffer(Type.Position);
                float[] vertexArray = BufferUtils.getFloatArray((FloatBuffer) buffer.getData());
                System.out.println(vertexArray.length);
//                System.out.println(checkRounded2dMousePos());
            }else if(name.equals("Action")) { //Pressed
                if(isPressed && !cancelIsPressed) {
                    actionIsPressed = true;
                    if(getSelectionArea() != null) {
                        placeSelectionBox(getRounded2dMousePos().x, getRounded2dMousePos().y);
                    }
                    updateSelectionBox();
                }else {
                    actionIsPressed = false;
                    if(getSelectionArea() != null) {
                        changeStateOfSelection(getSelectionArea(), currentAddTile);
                    }
                }
            }else if(name.equals("Cancel")) {
                if(isPressed && !actionIsPressed) {
                    cancelIsPressed = true;
                    Material tempMat = selectionBox.getGeo().getMaterial();
                    tempMat.setColor("Color", ColorRGBA.Blue);
                    selectionBox.getGeo().setMaterial(tempMat);
                    if(getSelectionArea() != null) {
                        placeSelectionBox(getRounded2dMousePos().x, getRounded2dMousePos().y);
                    }
                    updateSelectionBox();
                }else {
                    cancelIsPressed = false;
                    Material tempMat = selectionBox.getGeo().getMaterial();
                    tempMat.setColor("Color", ColorRGBA.Red);
                    selectionBox.getGeo().setMaterial(tempMat);
                    if(getSelectionArea() != null) {
                        changeStateOfSelection(getSelectionArea(), currentDeleteTile);
                    }
                }
            }else if(name.equals("Toggle Rotation")) {
                if(isPressed) {
                    selectionBoxNode.detachChild(selectionBox.getGeo());
                }else {
                    selectionBoxNode.attachChild(selectionBox.getGeo());
                }
            }
        }
    };

    private void placeSelectionBox(float x, float z)
    {
        selectionStart.set(x, z);
    }
    SelectionArea selectionArea = new SelectionArea(new Vector2f(0, 0), new Vector2f(0, 0));

    private SelectionArea getSelectionArea()
    {
        //TODO: perhaps moving the whole selection part to a new class
        if(getRounded2dMousePos() != null) {
            if(getRounded2dMousePos().x < selectionStart.x) {
                selectionArea.start.x = getRounded2dMousePos().x;
                selectionArea.end.x = selectionStart.x;
            }else {
                selectionArea.start.x = selectionStart.x;
                selectionArea.end.x = getRounded2dMousePos().x;
            }
            if(getRounded2dMousePos().y < selectionStart.y) {
                selectionArea.start.y = getRounded2dMousePos().y;
                selectionArea.end.y = selectionStart.y;
            }else {
                selectionArea.start.y = selectionStart.y;
                selectionArea.end.y = getRounded2dMousePos().y;
            }
            return selectionArea;
        }
        return null;
    }

    private void updateSelectionBox()
    {
        selectionBox.updateSelectionBoxVertices(getSelectionArea());
    }

    /**
     * TODO: perhaps move this to the tile class?
     * returns false for each side where there's a wall, for checking individual tiles
     * @return a boolean array to feed too the wall
     */
    boolean[] checkSurroundingTiles(int x, int y)
    {
        System.out.println(x + " : " + y);
        boolean[] tempBool = {false, false, false, false};
        //front
        if(tile[x][y + 1].getTileType().getHeight() != 2) {
            tempBool[0] = true;
        }
        //back
        if(tile[x][y - 1].getTileType().getHeight() != 2) {
            tempBool[1] = true;
        }
        //left
        if(tile[x - 1][y].getTileType().getHeight() != 2) {
            tempBool[2] = true;
        }
        //right
        if(tile[x + 1][y].getTileType().getHeight() != 2) {
            tempBool[3] = true;
        }
        return tempBool;
    }

    /**
     * Changes the state of the selected area to whatever tiletype there's gonna be
     * @param sel Selected area
     * @param tileType Tiletype to place
     */
    private void changeStateOfSelection(SelectionArea sel, TileType tileType)
    {
        //do this a little more dynamic, not only create walls ;)
        for(int y1 = (int) sel.start.y + mapHeight / 2; y1 <= sel.end.y + mapHeight / 2; y1++) {
            for(int x1 = (int) sel.start.x + mapWidth / 2; x1 <= sel.end.x + mapWidth / 2; x1++) {
                int x = x1 - mapWidth / 2, y = y1 - mapHeight / 2;

                Geometry geo;
                /**
                 * Sides = true when there's *not* a connected wall piece
                 * 0 = back
                 * 1 = front
                 * 2 = left
                 * 3 = right
                 */
                boolean[] tempBool = {false, false, false, false};
//                if(tile[x1][y1].getTileType() != tileType) {
                //Front
                if(y1 == sel.end.y + mapHeight / 2 && y1 < mapHeight - 1) {
                    if(tile[x1][y1 + 1].getTileType().getHeight() == 2) {
                        boolean[] tempBool2 = checkSurroundingTiles(x1, y1 + 1);
                        if(tileType.getHeight() == 2) {
                            tempBool2[1] = false;
                        }else {
                            tempBool2[1] = true;
                        }
                        Wall tempWall2 = new Wall(new Vector2f(x, y + 1), tempBool2);
                        Geometry geo2 = tempWall2.getGeo();

                        geo2.setMaterial(tile[x1][y1 + 1].getTileType().getMaterial());
                        replaceTile(x1, y1 + 1, new Tile(0, geo2, stoneWall));
                    }else {
                        tempBool[0] = true;
                    }
                }
                //Back
                if(y1 == sel.start.y + mapHeight / 2 && y1 > 0) {
                    if(tile[x1][y1 - 1].getTileType().getHeight() == 2) {
                        boolean[] tempBool2 = checkSurroundingTiles(x1, y1 - 1);
                        if(tileType.getHeight() == 2) {
                            tempBool2[0] = false;
                        }else {
                            tempBool2[0] = true;
                        }
                        Wall tempWall2 = new Wall(new Vector2f(x, y - 1), tempBool2);
                        Geometry geo2 = tempWall2.getGeo();

                        geo2.setMaterial(tile[x1][y1 - 1].getTileType().getMaterial());
                        replaceTile(x1, y1 - 1, new Tile(0, geo2, stoneWall));
                    }else {
                        tempBool[1] = true;
                    }
                }
                //Left
                if(x1 == sel.start.x + mapWidth / 2 && x1 > 0) {
                    if(tile[x1 - 1][y1].getTileType().getHeight() == 2) {
                        boolean[] tempBool2 = checkSurroundingTiles(x1 - 1, y1);
                        if(tileType.getHeight() == 2) {
                            tempBool2[3] = false;
                        }else {
                            tempBool2[3] = true;
                        }
                        Wall tempWall2 = new Wall(new Vector2f(x - 1, y), tempBool2);
                        Geometry geo2 = tempWall2.getGeo();

                        geo2.setMaterial(tile[x1 - 1][y1].getTileType().getMaterial());
                        replaceTile(x1 - 1, y1, new Tile(0, geo2, stoneWall));
                    }else {
                        tempBool[2] = true;
                    }
                }
                //Right
                if(x1 == sel.end.x + mapWidth / 2 && x1 < mapWidth - 1) {
                    if(tile[x1 + 1][y1].getTileType().getHeight() == 2) {
                        boolean[] tempBool2 = checkSurroundingTiles(x1 + 1, y1);
                        if(tileType.getHeight() == 2) {
                            tempBool2[2] = false;
                        }else {
                            tempBool2[2] = true;
                        }
                        Wall tempWall2 = new Wall(new Vector2f(x + 1, y), tempBool2);
                        Geometry geo2 = tempWall2.getGeo();

                        geo2.setMaterial(tile[x1 + 1][y1].getTileType().getMaterial());
                        replaceTile(x1 + 1, y1, new Tile(0, geo2, stoneWall));
                    }else {
                        tempBool[3] = true;
                    }
                }
                if(tileType.getHeight() == 2) {
                    Wall tempWall2 = new Wall(new Vector2f(x, y), tempBool);
                    geo = tempWall2.getGeo();
                }else {
                    geo = createFloorPiece(new Vector2f(x, y));
                }
                geo.setMaterial(tileType.getMaterial());
                replaceTile(x1, y1, new Tile(0, geo, tileType));
            }
        }
    }

    //TODO: find a way to put this inside the tile class?
    /**
     * Replaces the specific tile at the position
     * @param x X position
     * @param y Y position
     * @param tile Tile to place
     */
    private void replaceTile(int x, int y, Tile tile)
    {
        mapParts.detachChild(this.tile[x][y].getGeometry());
        this.tile[x][y].replaceTile(tile);
        mapParts.attachChild(tile.getGeometry());

    }

    /**
     * Replaces the specific tile at the position
     * @param pos Position
     * @param tile Tile to replace
     */
    private void replaceTile(Vector2f pos, Tile tile)
    {
        mapParts.detachChild(this.tile[(int) pos.x][(int) pos.y].getGeometry());
        this.tile[(int) pos.x][(int) pos.y].replaceTile(tile);
        mapParts.attachChild(tile.getGeometry());

    }

    /**
     * Returns the mouse position as a Vector2f with rounded values (int)
     * @return The position of the mouse
     */
    private Vector2f getRounded2dMousePos()
    {
        Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        selectableFloor.collideWith(ray, results);

        if(results.size() > 0) {
            CollisionResult closest = results.getClosestCollision();
            return new Vector2f(Math.round(closest.getContactPoint().x), Math.round(closest.getContactPoint().z));
        }
        return null;
    }
}
