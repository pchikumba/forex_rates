package com.wiremit.forex_rates.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "currencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency extends BaseEntity {
    @NotNull(message = "Currency code is required")
    private String code;
    @NotNull(message = "Currency name is required")
    private String name;
}
