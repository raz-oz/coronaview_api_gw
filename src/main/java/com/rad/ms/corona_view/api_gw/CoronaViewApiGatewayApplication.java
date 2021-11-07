package com.rad.ms.corona_view.api_gw;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableEurekaClient
@SpringBootApplication
public class CoronaViewApiGatewayApplication implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ApplicationContext applicationContext;


    public static void main(String[] args) {
        SpringApplication.run(CoronaViewApiGatewayApplication.class, args);
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event)
    {
        try
        {

            String ip       = InetAddress.getLocalHost().getHostAddress();
            String hostName = InetAddress.getLocalHost().getHostName();
            int port        = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 0);

            System.out.println("*****************************************************");
            System.out.println("* Coronaview API-GW is UP and Ready ");
            System.out.println("* Host=" + hostName + ", IP=" + ip + ", Port=" + port);
            System.out.println("*****************************************************");
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }


}
