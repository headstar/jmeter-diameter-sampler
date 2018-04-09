package com.headstartech.jmeter.diameter;

import org.jdiameter.api.Answer;
import org.jdiameter.api.Request;

/**
 * @author Per Johansson
 */
public class DiameterResponse {
    public final Request request;
    public final Answer answer;

    public DiameterResponse(Request request, Answer answer) {
        this.request = request;
        this.answer = answer;
    }
}
