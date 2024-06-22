package ips.mannawarhussain.uk.ips.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.hl7.fhir.r5.model.Reference;

import java.io.IOException;

public class ReferenceSerializer extends StdSerializer<Reference> {
    public ReferenceSerializer() {
        this(null);
    }

    public ReferenceSerializer(Class<Reference> t) {
        super(t);
    }

    @Override
    public void serialize(Reference reference, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        if(reference.getReferenceElement() != null){
            jsonGenerator.writeStringField("referenceElement", reference.getReferenceElement().getValue());
        }
        jsonGenerator.writeEndObject();
    }
}
