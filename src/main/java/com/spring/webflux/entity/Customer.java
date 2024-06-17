package com.spring.webflux.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@SuperBuilder
@Table("customer")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntity implements Persistable<UUID> {

    private String name;
    private String email;

    @Transient
    @JsonIgnore
    private boolean isNew;
    @Override
    public boolean isNew() {
        return isNew || getId() == null;
    }

    public void setAsNew() {
        this.isNew = true;
    }

}
