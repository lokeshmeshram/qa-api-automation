package com.automation.platform.shared.services.tests;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import static com.automation.platform.shared.services.SAASConstants.*;

@Epic("Observability Framework")
@Feature("Observability Framework feature")
public class ObservabilityTest extends BaseTest {

    private final static Logger LOGGER = Logger.getLogger(ObservabilityTest.class.getName());
    private static CoreV1Api api;
    private static AppsV1Api appsV1Api;
    private static BatchV1Api batchV1Api;

    ObservabilityTest() throws IOException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        api = new CoreV1Api();
        appsV1Api = new AppsV1Api(client);
        batchV1Api = new BatchV1Api(client);
    }

    @DataProvider(name = "kubernetes-data")
    public Object[] getData(Method m) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        V1Pod yamlPod;
        V1Deployment yamlDeployment;
        V1Job yamlJob;
        File file;
        if(m.getName().equalsIgnoreCase("ShouldBeAbleToObservePodCreationAndDeletionEvent"))
        {
             file = new File(classLoader.getResource(KUBERNETES_DATA_PATH+CREATE_DELETE_NGINX_POD+YAML_EXTENSION).getPath());
             yamlPod = (V1Pod) Yaml.load(file);
             LOGGER.info(Yaml.dump(yamlPod));
             return new V1Pod[]{yamlPod};
        }
        else if(m.getName().equalsIgnoreCase("ShouldBeAbleToObserveDeploymentCreationAndDeletionEvent"))
        {
             file = new File(classLoader.getResource(KUBERNETES_DATA_PATH+CREATE_DELETE_NGINX_DEPLOYMENT+YAML_EXTENSION).getPath());
             yamlDeployment = (V1Deployment) Yaml.load(file);
             LOGGER.info(Yaml.dump(yamlDeployment));
             return new V1Deployment[]{yamlDeployment};

        }
        else if(m.getName().equalsIgnoreCase("ShouldBeAbleToObserveJobCreationAndDeletionEvent"))
        {
            file = new File(classLoader.getResource(KUBERNETES_DATA_PATH+CREATE_DELETE_NGINX_JOB+YAML_EXTENSION).getPath());
            yamlJob = (V1Job) Yaml.load(file);
            LOGGER.info(Yaml.dump(yamlJob));
            return new V1Job[]{yamlJob};
        }
        return new Object[0];
    }

    @Story("Observability Framework Test")
    @Description("Observability Framework Test for POD CREATION AND DELETION KUBERNETES EVENT")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "kubernetes-data", groups = {"observabilityFrameworktest", "Positive"})
    public void ShouldBeAbleToObservePodCreationAndDeletionEvent(V1Pod[] yamlPod) {

        try {
            V1Pod createPodResult = api.createNamespacedPod(yamlPod[0].getMetadata().getNamespace(), yamlPod[0], null, null, null);
            LOGGER.info(createPodResult.toString());

            V1Pod deleteResult = api.deleteNamespacedPod(yamlPod[0].getMetadata().getName(), yamlPod[0].getMetadata().getNamespace(), null, null, 10, true, null, null);
            LOGGER.info(deleteResult.toString());
        }catch (ApiException e) {
            e.printStackTrace();
            LOGGER.info("Exception Body" + e.getResponseBody());
        }
    }

    @Description("Observability Framework Test for DEPLOYMENT CREATION AND DELETION KUBERNETES EVENT")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "kubernetes-data", groups = {"observabilityFrameworktest", "Positive"})
    public void ShouldBeAbleToObserveDeploymentCreationAndDeletionEvent(V1Deployment[] yamlDeployment) {

        try {
            V1Deployment createDeploymentResult = appsV1Api.createNamespacedDeployment(
                    yamlDeployment[0].getMetadata().getNamespace(), yamlDeployment[0], null, null, null);
            LOGGER.info(createDeploymentResult.toString());

            V1Status deleteResult= appsV1Api.deleteNamespacedDeployment(yamlDeployment[0].getMetadata().getName(),yamlDeployment[0].getMetadata().getNamespace(),null,null,10,true,null,null);
            LOGGER.info(deleteResult.toString());
        }catch (ApiException e) {
            e.printStackTrace();
            LOGGER.info("Exception Body" + e.getResponseBody());
        }
    }

    @Description("Observability Framework Test for JOB CREATION AND DELETION KUBERNETES EVENT")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "kubernetes-data", groups = {"observabilityFrameworktest", "Positive"})
    public void ShouldBeAbleToObserveJobCreationAndDeletionEvent(V1Job[] yamlJob) {

        try {
            V1Job createJobResult = batchV1Api.createNamespacedJob(yamlJob[0].getMetadata().getNamespace(), yamlJob[0], "true", null, null);
            LOGGER.info(createJobResult.toString());

            V1Status deleteResult = batchV1Api.deleteNamespacedJob(yamlJob[0].getMetadata().getName(),yamlJob[0].getMetadata().getNamespace(),null,null,10,null,null,null);
            LOGGER.info(deleteResult.toString());
        }catch (ApiException e) {
            e.printStackTrace();
            LOGGER.info("Exception Body" + e.getResponseBody());
        }
    }
}
