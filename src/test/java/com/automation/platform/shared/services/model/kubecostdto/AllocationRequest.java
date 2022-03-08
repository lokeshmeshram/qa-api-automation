
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
public class AllocationRequest {
    private List<String> kubecostEndpoints;
    private String window;
    private String aggregate;
    private String idle;
    private String step;
}
