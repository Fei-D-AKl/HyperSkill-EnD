package JavaLearning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Cipherer().process(args);
    }
}

class Cipherer {
    private String mode = "enc";
    private String data = "";
    private String fileName = "";
    private String alg = "shift";
    private int key = 0;
    private boolean writeToFile = false;

    void process(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-mode")) {
                mode = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-key")) {
                key = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-data")) {
                data = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-in") && data.isEmpty()) {
                File input = new File(args[i + 1]);
                try (Scanner sc = new Scanner(input)) {
                    data = sc.nextLine();
                } catch (FileNotFoundException e) {
                    System.out.println("Error " + e.getMessage());
                }
            } else if (args[i].equalsIgnoreCase("-out")) {
                fileName = args[i + 1];
                writeToFile = true;
            } else if (args[i].equalsIgnoreCase("-alg")) {
                alg = args[i + 1];
            }
        }

        if (alg.equalsIgnoreCase("unicode")) {
            if (mode.equalsIgnoreCase("enc") && !writeToFile) {
                new unicodeEncrypt().enOrDe(data.toCharArray(), key);
            } else if (mode.equalsIgnoreCase("dec") && !writeToFile) {
                new unicodeDecrypt().enOrDe(data.toCharArray(), key);
            } else if (mode.equalsIgnoreCase("enc") && writeToFile) {
                new unicodeEncrypt().enOrDeFile(data.toCharArray(), fileName, key);
            } else {
                new unicodeDecrypt().enOrDeFile(data.toCharArray(), fileName, key);
            }
        } else if (alg.equalsIgnoreCase("shift")) {
            if (mode.equalsIgnoreCase("enc") && !writeToFile) {
                new shiftEncrypt().enOrDe(data.toCharArray(), key);
            } else if (mode.equalsIgnoreCase("dec") && !writeToFile) {
                new shiftDecrypt().enOrDe(data.toCharArray(), key);
            } else if (mode.equalsIgnoreCase("enc") && writeToFile) {
                new shiftEncrypt().enOrDeFile(data.toCharArray(), fileName, key);
            } else {
                new shiftDecrypt().enOrDeFile(data.toCharArray(), fileName, key);
            }
        }
    }
}

interface encryptdecrypt {

    void enOrDe(char[] data, int key);

    void enOrDeFile(char[] data, String filename, int key);
}

class unicodeEncrypt implements encryptdecrypt {

    @Override
    public void enOrDe(char[] data, int key) {
        for (char c : data) {
            System.out.print((char) (c + key));
        }
    }

    @Override
    public void enOrDeFile(char[] data, String fileName, int key) {
        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file)) {
            for (char c : data) {
                writer.write((char) (c + key));
            }
        } catch (IOException e) {
            System.out.printf("Error  %s", e.getMessage());
        }
    }
}


class unicodeDecrypt implements encryptdecrypt {

    @Override
    public void enOrDe(char[] data, int key) {
        for (char c : data) {
            System.out.print((char) (c - key));
        }
    }

    @Override
    public void enOrDeFile(char[] data, String fileName, int key) {
        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file)) {
            for (char c : data) {
                writer.write((char) (c - key));
            }
        } catch (IOException e) {
            System.out.printf("Error  %s", e.getMessage());
        }
    }
}

class shiftEncrypt implements encryptdecrypt {
    private final String lower = "abcdefghijklmnopqrstuvwxyz";
    private final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public void enOrDe(char[] data, int key) {
        for (char c : data) {
            if (lower.indexOf(c) >= 0) {
                System.out.print(lower.charAt((lower.indexOf(c) + key) % 26));
            } else if (upper.indexOf(c) >= 0) {
                System.out.print(upper.charAt((upper.indexOf(c) + key) % 26));
            } else {
                System.out.print(c);
            }
        }
    }

    @Override
    public void enOrDeFile(char[] data, String fileName, int key) {
        File file = new File(fileName);

        try (FileWriter writer = new FileWriter(file)) {
            for (char c : data) {
                if (lower.indexOf(c) >= 0) {
                    writer.write(lower.charAt((lower.indexOf(c) + key) % 26));
                } else if (upper.indexOf(c) >= 0) {
                    writer.write(upper.charAt((upper.indexOf(c) + key) % 26));
                } else {
                    writer.write(c);
                }
            }
        } catch (IOException e) {
            System.out.printf("Error  %s", e.getMessage());
        }
    }
}

class shiftDecrypt implements encryptdecrypt {
    private final String lower = "abcdefghijklmnopqrstuvwxyz";
    private final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public void enOrDe(char[] data, int key) {
        for (char c : data) {
            if (lower.indexOf(c) >= 0) {
                System.out.print(lower.charAt((lower.indexOf(c) - key + 26) % 26));
            } else if (upper.indexOf(c) >= 0) {
                System.out.print(upper.charAt((upper.indexOf(c) - key + 26) % 26));
            } else {
                System.out.print(c);
            }
        }
    }

    @Override
    public void enOrDeFile(char[] data, String filename, int key) {
        File file = new File(filename);

        try (FileWriter writer = new FileWriter(file)) {
            for (char c : data) {
                if (lower.indexOf(c) >= 0) {
                    writer.write(lower.charAt((lower.indexOf(c) - key + 26) % 26));
                } else if (upper.indexOf(c) >= 0) {
                    writer.write(lower.charAt((upper.indexOf(c) - key + 26) % 26));
                } else {
                    writer.write(c);
                }
            }
        } catch (IOException e) {
            System.out.printf("Error  %s", e.getMessage());
        }
    }
}