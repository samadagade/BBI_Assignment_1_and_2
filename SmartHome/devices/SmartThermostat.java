package devices;

import interfaces.NetworkConnected;
import interfaces.PowerControl;
import interfaces.TemperatureControl;

public class SmartThermostat implements PowerControl, NetworkConnected, TemperatureControl {
  private boolean isOn;
  private boolean isConnected;
  private int temperature;

  @Override
  public void setTemperature(int temperature) {
    // for thermostat temprature should between 30 to 45
    if (temperature < 30 || temperature > 45) {
      System.out.println("Temperature Should Between 30-45");
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
  }

  @Override
  public void turnOnDevice() {
    isOn = true;
    System.out.print("SmartThermostat is ON");
  }

  @Override
  public void turnOffDevice() {
    isOn = false;
    System.out.print("SmartThermostat is OFF");
  }

  @Override
  public boolean powerStatus() {
    return isOn;
  }

}
