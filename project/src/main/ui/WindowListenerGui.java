package ui;


import model.Event;
import model.EventLog;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;

public class WindowListenerGui implements WindowListener {

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        for (Iterator<Event> it = EventLog.getInstance().iterator(); it.hasNext(); ) {
            Event event = it.next();
            System.out.println(event.getDate() + " -> " + event.getDescription());
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
