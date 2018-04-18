package com.headstartech.jmeter.diameter;

import org.jdiameter.api.ApplicationId;

import java.io.File;

/**
 * @author Per Johansson
 */
public class DiameterStackConfiguration {

    private final int commandCode;
    private final ApplicationId appId;
    private final long vendorId;
    private final String realm;
    private final String serverURI;
    private final File jdiameterConfigFile;
    private final File dictionaryFile;

    public DiameterStackConfiguration(int commandCode, ApplicationId appId, long vendorId, String realm, String serverURI, File jdiameterConfigFile, File dictionaryFile) {
        this.commandCode = commandCode;
        this.appId = appId;
        this.vendorId = vendorId;
        this.realm = realm;
        this.serverURI = serverURI;
        this.jdiameterConfigFile = jdiameterConfigFile;
        this.dictionaryFile = dictionaryFile;
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

    public File getJdiameterConfigFile() {
        return jdiameterConfigFile;
    }

    public String getRealm() {
        return realm;
    }

    public File getDictionaryFile() {
        return dictionaryFile;
    }
}
