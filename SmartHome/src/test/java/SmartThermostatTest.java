package src.test.java;

import org.junit.Test;
import devices.SmartThermostat;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;

public class SmartThermostatTest {
    //Power Test Cases
    @Test
    public void powerOffTest() {
        SmartThermostat thermostat = new SmartThermostat();
        assertFalse("Should be off initially", thermostat.powerStatus());
    }

    @Test
    public void powerOnTest(){
        SmartThermostat thermostat = new SmartThermostat();
        thermostat.turnOnDevice();
        assertTrue("First Turn on  Device.", thermostat.powerStatus());
    }


    //wifi Test Cases
    @Test //check if is it possible to check connection when device is off
    public void checkConnectionIfDeviceOffTest(){
        SmartThermostat thermostat = new SmartThermostat();
        thermostat.turnOffDevice();
        assertFalse("first turn of Device for check connection", thermostat.checkConnection());
    }

    @Test//check if is it possible to check connection when device is on
    public void checkConnectionIfDeviceOnTest(){
        SmartThermostat thermostat = new SmartThermostat();
        thermostat.turnOnDevice();
        assertFalse("Initially Wifi off", thermostat.checkConnection());
    }

    @Test  //is connect to wifi while device is off
    public void connectWifiDeviceOffTest(){
        SmartThermostat thermostat = new SmartThermostat();
        thermostat.turnOffDevice();
        thermostat.connectWIFI("myHome");
        assertFalse("First Turn on Device", thermostat.checkConnection());
    }

    @Test  //is connect to wifi while device is on
    public void connectWifiTest() {
        SmartThermostat thermostat = new SmartThermostat();
        thermostat.turnOnDevice();
        thermostat.connectWIFI("myhome");
        assertTrue("turn on device and check connection", thermostat.checkConnection());
    }

    //Temperature Test Cases
    public void turnOnSomeTemperature(){
        SmartThermostat thermostat = new SmartThermostat();
        thermostat.turnOnDevice();
        assertEquals("when device is turned on device should have some audible", 30, thermostat.getTemperature());
    }

    @Test  //user will not get temperature if device is off
    public void turnOffCheckGetTemp(){
        SmartThermostat thermostat = new SmartThermostat();
        // thermostat.turnOnDevice();
        assertEquals("Please Turn On Device", -100 ,thermostat.getTemperature());
    }

    @Test  //user will not get temperature if device is off
    public void notConnectedCheckGetTemp(){
        SmartThermostat thermostat = new SmartThermostat();
        thermostat.turnOnDevice();
        assertEquals("Please Turn On Internet", -200 ,thermostat.getTemperature());
    }

    @Test //user will adjust temp while device is on
    public void turnOnCheckSetTemp(){
        SmartThermostat thermostat = new SmartThermostat();
        thermostat.turnOnDevice();
        thermostat.connectWIFI("MyHome");

        assertTrue("First Turn On Device",thermostat.powerStatus()); //on
        thermostat.setTemperature(12);
        assertEquals("Volume is not being asserted",12,thermostat.getTemperature());
    }

    @Test  //user will not adjust volume while device is off
    public void turnOffCheckSetTemp(){
        SmartThermostat thermostat = new SmartThermostat();
        assertFalse("First Turn On Device",thermostat.powerStatus()); //off
        thermostat.setTemperature(45);
        assertNotSame("Volume is not being asserted",45,thermostat.getTemperature());
    }
}
