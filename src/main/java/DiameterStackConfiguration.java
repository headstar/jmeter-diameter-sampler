import org.jdiameter.api.ApplicationId;

/**
 * @author Per Johansson
 */
public class DiameterStackConfiguration {

    private final int commandCode;
    private final ApplicationId appId;
    private final long vendorId;
    private final String serverURI;
    private final String jdiameterConfigurationFile;

    public DiameterStackConfiguration(int commandCode, ApplicationId appId, long vendorId, String serverURI, String jdiameterConfigurationFile) {
        this.commandCode = commandCode;
        this.appId = appId;
        this.vendorId = vendorId;
        this.serverURI = serverURI;
        this.jdiameterConfigurationFile = jdiameterConfigurationFile;
    }

    public int getCommandCode() {
        return commandCode;
    }

    public ApplicationId getAppId() {
        return appId;
    }

    public long getVendorId() {
        return vendorId;
    }

    public String getServerURI() {
        return serverURI;
    }

    public String getJdiameterConfigurationFile() {
        return jdiameterConfigurationFile;
    }
}
