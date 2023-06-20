package org.saga.orch.model;

public class ServiceCallBuilder<S, C> {

    private String serviceName;
    private ServiceCallType serviceCallType;
    private CustomSupplier<S> serviceCall;
    private CustomSupplier<C> compensationCall;

    public ServiceCallBuilder() {

    }

    public ServiceCallBuilder(String serviceName, ServiceCallType serviceCallType, CustomSupplier<S> serviceCall,
                              CustomSupplier<C> compensationCall) {
        this.serviceName = serviceName;
        this.serviceCallType = serviceCallType;
        this.serviceCall = serviceCall;
        this.compensationCall = compensationCall;
    }

    public String getServiceName() {

        return serviceName;
    }

    public ServiceCallBuilder<S, C> setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public ServiceCallType getServiceCallType() {

        return serviceCallType;
    }

    public ServiceCallBuilder<S, C> setServiceCallType(ServiceCallType serviceCallType) {
        this.serviceCallType = serviceCallType;
        return this;
    }

    public CustomSupplier<S> getServiceCall() {

        return serviceCall;
    }

    public ServiceCallBuilder<S, C> setServiceCall(CustomSupplier<S> serviceCall) {
        this.serviceCall = serviceCall;
        return this;
    }

    public CustomSupplier<C> getCompensationCall() {

        return compensationCall;
    }

    public ServiceCallBuilder<S, C> setCompensationCall(CustomSupplier<C> compensationCall) {
        this.compensationCall = compensationCall;
        return this;
    }
}
