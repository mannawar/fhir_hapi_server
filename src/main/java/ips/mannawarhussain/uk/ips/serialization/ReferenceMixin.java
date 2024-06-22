package ips.mannawarhussain.uk.ips.serialization;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ReferenceMixin {
    @JsonIgnore
    abstract void setReferenceElement(org.hl7.fhir.instance.model.api.IIdType value);

    abstract void setReferenceElement(org.hl7.fhir.r5.model.StringType value);
}
