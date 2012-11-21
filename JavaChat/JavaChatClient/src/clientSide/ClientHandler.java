package clientSide;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */
public class ClientHandler 
{
    Client me;
    Server server;
    ClientList list;
    MessageMap map;
    
    ClientListListener listListener;
    ClientRequestSender requestSender;
    ClientMessageSender messageSender;
    ClientMessageListener messageListener;
    
    private static final String encryptionKey = Constants.ROOT;
    
    public ClientHandler(String serverAddress,String name) throws Client.HostAddressNullException
    {
        this.server = new Server();
        this.server.setHostName(serverAddress);
        this.me = new Client(name);
        
        initialize();
    }
    public ClientHandler(String serverAddress) throws Client.HostAddressNullException
    {
        this.me = new Client();
        this.server = new Server();
        this.server.setHostName(serverAddress);
        
        initialize();
    }
    protected void initialize()
    {
        map = new MessageMap(me);
        listListener = null;
        requestSender = null;
        messageSender = null;
        messageListener = null;
    }
    public void kill()
    {
        logout();
        if(listListener!=null)
            if(listListener.isAlive())
                listListener.finish();
        if(messageListener!=null)
            if(messageListener.isAlive())
                messageListener.finish();                       
    }
    public MessageMap getMessageMap()
    {
        return map;
    }
    public synchronized ClientList list()
    {
        return list;
    }
    public synchronized void startMessageListener()
    {
        if(messageListener==null)
            messageListener = new ClientMessageListener(me, this);
        messageListener.startMessageListener();
    }
    public synchronized void takeMessage(Client server, Message m)
    {
        System.out.println("Message taken from server: "+server);
        Client sender = m.getFrom();
        MessageList newList = map.getMessageList(sender);
        if(newList==null)
        {
            newList = new MessageList(m.getFrom());
            map.add(newList);
        }
        newList.add(m);
        System.out.println(m);
    }
    public synchronized void startListListener()
    {
        if(listListener==null)
            listListener = new ClientListListener(me, this);
        listListener.startListListener();
    }
    public synchronized void updateList(ClientList list)
    {
        this.list = list;
    }
    public synchronized void sendMessage(Message m)
    {
        messageSender = new ClientMessageSender(m, me, server);
        messageSender.startMessageSender();
    }
    public synchronized void login()
    {
        sendCommand(Constants.LOGIN);
    }
    public synchronized void logout()
    {
        sendCommand(Constants.LOGOUT);
    }
    public synchronized void refreshList()
    {
        sendCommand(Constants.GETLIST);
    }
    protected synchronized void sendCommand(byte b)
    {
        requestSender = new ClientRequestSender(b, me, server);
        requestSender.startRequestSender();
    }
}
