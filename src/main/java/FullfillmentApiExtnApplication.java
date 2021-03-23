import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.yantriks.yso"})
public class FullfillmentApiExtnApplication {

  public static void main(String[] args) {
    SpringApplication.run(FullfillmentApiExtnApplication.class, args);
  }
}

