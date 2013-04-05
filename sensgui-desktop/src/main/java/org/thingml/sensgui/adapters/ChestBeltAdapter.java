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
import org.thingml.chestbelt.desktop.ChestBeltFileLogger;
import org.thingml.chestbelt.desktop.ChestBeltMainFrame;
import org.thingml.chestbelt.driver.ChestBelt;
import org.thingml.chestbelt.driver.ChestBeltListener;
import org.thingml.rtsync.core.ITimeSynchronizerLogger;

public class ChestBeltAdapter extends AbstractSensGUIAdapter implements ChestBeltListener, ITimeSynchronizerLogger {

    protected ChestBelt sensor = null;
    protected ChestBeltMainFrame gui = new ChestBeltMainFrame();
    
    protected String name = "ChestBelt XXX";

    
    public ChestBeltAdapter() {
        gui.setVisible(false);
        gui.disableConnection();
    }
    
    @Override
    public String getSensorName() {
        return name;
    }
    
    public boolean connect() {
        sensor = gui.connectChestBelt();
        if (sensor == null) return false;
        sensor.addChestBeltListener(this);
        gui.do_connect(sensor);
        sensor.getTimeSynchronizer().addLogger(this);
        sensor.getSerialNumber();
        return true;
    }

    @Override
    public boolean isConnected() {
        return sensor != null;
    }

    @Override
    public void disconnect() {
        if (file_logger != null) stopLogging();
        sensor.close();
        gui.tryToCloseSerialPort();
        if (gui != null) {
            gui.setVisible(false);
            gui.dispose();
            gui = null;
        }
        sensor = null;
    }

    @Override
    public void showgui() {
        gui.setVisible(true);
        sensor.getSerialNumber();
    }

    @Override
    public void cUSerialNumber(long value, int timestamp) {
        name = "ChestBelt " + value;
        for (SensGUI l : listeners) l.refreshSensorView();
    }

    @Override
    public void cUFWRevision(String value, int timestamp) {

    }

    @Override
    public void batteryStatus(int value, int timestamp) {
        notify_Activity(50);
    }

    @Override
    public void indication(int value, int timestamp) {
        notify_Activity(25);
    }

    @Override
    public void status(int value, int timestamp) {
        notify_Activity(25);
    }

    @Override
    public void messageOverrun(int value, int timestamp) {

    }

    @Override
    public void referenceClockTime(long value, boolean seconds) {
        notify_Activity(40);
    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
        notify_Activity(30);
    }
    
    @Override
    public void referenceClockTimeSync(int timeSyncSeqNum, long value) {
         notify_Activity(50);
    }

    @Override
    public void heartRate(int value, int timestamp) {
        notify_Activity(50);
    }

    @Override
    public void heartRateConfidence(int value, int timestamp) {

    }

    @Override
    public void eCGData(int value) {
        notify_Activity(2);
    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {

    }

    @Override
    public void eCGRaw(int value, int timestamp) {
        notify_Activity(2);
    }

    @Override
    public void eMGData(int value) {
        notify_Activity(1);
    }

    @Override
    public void eMGSignalQuality(int value, int timestamp) {

    }

    @Override
    public void eMGRaw(int value, int timestamp) {
        notify_Activity(1);
    }

    @Override
    public void eMGRMS(int channelA, int channelB, int timestamp) {
        notify_Activity(20);
    }

    @Override
    public void gyroPitch(int value, int timestamp) {
        notify_Activity(3);
    }

    @Override
    public void gyroRoll(int value, int timestamp) {
        notify_Activity(3);
    }

    @Override
    public void gyroYaw(int value, int timestamp) {
        notify_Activity(3);
    }

    @Override
    public void accLateral(int value, int timestamp) {
        notify_Activity(3);
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
        notify_Activity(3);
    }

    @Override
    public void accVertical(int value, int timestamp) {
        notify_Activity(3);
    }

    @Override
    public void rawActivityLevel(int value, int timestamp) {
         notify_Activity(25);
    }

    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        notify_Activity(10);
    }

    @Override
    public void skinTemperature(int value, int timestamp) {
        notify_Activity(50);
    }

    @Override
    public void connectionLost() {
        gui.tryToCloseSerialPort();
        sensor = null;
        for (SensGUI l : listeners) l.refreshSensorView();
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

    public static ImageIcon icon = new ImageIcon(ChestBeltAdapter.class.getResource("/chestbelt48.png"));
    
    @Override
    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    public int getMaxBandwidth() {
        return 11500;
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

    protected ChestBeltFileLogger file_logger;
    
    @Override
    public void startLogging(File folder) {
        if (file_logger != null) stopLogging();
        file_logger = new ChestBeltFileLogger(folder, sensor, true);
        sensor.addChestBeltListener(file_logger);
        file_logger.startLoggingInFolder(folder);
    }

    @Override
    public void stopLogging() {
        if (file_logger != null) {
            file_logger.stopLogging();
            sensor.removeChestBeltListener(file_logger);   
        }
        file_logger = null;
    }
    
    @Override
    public void identify() {
        if (sensor != null) {
            sensor.sendAlert(2);
        }
    }
    
}
