package globals;

import com.jme3.math.ColorRGBA;

/**
 *
 * @author lohnn
 */
public class TeamColors
{
//    public static ColorRGBA p1 = ColorRGBA.Red,
//            p2 = ColorRGBA.Blue,
//            p3 = ColorRGBA.Orange,
//            p4 = ColorRGBA.Green;
    public static ColorRGBA[] teamColors = {ColorRGBA.Red, ColorRGBA.Blue, ColorRGBA.Orange, ColorRGBA.Green};

    //returns the team color
    public static ColorRGBA getTeamColor(int i)
    {
        i -= 1;
        return teamColors[i];
    }
}
