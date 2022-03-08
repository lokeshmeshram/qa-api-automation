package com.automation.platform.shared.services.api.awscost;

import com.automation.platform.shared.services.SAASConstants;
import com.automation.platform.shared.services.api.RestResource;
import com.automation.platform.shared.services.model.awsdto.AWSCostRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class AWSTotalCostApi {

    @Step
    public static Response post(AWSCostRequest awsCostRequest){
        return RestResource.post(SAASConstants.AWS_TOTALCOST_PATH, SAASConstants.AWS_APP_HOSTED_PORT,awsCostRequest);
    }
}
