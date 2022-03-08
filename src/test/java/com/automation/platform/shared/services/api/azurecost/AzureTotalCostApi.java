package com.automation.platform.shared.services.api.azurecost;

import com.automation.platform.shared.services.SAASConstants;
import com.automation.platform.shared.services.api.RestResource;
import com.automation.platform.shared.services.model.azuredto.AzureCostRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class AzureTotalCostApi {

    @Step
    public static Response post(AzureCostRequest azureCostRequest){
        return RestResource.post(SAASConstants.AZURE_TOTALCOST_PATH, SAASConstants.AZURE_APP_HOSTED_PORT,azureCostRequest);
    }
}
