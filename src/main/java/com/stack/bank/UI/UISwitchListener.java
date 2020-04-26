package com.stack.bank.UI;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class UISwitchListener implements PropertyChangeListener {
    JComponent componentToSwitch;

    public UISwitchListener(JComponent c) {
        componentToSwitch = c;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String name = e.getPropertyName();
		if (name.equals("lookAndFeel")) {
		    SwingUtilities.updateComponentTreeUI(componentToSwitch);
		    componentToSwitch.invalidate();
		    componentToSwitch.validate();
		    componentToSwitch.repaint();
		}
    }
}
