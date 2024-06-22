package ips.mannawarhussain.uk.ips.serialization;

import com.fasterxml.jackson.annotation.JsonSetter;

public abstract class DecimalTypeMixin {
    @JsonSetter
    public abstract void setValue(double value);

    @JsonSetter
    public abstract void setValue(long value);
}
