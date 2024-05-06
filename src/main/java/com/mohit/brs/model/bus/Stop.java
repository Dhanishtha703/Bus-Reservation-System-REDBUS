package com.mohit.brs.model.bus;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "stop")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stop_id")
public class Stop {

    @Id
    @Column(name = "stop_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stopId;

    @Column(name = "code")
    private String code;

    @Column(name="details")
    private String details;

    @Column(name = "name")
    private String name;

}
