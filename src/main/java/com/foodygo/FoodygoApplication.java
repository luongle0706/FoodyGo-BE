package com.foodygo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class FoodygoApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+07:00"));
        Dotenv dotenv = Dotenv.configure().filename("local.env").ignoreIfMissing().load();
        System.setProperty("PAY_URL", dotenv.get("PAY_URL"));
        System.setProperty("TMN_CODE", dotenv.get("TMN_CODE"));
        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
        System.setProperty("RETURN_URL", dotenv.get("RETURN_URL"));
        System.setProperty("VERSION", dotenv.get("VERSION"));
        System.setProperty("COMMAND", dotenv.get("COMMAND"));
        System.setProperty("ORDER_TYPE", dotenv.get("ORDER_TYPE"));

        SpringApplication.run(FoodygoApplication.class, args);
    }

}
