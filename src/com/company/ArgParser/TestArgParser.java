package com.company.ArgParser;

public class TestArgParser {

    public static void test(boolean expr, String message) {
        if (!expr) {
            System.out.println("Test failed: " + message);
        }
    }

    public static void main(String[] args) {
        String[] testCase = "-a 4 -v".split(" ");
        ArgParser parser = new ArgParser()
                .putIfPresent("-v", "v")
                .putKeyValue("-a", "a")
                .putKeyValue("-b", "b")
                .putDefault("b", "5")
                .parse(testCase);
        test(parser.getValue("a").equals("4"), "key value");
        test(parser.isPresent("v"), "is present");
        test(!parser.isPresent("d"), "is present");
        test(parser.getValue("b").equals("5"), "key value");
    }
}
