package com.spring.webflux.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;


@Data
@SuperBuilder
@Table("address")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Address extends BaseEntity implements Persistable<UUID> {

    private String street;
    private String city;

    @Column("customer_id")
    private UUID customerId;

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
