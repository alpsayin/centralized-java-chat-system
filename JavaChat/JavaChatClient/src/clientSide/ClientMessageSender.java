package clientSide;

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

public class ClientMessageSender implements Runnable
{
    protected Thread thread;
    
    protected Message message;
    protected Client me;
    protected Server server;
    
    protected Socket socket;
    
    protected OutputStream outStream;
    
    protected DataOutputStream outDataStream;
    
    protected final int port = Constants.CLIENT_MESSAGESENDER_PORT;
    
    public ClientMessageSender(Message m, Client me, Server s)
    {
        this.message = m;
        this.me = me;
        this.server = s;
        
        this.thread = new Thread(this);
        thread.setName("ClientMessageSender");
    }
    public void startMessageSender()
    {
        if(thread==null)
        {
            thread = new Thread(this);
            thread.setName("ClientMessageSender");
        }
        if(!thread.isAlive())
        {
            thread.start();
        }
        else
            System.err.println("ClientMessageSender is already active.");
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
            socket = new Socket(server.getHostName(), port);
            
            System.out.println("MessageSender connection established with: "+socket.getInetAddress());
            
            outStream = socket.getOutputStream();
            
            outDataStream = new DataOutputStream(outStream);
            
            me.sendClientInfo(outDataStream);
                        
            message.sendMessageInTheBottle(outDataStream);
            
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
