package com.automation.platform.shared.services;

public class SAASConstants {
    public static final String BASE_PATH = "/v1";
    public static final String API = "/api";
    public static final String TOKEN = "/token";
    public static final String HOSTNAME = "http://localhost";
    public static final String XLSX = ".xlsx";
    public static final String TESTDATA_PKG_PATH = "testdata/";
    public static final String CLOUDTYPE_AWS="aws";
    public static final String CLOUDTYPE_AZURE="azure";
    public static final Double PERCENTAGE_MATCH_VALUE= 0.3;
    public static final String DATABASE_PKG_PATH = "database/";
    public static final String DATABASE_FILENAME = "dbconfig";
    public static final String DATABASE_PROPERTIES_EXTENSION = ".properties";


    // kubecost constants
    public static final String ALLOCATIONREQUEST = "allocationRequest";
    public static final String KUBECOST_FILENAME = "kubecost";
    public static final String ALLOCATION_PATH = "cost/allocation";
    public static final int KUBECOST_APP_HOSTED_PORT = 8080;

    //azurecost constants
    public static final String AZURECOST_FILENAME = "azurecost";
    public static final String TOTALCOST_SHEETNAME = "totalcost";
    public static final String AZURE_TOTALCOST_PATH = "azure/totalcost";
    public static final int AZURE_APP_HOSTED_PORT = 9191;
    public static final String AZURE_TENANTID = System.getenv("AZURE_TENANTID");
    public static final String AZURE_SUBSCRIPTIONID = System.getenv("AZURE_SUBSCRIPTIONID");
    public static final String AZURE_CLIENTID = System.getenv("AZURE_CLIENTID");
    public static final String AZURE_CLIENTSECRET = System.getenv("AZURE_CLIENTSECRET");

    //awscost constants
    public static final String AWSCOST_FILENAME = "awscost";
    public static final String AWS_TOTALCOST_PATH = "aws/totalcost";
    public static final int AWS_APP_HOSTED_PORT = 9292;
    public static final String AWS_ACCESSKEY = System.getenv("AWS_ACCESSKEY");
    public static final String AWS_SECRETKEY = System.getenv("AWS_SECRETKEY");

    //aggregate service constants
    public static final String AGGREGATE_SERVICE_PATH = "cost/totalcost";
    public static final int AGGREGATE_APP_HOSTED_PORT = 9090;

    //observability framework testing constants
    public static final String KUBERNETES_DATA_PATH = "kubernetesdata/";
    public static final String YAML_EXTENSION = ".yml";
    public static final String CREATE_DELETE_NGINX_POD = "create-delete-nginx-pod";
    public static final String CREATE_DELETE_NGINX_DEPLOYMENT = "create-delete-nginx-deployment";
    public static final String CREATE_DELETE_NGINX_JOB = "create-delete-nginx-job";

    //database details
    public static final String DB_USERNAME = System.getenv("DB_USERNAME");
    public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");



}
