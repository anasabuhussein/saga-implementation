package org.saga.orch;

import org.saga.orch.example.ExampleModel;
import org.saga.orch.model.DefaultServiceCallBuilder;
import org.saga.orch.model.ServiceCallBuilder;
import org.saga.orch.model.ServiceCallResult;
import org.saga.orch.service.SagaServiceCall;

public class AppV2 {

    public static void main( String[] args ) {
        SagaServiceCall sagaServiceCall = new SagaServiceCall();
        ServiceCallBuilder<ExampleModel, String> call1 = new ServiceCallBuilder<>();
        call1.setServiceName("TEST1");
        call1.setServiceCall(() -> new ExampleModel("Test 1", "list of tests"));
        call1.setCompensationCall(() -> "test Compensation call 1.1");

        sagaServiceCall.prepareNextServiceCall(new DefaultServiceCallBuilder(call1));
        sagaServiceCall.doServiceCalls();

        ServiceCallResult<ExampleModel, String> valueTest101 = sagaServiceCall.getResults().get("TEST1").getServiceCallResult();
        System.out.println(valueTest101.getServiceCallResult().getName());
        System.out.println(valueTest101.getServiceCallResult().getExampleReference());
    }
}
