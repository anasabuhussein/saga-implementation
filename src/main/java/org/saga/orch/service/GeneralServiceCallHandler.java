package org.saga.orch.service;

import org.saga.orch.model.GeneralServiceCallBuilder;

public abstract class GeneralServiceCallHandler {

    private final GeneralServiceCallBuilder generalServiceCallBuilder;

    public GeneralServiceCallHandler(GeneralServiceCallBuilder generalServiceCallBuilder) {
        this.generalServiceCallBuilder = generalServiceCallBuilder;
    }

    // Handle exceptions
    // Handle service calls
    // Handle breakers
    public abstract <T> T getServiceCallResult();

    // Handle exceptions
    // Handle service calls
    // Handle breakers
    public abstract <T> T getCompensationCallResult();

    public GeneralServiceCallBuilder getGeneralServiceCallBuilder() {
        return this.generalServiceCallBuilder;
    }

}
