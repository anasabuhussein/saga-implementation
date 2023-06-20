package org.saga.orch.model;

public class ServiceCallResult<S, C> {

    private S serviceCallResult;
    private C compensationCallResult;

    public S getServiceCallResult() {
        return serviceCallResult;
    }

    public void setServiceCallResult(S serviceCallResult) {
        this.serviceCallResult = serviceCallResult;
    }

    public C getCompensationCallResult() {
        return compensationCallResult;
    }

    public void setCompensationCallResult(C compensationCallResult) {
        this.compensationCallResult = compensationCallResult;
    }
}
