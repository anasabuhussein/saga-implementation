package org.saga.orch.model;

@SuppressWarnings("unchecked")
public class DefaultServiceCallResultBuilder implements GeneralServiceCallResultBuilder {

    private final ServiceCallResult<?, ?> serviceCallResult;

    public DefaultServiceCallResultBuilder(ServiceCallResult<?, ?> serviceCallResult) {
        this.serviceCallResult = serviceCallResult;
    }

    @Override
    public ServiceCallResult<?, ?> getServiceCallResult() {
        return this.serviceCallResult;
    }
}
