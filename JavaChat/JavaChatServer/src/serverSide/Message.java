package serverSide;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Message 
{
    private Client from;
    private Client to;
    private String message;
    
    public Message()
    {
        from = Constants.DEFAULTCLIENT;
        to = Constants.DEFAULTCLIENT;
        message="faulty message!?!";
    }
    
    public Message(Client from, Client to, String s)
    {
        this.from=from;
        this.to=to;
        message=s;
    }
    public Client getFrom()
    {
        return from;
    }
    public void setFrom(Client from)
    {
        this.from = from;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Client getTo() {
        return to;
    }

    public void setTo(Client to) {
        this.to = to;
    }
    public String encrypt(String s)
    {
        String mix = "";
        for(int i=0; i<message.length(); i++)
            mix += ( s.charAt( i%s.length() ) + message.charAt(i) );
        return mix;
    }
    public String decrypt(String s)
    {
        String demix = "";
        for(int i=0; i<message.length(); i++)
            demix += ( message.charAt(i) - s.charAt( i%s.length() ) );
        return demix;
    }
    public void sendMessageInTheBottle(DataOutputStream out)
    {
        try
        {
            from.sendClientInfo(out);
            to.sendClientInfo(out);
            out.writeUTF(message);
        }
        catch(Exception e)
        {
            System.err.println(e);
            e.printStackTrace();
        }
    }
    public String toString()
    {
        String result = from.getName() + "\n";
        result += to.getName() + "\n";
        result += message + "\n";
        return result; 
    }
    public static Message openMessageInTheBottle(DataInputStream in)
    {
        try
        {
            Client from = Client.getClientInfo(in);
            Client to = Client.getClientInfo(in);
            String msg = in.readUTF();
            return new Message(from, to, msg);
        }
        catch(Exception e)
        {
            System.err.println(e);
            e.printStackTrace();
            return new Message();
        }
    }
}
