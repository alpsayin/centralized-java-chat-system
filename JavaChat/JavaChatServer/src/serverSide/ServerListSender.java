package serverSide;







/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */

import java.net.Socket;

import java.io.OutputStream;
import java.io.DataOutputStream;

public class ServerListSender implements Runnable
{
    protected Thread thread = new Thread(this);
    
    protected Server me;
    protected Client remote;
    protected ClientList list;
    
    protected ServerHandler handler;
    
    protected Socket socket;
    
    protected OutputStream outStream;
    
    protected DataOutputStream outDataStream;
    
    protected final int port = Constants.LISTPORT;
    
    public ServerListSender(ClientList list, Client remote, Server s, ServerHandler h)
    {
        this.list = list;
        this.remote = remote;
        this.me = s;
        this.handler = h;
        this.thread = new Thread(this);
        thread.setName("ServeListSender");
    }
    public Server getServer()
    {
        return me;
    }
    public void setServer(Server s)
    {
        this.me = s;
    }
    public ServerHandler getHandler()
    {
        return handler;
    }
    public void setHandler(ServerHandler h)
    {
        this.handler = h;
    }
    public void startListSender()
    {
        if(thread==null)
        {
            thread = new Thread(this);
            thread.setName("ServerListSender");
        }
        if(!thread.isAlive())
        {
            thread.start();
        }
        else
            System.err.println("ListSender is already active.");
    }
    public void run()
    {
        try
        {
            thread.sleep(10);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            socket = new Socket(remote.getHostName(),port);

            outStream = socket.getOutputStream();

            outDataStream = new DataOutputStream(outStream);

            if(handler.isVisible())
                System.out.println("ServerListSender: Connection established with "+socket.getInetAddress());

            me.sendClientInfo(outDataStream);

            list.sendClientList(outDataStream);

            socket.shutdownInput();
            socket.shutdownOutput();

            socket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
