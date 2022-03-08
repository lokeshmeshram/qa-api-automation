package com.automation.platform.shared.services.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.automation.platform.shared.services.api.StatusCode;
import com.automation.platform.shared.services.api.common.CommonApi;
import com.automation.platform.shared.services.api.kubecost.AllocationApi;
import com.automation.platform.shared.services.model.common.CostRequest;
import com.automation.platform.shared.services.model.common.CostResponse;
import com.automation.platform.shared.services.model.kubecostdto.AllocationRequest;
import com.automation.platform.shared.services.model.kubecostdto.ClusterAllocationCost;
import com.automation.platform.shared.services.model.Error;
import com.automation.platform.shared.services.utils.DataHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static com.automation.platform.shared.services.SAASConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

@Epic("Kubecost 1.0")
@Feature("Kubecost API : /cost/allocation")
public class KubecostTests extends BaseTest {

    AllureLifecycle lifecycle = Allure.getLifecycle();
    private final static Logger LOGGER = Logger.getLogger(KubecostTests.class.getName());

    @DataProvider(name = "kubecost-data")
    public Object[][] getData() {
        ClassLoader classLoader = getClass().getClassLoader();
        return DataHelper.data(classLoader.getResource(TESTDATA_PKG_PATH + KUBECOST_FILENAME + XLSX).getPath(), ALLOCATIONREQUEST);
    }

    @Story("Kubecost API Test")
    @Description("Kubecost API Test description")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "kubecost-data", groups = {"kubecostfunctest", "Positive"},suiteName = "kubecosttest-suite")
    public void ShouldBeAbleToGetKubeCostResponse(String testcaseId,String kubecostEndpoints, String window, String aggregate, String idle, String step) throws JsonProcessingException {

        AllocationRequest allocationRequest = allocationRequestBuilder(Arrays.asList(kubecostEndpoints), window, aggregate, idle, step);

        CostRequest costRequest = costAzureRequestBuilder("DAILY", "", "{}", null, allocationRequest,CLOUDTYPE_AZURE);
        Response costResponse = CommonApi.post(costRequest);
        assertStatusCode(costResponse.statusCode(), StatusCode.CODE_200);

        Response allocationResponse = AllocationApi.post(allocationRequest);
        assertStatusCode(allocationResponse.statusCode(), StatusCode.CODE_200);

        assertTotalCost(costResponse.as(CostResponse.class), allocationResponse.as(ClusterAllocationCost[].class));
        lifecycle.updateTestCase(testResult -> testResult.setName("ShouldBeAbleToGetKubeCostResponse_"+testcaseId));
    }

    @Description("should not be able to get response for invalid param -->  Window : 1")
    @Severity(SeverityLevel.MINOR)
    @Test(groups = {"kubecostfunctest", "Negative"},suiteName = "kubecosttest-suite",testName = "SAAS-02")
    public void ShouldNotBeAbleToGetKubeCostResponseForWindow1() {
        AllocationRequest allocationRequest = allocationRequestBuilder(Arrays.asList("http://localhost:9003"), "1", "namespace", "", "");
        Response response = AllocationApi.post(allocationRequest);
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
        assertMessageAndTotalCost(response.as(ClusterAllocationCost[].class));
    }

    @Step
    public AllocationRequest allocationRequestBuilder(List<String> kubecostEndpoints, String window, String aggregate, String idle, String step) {
        return AllocationRequest.builder().
                kubecostEndpoints(kubecostEndpoints).
                window(window).
                aggregate(aggregate).
                idle(idle).
                step(step).
                build();
    }

    @Step
    public void assertTotalCost(CostResponse costResponse,ClusterAllocationCost[] clusterAllocationCost) {

        LOGGER.info("----aggregate getTotalCost " + costResponse.getClusterCost().get(0).getTotalCost());
        LOGGER.info("----kubecost getTotalCost " + clusterAllocationCost[0].getTotalCost());
        assertThat( costResponse.getClusterCost().get(0).getTotalCost(), is(notNullValue()));
        assertThat(clusterAllocationCost[0].getTotalCost(), is(notNullValue()));
        double buffer = costResponse.getClusterCost().get(0).getTotalCost() * PERCENTAGE_MATCH_VALUE;

        assertThat(costResponse.getTotalCost(), is(notNullValue()));
        assertThat(clusterAllocationCost[0].getTotalCost(), closeTo(costResponse.getClusterCost().get(0).getTotalCost(), buffer));

    }

    @Step
    public void assertMessageAndTotalCost(ClusterAllocationCost[] clusterAllocationCost) {
        assertThat(clusterAllocationCost[0].getMessage(), equalTo("Could not fetch the cost of the given cluster"));
        assertThat(clusterAllocationCost[0].getTotalCost(), is(0.0));
        assertThat(clusterAllocationCost[0].getAllocations(), is(nullValue()));
    }

    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode statusCode) {
        assertThat(actualStatusCode, equalTo(statusCode.code));
    }

    @Step
    public void assertError(Error responseErr, StatusCode statusCode) {
        assertThat(responseErr.getError().getStatus(), equalTo(statusCode.code));
        assertThat(responseErr.getError().getMessage(), equalTo(statusCode.msg));
    }
}
