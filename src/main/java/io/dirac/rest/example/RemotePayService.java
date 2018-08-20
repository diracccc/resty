package io.dirac.rest.example;

import io.dirac.rest.annotation.Bind;
import io.dirac.rest.annotation.RemoteService;
import io.dirac.rest.annotation.RequestObject;
import io.dirac.rest.annotation.ServiceUri;

import java.util.Map;

@RemoteService("paPayServiceGroup")
public interface RemotePayService {

    @ServiceUri(value = "/api/v1/trans/{transId}", method = "GET")
    Map<String, Object> getOrderInfo(@Bind("transId") String txId) throws Exception;

    @ServiceUri("/api/v1/order")
    String createOrder(@RequestObject PayRequest request) throws Exception;
}
