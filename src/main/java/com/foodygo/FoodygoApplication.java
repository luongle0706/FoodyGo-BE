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
        // For each property, check if dotenv has a value; if not, fallback to System.getProperty
        String payUrl = dotenv.get("PAY_URL") != null ? dotenv.get("PAY_URL") : System.getProperty("PAY_URL", "");
        String tmnCode = dotenv.get("TMN_CODE") != null ? dotenv.get("TMN_CODE") : System.getProperty("TMN_CODE", "");
        String secretKey = dotenv.get("SECRET_KEY") != null ? dotenv.get("SECRET_KEY") : System.getProperty("SECRET_KEY", "");
        String returnUrl = dotenv.get("RETURN_URL") != null ? dotenv.get("RETURN_URL") : System.getProperty("RETURN_URL", "");
        String version = dotenv.get("VERSION") != null ? dotenv.get("VERSION") : System.getProperty("VERSION", "");
        String command = dotenv.get("COMMAND") != null ? dotenv.get("COMMAND") : System.getProperty("COMMAND", "");
        String orderType = dotenv.get("ORDER_TYPE") != null ? dotenv.get("ORDER_TYPE") : System.getProperty("ORDER_TYPE", "");

        System.setProperty("PAY_URL", payUrl);
        System.setProperty("TMN_CODE", tmnCode);
        System.setProperty("SECRET_KEY", secretKey);
        System.setProperty("RETURN_URL", returnUrl);
        System.setProperty("VERSION", version);
        System.setProperty("COMMAND", command);
        System.setProperty("ORDER_TYPE", orderType);

        SpringApplication.run(FoodygoApplication.class, args);
    }

}
