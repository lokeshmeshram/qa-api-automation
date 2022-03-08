package com.automation.platform.shared.services.api.common;

import com.automation.platform.shared.services.SAASConstants;
import com.automation.platform.shared.services.api.RestResource;
import com.automation.platform.shared.services.model.common.CostRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class CommonApi {

    @Step
    public static Response post(CostRequest costRequest){
        return RestResource.post(SAASConstants.AGGREGATE_SERVICE_PATH, SAASConstants.AGGREGATE_APP_HOSTED_PORT,costRequest);
    }
}
