package clientSide;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */
public class Server extends Client
{        
    public Server() throws HostAddressNullException
    {
        super("System Server",true);
    }
    protected Server(String n, String pn, String hn, boolean isServer)
    {
        super(n,pn,hn,isServer);
    }
    public static Server getDumbServer()
    //pretty dangerous to use
    {
        return new Server("Dumb Server","Dumb Server","localhost",true);
    }
            
}
