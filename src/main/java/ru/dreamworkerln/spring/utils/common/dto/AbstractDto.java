package ru.dreamworkerln.spring.utils.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public abstract class AbstractDto implements Serializable {


    protected Long id;

    //@Setter(AccessLevel.NONE)
    protected boolean enabled = true;

    //@Setter(AccessLevel.NONE)
    protected Instant created;

    //@Setter(AccessLevel.NONE)
    protected Instant updated;

    @Override
    public String toString() {
        return "AbstractDto{" +
            "id=" + id +
            ", created=" + created +
            ", updated=" + updated +
            '}';
    }
}
