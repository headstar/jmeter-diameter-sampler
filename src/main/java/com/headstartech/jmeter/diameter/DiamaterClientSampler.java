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
        DiameterStack diameterStack = (DiameterStack) context.getJMeterProperties().get(DiameterStackSetupSampler.DIAMETER_STACK_KEY);
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
                        getNewLogger().warn("thread interrupted", e);
                        sampleResult.setSuccessful(false);
                        return;
                    }
                }

                @Override
                public void timeoutExpired(Request request) {

                }
            };

            Request nextRequest = diametertTestScenario.createInitialRequest(diameterStack.getDiameterHelper(), session);
            while (nextRequest != null) {
                session.send(nextRequest, eventListener);
                DiameterResponse response = (DiameterResponse) answerQueue.poll();
                nextRequest = diametertTestScenario.createNextRequest(diameterStack.getDiameterHelper(), response, session);
            };
        } catch (InternalException | IllegalDiameterStateException | RouteException | OverloadException e) {
            sampleResult.setSuccessful(false);
            getNewLogger().error("Exception caught", e);
        } finally {
            if(session != null) {
                session.release();
            }
        }
        return sampleResult;
    }

}
