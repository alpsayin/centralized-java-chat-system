package clientSide;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */
import java.net.InetAddress;

public class IpWork 
{
    public static String getLocalHostAddress()
    {
        try
        {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static String getLocalHostName()
    {
        try
        {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostName();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
