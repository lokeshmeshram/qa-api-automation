
package com.automation.platform.shared.services.model.common;

import com.automation.platform.shared.services.model.kubecostdto.ClusterAllocationCost;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CostResponse {
    private Double totalCost;
    private String timeFrame;
    private List<ClusterAllocationCost> clusterCost;
}
