package org.saga.orch;

import org.saga.orch.example.ServiceName;
import org.saga.orch.model.ServiceCallBuilder;
import org.saga.orch.model.ServiceCallResult;
import org.saga.orch.model.DefaultServiceCallBuilder;
import org.saga.orch.model.ServiceCallType;
import org.saga.orch.service.SagaServiceCall;

public class AppV1 {

    public static void main( String[] args ) {
        SagaServiceCall sagaServiceCall = new SagaServiceCall();
        sagaServiceCall.prepareNextServiceCall(new DefaultServiceCallBuilder(serviceCall1(sagaServiceCall)));
        sagaServiceCall.prepareNextServiceCall(new DefaultServiceCallBuilder(serviceCall2(sagaServiceCall)));
        sagaServiceCall.prepareNextServiceCall(new DefaultServiceCallBuilder(serviceCall3(sagaServiceCall)));
        sagaServiceCall.doServiceCalls();

        ServiceCallResult<String, String> valueTest101 = sagaServiceCall.getResults(ServiceName.TEST1.name()).getServiceCallResult();
        ServiceCallResult<String, String> valueTest102 = sagaServiceCall.getResults(ServiceName.TEST2.name()).getServiceCallResult();
        ServiceCallResult<String, String> valueTest103 = sagaServiceCall.getResults(ServiceName.TEST3.name()).getServiceCallResult();

        System.out.println(valueTest101.getServiceCallResult());
        System.out.println(valueTest101.getCompensationCallResult());
        System.out.println(valueTest102.getServiceCallResult());
        System.out.println(valueTest102.getCompensationCallResult());

        System.out.println(valueTest103.getServiceCallResult());
        System.out.println(valueTest103.getCompensationCallResult());
    }

    public static ServiceCallBuilder<String, String> serviceCall1 (SagaServiceCall sagaServiceCall) {
        ServiceCallBuilder<String, String> call1 = new ServiceCallBuilder<>();
        call1.setServiceName(ServiceName.TEST1.name());
        call1.setServiceCallType(ServiceCallType.HTTP_CALL);
        call1.setServiceCall(() -> "test service call 1!");
        call1.setCompensationCall(() -> "test Compensation call 1.1");

        return call1;
    }

    public static ServiceCallBuilder<String, String> serviceCall2(SagaServiceCall sagaServiceCall) {
        ServiceCallBuilder<String, String> call2 = new ServiceCallBuilder<>();
        call2.setServiceName(ServiceName.TEST2.name());
        call2.setServiceCallType(ServiceCallType.LOGICAL_CALL);
        call2.setServiceCall(() -> {
            ServiceCallResult<String, String> valueTest101 = sagaServiceCall
                    .getResults()
                    .get(ServiceName.TEST1.name())
                    .getServiceCallResult();

            return valueTest101.getServiceCallResult();
        });
        call2.setCompensationCall(() -> "test Compensation call 2.1");

        return call2;
    }

    public static ServiceCallBuilder<String, String> serviceCall3(SagaServiceCall sagaServiceCall) {
        ServiceCallBuilder<String, String> call3 = new ServiceCallBuilder<>();
        call3.setServiceName(ServiceName.TEST3.name());
        call3.setServiceCallType(ServiceCallType.LOGICAL_CALL);
        call3.setServiceCall(() -> {
            throw new RuntimeException("test!");
        });
        call3.setCompensationCall(() -> "test Compensation call 3.1");

        return call3;
    }

}
