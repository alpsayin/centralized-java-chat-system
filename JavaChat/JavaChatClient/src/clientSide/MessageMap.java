package clientSide;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Alp
 */

import java.util.LinkedList;

public class MessageMap extends LinkedList<MessageList>
{
    Client owner;
    
    public MessageMap(Client c)
    {
        owner = c;
    }
    public MessageMap() throws Client.HostAddressNullException
    {
        owner = new Client();
    }
    public MessageList getMessageList(Client c)
    {
        for(MessageList cl : this)
            if(cl.getFrom().equals(c))
                return cl;
        return null;
    }
    public Client getOwner()
    {
        return owner;
    }
}
