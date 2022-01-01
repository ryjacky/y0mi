package utils;

public record CommandContext(String command, String... options) {

    public String getCommand() {
        return command;
    }

    public String[] getOptions() {
        return options;
    }
}
