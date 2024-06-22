package ips.mannawarhussain.uk.ips.serialization;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AttachmentMixin {
    @JsonIgnore
    abstract void setDuration(long value);

    abstract void setDuration(double value);
}
