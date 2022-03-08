
package com.automation.platform.shared.services.model.awsdto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Jacksonized
@Builder
public class AWSCostResponse {
    List<Object> costResults;
    Double totalCost;
    String clusterCost;
    String timeFrame;
}
