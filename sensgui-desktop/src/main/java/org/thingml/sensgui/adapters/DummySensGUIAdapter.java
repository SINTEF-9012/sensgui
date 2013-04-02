/**
 * Copyright (C) 2013 SINTEF <franck.fleurey@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingml.sensgui.adapters;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class DummySensGUIAdapter extends AbstractSensGUIAdapter implements Runnable {


    private static int id = 1;
    
    private boolean connected = false;
    private String name = "Dummy Sensor #" + (id++);
    
    @Override
    public String getSensorName() {
        return name;
    }

    @Override
    public boolean connect() {
        connected = true;
        new Thread(this).start();
        return connected;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void disconnect() {
        connected = false;
    }

    @Override
    public void showgui() {
        JOptionPane.showMessageDialog(null, "GUI for " + name);
    }
    
    private static Random rand = new Random();
    
    public void run() {
        
        int i = 0;
        
        int bwavg = rand.nextInt(7000) + 1000;
        int pingavg = rand.nextInt(500) + 10;
        
        while (connected) {
            try {
                Thread.sleep(rand.nextInt(150) + 100);
                for (SensGUI l : listeners) l.activity();
            } catch (InterruptedException ex) {
                Logger.getLogger(DummySensGUIAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            bytecount += bwavg/5;

            if (i == 2) {
                for (SensGUI l : listeners) l.setPing(rand.nextInt(50) + pingavg);
            }
            i = (i+1)%4;
        }
    }
    
    public static ImageIcon icon = new ImageIcon(ChestBeltAdapter.class.getResource("/test48.png"));
    
    @Override
    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    public int getMaxBandwidth() {
        return 10000;
    }

    long bytecount = 0;
    
    @Override
    public long getReceivedByteCount() {
        return bytecount;
    }
    
}
