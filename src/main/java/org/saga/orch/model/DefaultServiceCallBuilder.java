package org.saga.orch.model;

@SuppressWarnings("unchecked")
public class DefaultServiceCallBuilder implements GeneralServiceCallBuilder {

    private final ServiceCallBuilder<?, ?> serviceCallBuilder;

    public DefaultServiceCallBuilder(ServiceCallBuilder<?, ?> serviceCallBuilder) {
        this.serviceCallBuilder = serviceCallBuilder;
    }

    @Override
    public ServiceCallBuilder<?, ?> getServiceCallBuilder() {
        return serviceCallBuilder;
    }

}
