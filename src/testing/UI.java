package testing;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import java.util.ArrayList;

/**
 *
 * @author lohnn
 */
public class UI
{
    Nifty nifty;

    public void UI(Nifty nifty)
    {
        this.nifty = nifty;

        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");

        ArrayList<ImageInfo> floors = new ArrayList<ImageInfo>();
        floors.add(new ImageInfo("Dirt", "Textures/Floors/Dirt/Dirt-Texture-1024x1024.jpg"));
        floors.add(new ImageInfo("Unused", "Textures/Floors/Unused/Unused floor_texture.png"));

        ArrayList<ImageInfo> buildings = new ArrayList<ImageInfo>();
        buildings.add(new ImageInfo("Dungeon Heart", "Interface/Images/DungeonHeart.png"));

        ArrayList<ImageInfo> walls = new ArrayList<ImageInfo>();
        walls.add(new ImageInfo("Stone Walls", "Interface/Images/StoneWall.png"));

        //TODO: Later gonna load from the file, temporary just loading all from the above lists
        ArrayList<ImageInfo> QuickslotList = new ArrayList<ImageInfo>();
        QuickslotList.addAll(floors);
        QuickslotList.addAll(buildings);
        QuickslotList.addAll(walls);


        ArrayList<ToolShelf> toolShelves = new ArrayList<ToolShelf>();
        toolShelves.add(new ToolShelf("QuickSlotList", QuickslotList));
//        toolShelves.add(new ToolShelf("Floors", floors));
//        toolShelves.add(new ToolShelf("Buildings", buildings));
//        toolShelves.add(new ToolShelf("Walls", walls));


        for(final ToolShelf toolShelf : toolShelves) {
            //TODO: Loop through all toolshelves here
            nifty.addScreen(toolShelf.getName(), new ScreenBuilder(toolShelf.getName())
            {
                {
                    controller(new DefaultScreenController()); // Screen properties

                    // <layer>
                    layer(new LayerBuilder("foreground")
                    {
                        {
                            childLayoutHorizontal(); // layer properties, add more...

                            // <panel>
                            panel(new PanelBuilder("panel_left")
                            {
                                {
                                    childLayoutVertical(); // panel properties, add more...
                                    width("110px");
                                    height("100%");
                                    backgroundColor(Color.BLACK);
                                    onStartScreenEffect(move(300, 200, "in", "left"));
                                    onEndScreenEffect(move(300, 200, "out", "left"));

                                    //To get black background and not transparent
                                    panel(new PanelBuilder()
                                    {
                                        {
                                            childLayoutVertical();
                                            width("100%");
                                            height("100%");
                                            backgroundColor(new Color(5, 5, 5, 3));

                                            for(final ImageInfo io : toolShelf.getImages()) {
                                                panel(new PanelBuilder(io.getName())
                                                {
                                                    {
                                                        childLayoutCenter();
                                                        width("100%");
                                                        height("110px");
                                                        backgroundColor(new Color(0, 0, 0, 0));
                                                        onStartScreenEffect(border(1, "#000f"));
                                                        image(loadImage(io.getUrl(), 90, 90));
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                            // </panel>
                        }
                    });
                    // </layer>

                    // <layer>
                    layer(new LayerBuilder("selectionLayer")
                    {
                        {
                            childLayoutAbsolute(); // layer properties, add more...

                            panel(arrow(4));

                            // <panel>
                            panel(new PanelBuilder("selectionArea")
                            {
                                {
                                    childLayoutVertical(); // panel properties, add more...
                                    //TODO: Change according to width on screen
                                    width("500px");
                                    height("400px");
                                    x("140px");
                                    y("80px");
                                    backgroundColor(Color.BLACK);
                                    visibleToMouse(true);

                                    onStartScreenEffect(move(300, 200, "in", "left"));
                                    onEndScreenEffect(move(300, 200, "out", "left"));

                                    //To get black background and not transparent
                                    panel(new PanelBuilder()
                                    {
                                        {
                                            childLayoutVertical();
                                            width("100%");
                                            height("100%");
                                            backgroundColor(new Color(5, 5, 5, 3));
                                            //Divides by columnlenght (which right now is 5, I want this to be dynamic later on)
                                            int columnLenght = 5;
                                            for(int row = 0; row < Math.ceil(toolShelf.getImages().size() / (float) columnLenght); row++) {
                                                ArrayList<ImageInfo> temp = new ArrayList<ImageInfo>();
                                                for(int j = row * columnLenght; j < toolShelf.getImages().size() && j < row * columnLenght + columnLenght; j++) {
                                                    temp.add(toolShelf.getImages().get(j));
                                                }
                                                panel(createRow("temp", temp));
                                            }
                                        }
                                    });
                                }
                            });
                            // </panel>
                        }
                    });
                    // </layer>
                }
            }.build(nifty));
        }
    }

    /**
     * Creates an image centered
     * 
     * @param url url to image
     * @param width width of image
     * @param height height of image
     * @return 
     */
    public ImageBuilder loadImage(final String url, final int width, final int height)
    {
        return new ImageBuilder()
        {
            {
                filename(url);
                valign(VAlign.Center);
                align(Align.Center);
                height(Integer.toString(height) + "px");
                width(Integer.toString(width) + "px");
            }
        };
    }

    /**
     * Creates the arrow and positions itself at the position corresponding to the selected item
     * @param selected The selected item
     */
    public PanelBuilder arrow(final int selected)
    {
        return new PanelBuilder("arrow")
        {
            {
                childLayoutCenter();
                x("125px");
                backgroundColor(Color.NONE);
                width("15px");
                height("30px");
                y(Integer.toString(arrowYPos(selected)) + "px");
                image(loadImage("Interface/Images/Arrow.png", 15, 30));
            }
        };
    }

    public void setArrowPos(int selected)
    {
        Element niftyElement = nifty.getCurrentScreen().findElementByName("arrow");
        niftyElement.setConstraintY(new SizeValue(Integer.toString(arrowYPos(selected)) + "px"));
        niftyElement.getRenderer(ImageRenderer.class).setImage(nifty.getRenderEngine().createImage("Textures/Floors/Dirt/Dirt-Texture-1024x1024.jpg", false));
    }

    /**
     * Returns the absolute yPosition it should be placed (made this its own since I want to be able to reuse it)
     * @param selected the selected item
     * @return 
     */
    public int arrowYPos(int selected)
    {
        int yPos = selected * 110 - 70;
        if(yPos < 50) {
            yPos = 80;
        }else if(yPos > 450) {
            yPos = 450;
        }
        return yPos;
    }

    /**
     * For moving the stuff at end and start
     * @param length duration
     * @param delay delay before start
     * @param mode mode (in/out)
     * @param direction direction
     * @return 
     */
    public EffectBuilder move(final int length, final int delay, final String mode, final String direction)
    {
        return new EffectBuilder("move")
        {
            {
                effectParameter("mode", mode);
                effectParameter("direction", direction);
                length(length);
                startDelay(delay);
                inherit(true);
            }
        };
    }

    /**
     * Creates a border
     * @param width width
     * @param color color
     * @return 
     */
    public EffectBuilder border(final int width, final String color)
    {
        return new EffectBuilder("border")
        {
            {
                effectParameter("border", Integer.toString(width) + "px");
                effectParameter("color", color);
                timeType("infinite");
            }
        };
    }

    /**
     * Creates a new row for the selection area
     * @param rowName Name of the row
     * @param toolShelf The items to be added to this row
     * @return The row
     */
    public PanelBuilder createRow(final String rowName, final ArrayList<ImageInfo> toolShelf)
    {
        return new PanelBuilder(rowName)
        {
            {
                childLayoutHorizontal();
                width("100%");
                height("100px");
                backgroundColor(new Color(0, 0, 0, 0));
                for(final ImageInfo io : toolShelf) {
                    panel(new PanelBuilder(io.getName())
                    {
                        {
                            childLayoutCenter();
                            width("100px");
                            height("100px");
                            onStartScreenEffect(border(1, "#000f"));
                            image(loadImage(io.getUrl(), 90, 90));
                        }
                    });
                }
            }
        };
    }
}
class ToolShelf
{
    private String name;
    private ArrayList<ImageInfo> images;

    public ToolShelf(String name, ArrayList<ImageInfo> images)
    {
        this.name = name;
        this.images = images;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the images
     */
    public ArrayList<ImageInfo> getImages()
    {
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(ArrayList<ImageInfo> images)
    {
        this.images = images;
    }
}
class ImageInfo
{
    private String name;
    private String url;

    public ImageInfo(String name, String url)
    {
        this.name = name;
        this.url = url;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url)
    {
        this.url = url;
    }
}
