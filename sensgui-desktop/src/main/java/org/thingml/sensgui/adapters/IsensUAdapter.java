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

import org.thingml.traale.desktop.BLEExplorerDialog;
import org.thingml.traale.desktop.TraaleFrame;
import org.thingml.traale.driver.Traale;
import org.thingml.traale.driver.TraaleListener;

public class IsensUAdapter implements SensGUIAdapter, TraaleListener {
    
    protected BLEExplorerDialog bledialog = new BLEExplorerDialog();
    protected TraaleFrame gui = new TraaleFrame();
    protected Traale sensor = null;
    
    protected String name = "ISenseU ???";
    
    public IsensUAdapter() {
        bledialog.setModal(true);
        gui.setVisible(false);
        gui.disableConnectionButton();
    }
    
    @Override
    public String getSensorName() {
        System.out.println("getSensorName");
        return name;
    }
    
    @Override
    public boolean connect() {

        if (sensor != null) sensor.stopTimeSync();
        
        bledialog.setVisible(true);
        
        if (bledialog.isConnected()) {
            sensor = new Traale(bledialog.getBgapi(), bledialog.getConnection());
            gui.setSensor(sensor);
            sensor.addTraaleListener(this);
            sensor.subscribeBattery();
            sensor.startTimeSync();
            sensor.requestDeviceInfo();
            return true;
        }

        return false;
    }

    @Override
    public boolean isConnected() {
        return sensor != null;
    }

    @Override
    public void disconnect() {
        bledialog.disconnect();
        sensor = null;
    }

    @Override
    public void showgui() {
        gui.setVisible(true);
        sensor.requestDeviceInfo();
    }

    @Override
    public void skinTemperature(double temp, int timestamp) {
        
    }

    @Override
    public void skinTemperatureInterval(int value) {
        
    }

    @Override
    public void humidity(int t1, int h1, int t2, int h2, int timestamp) {
        
    }

    @Override
    public void humidityInterval(int value) {
        
    }

    @Override
    public void imu(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        
    }

    @Override
    public void quaternion(int w, int x, int y, int z, int timestamp) {
       
    }

    @Override
    public void imuMode(int value) {
       
    }

    @Override
    public void imuInterrupt(int value) {
        
    }

    @Override
    public void magnetometer(int x, int y, int z, int timestamp) {
        
    }

    @Override
    public void magnetometerInterval(int value) {
        
    }

    @Override
    public void battery(int battery, int timestamp) {
        
    }

    @Override
    public void testPattern(byte[] data, int timestamp) {
        
    }

    @Override
    public void timeSync(int seq, int timestamp) {
        
    }

    @Override
    public void manufacturer(String value) {
        
    }

    @Override
    public void model_number(String value) {
        
    }

    @Override
    public void serial_number(String value) {
        name = "ISenseU " + value;
        //System.out.println("Name = " + name );
    }   

    @Override
    public void hw_revision(String value) {
        
    }

    @Override
    public void fw_revision(String value) {
        
    }

    @Override
    public void alertLevel(int value) {
        
    }
}
