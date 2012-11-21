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

public class ClientRequestSender implements Runnable
{
    protected Thread thread;
    protected byte command;
    
    protected Client me;
    protected Server server;
    
    protected Socket socket;
    
    protected OutputStream outStream;
    
    protected DataOutputStream outDataStream;
    
    protected final int port = Constants.REQUESTPORT;
    
    public ClientRequestSender(byte command, Client me, Server s)
    {
        this.command = command;
        this.me = me;
        this.server = s;
        
        thread = new Thread(this);
        thread.setName("ClientRequestSender");
    }  
    public void startRequestSender()
    {
        if(thread==null)
        {
            thread = new Thread(this);
            thread.setName("ClientRequestSender");
        }
        if(!thread.isAlive())
        {
            thread.start();
        }
        else
            System.err.println("ClientRequestSender is already active.");
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
            socket = new Socket(server.getHostName(),port);

            System.out.println("RequestSender connection established with "+socket.getInetAddress());

            outStream = socket.getOutputStream();

            outDataStream = new DataOutputStream(outStream);

            me.sendClientInfo(outDataStream);

            outDataStream.writeByte(command);

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
