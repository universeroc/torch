
package family.i.torch;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.RichTextField;

import java.util.Vector;
import java.util.Enumeration;
import net.rim.device.api.system.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;
import javax.microedition.amms.control.camera.*;


/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class Torch extends UiApplication
{    
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
        Torch theApp = new Torch();       
        theApp.enterEventDispatcher();
    }
    

    /**
     * Creates a new Torch object
     */
    public Torch()
    {        
        // Push a screen onto the UI stack for rendering.
        pushScreen(new TorchScreen());
    }    
}


/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
final class TorchScreen extends MainScreen
{
    private Player _player;
    private VideoControl _videoControl;
    private RecordControl _recordControl;
    private FlashControl _flashControl;
    /**
     * Creates a new TorchScreen object
     */
    TorchScreen()
    {        
        // Set the displayed title of the screen       
        setTitle("Torch");

        // Add a read only text field (RichTextField) to the screen.  The
        // RichTextField is focusable by default. Here we provide a style
        // parameter to make the field non-focusable.
        // add(new RichTextField("Hello World!", Field.NON_FOCUSABLE));
        
        
        // Here is what we want :P
                
        try
        {
            // Start capturing video from the camera
            String encoding = getVideoEncodings()[0];
            try {
                _player = javax.microedition.media.Manager.createPlayer("capture://video?" + encoding);
                _player.start();
            } catch (Exception e) 
            {
                    // Dispose of the player if it was created
            }
            
            _videoControl = (VideoControl) _player.getControl("VideoControl");
            _flashControl = (FlashControl) _player.getControl("javax.microedition.amms.control.camera.FlashControl");
            
            // Initialize the video display
            Field videoField = (Field) _videoControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");            

            _videoControl.setDisplaySize( 1, 1 );
            _videoControl.setVisible(false);
            _flashControl.setMode(FlashControl.FORCE);
            add(videoField);
        }
        catch( MediaException me )
        {
            // setDisplaySize is not supported            
            if( _player != null )
            {
                _player.close();
            }
            _player = null;
        }
    }
    
    /**
     * Retrieves a list of all the video encodings available on the current device
     * 
     * @return Newly created array of Strings whose elements are the video encodings supported by this device. Returns <code>null</code> if this device does not support video encoding.
     */
    public static String[] getVideoEncodings()
    {
        // Retrieve the supported video encodings available on this device
        String encodingsString = System.getProperty("video.encodings");
        
        // Return null if this device does not support video encoding
        if( encodingsString == null )
        {
            return null;
        }

        // Split the whitespace delimited encodingsString into a 
        // String array of encodings.
        Vector encodings = new Vector();       
        int start = 0;
        int space = encodingsString.indexOf(' ');
        while( space != -1 )
        {
            encodings.addElement(encodingsString.substring(start, space));          
            start = space + 1;
            space = encodingsString.indexOf(' ', start);
        }        
        encodings.addElement(encodingsString.substring(start, encodingsString.length())); 
        
        // Copy the encodings into a String array
        String[] encodingArray = new String[encodings.size()];
        encodings.copyInto(encodingArray);
        return encodingArray;
    }
    
    /**
     * Displays a dialog box to the user with the text "Goodbye!" when the
     * application is closed.
     * 
     * @see net.rim.device.api.ui.Screen#close()
     */
    public void close()
    {
        // Display a farewell message before closing the application
        // Dialog.alert("Goodbye!");     
        super.close();
    }   
}
