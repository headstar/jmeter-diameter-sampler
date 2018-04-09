package com.headstartech.jmeter.diameter;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.jdiameter.api.*;

import java.util.concurrent.SynchronousQueue;

/**
 * @author Per Johansson
 */
public class DiamaterClientSampler extends AbstractJavaSamplerClient {

    public static final String DIAMETER_TEST_SCENARIO_KEY = "diameterTestScenario";

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        DiameterStack diameterStack = (DiameterStack) context.getJMeterProperties().get(DiameterStackSampler.DIAMETER_STACK_KEY);

        // TODO: check type and log error
        DiametertTestScenario diametertTestScenario = (DiametertTestScenario) context.getJMeterVariables().getObject(DIAMETER_TEST_SCENARIO_KEY);

        SampleResult sampleResult = new SampleResult();

        Session session = null;
        try {
            session = diameterStack.getSessionFactory().getNewSession();
            SynchronousQueue answerQueue = new SynchronousQueue();
            EventListener<Request, Answer> eventListener = new EventListener<Request, Answer>() {
                @Override
                public void receivedSuccessMessage(Request request, Answer answer) {
                    try {
                        answerQueue.put(new DiameterResponse(request, answer));
                    } catch (InterruptedException e) {
                        // TODO: handle this
                    }
                }

                @Override
                public void timeoutExpired(Request request) {

                }
            };

            Request nextRequest = diametertTestScenario.createInitialRequest(session, diameterStack.getDiameterStackConfiguration());
            while (nextRequest != null) {
                try {
                    session.send(nextRequest, eventListener);
                } catch (InternalException e) {
                    e.printStackTrace();
                } catch (IllegalDiameterStateException e) {
                    e.printStackTrace();
                } catch (RouteException e) {
                    e.printStackTrace();
                } catch (OverloadException e) {
                    e.printStackTrace();
                }
                DiameterResponse response = (DiameterResponse) answerQueue.poll();
                nextRequest = diametertTestScenario.createNextRequest(response, session, diameterStack.getDiameterStackConfiguration());
            };
        } catch (InternalException e) {
            // TODO: what to return?
            return sampleResult;
        } finally {
            if(session != null) {
                session.release();
            }
        }

        return sampleResult;
    }

}
