package com.example.sattelliteserrver.data;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name = "data")
public class DataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Double longitude;

    @Column
    private Double latitude;

    @Column
    private Double temperature;

    @Column
    private Double altitude;

    @Column
    private Double pressure;

    @Column
    private Double roll;

    @Column
    private Double pitch;

    @Column
    private Double yaw;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant timestamp;

}