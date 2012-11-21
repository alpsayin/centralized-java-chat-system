package clientSide;

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

public class ClientMessageListener implements Runnable
{
    protected Thread thread;
    
    protected Client me;
    protected ClientHandler handler;
    protected boolean run = true;
    
    protected ServerSocket serverSocket;
    protected Socket socket;
    
    protected InputStream inStream;
    
    protected DataInputStream inDataStream;
    
    protected final int port = Constants.CLIENT_MESSAGELISTENER_PORT;
    
    public ClientMessageListener(Client me, ClientHandler h)
    {
        this.me = me;
        this.handler = h;
        
        this.thread = new Thread(this);
        this.thread.setName("ClientMessageListener");
    }
    public void startMessageListener()
    {
        if(thread==null)
        {
            thread = new Thread(this);
            thread.setName("ClientMessageListener");
        }
        if(!thread.isAlive())
        {
            thread.start();
        }
        else
            System.err.println("ClientMessageListener is already active.");
    }
    public boolean isAlive()
    {
        return run;
    }
    public void finish()
    {
        run = false;
    }
    public void run()
    {
        run = true;
        while(run)
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
                serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(Constants.SOCKETTIMEOUT);
                
                try
                {
                    socket = serverSocket.accept();
                    
                    System.out.println("ClientMessageListener connection established with: "+socket.getInetAddress());

                    inStream = socket.getInputStream();
                    
                    inDataStream = new DataInputStream(inStream);
                    
                    Client remote = Client.getClientInfo(inDataStream);

                    Message m = Message.getDumbMessage();

                    if(remote!=null)
                        m = Message.openMessageInTheBottle(inDataStream);
                    else
                        System.err.println("Remote server info couldn't be grabbed");


                    socket.shutdownInput();
                    socket.shutdownOutput();

                    socket.close();
                    serverSocket.close();

                    handler.takeMessage(remote,m);
                }
                catch(SocketTimeoutException st)
                {
                    serverSocket.close();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("ClientMessageListener stopped");
    }
}
