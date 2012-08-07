package testing;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author lohnn
 */
public class MyClassLoader extends ClassLoader
{
    public MyClassLoader(ClassLoader parent)
    {
        super(parent);
    }

    @Override
    public Class loadClass(String name) throws ClassNotFoundException
    {
        URL[] urls = null;
        try {
            // Convert the file object to a URL
            File dir = new File(System.getProperty("user.dir")
                    + File.separator + "dir" + File.separator);
            URL url = dir.toURL();        // file:/c:/almanac1.4/examples/
            urls = new URL[]{url};
        }catch(MalformedURLException e) {
        }

        try {
            // Create a new class loader with the directory
            ClassLoader cl = new URLClassLoader(urls);

            // Load in the class
            Class cls = cl.loadClass("MyReloadableClassImpl");

            // Create a new instance of the new class
//            return (MyReloadableClass) cls.newInstance();
            return null;
        }catch(ClassNotFoundException e) {
        }
        return null;
    }
}
