package devices;

import interfaces.AudioControl;
import interfaces.NetworkConnected;
import interfaces.PowerControl;
import interfaces.TemperatureControl;

public class SmartTV implements NetworkConnected, PowerControl, TemperatureControl, AudioControl {
  private boolean isOn;
  private boolean isConnected;
  private int temperature = 30;
  private int volume = 50;
  private boolean isMuted;

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
    if (isOn) {
      isMuted = true;
      System.out.println("SmartTV is Muted");
    } else {
      System.out.println("Please Turn On device.");
    }

  }

  @Override
  public void setTemperature(int temperature) {
    // temperature of smartTV should be between 25 to 50
    if (temperature < 25 || temperature > 50) {
      System.out.println("Temperature Should Between 25-50");
      return;
    }
    if (!isOn) {
      System.out.println("Please Turn On Device.");
    } else if (!isConnected) {
      System.out.print("Please Turn On Internet");
    } else {
      this.temperature = temperature;
      System.out.println("Temperature set to " + temperature + " degree.");
    }
  }

  @Override
  public int getTemperature() {
    if (!isOn) {
      return -100;
    } else if (!isConnected) {
      return -200;
    } else {
      return temperature;
    }
  }

  @Override
  public void turnOnDevice() {
    isOn = true;
    System.out.println("Smart TV is ON.");
  }

  @Override
  public void turnOffDevice() {
    isOn = false;
    System.out.println("Smart TV is OFF.");
  }

  @Override
  public void connectWIFI(String address) {
    if (isOn) {
      isConnected = true;
      System.out.println("Connected to WiFi network: " + address);
    } else {
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
    // if(!isOn){
    // System.out.println("Turn on Device.");
    // return false;
    // }
    // return isConnected && isOn;
  }

  @Override
  public void unMute() {
    if (isOn) {
      isMuted = false;
      System.out.println("SmartTV Unmuted. Now Volum level is" + volume);
    } else {
      System.out.println("Please Turn On Device.");
    }

  }

  @Override
  public int getVolume() {

    if (!isOn) {
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
  public boolean powerStatus() {
    return isOn;
  }

  @Override
  public boolean muteStatus() {
    return isMuted;
  }

}
