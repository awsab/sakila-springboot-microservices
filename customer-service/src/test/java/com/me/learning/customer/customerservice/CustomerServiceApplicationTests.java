package com.me.learning.customer.customerservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class CustomerServiceApplicationTests {

    @Test
    void contextLoads () {
        assertDoesNotThrow (() -> {}, "The Spring application context should load without exceptions");
    }

}
