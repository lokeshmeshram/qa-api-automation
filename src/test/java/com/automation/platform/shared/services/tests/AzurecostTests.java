package com.automation.platform.shared.services.tests;

import com.automation.platform.shared.services.utils.DataHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.automation.platform.shared.services.api.StatusCode;
import com.automation.platform.shared.services.api.azurecost.AzureTotalCostApi;
import com.automation.platform.shared.services.api.common.CommonApi;
import com.automation.platform.shared.services.model.azuredto.AzureCostRequest;
import com.automation.platform.shared.services.model.azuredto.AzureCostResponse;
import com.automation.platform.shared.services.model.common.CostRequest;
import com.automation.platform.shared.services.model.common.CostResponse;
import com.automation.platform.shared.services.model.common.TimePeriod;
import com.automation.platform.shared.services.model.kubecostdto.AllocationRequest;
import com.automation.platform.shared.services.utils.DateUtils;
import com.automation.platform.shared.services.utils.StringUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static com.automation.platform.shared.services.SAASConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

@Epic("Azurecost 1.0")
@Feature("Azurecost API : /azure/totalcost")
public class AzurecostTests extends BaseTest {

    AllureLifecycle lifecycle = Allure.getLifecycle();
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Logger LOGGER = Logger.getLogger(AzurecostTests.class.getName());

    @DataProvider(name = "azurecost-data")
    public Object[][] getData() {
        ClassLoader classLoader = getClass().getClassLoader();
        return DataHelper.data(classLoader.getResource(TESTDATA_PKG_PATH + AZURECOST_FILENAME + XLSX).getPath(), TOTALCOST_SHEETNAME);
    }

    @Story("Azurecost API Test")
    @Description("Azurecost API Test description")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "azurecost-data", groups = {"azurecostcostfunctest", "Positive"})
    public void ShouldBeAbleToGetAzureCostResponse(String testcaseId,String granularity, String timeframe, String customTimePeriod, String groupings) throws JsonProcessingException {
        CostRequest costRequest = costAzureRequestBuilder(granularity, timeframe, customTimePeriod, StringUtils.manipulateListOfString(groupings),null,CLOUDTYPE_AZURE);
        Response response = CommonApi.post(costRequest);

        assertStatusCode(response.statusCode(), StatusCode.CODE_200);

        AzureCostRequest azureCostRequest = azureCostRequestBuilder(granularity, timeframe, customTimePeriod, StringUtils.manipulateListOfString(groupings));
        Response azresponse = AzureTotalCostApi.post(azureCostRequest);
        assertStatusCode(azresponse.statusCode(), StatusCode.CODE_200);

        assertTotalCost(response.as(CostResponse.class), azresponse.as(AzureCostResponse.class));
        lifecycle.updateTestCase(testResult -> testResult.setName("ShouldBeAbleToGetAzureCostResponse"+testcaseId));
    }

    @Description("Azurecost API Test for param timeframe: 123")
    @Severity(SeverityLevel.MINOR)
    @Test(groups = {"azurecostcostfunctest", "Negative"})
    public void ShouldNotGetAzureCostResponseForInvalidParamValueTimeframe() throws JsonProcessingException {
        CostRequest costRequest = costAzureRequestBuilder("DAILY", "123", "{}", null,null,CLOUDTYPE_AZURE);
        Response response = CommonApi.post(costRequest);

        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
        assertTotalCostToZeroValue(response.as(CostResponse.class));

    }

    @Description("Azurecost API Test for param timeframe: Custom and  timePeriod: where from > To (greater than)")
    @Severity(SeverityLevel.MINOR)
    @Test(groups = {"azurecostcostfunctest", "Negative"})
    public void ShouldNotGetAzureCostResponseForInvalidParamValueTimePeriod() throws JsonProcessingException {

        TimePeriod timePeriod = new TimePeriod();
        Date yesterday = DateUtils.dateValue(-1);
        Date dayBeforeYesterday =  DateUtils.dateValue(-2);
        timePeriod.setFrom(yesterday);
        timePeriod.setTo(dayBeforeYesterday);

        CostRequest costRequest = costRequestBuilder("DAILY", "Custom", timePeriod, null,null,CLOUDTYPE_AZURE);
        Response response = CommonApi.post(costRequest);

        assertStatusCode(response.statusCode(), StatusCode.CODE_400);

    }

    @Step
    public CostRequest costRequestBuilder(String granularity, String timeframe, TimePeriod customTimePeriod, List<String> groupings, AllocationRequest clusterCost, String type) {
        return CostRequest.builder().
                granularity(granularity).
                timeframe(timeframe).
                timePeriod(customTimePeriod).
                groupings(groupings).
                clusterCost(clusterCost).
                type(type).
                tenantId(AZURE_TENANTID).
                subscriptionId(AZURE_SUBSCRIPTIONID).
                clientId(AZURE_CLIENTID).
                clientSecret(AZURE_CLIENTSECRET).
                build();
    }

    @Step
    public AzureCostRequest azureCostRequestBuilder(String granularity, String timeframe, String customTimePeriod,List<String> groupings) throws JsonProcessingException {
        TimePeriod timePeriod = objectMapper.readValue(customTimePeriod, TimePeriod.class);
        return AzureCostRequest.builder().
                tenantId(AZURE_TENANTID).
                subscriptionId(AZURE_SUBSCRIPTIONID).
                clientId(AZURE_CLIENTID).
                clientSecret(AZURE_CLIENTSECRET).
                granularity(granularity).
                timeframe(timeframe).
                timePeriod(timePeriod).
                groupings(groupings).
                build();
    }

    @Step
    public void assertTotalCost(CostResponse costResponse,AzureCostResponse azureCostResponse) {
        LOGGER.info("----aggregate getTotalCost " + costResponse.getTotalCost());
        LOGGER.info("----azure getTotalCost " + azureCostResponse.getTotalCost());
        double buffer = costResponse.getTotalCost() * PERCENTAGE_MATCH_VALUE;

        assertThat(costResponse.getTotalCost(), is(notNullValue()));
        assertThat(azureCostResponse.getTotalCost(), closeTo(costResponse.getTotalCost(), buffer));
    }

    @Step
    public void assertTotalCostToZeroValue(CostResponse costResponse) {
        LOGGER.info("----azure getTotalCost " + costResponse.getTotalCost());
        assertThat(costResponse.getTotalCost(), equalTo(0.0));
    }

    @Step
    public void assertTotalCost(AzureCostResponse azureCostResponse) {
        LOGGER.info("----azure getTotalCost " + azureCostResponse.getTotalCost());
        assertThat(azureCostResponse.getTotalCost(), is(notNullValue()));
    }

    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode statusCode) {
        assertThat(actualStatusCode, equalTo(statusCode.code));
    }

}
