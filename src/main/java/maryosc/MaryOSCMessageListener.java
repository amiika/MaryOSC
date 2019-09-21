package maryosc;

import java.util.List;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.OSCMessageInfo;
import com.illposed.osc.OSCMessageListener;

public class MaryOSCMessageListener implements OSCMessageListener {

    private int messageReceivedCount;
    private static MaryUtil mary;
    private boolean debug;

    public MaryOSCMessageListener(MaryUtil mary) {
        this.messageReceivedCount = 0;
        this.mary = mary;
        this.debug = false;
    }

    public int getMessageCount() {
        return messageReceivedCount;
    }

    public void setDebug() {
        this.debug = !this.debug;
    }

    @Override
    public void acceptMessage(final OSCMessageEvent event) {
        messageReceivedCount++;
        OSCMessage message = event.getMessage();
        String address = message.getAddress();
        OSCMessageInfo info = message.getInfo();
        List<Object> arguments = message.getArguments();
        if (this.debug) System.out.println("Message "+messageReceivedCount+" to " + address+" with "+arguments.size()+" argument(s)");
        if (arguments.size() > 0) {
            Object firstParameter = arguments.get(0);
            if (address.contains("save")) {
                if (arguments.size()==2) {
                    String path = (String) firstParameter;
                    String input = (String) arguments.get(1);
                    mary.save(path,input);
                } else {
                    throw new IllegalArgumentException("Expected two string arguments!");
                }
            } else if (address.contains("voice")) {
                if (firstParameter instanceof String) {
                    String text = (String) firstParameter;
                    mary.setVoice(text);
                } else if (firstParameter instanceof Integer) {
                    Integer voiceNum = (Integer) firstParameter;
                    mary.setVoice(voiceNum);
                } else {
                    throw new IllegalArgumentException("Voice argument should be int or string");
                }
            } else if (address.contains("effects")) {
                if (firstParameter instanceof String) {
                    String word = (String) firstParameter;
                    mary.setAudioEffects(word);
                } else {
                    throw new IllegalArgumentException("Effect argument should be string");
                }
            } else if (address.contains("say")) {
                if (firstParameter instanceof String) {
                    String word = (String) firstParameter;
                    mary.say(word);
                } else {
                    throw new IllegalArgumentException("Argument should be string!");
                }
            } else if (address.contains("locale")) {
                if (firstParameter instanceof String) {
                    String locale = (String) firstParameter;
                    mary.setLocale(locale);
                } else {
                    throw new IllegalArgumentException("Locale should be string!");
                }
            }
        }
    }

}
