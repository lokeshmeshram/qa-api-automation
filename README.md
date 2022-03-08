# shared-service-automation
REST Assured API test automation framework using Java + Maven + TestNG.
Framework follows many of the industry best practices.

Technologies/Tools used in building the framework
=================================================
- Rest Assured
- TestNG
- Java
- Allure Reports
- Hamcrest
- Jackson API
- Lombok
- GitHub

shared-service-automation framework Build & Test Execution Details using the command line :
=================================================

****Pre-requisites :**** 

Following credentials need to be exported before running framework execution : 
  
- For `aws` cloud provider

  `export ACCESS_KEY=<ACCESS_KEY>;`

  `export SECRET_KEY=<SECRET_KEY>;`


- For `azure` cloud provider

  `export AZURE_TENANTID=<AZURE_TENANTID>;`

  `export AZURE_SUBSCRIPTIONID=<AZURE_SUBSCRIPTIONID>;`

   ` export AZURE_CLIENTID=<AZURE_CLIENTID>;`

  `export AZURE_CLIENTSECRET=<AZURE_CLIENTSECRET>;`


- DB credentials

  `export DB_USERNAME=<DB_USERNAME>;`

  `export DB_PASSWORD=<DB_PASSWORD>;`


- Prior to running kubernetes test cases (e.g. ObservabilityTest class) kubernetes cluster should be connected. For local test execution ,cluster connected via command : `export KUBECONFIG=<path_to_kubeconfig_file>` 
  
**Steps for Build & Test Execution**

- Checkout Automation Project from source repository : `git clone git@github.com:lumada-saas/shared-service-automation.git`
- Build Automation Project Using maven command on root directory : `mvn clean install -DskipTests`
- Once Build is successful , run automation tests using maven command on root directory : `mvn clean test` 

This project support TestNG XML file for running tests . All Suite mentioned in TestNG XML file get executed .

shared-service-automation Result &  Report Details:
=================================================

This framework support default TestNG report and customised allure report. 

- Default TestNG report location : `<project_dir>/target/surefire-reports/index.html`

- For Generating Customise Allure report execute allure command : `allure generate target/allure-results --clean -o target/saas-allure-report`.

    This will generate customised allure report on location : `<project_dir>/target/saas-allure-report/index.html`

Framework implements below best practices
=========================================
- Scalable and extensible
- Reusable Rest Assured specifications
- Reusable Rest Assured API requests
- Separation of API layer from test layer
- POJOs for Serialization and Deserialization
- Singleton Design Pattern
- Lombok for reducing Boilerplate code
- Builder pattern for Setter methods in POJOs
- Robust reporting and logging using Allure
- Automate positive and negative scenarios
- Support parallel execution
- Data driven using TestNG Data Provider
- Automated access token renewal
- Maven command line execution
- Integration with Git
