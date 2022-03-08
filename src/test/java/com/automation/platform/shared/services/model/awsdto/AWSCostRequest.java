
package com.automation.platform.shared.services.model.awsdto;

import com.automation.platform.shared.services.model.common.TimePeriod;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Jacksonized
@Builder
public class AWSCostRequest {
    String awsAccessKey;
    String awsSecretKey;
    String metric;
    String granularity;
    String timeframe;
    TimePeriod timePeriod;
    List<String> groupings;
}
