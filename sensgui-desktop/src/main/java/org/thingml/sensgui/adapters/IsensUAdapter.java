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

import java.io.File;
import javax.swing.ImageIcon;
import org.thingml.rtsync.core.ITimeSynchronizerLogger;
import org.thingml.traale.desktop.BLEExplorerDialog;
import org.thingml.traale.desktop.TraaleFileLogger;
import org.thingml.traale.desktop.TraaleFrame;
import org.thingml.traale.driver.Traale;
import org.thingml.traale.driver.TraaleListener;

public class IsensUAdapter extends AbstractSensGUIAdapter implements TraaleListener,ITimeSynchronizerLogger {
    
    protected BLEExplorerDialog bledialog = new BLEExplorerDialog();
    protected TraaleFrame gui = new TraaleFrame();
    protected Traale sensor = null;
    
    protected String name = "ISenseU XXX";
    
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
            sensor.getTimeSynchronizer().addLogger(this);
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
        if (gui != null) {
            gui.setVisible(false);
            gui.dispose();
            gui = null;
        }
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
        notify_Activity(50);
    }

    @Override
    public void skinTemperatureInterval(int value) {
        
    }

    @Override
    public void humidity(int t1, int h1, int t2, int h2, int timestamp) {
        notify_Activity(50);
    }

    @Override
    public void humidityInterval(int value) {
        
    }

    @Override
    public void imu(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        notify_Activity(5);
    }

    @Override
    public void quaternion(int w, int x, int y, int z, int timestamp) {
       notify_Activity(5);
    }

    @Override
    public void imuMode(int value) {
       
    }

    @Override
    public void imuInterrupt(int value) {
        notify_Activity(100);
    }

    @Override
    public void magnetometer(int x, int y, int z, int timestamp) {
        notify_Activity(10);
    }

    @Override
    public void magnetometerInterval(int value) {
        
    }

    @Override
    public void battery(int battery, int timestamp) {
        notify_Activity(100);
    }

    @Override
    public void testPattern(byte[] data, int timestamp) {
        notify_Activity(5);
    }

    @Override
    public void timeSync(int seq, int timestamp) {
        notify_Activity(25);
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
        for (SensGUI l : listeners) l.refreshSensorView();
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
    
    private int act_counter = 0;
    private void notify_Activity(int weight) {
        act_counter += weight;
        if (act_counter > 100) {
            for(SensGUI l : listeners) {
                l.activity();
            }
            act_counter = 0;
        }
    }
    
    
    public static ImageIcon icon = new ImageIcon(ChestBeltAdapter.class.getResource("/isenseu48.png"));
    
    @Override
    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    public int getMaxBandwidth() {
        return 2000;
    }

    @Override
    public long getReceivedByteCount() {
        if (sensor!=null) return sensor.getReceivedBytes();
        else return 0;
    }
    
    @Override
    public void timeSyncStart() {
        
    }

    @Override
    public void timeSyncReady() {
        
    }

    @Override
    public void timeSyncStop() {
        
    }

    @Override
    public void timeSyncPingTimeout(int pingSeqNum, long tmt) {
       
    }

    @Override
    public void timeSyncWrongSequence(int pingSeqNum, int pongSeqNum) {
       
    }

    @Override
    public void timeSyncPong(int delay, int dtt, int dtr, int dts) {
        for(SensGUI l : listeners) l.setPing(delay);
    }

    @Override
    public void timeSyncDtsFilter(int dts) {
       
    }

    @Override
    public void timeSyncErrorFilter(int error) {
       
    }

    @Override
    public void timeSyncLog(String time, long ts, long tmt, long tmr, long delay, long offs, long error, long errorSum, long zeroOffset, long regOffsMs, int skipped, long tsOffset) {
        
    }

    @Override
    public void timeSyncPongRaw(String time, int rcvPingSeqNum, int expectedPingSeqNum, long tmt, long tmr, long ts) {
        
    }
    
    TraaleFileLogger file_logger;

   @Override
    public void startLogging(File folder) {
        if (file_logger != null) stopLogging();
        file_logger = new TraaleFileLogger(folder, sensor);
        sensor.addTraaleListener(file_logger);
        file_logger.startLoggingInFolder(folder);
    }

    @Override
    public void stopLogging() {
        if (file_logger != null) {
            file_logger.stopLogging();
            sensor.removeTraaleListener(file_logger);   
        }
        file_logger = null;
    }
    
}
