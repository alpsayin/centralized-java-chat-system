package clientSide;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */

import java.io.File;
import java.util.Scanner;

public class Constants 
{
    public static final String author_Name = "Alp Sayın";
    public static final String author_Email = "alp_sayin@ieee.org";
    
    //when choosing ports it is better to stay away from popular ports
    //80(http), 22(telnet), 23(ssh), 21(ftp), 25(smtp), 79(finger)
    //53(dns), 69(tftp), 110(pop3), nntp(119), snmp(161)
    //Msn messenger generally uses ports between 4000 and 8000 etc.
    public static String SERVER_ADDRESS = "";
    //note that request sending and listening ports should be the same
    //note that they mustn't be same with messaging ports
    public static int REQUESTPORT = 45538;
    public static int LISTPORT = 45537;
    //note that message sending and listening port should be the same
    public static int CLIENT_MESSAGELISTENER_PORT = 45536;
    public static int SERVER_MESSAGESENDER_PORT = CLIENT_MESSAGELISTENER_PORT;
    public static int CLIENT_MESSAGESENDER_PORT = 45535;
    public static int SERVER_MESSAGELISTENER_PORT = CLIENT_MESSAGESENDER_PORT;
    //request constants mustn't be the same, they should be between -128 & 127
    //except that their values doesn't matter
    public static final byte LOGIN = 1;
    public static final byte LOGOUT = 2;
    public static final byte GETLIST = 3;
    //I don't where I'll use them..
    public static final String ZERO_IP = "0.0.0.0";
    public static String ROOT = "03011990";
    //these dumbs are really dangerous if you are going to make any tests...
    public static final Server DEFAULTSERVER = Server.getDumbServer();
    public static final Client DEFAULTCLIENT = Client.getDumbClient();
    public static final Message DEFAULTMESSAGE = new Message();
    //Socket timeout delay (milliseconds)
    public static int SOCKETTIMEOUT = 1000;
    
    public static void readSettingsFromFile(String fileName)
    {
        try
        {
            File f = new File(System.getProperty("java.class.path")+File.separator+".."+File.separator+fileName);
            Scanner scan = new Scanner(f);
            
            String sROOT=ROOT;
            String sSERVER_ADDRESS=SERVER_ADDRESS;
            int sREQUESTPORT=REQUESTPORT;
            int sLISTPORT=LISTPORT;
            int sSERVER_MESSAGELISTENER_PORT=SERVER_MESSAGELISTENER_PORT;
            int sCLIENT_MESSAGELISTENER_PORT=CLIENT_MESSAGELISTENER_PORT;
            int sSERVER_MESSAGESENDER_PORT=SERVER_MESSAGESENDER_PORT;
            int sCLIENT_MESSAGESENDER_PORT=CLIENT_MESSAGESENDER_PORT;
            int sSOCKETTIMEOUT=SOCKETTIMEOUT;
            
            while(scan.hasNextLine())
            {
                String line = scan.nextLine();
                if(line.length() < 2)
                    continue;
                if(line.substring(0,2).equals("//"))
                    continue;
                if(line.substring(0,line.indexOf('=')).equals("encryptionKey"))
                    sROOT = line.substring(line.indexOf('=')+1);
                if(line.substring(0,line.indexOf('=')).equals("serverAddress"))
                    sSERVER_ADDRESS = line.substring(line.indexOf('=')+1);
                if(line.substring(0, line.indexOf('=')).equals("requestPort"))
                    sREQUESTPORT = Integer.parseInt(line.substring(line.indexOf('=')+1));
                if(line.substring(0, line.indexOf('=')).equals("listPort"))
                    sLISTPORT = Integer.parseInt(line.substring(line.indexOf('=')+1));
                if(line.substring(0, line.indexOf('=')).equals("serverMessageListener"))
                    sSERVER_MESSAGELISTENER_PORT = Integer.parseInt(line.substring(line.indexOf('=')+1));
                if(line.substring(0, line.indexOf('=')).equals("clientMessageListener"))
                    sCLIENT_MESSAGELISTENER_PORT = Integer.parseInt(line.substring(line.indexOf('=')+1));
                if(line.substring(0, line.indexOf('=')).equals("serverMessageSender"))
                    sSERVER_MESSAGESENDER_PORT = Integer.parseInt(line.substring(line.indexOf('=')+1));
                if(line.substring(0, line.indexOf('=')).equals("clientMessageSender"))
                    sCLIENT_MESSAGESENDER_PORT = Integer.parseInt(line.substring(line.indexOf('=')+1));
                if(line.substring(0, line.indexOf('=')).equals("socketTimeout"))
                    sSOCKETTIMEOUT = Integer.parseInt(line.substring(line.indexOf('=')+1));
                    
            }
            ROOT = sROOT;
            SERVER_ADDRESS = sSERVER_ADDRESS;
            REQUESTPORT = sREQUESTPORT;
            LISTPORT = sLISTPORT;
            SERVER_MESSAGELISTENER_PORT = sSERVER_MESSAGELISTENER_PORT;
            CLIENT_MESSAGELISTENER_PORT = sCLIENT_MESSAGELISTENER_PORT;
            SERVER_MESSAGESENDER_PORT = sSERVER_MESSAGESENDER_PORT;
            CLIENT_MESSAGESENDER_PORT = sCLIENT_MESSAGESENDER_PORT;
            SOCKETTIMEOUT = sSOCKETTIMEOUT;
            
        }
        catch(Exception e)
        {
            System.err.println("Bad Settings File!\nRegular Settings are set.\n");
            e.printStackTrace();
        }
    }
}
