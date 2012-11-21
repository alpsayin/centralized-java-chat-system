package serverSide;







/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import java.io.InputStream;
import java.io.DataInputStream;

public class ServerRequestListener implements Runnable
{
    protected Thread thread;
    
    protected Server me;
    protected ServerHandler handler;
    protected boolean run = true;
    
    protected ServerSocket serverSocket;
    protected Socket socket;
    
    protected InputStream inStream;
    
    protected DataInputStream inDataStream;
    
    protected final int port = Constants.REQUESTPORT;
    
    public ServerRequestListener(Server s, ServerHandler a)
    {
        this.me = s;
        this.handler = a;
        this.thread = new Thread(this);
        thread.setName("ServerRequestListener");
    }
    public Client getServer()
    {
        return me;
    }
    public void setHandler(ServerHandler h)
    {
        this.handler = h;
    }
    public ServerHandler getHandler()
    {
        return handler;
    }
    public void setServer(Server c)
    {
        this.me = c;
    }
    public void startRequestListener()
    {
        run = true;
        if(thread==null)
        {
            thread = new Thread(this);
            thread.setName("ServerRequestListener");
        }
        if(!thread.isAlive())
        {
            thread.start();
        }
        else
            System.err.println("RequestListener is already active.");
    }
    public boolean isAlive()
    {
        return run;
    }
    public void run()
    {
        while(run)
        {
            try
            {
                Thread.sleep(10);
            }
            catch(Exception ex)
            {
                System.err.println("Sleep Error!");
                ex.printStackTrace();
            }
            try
            {
                serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(Constants.SOCKETTIMEOUT);
                
                try
                {
                    socket = serverSocket.accept();
                    
                    if(handler.isVisible())
                        System.out.println("ServerRequestListener: Connection established with "+socket.getInetAddress());

                    inStream = socket.getInputStream();

                    inDataStream = new DataInputStream(inStream);

                    Client remote = Client.getClientInfo(inDataStream);

                    if(handler.isVisible())
                        System.out.println("Remote user's name: "+remote.getName());

                    if(remote!=null)
                    {
                        byte request = inDataStream.readByte();
                        switch(request)
                        {
                            case Constants.LOGOUT:
                                if(handler.isVisible())
                                    System.out.println(remote+" sent LOGOUT");
                                handler.removeClient(remote);
                                break;
                            case Constants.LOGIN:
                                if(handler.isVisible())
                                    System.out.println(remote+" sent LOGIN");
                                handler.addClient(remote);
                            case Constants.GETLIST:
                                if(handler.isVisible()) 
                                    System.out.println(remote+" sent GETLIST");
                                handler.sendList(remote);
                                break;
                            default:
                                System.err.println(remote+" has sent an unknown request");
                        }
                    }
                
                    socket.shutdownInput();
                    socket.shutdownOutput();

                    socket.close();
                    serverSocket.close();
                }
                catch(SocketTimeoutException e)
                {
                    serverSocket.close();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        if(handler.isVisible())
            System.out.println("ServerRequestListener stopped");
        handler.fireAction("ServerRequestListener stopped");
    }
    public void finish()
    {
        run = false;
    }
}
