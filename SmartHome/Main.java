import java.util.Scanner;
import devices.SmartSpeaker;
import devices.SmartTV;
import devices.SmartThermostat;
import interfaces.AudioControl;
import interfaces.NetworkConnected;
import interfaces.PowerControl;
import interfaces.TemperatureControl;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create instances of SmartHomeHub and devices
        SmartHomeHub smartHub = new SmartHomeHub();
        SmartTV smartTV = new SmartTV();
        SmartSpeaker smartSpeaker = new SmartSpeaker();
        SmartThermostat smartThermostat = new SmartThermostat();

        System.out.println("Welcome to the SmartHub Console!");
        System.out.print("================================");

        boolean exit = false;

        while (!exit) {
            System.out.println("\nChoose a device to control:");
            System.out.println("1. Smart TV");
            System.out.println("2. Smart Speaker");
            System.out.println("3. Smart Thermostat");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            

            switch (choice) {
                case 1:


                    controlDevice(scanner, smartHub, smartTV, "Smart TV");
                    break;
                case 2:
                    controlDevice(scanner, smartHub, smartSpeaker, "Smart Speaker");
                    break;
                case 3:
                    controlDevice(scanner, smartHub, smartThermostat, "Smart Thermostat");
                    break;
                case 4:
                    System.out.println("\nExiting SmartHub. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void controlDevice(Scanner scanner, SmartHomeHub smartHub, Object device, String deviceName) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n" + deviceName + " Control Menu:");
            System.out.println("1. Turn ON");
            System.out.println("2. Turn OFF");
            if (device instanceof NetworkConnected) {
                System.out.println("3. Connect to WiFi");
                System.out.println("4. Check Connection");
            }
            if (device instanceof AudioControl) {
                System.out.println("5. Adjust Volume");
                System.out.println("6. Mute Volume");
                System.out.println("7. Unmute Volume");
                System.out.println("8. Get Current Volume");
            }
            if (device instanceof TemperatureControl) {
                System.out.println("9. Set Temperature");
                System.out.println("10. Get Current Temperature");
            }
            System.out.println("11. Back");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    smartHub.controlPower((PowerControl) device, true);
                    break;
                case 2:
                    smartHub.controlPower((PowerControl) device, false);
                    break;
                case 3:
                    if (device instanceof NetworkConnected) {
                        System.out.print("Enter WiFi network name: ");
                        String wifi = scanner.nextLine();
                        smartHub.connectToWiFi((NetworkConnected) device, wifi);
                    } 
                    break;
                case 4:
                    if (device instanceof NetworkConnected) {
   
                        System.out.println("WiFi Connection Status: " +
                                (((NetworkConnected) device).checkConnection() ? "Connected" : "Not Connected"));
                    } else {
                        System.out.println("This device does not support WiFi.");
                    }
                    break;
                case 5:
                    if (device instanceof AudioControl) {
                        System.out.print("Enter volume level: ");
                        int volume = scanner.nextInt();
                        smartHub.setVolume((AudioControl) device, volume);
                    } else {
                        System.out.println("This device does not support volume control.");
                    }
                    break;
                case 6:
                    if (device instanceof AudioControl) {
                        smartHub.mute((AudioControl) device);
                    } else {
                        System.out.println("This device does not support mute functionality.");
                    }
                    break;
                case 7:
                    if (device instanceof AudioControl) {
                        smartHub.unMute((AudioControl) device);
                    } else {
                        System.out.println("This device does not support unmute functionality.");
                    }
                    break;
                case 8:
                    if (device instanceof AudioControl) {
                         ((AudioControl) device).getVolume();
                    } else {
                        System.out.println("This device does not support volume control.");
                    }
                    break;
                case 9:
                    if (device instanceof TemperatureControl) {
                        System.out.print("Enter temperature: ");
                        int temp = scanner.nextInt();
                        smartHub.setTemperature((TemperatureControl) device, temp);
                    } else {
                        System.out.println("This device does not support temperature control.");
                    }
                    break;
                case 10:
                    if (device instanceof TemperatureControl) {
                        if(((TemperatureControl) device).getTemperature() == -100){
                            System.out.println("Please Turn On Device.");
                        }
                        else if(((TemperatureControl) device).getTemperature() == -200){
                            System.out.println("Please Turn On Internet.");
                        }
                        else{
                             System.out.println("Current Temperature: " +
                               ((TemperatureControl) device).getTemperature() + "Degree");
                        }
                    } else {
                        System.out.println("This device does not support temperature readings.");
                    }
                    break;
                case 11:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
