# saga-implementation

This projects refers to saga implementation and how we can implement service orchestrations and collect the data from different source in addition, 
handle service calls, exceptions.


### Saga Orchestrations:

- Saga should handle both side success transactions and fail transactions.
- Saga should define service name for each service call. 
- Saga should collect the data for success and fail service call.
- Saga should be able to retrieve the data from service call.


Here how you can build Service call and you can implement what you need, by use functional interface with suppler functions. <br>
Also here you need to define the service name, and service type to follow different behaviors based on what you need! <br>
Also you should fill `setServiceCall` function with your implementations, but you can skip `setCompensationCall` function. <br>
if you need to handle the exception like reverse few actions you may need to implement `setCompensationCall` function.
```java
        ServiceCallBuilder<String, String> call1 = new ServiceCallBuilder<>();
        call1.setServiceName(ServiceName.TEST1.name());
        call1.setServiceCallType(ServiceCallType.HTTP_CALL);
        call1.setServiceCall(() -> "test service call 1!");
        call1.setCompensationCall(() -> "test Compensation call 1.1");
```

Here you can fill you service calls and sort it based on your service call structure. <br>
use this method to fill services call `prepareNextServiceCall`; <br>
to excute the calls use this methods `doServiceCalls`;
```java
        SagaServiceCall sagaServiceCall = new SagaServiceCall();
        sagaServiceCall.prepareNextServiceCall(new DefaultServiceCallBuilder(call1));
        sagaServiceCall.doServiceCalls();
```

To store and use services result you can use this methods `valueTest101.getServiceCallResult()` for success service call. <br>
and for exceptions and failures you can use this method call `valueTest101.getCompensationCallResult()`.
```java
        ServiceCallResult<String, String> valueTest101 = sagaServiceCall.getResults().get(ServiceName.TEST1.name()).getServiceCallResult();
        ServiceCallResult<String, String> valueTest102 = sagaServiceCall.getResults().get(ServiceName.TEST2.name()).getServiceCallResult();

        System.out.println(valueTest101.getServiceCallResult());
        System.out.println(valueTest101.getCompensationCallResult()); 
```

for more details on how to use this library or code snapshot, check below: 
```java

public class AppV1 {

    public static void main( String[] args ) {
        SagaServiceCall sagaServiceCall = new SagaServiceCall();
        ServiceCallBuilder<String, String> call1 = new ServiceCallBuilder<>();
        call1.setServiceName(ServiceName.TEST1.name());
        call1.setServiceCallType(ServiceCallType.HTTP_CALL);
        call1.setServiceCall(() -> "test service call 1!");
        call1.setCompensationCall(() -> "test Compensation call 1.1");

        ServiceCallBuilder<String, String> call2 = new ServiceCallBuilder<>();
        call2.setServiceName(ServiceName.TEST2.name());
        call2.setServiceCallType(ServiceCallType.LOGICAL_CALL);
        call2.setServiceCall(() -> {
            ServiceCallResult<String, String> valueTest101 = sagaServiceCall.getResults().get(ServiceName.TEST1.name()).getServiceCallResult();
            return valueTest101.getServiceCallResult();
        });
        call2.setCompensationCall(() -> "test Compensation call 2.1");

        ServiceCallBuilder<String, String> call3 = new ServiceCallBuilder<>();
        call3.setServiceName(ServiceName.TEST3.name());
        call3.setServiceCallType(ServiceCallType.LOGICAL_CALL);
        call3.setServiceCall(() -> {
            throw new RuntimeException("test!");
        });
        call3.setCompensationCall(() -> "test Compensation call 3.1");

        sagaServiceCall.prepareNextServiceCall(new DefaultServiceCallBuilder(call1));
        sagaServiceCall.prepareNextServiceCall(new DefaultServiceCallBuilder(call2));
        sagaServiceCall.prepareNextServiceCall(new DefaultServiceCallBuilder(call3));
        sagaServiceCall.doServiceCalls();

        ServiceCallResult<String, String> valueTest101 = sagaServiceCall.getResults().get(ServiceName.TEST1.name()).getServiceCallResult();
        ServiceCallResult<String, String> valueTest102 = sagaServiceCall.getResults().get(ServiceName.TEST2.name()).getServiceCallResult();

        System.out.println(valueTest101.getServiceCallResult());
        System.out.println(valueTest101.getCompensationCallResult());
        System.out.println(valueTest102.getServiceCallResult());
        System.out.println(valueTest102.getCompensationCallResult());
    }
}
```


