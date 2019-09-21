package maryosc;

import java.io.IOException;
import java.util.Scanner;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.messageselector.OSCPatternAddressMessageSelector;
import com.illposed.osc.transport.udp.OSCPortIn;

public class MaryOSC {

    protected static OSCPortIn receiver;
    private static MaryUtil mary = new MaryUtil("dfki-poppy-hsmm", null, false);
    private static MaryOSCMessageListener listener = new MaryOSCMessageListener(mary);

    public static void main(String args[]) {
        try {

            int port = 9000;
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            try {
                receiver = new OSCPortIn(port);
            } catch (IOException e) {
                e.printStackTrace();
            }

            OSCMessage message = new OSCMessage("/mary/");

            receiver.getDispatcher().addListener(new OSCPatternAddressMessageSelector("/mary/voice"), listener);
            receiver.getDispatcher().addListener(new OSCPatternAddressMessageSelector("/mary/locale"), listener);
            receiver.getDispatcher().addListener(new OSCPatternAddressMessageSelector("/mary/effects"), listener);
            receiver.getDispatcher().addListener(new OSCPatternAddressMessageSelector("/mary/say"), listener);
            receiver.getDispatcher().addListener(new OSCPatternAddressMessageSelector("/mary/save"), listener);
            receiver.startListening();

            logo();
            System.out.println("\nRunning MaryTTS in OSC port: " + port);
            System.out.println("\nEnter '-h' for help ...");

            Scanner sc = new Scanner(System.in);
            String input = "";

            while (!input.equals("-q")) {
                input = sc.nextLine().trim();
                handleInput(input);
            }

            receiver.stopListening();
            receiver.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void logo() {
        System.out.println("   __  ___                  ____   ____ _____\n" +
            "  /  |/  /___ _ ____ __ __ / __ \\ / __// ___/\n" +
            " / /|_/ // _ `// __// // // /_/ /_\\ \\ / /__  \n" +
            "/_/  /_/ \\_,_//_/   \\_, / \\____//___/ \\___/  \n" +
            "                   /___/                     ");
    }

    private static void handleInput(String input) throws InterruptedException {
        if (input.startsWith("-")) {
            String[] params = input.split(" ");
            if (input.equals("-q")) {
                mary.say("Bye bye!");
                Thread.sleep(500);
                System.out.println("Bye bye!");
            } else if (input.equals("-h")) {
                System.out.println("-------HELP------");
                System.out.println("'-h' Help");
                System.out.println("'-d' Debug events");
                System.out.println("'-e' Print effects");
                System.out.println("'-v' Print voices");
                System.out.println("'-l' Print locales");
                System.out.println("'-q' Quit");
                System.out.println("-----------------");
            } else if (input.equals("-d")) {
                listener.setDebug();
                mary.setDebug();
            } else if (input.equals("-e")) {
                System.out.println("------------------EFFECTS--------------------------------------");
                mary.printEffects();
                System.out.println("Example usage in Sonic Pi:");
                System.out.println("osc \"/mary/effects\", \"F0Add(f0Add:30.0)+Robot(amount:100)\"");
                System.out.println("---------------------------------------------------------------");
            } else if (input.startsWith("-v")) {
                if (params.length>1) {
                    if(params[1].matches("^\\d+$")) {
                        mary.setVoice(Integer.parseInt(params[1]));
                    } else {
                        mary.setVoice(params[1]);
                    }
                } else {
                    System.out.println("--------------VOICES------------------");
                    mary.printVoices();
                    System.out.println("\n\nExample usage in Sonic Pi:");
                    System.out.println("osc \"/mary/voice\", 1");
                    System.out.println("osc \"/mary/voice\", \"dfki-ot-hsmm\"");
                    System.out.println("---------------------------------------");
                }
            } else if (input.equals("-l")) {
                System.out.println("-------------LOCALES------------");
                mary.printLocales();
                System.out.println("\n\nExample usage in Sonic Pi:");
                System.out.println("osc \"/mary/locale\", \"ru\"");
                System.out.println("--------------------------------");
            }
        } else {
            if (!input.isEmpty()) {
                mary.say(input);
                Thread.sleep(500);
            }
        }
    }

}
