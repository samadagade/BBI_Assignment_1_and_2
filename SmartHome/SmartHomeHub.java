import interfaces.AudioControl;
import interfaces.NetworkConnected;
import interfaces.PowerControl;
import interfaces.TemperatureControl;

public class SmartHomeHub {

    public void controlPower(PowerControl device, boolean turnOn) {
        if (turnOn) {
            device.turnOnDevice();
        } else {
            device.turnOffDevice();
        }
    }

    public void connectToWiFi(NetworkConnected device, String networkName) {
        device.connectWIFI(networkName);
    }

    public void setTemperature(TemperatureControl device, int temperature) {
        device.setTemperature(temperature);
    }

    public void getTemperature(TemperatureControl device) {
        device.getTemperature();
    }

    public void setVolume(AudioControl device, int volume) {
        device.adjustVolume(volume);
    }

    public void mute(AudioControl device) {
        device.muteVolume();
    }

    public void unMute(AudioControl device) {
        device.unMute();
    }

    public void getVolume(AudioControl device) {
        device.getVolume();
    }
}