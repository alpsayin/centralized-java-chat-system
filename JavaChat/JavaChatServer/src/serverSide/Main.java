package serverSide;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */
import javax.swing.Timer;

import java.util.Scanner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main 
{
    public static void main(String args[]) throws Exception
    {
        Constants.readSettingsFromFile("settings.freechatsettings");
        System.out.println(Constants.ROOT);
        ServerHandler sH = new ServerHandler();
        
        int wait = 5000;
        
        if(args.length>=1)
        {
            if(args[0].equals("-t"))
                if(args.length>=2)
                    try
                    {
                        wait = Integer.parseInt(args[1]);
                    }
                    catch(Exception e)
                    {
                        wait = 5000;
                        System.err.println("You entered wrong parameter. \n Timer has been set to 5000 millisecs.");
                    }
        }
        ActionListener a = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                //sH.getInfo();
            }
        };
        Timer t = new Timer(wait,a);
        sH.startMessageListener();
        sH.startRequestListener();
        System.out.println("Server is started...");
        //t.start();
        Scanner scan = new Scanner(System.in);
        boolean run = true;
        while(run)
        {
            String line = scan.nextLine();
            if(line.equals("kill"))
            {
                if(sH!=null)
                    sH.kill();
                sH = null;
                run = false;
            }
            else if(line.equals("stop"))
            {
                if(sH!=null)
                    sH.kill();
            }
            else if(line.equals("getinfo"))
            {
                if(sH!=null)
                    sH.getInfo((byte)3);
                System.out.println();
                Thread[] arr = new Thread[Thread.activeCount()];
                Thread.enumerate(arr);
                for(Thread th : arr)
                    System.out.println(th);
            }
            else if(line.equals("clients"))
            {
                System.out.println(sH.list);
                System.out.println();
            }
            else
            {
                System.out.println("*********************************");
                System.out.println("kill - exits");
                System.out.println("getinfo - prints Threads");
                System.out.println("clients - prints clientlist");
                System.out.println("help - brings this text");
                System.out.println("*********************************");
                System.out.println();
            }
        }
        System.out.println(run);
        Thread[] arr = new Thread[Thread.activeCount()];
        Thread.enumerate(arr);
        for(Thread th : arr)
            System.out.println(th);
    }
}
