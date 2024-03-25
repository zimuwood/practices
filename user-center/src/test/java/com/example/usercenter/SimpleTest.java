package com.example.usercenter;

import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author CloudyW
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SimpleTest {
    @Test
    public void myTest(){
        System.out.println("Hello");
    }
}
