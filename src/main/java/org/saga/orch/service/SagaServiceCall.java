package org.saga.orch.service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.saga.orch.model.*;

public class SagaServiceCall {

    private static final Logger LOGGER = Logger.getLogger(SagaServiceCall.class.getName());

    private final Map<String, GeneralServiceCallResultBuilder> results = new HashMap<>();
    private final Queue<GeneralServiceCallBuilder> nextServiceCall = new LinkedList<>();
    private final Deque<GeneralServiceCallBuilder> finishedServiceCall = new LinkedList<>();

    public SagaServiceCall prepareNextServiceCall(GeneralServiceCallBuilder generalServiceCallBuilder) {
        LOGGER.log(Level.INFO, "prepare next service call!");
        nextServiceCall.add(generalServiceCallBuilder);
        return this;
    }

    public void doServiceCalls() {
        LOGGER.log(Level.INFO, "do service call sequentially from down to top!");
        nextServiceCall.forEach(serviceCall -> {
            try {
                LOGGER.log(Level.INFO, "start to call services !");
                ServiceCallHandler serviceCallHandler = new ServiceCallHandler(serviceCall);
                ServiceCallResult<?, ?> serviceCallResult = new ServiceCallResult<>();
                serviceCallResult.setServiceCallResult(serviceCallHandler.getServiceCallResult());
                GeneralServiceCallResultBuilder resultBuilder = new DefaultServiceCallResultBuilder(serviceCallResult);

                String serviceName = serviceCall.getServiceCallBuilder().getServiceName();
                results.put(serviceName, resultBuilder);

                LOGGER.log(Level.INFO, "store success result of service {0} ", serviceName);
                finishedServiceCall.addLast(serviceCall);
            } catch (RuntimeException runtimeException) {
                LOGGER.log(Level.WARNING, "something wrong! {0}", runtimeException.getMessage());
                nextServiceCall.clear();
                rollbackServiceCalls();
            }
        });
    }

    private void rollbackServiceCalls() {
        LOGGER.log(Level.INFO, "rollback service call sequentially from top to down!");
        finishedServiceCall.forEach(serviceCall -> {
            try {
                String serviceName = serviceCall.getServiceCallBuilder().getServiceName();
                ServiceCallHandler serviceCallHandler = new ServiceCallHandler(serviceCall);
                GeneralServiceCallResultBuilder existGeneralServiceCall = results.get(serviceName);

                LOGGER.log(Level.INFO, "get stored result from success service call for service {0}", serviceName);
                if (existGeneralServiceCall != null) {
                    ServiceCallResult<?, ?> serviceCallResult = existGeneralServiceCall.getServiceCallResult();
                    if (serviceCallResult != null) {
                        LOGGER.log(Level.INFO, "set compensation service call result for service {0}", serviceName);
                        serviceCallResult.setCompensationCallResult(serviceCallHandler.getCompensationCallResult());
                    }
                } else {
                    ServiceCallResult<?, ?> serviceCallResult = new ServiceCallResult<>();
                    serviceCallResult.setCompensationCallResult(serviceCallHandler.getCompensationCallResult());

                    LOGGER.log(Level.INFO, "fail to find success service call result for service! {0}", serviceName);
                    LOGGER.log(Level.INFO, "set compensation service call result for service {0}", serviceName);
                    GeneralServiceCallResultBuilder generalResultBuilder = new DefaultServiceCallResultBuilder(serviceCallResult);
                    results.put(serviceName, generalResultBuilder);
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
