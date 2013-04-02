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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.sensgui.adapters;

import javax.swing.ImageIcon;

/**
 *
 * @author ffl
 */
public interface SensGUIAdapter {
    
    public void addListener(SensGUI sensgui);
    public void removeListener(SensGUI sensgui);
    public void clearListeners();
    
    public String getSensorName();
    public boolean connect();
    public boolean isConnected();
    public void disconnect();
    public void showgui();
    
    public ImageIcon getIcon();
    public int getMaxBandwidth();
    
    public long getReceivedByteCount();
    
}
