package com.headstartech.jmeter.diameter;

import org.jdiameter.api.ApplicationId;

import java.io.File;

/**
 * @author Per Johansson
 */
public class DiameterStackConfiguration {

    private final File jdiameterConfigFile;
    private final File dictionaryFile;
    private final ApplicationId applicationId;

    public DiameterStackConfiguration(File jdiameterConfigFile, File dictionaryFile, ApplicationId applicationId) {
        this.jdiameterConfigFile = jdiameterConfigFile;
        this.dictionaryFile = dictionaryFile;
        this.applicationId = applicationId;
    }

    public File getJdiameterConfigFile() {
        return jdiameterConfigFile;
    }

    public File getDictionaryFile() {
        return dictionaryFile;
    }

    public ApplicationId getApplicationId() {
        return applicationId;
    }
}
