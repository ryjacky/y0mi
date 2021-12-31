package utils;

import java.util.ArrayList;

public class CommandContext {
    public enum Commands {JOIN, PURIFY, HELP, SET_VOICE, SET_PREFIX, NOT_FOUND}

    private Commands command;
    private ArrayList<String> parameters;

    public CommandContext(Commands command, ArrayList<String> parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    public Commands getCommand() {
        return command;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }
}
