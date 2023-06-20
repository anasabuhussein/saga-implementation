package org.saga.orch.service;

import org.saga.orch.model.GeneralServiceCallBuilder;

public class ServiceCallHandler {

    private final GeneralServiceCallBuilder generalServiceCallBuilder;

    public ServiceCallHandler(GeneralServiceCallBuilder generalServiceCallBuilder) {
        this.generalServiceCallBuilder = generalServiceCallBuilder;
    }

    // Handle exceptions
    // Handle service calls
    // Handle breakers
    public <T> T getServiceCallResult() {
        return (T) generalServiceCallBuilder.getServiceCallBuilder().getServiceCall().get();
    }

    // Handle exceptions
    // Handle service calls
    // Handle breakers
    public <T> T getCompensationCallResult() {
        return (T) generalServiceCallBuilder.getServiceCallBuilder().getCompensationCall().get();
    }

    public GeneralServiceCallBuilder getGeneralServiceCallBuilder() {
        return generalServiceCallBuilder;
    }
}
