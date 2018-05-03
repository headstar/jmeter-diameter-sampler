package com.headstartech.jmeter.diameter;

import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.Message;
import org.mobicents.diameter.dictionary.AvpDictionary;
import org.mobicents.diameter.dictionary.AvpRepresentation;

import java.nio.charset.StandardCharsets;

/**
 * @author Per Johansson
 */
public class DiameterHelper {

    private final AvpDictionary dictionary;

    public DiameterHelper(AvpDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public AvpDictionary getDictionary() {
        return dictionary;
    }

    public String buildMessageDescription(Message message, boolean sending) throws AvpDataException {
        StringBuilder sb = new StringBuilder();

        sb.append((sending ? "Sending " : "Received ")
                + (message.isRequest() ? "Request: " : "Answer: ")
                + message.getCommandCode()
                + "\nE2E:"
                + message.getEndToEndIdentifier()
                + "\nHBH:" + message.getHopByHopIdentifier()
                + "\nAppID:" + message.getApplicationId());
        sb.append("\n");
        sb.append("AVPS[\"+message.getAvps().size()+\"]: \n");
        sb.append(buildAvpsString(message.getAvps()));
        return sb.toString();
    }

    private String buildAvpsString(AvpSet avpSet) throws AvpDataException {
        return buildAvpsString(avpSet, 0);
    }

    private String buildAvpsString(AvpSet avpSet, int level) throws AvpDataException {
        StringBuilder sb = new StringBuilder();

        String prefix = "                      ".substring(0, level * 2);

        for (Avp avp : avpSet) {
            AvpRepresentation avpRep = dictionary.getAvp(avp.getCode(), avp.getVendorId());

            if (avpRep != null && avpRep.getType().equals("Grouped")) {
                sb.append(prefix
                        + "<avp name=\""
                        + avpRep.getName() + "\" code=\""
                        + avp.getCode() + "\" vendor=\""
                        + avp.getVendorId()
                        + "\">");
                buildAvpsString(avp.getGrouped(), level + 1);
                sb.append(prefix + "</avp>");
            } else if (avpRep != null) {
                String value = "";

                if (avpRep.getType().equals("Integer32"))
                    value = String.valueOf(avp.getInteger32());
                else if (avpRep.getType().equals("Integer64") || avpRep.getType().equals("Unsigned64"))
                    value = String.valueOf(avp.getInteger64());
                else if (avpRep.getType().equals("Unsigned32"))
                    value = String.valueOf(avp.getUnsigned32());
                else if (avpRep.getType().equals("Float32"))
                    value = String.valueOf(avp.getFloat32());
                else
                    value = new String(avp.getOctetString(), StandardCharsets.UTF_8);

                sb.append(prefix
                        + "<avp name=\""
                        + avpRep.getName()
                        + "\" code=\""
                        + avp.getCode()
                        + "\" vendor=\""
                        + avp.getVendorId()
                        + "\" value=\""
                        + value + "\" />");
            }
        }
        return sb.toString();
    }
}
