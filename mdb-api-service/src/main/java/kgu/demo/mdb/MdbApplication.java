package kgu.demo.mdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringBootApplication
@EnableZuulProxy
class MdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MdbApplication.class, args);
    }

}
