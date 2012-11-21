package clientSide;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Alp
 */

import java.util.Scanner;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Client.HostAddressNullException
    {
        try
        {
            Constants.readSettingsFromFile("settings.freechatsettings");

            Scanner scan = new Scanner(System.in);
            System.out.println("Enter your alias: ");
            String alias = scan.nextLine();
            if(Constants.SERVER_ADDRESS.equals(""))
            {
                System.out.println("Enter Server IP address: ");
                Constants.SERVER_ADDRESS = scan.nextLine();
            }
            ClientHandler h = new ClientHandler(Constants.SERVER_ADDRESS,alias);
            h.startListListener();
            h.startMessageListener();
            h.login();
            boolean run = true;
            while(run)
            {
                String line = scan.nextLine();
                if(line.equals("send"))
                {
                    System.out.println(h.list());
                    System.out.println("Enter the assigned number of the client: ");
                    boolean t = true;
                    int ip=0;
                    while(t)
                    {
                        try
                        {
                            ip = Integer.parseInt(scan.nextLine());
                            assert(ip>=0 && ip<h.list.clients.size());
                            t = false;
                        }
                        catch(Exception e)
                        {
                            System.out.println("The number you entered is found to be faulty, try again...");
                        }
                    }
                    System.out.println("Enter your message: ");
                    String msg = scan.nextLine();
                    Client remote = h.list.clients.get(ip);
                    Message m = new Message(h.me, remote, msg);
                    h.sendMessage(m);
                }
                else if(line.equals("kill"))
                {
                    h.kill();
                    run = false;
                }
                else if(line.equals("stop"))
                {
                    h.kill();
                }
                else if(line.equals("getinfo"))
                {
                    Thread[] arr = new Thread[Thread.activeCount()];
                    Thread.enumerate(arr);
                    for(Thread th : arr)
                        System.out.println(th);
                }
                else if(line.equals("clients"))
                {
                    h.refreshList();
                    System.out.println(h.list());
                }
                else
                {
                    System.out.println("help - this message");
                    System.out.println("send - send a message");
                    System.out.println("kill - kills");
                    System.out.println("stop - stops services");
                    System.out.println("getinfo - shows active threads");
                    System.out.println("clients - prints connected clients");
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
