package com.mohit.brs.model.request;

import com.mohit.brs.model.bus.Agency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusDto {

    private String code;

    private int capacity;

    private String model;

//    private Agency agency;

}
