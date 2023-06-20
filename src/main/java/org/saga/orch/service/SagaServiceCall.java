package org.saga.orch.service;

import java.util.*;
import org.saga.orch.model.*;

public class SagaServiceCall {

    private final Map<String, GeneralServiceCallResultBuilder> results = new HashMap<>();
    private final Queue<GeneralServiceCallBuilder> nextServiceCall = new LinkedList<>();
    private final Deque<GeneralServiceCallBuilder> finishedServiceCall = new LinkedList<>();

    public SagaServiceCall() {

    }

    public SagaServiceCall prepareNextServiceCall(GeneralServiceCallBuilder generalServiceCallBuilder) {
        nextServiceCall.add(generalServiceCallBuilder);
        return this;
    }

    public void doServiceCalls() {
        nextServiceCall.forEach(serviceCall -> {
            try {
                ServiceCallHandler serviceCallHandler = new ServiceCallHandler(serviceCall);
                ServiceCallResult<?, ?> serviceCallResult = new ServiceCallResult<>();
                serviceCallResult.setServiceCallResult(serviceCallHandler.getServiceCallResult());

                GeneralServiceCallResultBuilder resultBuilder = new DefaultServiceCallResultBuilder(serviceCallResult);
                results.put(serviceCall.getServiceCallBuilder().getServiceName(), resultBuilder);
                finishedServiceCall.addLast(serviceCall);
            } catch (RuntimeException runtimeException) {
                nextServiceCall.clear();
                rollbackServiceCalls();
            }
        });
    }

    private void rollbackServiceCalls() {
        finishedServiceCall.forEach(serviceCall -> {
            try {
                ServiceCallHandler serviceCallHandler = new ServiceCallHandler(serviceCall);
                GeneralServiceCallResultBuilder existGeneralServiceCall = results.get(serviceCall
                        .getServiceCallBuilder()
                        .getServiceName());

                if (existGeneralServiceCall != null) {
                    ServiceCallResult<?, ?> serviceCallResult = existGeneralServiceCall.getServiceCallResult();
                    if (serviceCallResult != null) {
                        serviceCallResult.setCompensationCallResult(serviceCallHandler.getCompensationCallResult());
                    }
                } else {
                    ServiceCallResult<?, ?> serviceCallResult = new ServiceCallResult<>();
                    serviceCallResult.setCompensationCallResult(serviceCallHandler.getCompensationCallResult());

                    GeneralServiceCallResultBuilder
                            generalServiceCallResultBuilder = new DefaultServiceCallResultBuilder(serviceCallResult);
                    results.put(serviceCall.getServiceCallBuilder().getServiceName(), generalServiceCallResultBuilder);
                }
            } catch (RuntimeException runtimeException) {
                finishedServiceCall.pop();
                rollbackServiceCalls();
            }
        });
    }

    public Map<String, GeneralServiceCallResultBuilder> getResults() {
        return results;
    }

    public Queue<GeneralServiceCallBuilder> getNextServiceCall() {
        return nextServiceCall;
    }

    public Deque<GeneralServiceCallBuilder> getFinishedServiceCall() {
        return finishedServiceCall;
    }
}
