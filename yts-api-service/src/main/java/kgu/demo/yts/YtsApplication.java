package kgu.demo.yts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringBootApplication
@EnableZuulProxy
public class YtsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtsApplication.class, args);
    }

}
