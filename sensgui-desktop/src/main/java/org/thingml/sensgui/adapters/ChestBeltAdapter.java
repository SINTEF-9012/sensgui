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

import org.thingml.chestbelt.desktop.ChestBeltMainFrame;
import org.thingml.chestbelt.driver.ChestBelt;
import org.thingml.chestbelt.driver.ChestBeltListener;

public class ChestBeltAdapter implements SensGUIAdapter, ChestBeltListener{

    protected ChestBelt sensor = null;
    protected ChestBeltMainFrame gui = new ChestBeltMainFrame();
    
    protected String name = "ChestBelt ???";
    
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
        return true;
    }

    @Override
    public boolean isConnected() {
        return sensor != null;
    }

    @Override
    public void disconnect() {
        sensor.close();
        gui.tryToCloseSerialPort();
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
    }

    @Override
    public void cUFWRevision(String value, int timestamp) {

    }

    @Override
    public void batteryStatus(int value, int timestamp) {

    }

    @Override
    public void indication(int value, int timestamp) {

    }

    @Override
    public void status(int value, int timestamp) {

    }

    @Override
    public void messageOverrun(int value, int timestamp) {

    }

    @Override
    public void referenceClockTime(long value, boolean seconds) {

    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {

    }

    @Override
    public void heartRate(int value, int timestamp) {

    }

    @Override
    public void heartRateConfidence(int value, int timestamp) {

    }

    @Override
    public void eCGData(int value) {

    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {

    }

    @Override
    public void eCGRaw(int value, int timestamp) {

    }

    @Override
    public void eMGData(int value) {

    }

    @Override
    public void eMGSignalQuality(int value, int timestamp) {

    }

    @Override
    public void eMGRaw(int value, int timestamp) {

    }

    @Override
    public void eMGRMS(int channelA, int channelB, int timestamp) {

    }

    @Override
    public void gyroPitch(int value, int timestamp) {

    }

    @Override
    public void gyroRoll(int value, int timestamp) {

    }

    @Override
    public void gyroYaw(int value, int timestamp) {

    }

    @Override
    public void accLateral(int value, int timestamp) {

    }

    @Override
    public void accLongitudinal(int value, int timestamp) {

    }

    @Override
    public void accVertical(int value, int timestamp) {

    }

    @Override
    public void rawActivityLevel(int value, int timestamp) {

    }

    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {

    }

    @Override
    public void skinTemperature(int value, int timestamp) {

    }

    @Override
    public void connectionLost() {
        gui.tryToCloseSerialPort();
        sensor = null;
    }
    
}
