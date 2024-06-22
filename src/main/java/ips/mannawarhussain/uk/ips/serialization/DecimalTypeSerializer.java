package ips.mannawarhussain.uk.ips.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.hl7.fhir.r5.model.DecimalType;

import java.io.IOException;

public class DecimalTypeSerializer extends StdSerializer<DecimalType> {
    public DecimalTypeSerializer() {
        super(DecimalType.class);
    }

    @Override
    public void serialize(DecimalType decimalType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(decimalType == null || decimalType.getValue() == null){
            jsonGenerator.writeNull();
        }else {
            jsonGenerator.writeNumber(decimalType.getValue().intValue());
        }
    }
}
