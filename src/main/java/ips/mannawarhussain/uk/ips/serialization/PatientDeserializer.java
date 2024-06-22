package ips.mannawarhussain.uk.ips.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.hl7.fhir.r5.model.Patient;

import java.io.IOException;

public class PatientDeserializer extends StdDeserializer<Patient> {
    protected PatientDeserializer(Class<?> vc) {
        super(Patient.class);
    }

    @Override
    public Patient deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
            JacksonException {
        Patient patient = new Patient();

        return patient;
    }
}
