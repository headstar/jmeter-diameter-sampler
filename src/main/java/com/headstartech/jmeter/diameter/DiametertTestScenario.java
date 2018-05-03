package com.headstartech.jmeter.diameter;

import org.jdiameter.api.Request;
import org.jdiameter.api.Session;

/**
 * @author Per Johansson
 */
public interface DiametertTestScenario {

    Request createInitialRequest(DiameterHelper diameterHelper, Session session);

    Request createNextRequest(DiameterHelper diameterHelper, DiameterResponse response, Session session);
}
