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
import java.util.Iterator;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ClientList 
{
    public LinkedList<Client> clients;
    
    public ClientList()
    {
        clients = new LinkedList<Client>();
    }
    public boolean addClient(Client c)
    {
        for(Client cl : clients)
            if(cl.equals(c))
                return false;
        return clients.add(c);
    }
    public boolean removeClient(Client c)
    {
        for(Client cl : clients)
            if(cl.equals(c))
                return clients.remove(cl);
        return false;
    }
    public boolean contains(Client c)
    {
        return clients.contains(c);
    }
    public Iterator<Client> iterator()
    {
        return clients.iterator();
    }
    public static ClientList getClientList(DataInputStream in)
    {
        try
        {
            ClientList list = new ClientList();
            int size = in.readInt();
            for(int i=0; i<size; i++)
                list.clients.add(Client.getClientInfo(in));
            return list;
        }
        catch(Exception e)
        {
            System.err.println(e);
            e.printStackTrace();
            return null;
        }
    }
    public String toString()
    {
        String result="";
        for(int i=0; i<clients.size(); i++)
            result+="["+i+"]"+clients.get(i) + "\n";
        return result;
    }
    public static ClientList getClientList(ClientList list, DataInputStream in)
    {
        try
        {
            list.clients.clear();
            int size = in.readInt();
            for(int i=0; i<size; i++)
                list.clients.add(Client.getClientInfo(in));
            return list;
        }
        catch(Exception e)
        {
            System.err.println(e);
            e.printStackTrace();
            return null;
        }
    }
    public void sendClientList(DataOutputStream out)
    {
        try
        {
            out.writeInt(clients.size());
            Iterator<Client> i = clients.iterator();
            while(i.hasNext())
                i.next().sendClientInfo(out);
        }
        catch(Exception e)
        {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
