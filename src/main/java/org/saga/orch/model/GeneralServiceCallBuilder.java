package org.saga.orch.model;

public interface GeneralServiceCallBuilder {

    public <S, C> ServiceCallBuilder<S, C> getServiceCallBuilder();
}
