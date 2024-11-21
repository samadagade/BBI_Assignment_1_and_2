package src.test.java;

import devices.SmartTV;
import org.junit.Test;
import static org.junit.Assert.*;

//import static org.junit.Assert.assertFalse;

public class SmartTVtest {
    //Power Test Cases
    @Test
    public void powerOffTest() {
        SmartTV smartTV = new SmartTV();
        assertFalse("Should be off initially", smartTV.powerStatus());
    }

    @Test
    public void powerOnTest(){
        SmartTV smartTV = new SmartTV();
        smartTV.turnOnDevice();
        assertTrue("First Turn on  Device.", smartTV.powerStatus());
    }


    //wifi Test Cases
    @Test //check if is it possible to check connection when device is off
    public void checkConnectionIfDeviceOffTest(){
        SmartTV smartTV = new SmartTV();
        smartTV.turnOffDevice();
        assertFalse("first turn of Device for check connection", smartTV.checkConnection());
    }

    @Test//check if is it possible to check connection when device is on
    public void checkConnectionIfDeviceOnTest(){
        SmartTV smartTV = new SmartTV();
         smartTV.turnOnDevice();
        assertFalse("Initially Wifi off", smartTV.checkConnection());
    }

    @Test  //is connect to wifi while device is off
     public void connectWifiDeviceOffTest(){
        SmartTV smartTV = new SmartTV();
        smartTV.turnOffDevice();
        smartTV.connectWIFI("myHome");
        assertFalse("First Turn on Device", smartTV.checkConnection());
    }

    @Test  //is connect to wifi while device is on
    public void connectWifiTest() {
        SmartTV smartTV = new SmartTV();
        smartTV.turnOnDevice();
        smartTV.connectWIFI("myhome");
        assertTrue("turn on device and check connection", smartTV.checkConnection());
    }

    //Volume Test Cases
    @Test  //if device is turned on devices should be audible
    public void turnOnAudible(){
        SmartTV smartTV = new SmartTV();
        smartTV.turnOnDevice();
        assertEquals("when device is turned on device should be audible", 50, smartTV.getVolume());
    }

    @Test  //user will not get volume if device is off
    public void turnOffCheckGetVolume(){
        SmartTV smartTV = new SmartTV();
        //smartTV.turnOnDevice();
        assertEquals("Please Turn On Device", -1 ,smartTV.getVolume());
    }

    @Test //user will adjust volume while device is on
    public void turnOnCheckAdjustVolume(){
        SmartTV smartTV = new SmartTV();
        smartTV.turnOnDevice();
        assertTrue("First Turn On Device",smartTV.powerStatus()); //on
        smartTV.adjustVolume(45);
        assertEquals("Volume is not being asserted",45,smartTV.getVolume());
    }

    @Test  //user will not adjust volume while device is off
    public void turnOffCheckAdjustVolume(){
        SmartTV smartTV = new SmartTV();
        assertFalse("First Turn On Device",smartTV.powerStatus()); //off
        smartTV.adjustVolume(45);
        assertNotSame("Volume is not being asserted",45,smartTV.getVolume());
    }

    @Test
    public void turnOffCheckMute(){
        SmartTV smartTV = new SmartTV();
        assertFalse("First turn on device", smartTV.muteStatus());
    }

    @Test
    public void turnOnMuteCheckMute(){
        SmartTV smartTV = new SmartTV();
        smartTV.turnOnDevice();
        smartTV.muteVolume();
        assertTrue("First turn on device", smartTV.muteStatus());
    }


    //Temperature Test Cases
    public void turnOnSomeTemperature(){
        SmartTV smartTV = new SmartTV();
        smartTV.turnOnDevice();
        assertEquals("when device is turned on device should have some audible", 30, smartTV.getTemperature());
    }

    @Test  //user will not get temperature if device is off
    public void turnOffCheckGetTemp(){
        SmartTV smartTV = new SmartTV();
       // smartTV.turnOnDevice();
        assertEquals("Please Turn On Device", -100 ,smartTV.getTemperature());
    }

    @Test  //user will not get temperature if device is off
    public void notConnectedCheckGetTemp(){
        SmartTV smartTV = new SmartTV();
        smartTV.turnOnDevice();
        assertEquals("Please Turn On Internet", -200 ,smartTV.getTemperature());
    }

    @Test //user will adjust temp while device is on
    public void turnOnCheckSetTemp(){
        SmartTV smartTV = new SmartTV();
        smartTV.turnOnDevice();
        smartTV.connectWIFI("MyHome");

        assertTrue("First Turn On Device",smartTV.powerStatus()); //on
        smartTV.setTemperature(12);
        assertEquals("Volume is not being asserted",12,smartTV.getTemperature());
    }

    @Test  //user will not adjust volume while device is off
    public void turnOffCheckSetTemp(){
        SmartTV smartTV = new SmartTV();
        assertFalse("First Turn On Device",smartTV.powerStatus()); //off
        smartTV.setTemperature(45);
        assertNotSame("Volume is not being asserted",45,smartTV.getTemperature());
    }
}