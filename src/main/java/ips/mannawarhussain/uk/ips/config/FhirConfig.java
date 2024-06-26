package ips.mannawarhussain.uk.ips.config;
import ca.uhn.fhir.context.FhirContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class FhirConfig implements WebMvcConfigurer {
    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR5();
    }

}
