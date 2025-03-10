package com.foodygo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity(name = "OperatingHours")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "operating-hour")
public class OperatingHour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String day;
    boolean isOpen;
    boolean is24Hours;
    LocalTime openingTime;
    LocalTime closingTime;
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public void set24Hours(boolean is24Hours) {
        this.is24Hours = is24Hours;
        if (is24Hours) {
            this.openingTime = LocalTime.of(0,0);
            this.closingTime = LocalTime.of(0,0);
        } else {
            this.openingTime = LocalTime.of(7,0);
            this.closingTime = LocalTime.of(23,0);
        }
    }

    public void setOpeningTime(LocalTime openingTime) {
        if (!is24Hours) {
            this.openingTime = openingTime;
        }
    }

    public void setClosingTime(LocalTime closingTime) {
        if (!is24Hours) {
            this.closingTime = closingTime;
        }
    }
}
