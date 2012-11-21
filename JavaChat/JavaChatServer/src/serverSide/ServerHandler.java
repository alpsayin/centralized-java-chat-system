package serverSide;







/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerHandler implements ActionSender
{
    LinkedList<ActionListener> listenerList = new LinkedList<ActionListener>();
    Server server;
    ClientList list;
    ServerRequestListener requestListener;
    ServerListSender listSender;
    ServerMessageListener messageListener;
    ServerMessageSender messageSender;
    boolean visible = false;
    public ServerHandler()  throws Client.HostAddressNullException
    {
        
        this.server = new Server();
        this.list = new ClientList();
        initialize();
    }
    public ServerHandler(Server s)
    {
        this.server = s;
        this.list = new ClientList();
        initialize();
    }
    public ServerHandler(Server s, ClientList list)
    {
        this.server = s;
        this.list = list;
        initialize();
    }
    private void initialize()
    {
        requestListener = null;
        listSender = null;
        messageListener = null;
        messageSender = null;
    }
    public void setVisible(boolean b)
    {
        visible = b;
    }
    public boolean isVisible()
    {
        return visible;
    }
    public synchronized Server getServer()
    {
        return server;
    }
    public void getInfo(byte level)
    {
        if(visible)
        {
            if(level>=1) 
            {
                System.out.print("Local HostName: ");
                System.out.println(server.pcName);
            } 
            if(level>=2)
            {
                System.out.print("Local HostAddress: ");
                System.out.println(server.hostName);
            }
            if(level>=3)
            {
                System.out.print("Number of Active Threads: ");
                System.out.println(Thread.activeCount());
            }
            if(level>=4)
            {
                System.out.println("Active threads:");
                Thread[] tarray = new Thread[Thread.activeCount()];
                Thread.enumerate(tarray);
                for(int i=0; i<Thread.activeCount(); i++)
                    System.out.println(tarray[i]);
            }
            if(level<1)
                System.out.println("ServerHandler.getInfo(byte b) -> Wrong Option");
        }
    }
    public void getInfo()
    {
        getInfo((byte)4);
    }
    public synchronized void kill()
    {
        if(messageListener!=null)
            messageListener.finish();
        if(requestListener!=null)
            requestListener.finish();
        
        initialize();
    }
    public synchronized ClientList list()
    {
        return list;
    }
    public synchronized void startMessageListener()
    {
        if(messageListener==null)
            messageListener = new ServerMessageListener(server, this);
        messageListener.startMessageListener();
        fireAction("ServerMessageListener started");
    }
    public synchronized void startRequestListener()
    {
        if(requestListener==null) 
            requestListener = new ServerRequestListener(server, this); 
        requestListener.startRequestListener(); 
        fireAction("ServerRequestListener started");
    }
    public synchronized boolean addClient(Client c)
    {
        boolean result = list().addClient(c);
        if(result)
        {
            Iterator<Client> i = list().iterator();
            while(i.hasNext())
            {
                Client tmp = i.next();
                if(!tmp.equals(c))
                    sendList(tmp);
            }
        }
        fireAction(c+" logged in");
        return result;
    }
    public synchronized boolean removeClient(Client c)
    {
        boolean result = list().removeClient(c);
        if(result)
        {
            Iterator<Client> i = list().iterator();
            while(i.hasNext())
            {
                Client tmp = i.next();
                if(!tmp.equals(c))
                    sendList(tmp);
            }
        }
        fireAction(c+" logged out");
        return result;
    }
    public synchronized void sendList(Client c)
    {
        listSender = new ServerListSender(list(), c, server, this);
        listSender.startListSender();
        fireAction("ClientList sent to "+c);
    }
    public synchronized void printList()
    {
        if(visible)
            System.out.println(list());
    }
    public synchronized void sendMessage(Client c, Message m)
    {
        messageSender = new ServerMessageSender(c ,m, server, this);
        messageSender.startMessageSender();
        fireAction(c+" has sent a message to "+m.getTo());
    }
    public boolean addActionListener(ActionListener a)
    {
        return listenerList.add(a);
    }
    public ActionEvent fireAction(String cmd)
    {
        ActionEvent ae = new ActionEvent(this,0,cmd);
        for(ActionListener a : listenerList)
            a.actionPerformed(ae);
//            new ActionThread(ae,a);
        return ae;
    }
//    private class ActionThread extends Thread
//    {
//        ActionEvent ae;
//        ActionListener a;
//        public ActionThread(ActionEvent ae, ActionListener a)
//        {
//            this.ae = ae;
//            this.a = a;
//            super.start();
//        }
//        @Override public void run()
//        {
//            ae.actionPerformed(a);
//        }
//    }
}
