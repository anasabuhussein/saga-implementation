# saga-implementation

This projects refers to saga implementation and how we can implement service orchestrations and collect the data from different source in addition, 
handle service calls, exceptions.


### Saga Orchestrations:

> You have applied the Database per Service pattern. Each service has its own database. <br>
Some business transactions, however, span multiple service so you need a mechanism to implement transactions that span services. <br> 
For example, let’s imagine that you are building an e-commerce store where customers have a credit limit. <br>
The application must ensure that a new order will not exceed the customer’s credit limit. <br>
Since Orders and Customers are in different databases owned by different services the application cannot simply use a local ACID transaction. <br>
Reference : https://microservices.io/patterns/data/saga.html 

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

for more details on how to use this library or code snapshot, check below for success service call only: 
```java

public class AppV1 {

    public static void main( String[] args ) {
        SagaServiceCall sagaServiceCall = new SagaServiceCall();
        sagaServiceCall.prepareNextServiceCall(new DefaultServiceCallBuilder(serviceCall1(sagaServiceCall)));
        sagaServiceCall.prepareNextServiceCall(new DefaultServiceCallBuilder(serviceCall2(sagaServiceCall)));
        sagaServiceCall.doServiceCalls();

        ServiceCallResult<String, String> valueTest101 = sagaServiceCall.getResults().get(ServiceName.TEST1.name()).getServiceCallResult();
        ServiceCallResult<String, String> valueTest102 = sagaServiceCall.getResults().get(ServiceName.TEST2.name()).getServiceCallResult();

        System.out.println(valueTest101.getServiceCallResult());
        System.out.println(valueTest101.getCompensationCallResult());
        System.out.println(valueTest102.getServiceCallResult());
        System.out.println(valueTest102.getCompensationCallResult());
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
}

```


for more details on how to use this library or code snapshot, check below for success and fails service call 
```java

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
```


