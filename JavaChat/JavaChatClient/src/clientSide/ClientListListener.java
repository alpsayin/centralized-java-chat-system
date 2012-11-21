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

public class ClientListListener implements Runnable
{
    protected Thread thread;
    protected boolean run = true;
    
    protected ServerSocket serverSocket;
    protected Socket socket;
    
    InputStream inStream;
    DataInputStream inDataStream;
    
    protected Client me;
    protected Client server;
    
    protected ClientHandler handler;
    
    protected int port = Constants.LISTPORT;
    
    public ClientListListener(Client me, ClientHandler h)
    {
        this.me = me;
        this.handler = h;
        
        this.thread = new Thread(this);
        thread.setName("ClientListListener");
    }
    public void startListListener()
    {
        if(thread==null)
        {
            thread = new Thread(this);
            thread.setName("ClientListListener");
        }
        if(!thread.isAlive())
        {
            thread.start();
        }
        else
            System.err.println("ClientListListener is already active.");
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
                
                    System.out.println("ListListener connection established with: "+socket.getInetAddress());

                    inStream = socket.getInputStream();

                    inDataStream = new DataInputStream(inStream);

                    server = Client.getClientInfo(inDataStream);

                    ClientList newList = ClientList.getClientList(inDataStream);

                    handler.updateList(newList);

                    socket.shutdownInput();
                    socket.shutdownOutput();

                    socket.close();
                    serverSocket.close();
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
        System.out.println("ClientListListener stopped");
    }
}
