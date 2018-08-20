package io.dirac.rest.example;

import io.dirac.rest.spring.InvokeScannerConfigure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "io.dirac.rest")
public class TestConfig {

    @Bean
    public InvokeScannerConfigure configure() {
        InvokeScannerConfigure configure = new InvokeScannerConfigure();
        configure.setBasePackage("io.dirac.rest");
        return configure;
    }
}
