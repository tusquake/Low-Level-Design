public class RemoteControl {
    private Command[] commands;
    private Command lastCommand;

    public RemoteControl() {
        commands = new Command[7]; // 7 buttons
    }

    public void setCommand(int slot, Command command) {
        commands[slot] = command;
    }

    public void pressButton(int slot) {
        if (commands[slot] != null) {
            commands[slot].execute();
            lastCommand = commands[slot];
        }
    }

    public void pressUndo() {
        if (lastCommand != null) {
            lastCommand.undo();
        }
    }
}