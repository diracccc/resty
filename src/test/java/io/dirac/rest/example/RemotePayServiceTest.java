package io.dirac.rest.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RemotePayServiceTest {

    @Resource(name = "remotePayService")
    private RemotePayService service;

    @Test
    public void testObjectMethod() {
        System.out.println(service);
    }

    @Test
    public void getOrderInfo() throws Exception {
        Map<String, Object> result = service.getOrderInfo("tx1001");
        assertEquals(result.get("code"), "0");
    }

    @Test
    public void createOrder() throws Exception {
        String result = service.createOrder(new PayRequest());
        assertNotNull(result);
    }
}