package ips.mannawarhussain.uk.ips.config;
import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ips.mannawarhussain.uk.ips.converter.FhirJsonMessageConverter;
import ips.mannawarhussain.uk.ips.serialization.DecimalTypeSerializer;
import ips.mannawarhussain.uk.ips.serialization.ReferenceMixin;
import org.hl7.fhir.r5.model.DecimalType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.ref.Reference;
import java.util.List;

@Configuration
public class FhirConfig implements WebMvcConfigurer {
    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR5();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        converters.add(new FhirJsonMessageConverter(fhirContext()));
        converters.add(customJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.setMixInAnnotation(Reference.class, ReferenceMixin.class);
        module.addSerializer(DecimalType.class, new DecimalTypeSerializer());
        objectMapper.registerModule(module);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }
}
