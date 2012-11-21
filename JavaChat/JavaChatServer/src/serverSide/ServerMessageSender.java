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

public class ServerMessageSender implements Runnable
{
    protected Thread thread;
    
    protected Server me;
    protected Message message;
    private Client sender;
    
    private ServerHandler handler;
    
    protected Socket socket;
    
    protected OutputStream outStream;
    
    protected DataOutputStream outDataStream;
    
    protected final int port = Constants.SERVER_MESSAGESENDER_PORT;
    
    public ServerMessageSender(Client c, Message m, Server s, ServerHandler h)
    {
        this.sender = c;
        this.message = m;
        this.me = s;
        this.handler = h;
        
        thread = new Thread(this);
        thread.setName("ServerMessageSender");
    }
    public Server getServer() {
        return me;
    }

    public void setServer(Server me) {
        this.me = me;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public ServerHandler getHandler() {
        return handler;
    }

    public void setHandler(ServerHandler handler) {
        this.handler = handler;
    }
    public void startMessageSender()
    {
        if(thread==null)
        {
            thread = new Thread(this);
            thread.setName("ServerMessageSender");
        }
        if(!thread.isAlive())
        {
            thread.start();
        }
        else
            System.err.println("MessageSender is already active.");
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
            socket = new Socket(message.getTo().getHostName(),port);

            if(handler.isVisible())
                System.out.println("ServerMessageSender: Connection established with: "+socket.getInetAddress());

            outStream = socket.getOutputStream();

            outDataStream = new DataOutputStream(outStream);

            me.sendClientInfo(outDataStream);

            message.sendMessageInTheBottle(outDataStream);

            if(handler.isVisible())
                System.out.println("Message from "+sender.getName()+" has been sent to "+message.getTo().getName());

            socket.shutdownOutput();
            socket.shutdownInput();

            socket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
