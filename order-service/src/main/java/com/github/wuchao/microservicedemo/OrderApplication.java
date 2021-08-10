package com.github.wuchao.microservicedemo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.TimeZone;

@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
public class OrderApplication {

    private static final Logger log = LoggerFactory.getLogger(OrderApplication.class);

    private final Environment env;

    public OrderApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        ApplicationContext app = new SpringApplication(OrderApplication.class).run(args);
        Environment env = app.getEnvironment();
        orderApplicationStartup(env);
    }

    private static void orderApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }

        String hostAddress;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "localhost";
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }

        String eurekaCenter = env.getProperty("eureka.client.service-url.defaultZone");
        String registerWithEureka = env.getProperty("eureka.client.register-with-eureka");

        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t\t{}://localhost:{}{}\n\t" +
                        "External: \t\t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n\t" +
                        "JVMTimeZone: \t{}\n\t" +
                        "ZoneIdTimeZone: {}\n\t" +
                        "FileEncoding: \t{}\n\t" +
                        "DefaultCharset: {}\n\t" +
                        "Language: \t\t{}\n\t" +
                        "eurekaCenter : {}\n\t" +
                        "registerWithEureka : {}\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles(),
                System.getProperty("user.timezone"),
                TimeZone.getDefault().getID() + "[" + TimeZone.getDefault().getDisplayName() + "]",
                System.getProperty("file.encoding"),
                Charset.defaultCharset(),
                System.getProperty("user.language"),
                eurekaCenter,
                registerWithEureka);

        String configServerStatus = env.getProperty("configserver.status");
        if (configServerStatus == null) {
            configServerStatus = "Not found or not setup for this application";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Config Server: \t{}\n----------------------------------------------------------", configServerStatus);
    }

}
