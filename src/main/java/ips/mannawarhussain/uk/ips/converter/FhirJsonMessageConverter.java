package ips.mannawarhussain.uk.ips.converter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.JsonParser;
import org.hl7.fhir.r5.model.Patient;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FhirJsonMessageConverter extends AbstractHttpMessageConverter<Patient> {

    private final IParser jsonParser;

    public FhirJsonMessageConverter(FhirContext fhirContext) {
        super(MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
        this.jsonParser = fhirContext.newJsonParser();
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Patient.class.isAssignableFrom(clazz);
    }

    @Override
    protected Patient readInternal(Class<? extends Patient> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        String body = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);
        return jsonParser.parseResource(clazz, body);
    }

    @Override
    protected void writeInternal(Patient patient, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getBody().write(jsonParser.encodeResourceToString(patient).getBytes(StandardCharsets.UTF_8));
    }
}
