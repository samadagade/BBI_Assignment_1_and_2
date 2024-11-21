package devices;
import interfaces.AudioControl;
import interfaces.NetworkConnected;
import interfaces.PowerControl;

public class SmartSpeaker implements NetworkConnected, AudioControl, PowerControl  {
    private boolean isOn;
    private boolean isConnected;
    private int volume = 50;
    private boolean isMuted;

    @Override
    public void connectWIFI(String address) {
      if (isOn) {
        isConnected = true;
        System.out.println("Connected to WiFi network: " + address);
      }
      else{
        System.out.println("Please Turn On Device First");
      }
    }

    @Override
    public boolean checkConnection() {
      try {
        if (!isOn) {
            throw new IllegalStateException("Turn on Device."); // Throw an exception if the device is off.
        }
  
        // If the device is on, check the connection status.
        return isConnected;
    } catch (IllegalStateException e) {
        System.out.println(e.getMessage()); // Print the exception message ("Turn on Device.")
        return false; // Return false, as connection cannot be checked when the device is off.
    }
    }

    @Override
    public void turnOnDevice() {
       isOn = true;
       System.out.print("Smart Speaker is Turned ON");
    }

    @Override
    public void turnOffDevice() {
        isOn = false;
        System.out.print("Smart Speaker is Turned OFF");
    }

    @Override
    public void adjustVolume(int volume) {
      if (!isOn) {
        System.out.println("Please Turn On Device");
      } else {
  
        if (volume >= 0 && volume <= 100) {
          if (isMuted) {
            isMuted = false;
            System.out.println("Device is Auto Unmuted");
          }
  
          this.volume = volume;
          System.out.println("Volume adjusted to " + volume + ".");
        } else {
          System.out.println("volume should between (0-100) ");
        }
  
      }
    }

    @Override
    public void muteVolume() {
      if(isOn){
        isMuted = true;
        System.out.println("SmartTV is Muted");
      }
      else{
        System.out.println("Please Turn On device.");
      }
    }

    @Override
    public void unMute() {
      if (isOn) {
        isMuted = false;
        System.out.println("SmartTV Unmuted. Now Volum level is" + volume);
      }
      else{
        System.out.println("Please Turn On Device.");
      }
     
      }

    @Override
    public int getVolume() {
       
    if(!isOn){
      System.out.println("Please Turn On Device");
      return -1;
    }

    if (isMuted) {
      System.out.println("Device is Muted! Current Volume is : " + this.volume);
      return this.volume;
    } else {
      System.out.print("Current Volume is :" + this.volume);
      return this.volume;
    }
    }

    @Override
    public boolean powerStatus(){
      return isOn;
    }

    @Override
    public boolean muteStatus(){
       return isMuted;
    }
}
