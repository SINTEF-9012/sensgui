package org.thingml.sensgui.adapters;

import java.util.ArrayList;

/**
 *
 * @author ffl
 */
public abstract class AbstractSensGUIAdapter implements SensGUIAdapter {
    
    protected ArrayList<SensGUI> listeners = new ArrayList<SensGUI>();
    
    public void addListener(SensGUI sensgui) {
        listeners.add(sensgui);
    }
    
    public void removeListener(SensGUI sensgui) {
        listeners.remove(sensgui);
    }
    
    public void clearListeners() {
        listeners.clear();
    }
    
    
}
