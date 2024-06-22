package ips.mannawarhussain.uk.ips.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.StringType;

import java.io.IOException;

public class ReferenceDeSerializer extends StdDeserializer<Reference> {
    public ReferenceDeSerializer() {
        this(null);
    }

    public ReferenceDeSerializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Reference deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String referenceElement = jsonParser.readValueAs(String.class);
        Reference reference = new Reference();
        reference.setReferenceElement(new StringType(referenceElement));
        return  reference;
    }

}
