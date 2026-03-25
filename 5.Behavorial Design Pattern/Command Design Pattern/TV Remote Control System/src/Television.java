public class Television {
    private boolean isOn = false;
    private int volume = 0;
    private int channel = 1;

    public void turnOn() {
        isOn = true;
        System.out.println("TV is ON");
    }

    public void turnOff() {
        isOn = false;
        System.out.println("TV is OFF");
    }

    public void volumeUp() {
        volume++;
        System.out.println("Volume: " + volume);
    }

    public void volumeDown() {
        volume--;
        System.out.println("Volume: " + volume);
    }
}