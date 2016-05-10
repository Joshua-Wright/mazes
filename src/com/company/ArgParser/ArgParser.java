package com.company.ArgParser;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.HashMap;

public class ArgParser {
    private enum ArgumentType {
        KEY_VALUE,
        IF_PRESENT,
    }
    private HashMap<String, ArgumentType> argTypes;
    private HashMap<String, String> allArgs;
    private HashMap<String, String> flagToKey;

    public ArgParser() {
        this.argTypes = new HashMap<>();
        this.allArgs = new HashMap<>();
        this.flagToKey = new HashMap<>();
    }

    public ArgParser putKeyValue(String flag, String key) {
        argTypes.put(flag, ArgumentType.KEY_VALUE);
        flagToKey.put(flag, key);
        return this;
    }

    public ArgParser putDefault(String key, String defaultValue) {
        allArgs.put(key, defaultValue);
        return this;
    }

    public ArgParser putIfPresent(String flag, String key) {
        argTypes.put(flag, ArgumentType.IF_PRESENT);
        flagToKey.put(flag, key);
        return this;
    }

    public ArgParser parse(String[] args) {
        for (int i=0; i<args.length; i++) {
            if (!args[i].startsWith("-")) {
                /*TODO: better solution*/
                return this;
            }
            switch (argTypes.get(args[i])) {
                case KEY_VALUE:
                    allArgs.put(flagToKey.get(args[i]), args[i+1]);
                    i++; /*advance i because the next one is a non-argument*/
                    break;
                case IF_PRESENT:
                    /*empty non-null value*/
                    allArgs.put(flagToKey.get(args[i]), "");
                    break;
            }
        }
        return this;
    }

    public String getValue(String key) {
        return allArgs.get(key);
    }
    public boolean isPresent(String key) {
        return allArgs.get(key) != null;
    }
}
