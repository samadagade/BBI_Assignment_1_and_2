package interfaces;

public interface AudioControl {
    void adjustVolume(int volume);

    void muteVolume();

    void unMute();

    int getVolume();

    boolean muteStatus();
}
