package com.automation.platform.shared.services.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.automation.platform.shared.services.api.StatusCode;
import com.automation.platform.shared.services.api.awscost.AWSTotalCostApi;
import com.automation.platform.shared.services.api.common.CommonApi;
import com.automation.platform.shared.services.model.awsdto.AWSCostRequest;
import com.automation.platform.shared.services.model.awsdto.AWSCostResponse;
import com.automation.platform.shared.services.model.common.CostRequest;
import com.automation.platform.shared.services.model.common.CostResponse;
import com.automation.platform.shared.services.model.common.TimePeriod;
import com.automation.platform.shared.services.model.kubecostdto.AllocationRequest;
import com.automation.platform.shared.services.utils.DataHelper;
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

@Epic("AWScost ")
@Feature("AWSCOST API : /aws/totalcost")
public class AWScostTests extends BaseTest {

    AllureLifecycle lifecycle = Allure.getLifecycle();
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Logger LOGGER = Logger.getLogger(AWScostTests.class.getName());

    @DataProvider(name = "awscost-data")
    public Object[][] getData() {
        ClassLoader classLoader = getClass().getClassLoader();
        return DataHelper.data(classLoader.getResource(TESTDATA_PKG_PATH + AWSCOST_FILENAME + XLSX).getPath(), TOTALCOST_SHEETNAME);
    }

    @Story("AWScost API Test")
    @Description("AWScost API Test description")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "awscost-data", groups = {"awscostfunctest", "Positive"})
    public void ShouldBeAbleToGetAwsCostResponse(String testcaseId,String granularity, String timeframe, String customTimePeriod, String groupings,String metric) throws JsonProcessingException {
        CostRequest costRequest = costAWSRequestBuilder(granularity, timeframe, customTimePeriod, StringUtils.manipulateListOfString(groupings),null,metric,CLOUDTYPE_AWS);
        Response response = CommonApi.post(costRequest);

        assertStatusCode(response.statusCode(), StatusCode.CODE_200);

        AWSCostRequest awsCostRequest = awsCostRequestBuilder(granularity, timeframe, customTimePeriod, StringUtils.manipulateListOfString(groupings),metric);
        Response awsresponse = AWSTotalCostApi.post(awsCostRequest);
        assertStatusCode(awsresponse.statusCode(), StatusCode.CODE_200);

        assertTotalCost(response.as(CostResponse.class), awsresponse.as(AWSCostResponse.class));
        lifecycle.updateTestCase(testResult -> testResult.setName("ShouldBeAbleToGetAwsCostResponse"+testcaseId));
    }

    @Description("AWScost API Test for param timeframe: Custom and  timePeriod: where from > To (greater than)")
    @Severity(SeverityLevel.MINOR)
    @Test(groups = {"awscostfunctest", "Negative"})
    public void ShouldNotGetAWSCostResponseForInvalidParamValueTimePeriod() throws JsonProcessingException {

        TimePeriod timePeriod = new TimePeriod();
        Date yesterday = DateUtils.dateValue(-1);
        Date dayBeforeYesterday =  DateUtils.dateValue(-2);
        timePeriod.setFrom(yesterday);
        timePeriod.setTo(dayBeforeYesterday);

        CostRequest costRequest = costAWSRequestBuilder("DAILY", "Custom", timePeriod, null,null,null,CLOUDTYPE_AWS);
        Response response = CommonApi.post(costRequest);

        assertStatusCode(response.statusCode(), StatusCode.CODE_400);

    }

    @Step
    public CostRequest costAWSRequestBuilder(String granularity, String timeframe, TimePeriod customTimePeriod, List<String> groupings, AllocationRequest clusterCost, String metric,String type) {
        return CostRequest.builder().
                granularity(granularity).
                timeframe(timeframe).
                timePeriod(customTimePeriod).
                groupings(groupings).
                clusterCost(clusterCost).
                metric(metric).
                type(type).
                awsAccessKey(AWS_ACCESSKEY).
                awsSecretKey(AWS_SECRETKEY).
                build();
    }

    @Step
    public AWSCostRequest awsCostRequestBuilder(String granularity, String timeframe, String customTimePeriod, List<String> groupings,String metric) throws JsonProcessingException {
        TimePeriod timePeriod = objectMapper.readValue(customTimePeriod, TimePeriod.class);
        return AWSCostRequest.builder().
                awsAccessKey(AWS_ACCESSKEY).
                awsSecretKey(AWS_SECRETKEY).
                granularity(granularity).
                timeframe(timeframe).
                timePeriod(timePeriod).
                groupings(groupings).
                metric(metric).
                build();
    }

    @Step
    public void assertTotalCost(CostResponse costResponse,AWSCostResponse awsCostResponse) {
        LOGGER.info("----aggregate getTotalCost " + costResponse.getTotalCost());
        LOGGER.info("----aws getTotalCost " + awsCostResponse.getTotalCost());
        double buffer = costResponse.getTotalCost() * PERCENTAGE_MATCH_VALUE;

        assertThat(costResponse.getTotalCost(), is(notNullValue()));
        assertThat(awsCostResponse.getTotalCost(), closeTo(costResponse.getTotalCost(), buffer));
    }

    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode statusCode) {
        assertThat(actualStatusCode, equalTo(statusCode.code));
    }

}
