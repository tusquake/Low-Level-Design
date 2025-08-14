public class CommandPatternDemo {
    public static void main(String[] args) {
        // Receiver
        Television tv = new Television();

        // Commands
        Command turnOn = new TurnOnCommand(tv);
        Command volumeUp = new VolumeUpCommand(tv);

        // Invoker
        RemoteControl remote = new RemoteControl();
        remote.setCommand(0, turnOn);
        remote.setCommand(1, volumeUp);

        // Execute
        remote.pressButton(0); // TV ON
        remote.pressButton(1); // Volume UP
        remote.pressUndo();    // Undo last command
    }
}
