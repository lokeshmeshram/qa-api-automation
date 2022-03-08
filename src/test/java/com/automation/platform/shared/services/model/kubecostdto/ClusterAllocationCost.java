
package com.automation.platform.shared.services.model.kubecostdto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
//@Data
//@Getter @Setter
@Jacksonized
@Builder
public class ClusterAllocationCost {
    private String kubecostEndpoint;

    private String message;

    private double totalCost;

    private List<Object> allocations;
}
