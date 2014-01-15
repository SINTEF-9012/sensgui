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
import org.thingml.NoninOnyxII.desktop.OnyxMainFrame;
import org.thingml.onyx.driver.Onyx;
import org.thingml.onyx.driver.OnyxListener;
import org.thingml.NoninOnyxII.desktop.OnyxFileLogger;
import org.thingml.NoninOnyxII.desktop.OnyxUDPLogger;

public class OnyxAdapter extends AbstractSensGUIAdapter implements OnyxListener, ITimeSynchronizerLogger {

    protected Onyx sensor = null;
    protected OnyxMainFrame gui = new OnyxMainFrame();
    
    protected String name = "Onyx XXX";
    protected long sensorId = -1;

    
    public OnyxAdapter() {
        gui.setVisible(false);
        gui.disableConnection();
    }
    
    @Override
    public String getSensorName() {
        return name;
    }
    
    @Override
    public long getSensorId() {
        return sensorId;
    }
    
    @Override
    public boolean connect() {
        sensor = gui.connectOnyx();
        if (sensor == null) return false;
        sensor.addOnyxListener(this);
        gui.do_connect(sensor);
        //sensor.getTimeSynchronizer().addLogger(this); - Not supported by Onyx
        sensor.readSerialNumber();
        return true;
    }

    @Override
    public boolean isConnected() {
        return sensor != null;
    }

    @Override
    public void disconnect() {
        if (file_logger != null) stopLogging();
        if (udp_logger != null) stopUDPLogging();
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
        sensor.readSerialNumber();
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

    public static ImageIcon icon = new ImageIcon(OnyxAdapter.class.getResource("/onyx.png"));
    
    @Override
    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    public int getMaxBandwidth() {
        return 1000;
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

    protected OnyxFileLogger file_logger;
    
    @Override
    public void startLogging(File folder) {
        if (file_logger != null) stopLogging();
        System.err.println("startLogging(" + name + ") 21");
        file_logger = new OnyxFileLogger(folder, sensor);
        System.err.println("startLogging(" + name + ") 22");
        sensor.addOnyxListener(file_logger);
        System.err.println("startLogging(" + name + ") 23");
        file_logger.startLoggingInFolder(folder);
        System.err.println("startLogging(" + name + ") 24");
    }

    @Override
    public void stopLogging() {
        if (file_logger != null) {
            file_logger.stopLogging();
            sensor.removeOnyxListener(file_logger);   
        }
        file_logger = null;
    }
    
    @Override
    public void identify() {
        if (sensor != null) {
            // Onyx does not support alert message
            //sensor.sendAlert(2);
        }
    }

    protected OnyxUDPLogger udp_logger;
    
    @Override
    public void startUDPLogging(String unit) {
        if (udp_logger != null) stopUDPLogging();
        udp_logger = new OnyxUDPLogger(unit, sensor);
        sensor.addOnyxListener(udp_logger);
        udp_logger.startLogging();
    }

    @Override
    public void stopUDPLogging() {
        if (udp_logger != null) {
            udp_logger.stopLogging();
            sensor.removeOnyxListener(udp_logger);   
        }
        udp_logger = null;
    }

    @Override
    public void nUSerialNumber(String value) {
        name = "Onyx " + value;
        try {
            sensorId = Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + nfe.getMessage());
            sensorId = 0;
        }
        for (SensGUI l : listeners) l.refreshSensorView();
    }

    @Override
    public void nModelNumber(String value) {
    }

    @Override
    public void nFirmwareRevisionNumber(int rev1, int rev2) {
    }

    @Override
    public void nSetFormatResponse(boolean ack) {
    }

    @Override
    public void nSerialDataFormat8(byte status1, byte pulse_rate, byte spo2, byte status2) {
        notify_Activity(50);
        if ((status2 & 0x01)>0) {
            for (SensGUI l : listeners) l.setBattery(5);
        } else {
            for (SensGUI l : listeners) l.setBattery(100);
        }

    }
    
}
