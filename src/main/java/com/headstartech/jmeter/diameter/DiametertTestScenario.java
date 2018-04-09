package com.headstartech.jmeter.diameter;

import org.jdiameter.api.Request;
import org.jdiameter.api.Session;

/**
 * @author Per Johansson
 */
public interface DiametertTestScenario {

    Request createInitialRequest(Session session, DiameterStackConfiguration stackConfiguration);

    Request createNextRequest(DiameterResponse response, Session session, DiameterStackConfiguration stackConfiguration);
}