
package com.automation.platform.shared.services.model.common;

import com.automation.platform.shared.services.model.kubecostdto.AllocationRequest;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CostRequest {

    private String granularity;
    private String timeframe;
    private TimePeriod timePeriod;
    private List<String> groupings;
    private AllocationRequest clusterCost;
    private String type;

   //azure fields
    private String tenantId;
    private String subscriptionId;
    private String clientId;
    private String clientSecret;

   //aws fields
   private String awsAccessKey;
   private String awsSecretKey;
   private String metric;


}
