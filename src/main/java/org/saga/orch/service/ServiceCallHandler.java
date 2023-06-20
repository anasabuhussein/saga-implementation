package org.saga.orch.service;

import org.saga.orch.model.GeneralServiceCallBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceCallHandler {

    private static final Logger LOGGER = Logger.getLogger(ServiceCallHandler.class.getName());
    private final GeneralServiceCallBuilder generalServiceCallBuilder;

    public ServiceCallHandler(GeneralServiceCallBuilder generalServiceCallBuilder) {
        this.generalServiceCallBuilder = generalServiceCallBuilder;
    }

    // Handle exceptions
    // Handle service calls
    // Handle breakers
    public <T> T getServiceCallResult() {
        LOGGER.log(Level.INFO, "Actual service call!");
        return (T) generalServiceCallBuilder.getServiceCallBuilder().getServiceCall().get();
    }

    // Handle exceptions
    // Handle service calls
    // Handle breakers
    public <T> T getCompensationCallResult() {
        LOGGER.log(Level.INFO, "Actual service call for unexpected behaves!");
        return (T) generalServiceCallBuilder.getServiceCallBuilder().getCompensationCall().get();
    }

    public GeneralServiceCallBuilder getGeneralServiceCallBuilder() {
        return generalServiceCallBuilder;
    }
}
