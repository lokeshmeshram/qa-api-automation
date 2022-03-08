package com.automation.platform.shared.services.api.kubecost;

import com.automation.platform.shared.services.SAASConstants;
import com.automation.platform.shared.services.api.RestResource;
import com.automation.platform.shared.services.model.kubecostdto.AllocationRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class AllocationApi {

    @Step
    public static Response post(AllocationRequest allocationRequest){
        return RestResource.post(SAASConstants.ALLOCATION_PATH, SAASConstants.KUBECOST_APP_HOSTED_PORT,allocationRequest);
    }

  /*  public static Response get(String allocationRequest){
        return RestResource.get(ALLOCATION_PATH, allocationRequest);
    }*/

}
