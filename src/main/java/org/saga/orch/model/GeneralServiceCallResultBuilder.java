package org.saga.orch.model;

public interface GeneralServiceCallResultBuilder {

    public <S, C> ServiceCallResult<S, C> getServiceCallResult();
}
