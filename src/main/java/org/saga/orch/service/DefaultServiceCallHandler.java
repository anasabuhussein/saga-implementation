package org.saga.orch.service;

import org.saga.orch.model.GeneralServiceCallBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultServiceCallHandler extends GeneralServiceCallHandler{

    private static final Logger LOGGER = Logger.getLogger(DefaultServiceCallHandler.class.getName());
    private final GeneralServiceCallBuilder generalServiceCallBuilder;

    public DefaultServiceCallHandler(GeneralServiceCallBuilder generalServiceCallBuilder) {
        super(generalServiceCallBuilder);
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

}
