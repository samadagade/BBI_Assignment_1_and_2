package src.test.java;

import devices.SmartSpeaker;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;

public class SmartSpeakerTest {
    // Power Test Cases
    @Test
    public void powerOffTest() {
        SmartSpeaker speaker = new SmartSpeaker();
        assertFalse("Should be off initially", speaker.powerStatus());
    }

    @Test
    public void powerOnTest() {
        SmartSpeaker speaker = new SmartSpeaker();
        speaker.turnOnDevice();
        assertTrue("First Turn on  Device.", speaker.powerStatus());
    }

    // wifi Test Cases
    @Test // check if is it possible to check connection when device is off
    public void checkConnectionIfDeviceOffTest() {
        SmartSpeaker speaker = new SmartSpeaker();
        speaker.turnOffDevice();
        assertFalse("first turn of Device for check connection", speaker.checkConnection());
    }

    @Test // check if is it possible to check connection when device is on
    public void checkConnectionIfDeviceOnTest() {
        SmartSpeaker speaker = new SmartSpeaker();
        speaker.turnOnDevice();
        assertFalse("Initially Wifi off", speaker.checkConnection());
    }

    @Test // is connect to wifi while device is off
    public void connectWifiDeviceOffTest() {
        SmartSpeaker speaker = new SmartSpeaker();
        speaker.turnOffDevice();
        speaker.connectWIFI("myHome");
        assertFalse("First Turn on Device", speaker.checkConnection());
    }

    @Test // is connect to wifi while device is on
    public void connectWifiTest() {
        SmartSpeaker speaker = new SmartSpeaker();
        speaker.turnOnDevice();
        speaker.connectWIFI("myhome");
        assertTrue("turn on device and check connection", speaker.checkConnection());
    }

    // Volume Test Cases
    @Test // if device is turned on devices should be audible
    public void turnOnAudible() {
        SmartSpeaker speaker = new SmartSpeaker();
        speaker.turnOnDevice();
        assertEquals("when device is turned on device should be audible", 50, speaker.getVolume());
    }

    @Test // user will not get volume if device is off
    public void turnOffCheckGetVolume() {
        SmartSpeaker speaker = new SmartSpeaker();
        // speaker.turnOnDevice();
        assertEquals("Please Turn On Device", -1, speaker.getVolume());
    }

    @Test // user will adjust volume while device is on
    public void turnOnCheckAdjustVolume() {
        SmartSpeaker speaker = new SmartSpeaker();
        speaker.turnOnDevice();
        assertTrue("First Turn On Device", speaker.powerStatus()); // on
        speaker.adjustVolume(45);
        assertEquals("Volume is not being asserted", 45, speaker.getVolume());
    }

    @Test // user will not adjust volume while device is off
    public void turnOffCheckAdjustVolume() {
        SmartSpeaker speaker = new SmartSpeaker();
        assertFalse("First Turn On Device", speaker.powerStatus()); // off
        speaker.adjustVolume(45);
        assertNotSame("Volume is not being asserted", 45, speaker.getVolume());
    }

    @Test
    public void turnOffCheckMute() {
        SmartSpeaker speaker = new SmartSpeaker();
        assertFalse("First turn on device", speaker.muteStatus());
    }

    @Test
    public void turnOnMuteCheckMute() {
        SmartSpeaker speaker = new SmartSpeaker();
        speaker.turnOnDevice();
        speaker.muteVolume();
        assertTrue("First turn on device", speaker.muteStatus());
    }
}
