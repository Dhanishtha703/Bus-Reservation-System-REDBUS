package com.mohit.brs.model.request;

import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.bus.Bus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDto {

    private Integer fare;

    private Integer journeyTime;

    private String sourceStopCode;

    private String sourceStopName;

    private String destinationStopCode;

    private String destinationStopName;

    private String busCode;

    private String agencyCode;

}
