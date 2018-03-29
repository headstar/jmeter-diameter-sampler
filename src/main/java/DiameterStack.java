import org.jdiameter.api.*;
import org.jdiameter.client.impl.StackImpl;
import org.jdiameter.client.impl.helpers.XMLConfiguration;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class DiameterStack {

    private final Stack clientStack;
    private final SessionFactory sessionFactory;
    private final DiameterStackConfiguration diameterStackConfiguration;

    public DiameterStack(DiameterStackConfiguration diameterStackConfiguration) throws Exception {
        this.diameterStackConfiguration = diameterStackConfiguration;

        clientStack = new StackImpl();
        sessionFactory = clientStack.init(createConfiguration(diameterStackConfiguration.getJdiameterConfigurationFile()));

        // Register network request listener, even though we wont receive requests
        // this has to be done to inform stack that we support application
        Network network = clientStack.unwrap(Network.class);
        network.addNetworkReqListener(request -> null, diameterStackConfiguration.getAppId());
    }

    void start() throws InternalException, IllegalDiameterStateException {
        clientStack.start();
    }

    void stop() throws InternalException, IllegalDiameterStateException {
        clientStack.stop(10, TimeUnit.SECONDS, DisconnectCause.REBOOTING);
    }

    void destroy() {
        clientStack.destroy();
    }

    private Configuration createConfiguration(String configFile) throws Exception {
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream(configFile);
            return new XMLConfiguration(is);
        } finally {
            if(is != null) {
                is.close();
            }
        }
    }
}