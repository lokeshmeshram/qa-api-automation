
package com.automation.platform.shared.services.model.azuredto;

import com.automation.platform.shared.services.model.common.TimePeriod;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
//@Data
//@Getter @Setter
@Jacksonized
@Builder
public class AzureCostRequest {
    private String tenantId;
    private String subscriptionId;
    private String clientId;
    private String clientSecret;
    private String granularity;
    private String timeframe;
    private TimePeriod timePeriod;
    private List<String> groupings;
}
