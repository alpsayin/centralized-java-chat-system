package serverSide;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alp
 */
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public interface ActionSender 
{
    public boolean addActionListener(ActionListener a);
    public ActionEvent fireAction(String cmd);
}
