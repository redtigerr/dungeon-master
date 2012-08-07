package testing;

import GUI.MyMenu;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;

//TODO: Create own controller for selectionList
public class NiftyXMLTesting1 extends SimpleApplication
{
    public static void main(String[] args)
    {
        NiftyXMLTesting1 app = new NiftyXMLTesting1();
        app.setPauseOnLostFocus(false);
        app.start();
    }
    NiftyJmeDisplay niftyDisplay;
    Nifty nifty;
    String file = "Interface/UI.xml";
    String[] layer = {"QuickSlotList"
    };
    int currentLayer = 0, currentFile = 0;

    public void simpleInitApp()
    {
        setUpKeys();
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        MyMenu menu = new MyMenu("");
        menu.bind(nifty, null);
        // init Nifty start screen
        nifty.fromXml(file, layer[currentLayer]);

        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        // disable the fly cam
        flyCam.setDragToRotate(true);

        initCenteredText();
    }

    private void setUpKeys()
    {
        inputManager.addMapping("ReloadNifty", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(keyBoardActionListener, "ReloadNifty");

        inputManager.addMapping("changeLayer", new KeyTrigger(KeyInput.KEY_N));
        inputManager.addListener(keyBoardActionListener, "changeLayer");

        inputManager.addMapping("changeFile", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addListener(keyBoardActionListener, "changeFile");
    }
    private ActionListener keyBoardActionListener = new ActionListener()
    {
        public void onAction(String name, boolean isPressed, float tpf)
        {
            if(name.equals("ReloadNifty") && !isPressed) {
                reloadNifty();
            }

            if(name.equals("changeLayer") && !isPressed) {
                changeLayer();
            }
        }
    };

    private void changeLayer()
    {
        if(currentLayer + 1 < layer.length) {
            currentLayer += 1;
        }else {
            currentLayer = 0;
        }
        System.out.println("Selected " + layer[currentLayer]);
        reloadNifty();
    }

    private void reloadNifty()
    {
        System.out.println("Gonna reload xml!");
        nifty.fromXml(file, layer[currentLayer]);
        guiViewPort.removeProcessor(niftyDisplay);
        guiViewPort.addProcessor(niftyDisplay);
        ch.setText(layer[currentLayer]);
        ch2.setText(layer[currentLayer]);
        System.out.println("Done reload xml!");
    }
    BitmapText ch, ch2;

    protected void initCenteredText()
    {
        guiNode.detachAllChildren();

        ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText(layer[currentLayer]);
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        ch2 = new BitmapText(guiFont, false);
        ch2.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch2.setColor(ColorRGBA.Black);
        ch2.setText(layer[currentLayer]);
        ch2.setLocalTranslation( // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch2.getLineHeight() / 2, 0);
        guiNode.attachChild(ch2);

    }
}