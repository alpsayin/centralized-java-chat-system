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

public class ServerMessageListener implements Runnable
{   
    protected Thread thread;
    
    protected Server me;
    protected ServerHandler handler;
    protected boolean run = true;
    
    protected ServerSocket serverSocket;
    protected Socket socket;
    
    protected InputStream inStream;
    
    protected DataInputStream inDataStream;
    
    protected final int port = Constants.SERVER_MESSAGELISTENER_PORT;
    public ServerMessageListener(Server s, ServerHandler h)
    {
        this.me = s;
        this.handler = h;
        this.thread = new Thread(this);
        thread.setName("ServerMessageListener");
    }
    public void setHandler(ServerHandler h)
    {
        this.handler = h;
    }
    public void setServer(Server s)
    {
        this.me = s;
    }
    public Server getServer()
    {
        return me;
    }
    public ServerHandler getHandler()
    {
        return handler;
    }
    public void startMessageListener()
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
            System.err.println("MessageListener is already active.");
    }
    public void finish()
    {
        run = false;
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
            catch(Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(Constants.SOCKETTIMEOUT);
                
                try
                {
                    socket = serverSocket.accept();
                    
                    if(handler.isVisible())
                        System.out.println("ServerMessageListener: Conection established with "+socket.getInetAddress());

                    inStream = socket.getInputStream();

                    inDataStream = new DataInputStream(inStream);

                    Client remote = Client.getClientInfo(inDataStream);

                    if(handler.isVisible())
                        System.out.println("Remote user's name: "+remote.getName());

                    Message m = Message.openMessageInTheBottle(inDataStream);

                    socket.shutdownInput();
                    socket.shutdownOutput();

                    socket.close();
                    serverSocket.close();

                    if(m!=null)
                        handler.sendMessage(remote,m);
                    else
                        handler.sendMessage(remote,new Message(me, remote, "Your message couldn't be sent"));
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
            System.out.println("ServerMessageListener stopped");
        handler.fireAction("ServerMessageListener stopped");
    }
}
