package serverSide;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */
import java.io.DataOutputStream;
import java.io.DataInputStream;

public class Client 
{
    protected String name;
    protected String pcName;
    protected String hostName;
    protected boolean isServer = false;
    protected Client(String name, String pcName, String hostName, boolean isServer)
    {
        this.name = name;
        this.pcName = pcName;
        this.hostName = hostName;
        this.isServer = isServer;
    }
    public Client(String name, boolean isServer) throws HostAddressNullException
    {
        this.name = name;
        this.hostName = IpWork.getLocalHostAddress();
        this.pcName = IpWork.getLocalHostName();
        this.isServer = isServer;
    }
    public Client(String name) throws HostAddressNullException
    {
        this.name=name;
        this.hostName = IpWork.getLocalHostAddress();
        this.pcName = IpWork.getLocalHostName();
    }
    public Client() throws HostAddressNullException
    {
        this.name="default client";
        this.hostName = IpWork.getLocalHostAddress();
        this.pcName = IpWork.getLocalHostName();
    }
    public String getName() {
        return name;
    }
    public void setPcName(String s)
    {
        this.pcName = s;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPcName()
    {
        return pcName;
    }
    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean isServer) {
        this.isServer = isServer;
    }
    public void sendClientInfo(DataOutputStream out)
    {
        try
        {
            out.writeUTF(name);
            out.writeUTF(pcName);
            out.writeUTF(hostName);
            out.writeBoolean(isServer);
        }
        catch(Exception e)
        {
            System.err.println(e);
            e.printStackTrace();
            Object o = new Object();
        }
    }
    public String toString()
    {
        return this.name +" on "+ this.pcName +":"+ this.hostName;
    }
    public boolean equals(Object o)
    {
        try
        {
            Client c = (Client)o;
            if(c.hostName.equals(hostName))
                if( (c.isServer() && isServer) || (!c.isServer() && !isServer))
                    return true;
            return false;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    public static void sendClientInfo(String hostName, DataOutputStream out) throws HostAddressNullException
    // this method is like some kind of handshake...
    {
        (new Client(hostName)).sendClientInfo(out);
    }
    public static Client getClientInfo(DataInputStream in)
    {
       try
       {
           return new Client(in.readUTF(), in.readUTF(), in.readUTF(), in.readBoolean());
       }
       catch(Exception e)
       {
           e.printStackTrace();
           return null;
       }
    }
    public static Client getDumbClient()
    //pretty dangerous to use
    {
        return new Client("Dumb Client","Dumb Client","localhost",false);
    }
    public class HostAddressNullException extends Exception
    {
        public HostAddressNullException()
        {
            super("Client: Host Address is null");
        }
    }
}
