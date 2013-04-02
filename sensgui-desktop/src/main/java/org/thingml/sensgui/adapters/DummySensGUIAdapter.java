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

import javax.swing.JOptionPane;


public class DummySensGUIAdapter implements SensGUIAdapter {

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
    
}
