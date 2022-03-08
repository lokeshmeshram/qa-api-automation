
package com.automation.platform.shared.services.model.azuredto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
//@Data
//@Getter @Setter
@Jacksonized
@Builder
public class AzureCostResponse {
    private List<Object> resourceGroupCost;
    private Double totalCost;
    private String clusterCost;
    private String timeFrame;
}
