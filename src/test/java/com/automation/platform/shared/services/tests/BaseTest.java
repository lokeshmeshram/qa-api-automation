package com.automation.platform.shared.services.tests;

import com.automation.platform.shared.services.SAASConstants;
import com.automation.platform.shared.services.model.common.TimePeriod;
import com.automation.platform.shared.services.model.kubecostdto.AllocationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.automation.platform.shared.services.model.common.CostRequest;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

public abstract class BaseTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Logger LOGGER = Logger.getLogger(BaseTest.class.getName());

    @BeforeMethod
    public void beforeMethod(Method m){
        LOGGER.info("STARTING TEST: " + m.getName());
        LOGGER.info("THREAD ID: " + Thread.currentThread().getId());
    }

    @Step
    public CostRequest costAzureRequestBuilder(String granularity, String timeframe, String customTimePeriod, List<String> groupings, AllocationRequest clusterCost, String type) throws JsonProcessingException {
        TimePeriod timePeriod = objectMapper.readValue(customTimePeriod, TimePeriod.class);
        return CostRequest.builder().
                granularity(granularity).
                timeframe(timeframe).
                timePeriod(timePeriod).
                groupings(groupings).
                clusterCost(clusterCost).
                type(type).
                tenantId(SAASConstants.AZURE_TENANTID).
                subscriptionId(SAASConstants.AZURE_SUBSCRIPTIONID).
                clientId(SAASConstants.AZURE_CLIENTID).
                clientSecret(SAASConstants.AZURE_CLIENTSECRET).
                build();
    }

    @Step
    public CostRequest costAWSRequestBuilder(String granularity, String timeframe, String customTimePeriod, List<String> groupings, AllocationRequest clusterCost, String metric,String type) throws JsonProcessingException {
        TimePeriod timePeriod = objectMapper.readValue(customTimePeriod, TimePeriod.class);
        return CostRequest.builder().
                granularity(granularity).
                timeframe(timeframe).
                timePeriod(timePeriod).
                groupings(groupings).
                clusterCost(clusterCost).
                metric(metric).
                type(type).
                awsAccessKey(SAASConstants.AWS_ACCESSKEY).
                awsSecretKey(SAASConstants.AWS_SECRETKEY).
                build();
    }
}
