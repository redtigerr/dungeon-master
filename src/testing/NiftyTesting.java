package testing;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NiftyTesting extends SimpleApplication
{
    public static void main(String[] args)
    {
        NiftyTesting app = new NiftyTesting();
        app.setPauseOnLostFocus(false);
        app.start();
    }
    NiftyJmeDisplay niftyDisplay;
    Nifty nifty;
    String layer = "QuickSlotList";
    UI ui;
    int currentLayer = 0;

    public void simpleInitApp()
    {
        Logger.getLogger("").setLevel(Level.SEVERE);
        setUpKeys();
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        flyCam.setDragToRotate(true);

        ui = new UI();
        ui.UI(nifty);

        nifty.gotoScreen(layer);

        initCenteredText();
    }

    private void setUpKeys()
    {
        inputManager.addMapping("ReloadNifty", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(keyBoardActionListener, "ReloadNifty");

        inputManager.addMapping("changeLayer", new KeyTrigger(KeyInput.KEY_N));
        inputManager.addListener(keyBoardActionListener, "changeLayer");

        inputManager.addMapping("arrow1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addListener(keyBoardActionListener, "arrow1");

        inputManager.addMapping("arrow2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addListener(keyBoardActionListener, "arrow2");

        inputManager.addMapping("arrow3", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addListener(keyBoardActionListener, "arrow3");

        inputManager.addMapping("arrow4", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addListener(keyBoardActionListener, "arrow4");
    }
    private ActionListener keyBoardActionListener = new ActionListener()
    {
        public void onAction(String name, boolean isPressed, float tpf)
        {
            if(name.equals("ReloadNifty") && !isPressed) {
//                reloadNifty();
            }

            if(name.equals("changeLayer") && !isPressed) {
//                changeLayer();
            }
            if(name.equals("arrow1") && !isPressed) {
                ui.setArrowPos(1);
            }
            if(name.equals("arrow2") && !isPressed) {
                ui.setArrowPos(2);
            }
            if(name.equals("arrow3") && !isPressed) {
                ui.setArrowPos(3);
            }
            if(name.equals("arrow4") && !isPressed) {
                ui.setArrowPos(4);
            }
        }
    };
//    private void changeLayer()
//    {
//        if(currentLayer + 1 == layer.length) {
//            currentLayer = 0;
//        }else {
//            currentLayer += 1;
//        }
//        System.out.println("Selected " + layer[currentLayer]);
//        reloadNifty();
//    }
//    private void reloadNifty()
//    {
//        ui.UI(nifty);
//        nifty.gotoScreen(layer[currentLayer]);
//        ch.setText(layer[currentLayer]);
//        ch2.setText(layer[currentLayer]);
//        System.out.println("Done reload changing layer!");
//    }
    BitmapText ch, ch2;

    protected void initCenteredText()
    {
        guiNode.detachAllChildren();

        ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText(layer);
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        ch2 = new BitmapText(guiFont, false);
        ch2.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch2.setColor(ColorRGBA.Black);
        ch2.setText(layer);
        ch2.setLocalTranslation( // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch2.getLineHeight() / 2, 0);
        guiNode.attachChild(ch2);
    }

    public void expandSelection()
    {
        System.out.println("Expanding selection thingie");
    }
}
