package gmo.library.app;

import feign.Contract;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public Contract useFeignAnnotations() {
        return new Contract.Default();
    }
}
