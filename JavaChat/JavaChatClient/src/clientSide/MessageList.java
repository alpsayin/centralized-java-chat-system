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

public class MessageList extends LinkedList<Message> 
{
    Client from;
    public MessageList(Client f)
    {
        super();
        from = f;
    }
    public Client getFrom()
    {
        return from;
    }
}
