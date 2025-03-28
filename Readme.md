# SeleniumTest Project

This project is a Selenium-based automation test project using Java, Maven, and TestNG. It automates scenarios on the Demoblaze website such as user sign-up, login, product selection, adding to cart, and purchase flow.

## Prerequisites

- **Java 17 or later** – Ensure Java is installed and JAVA_HOME is set.
- **Maven** – Used to build the project and manage dependencies.
- **Google Chrome** – The browser used for tests.
- **ChromeDriver** – Must match your installed Chrome version.
  - Download ChromeDriver from [ChromeDriver Downloads](https://sites.google.com/chromium.org/driver/downloads)
  - Place the ChromeDriver binary in the `drivers` folder (or update the path in your test code accordingly). (Please note that the chromedriver that included in this project right now have the version 134.0.6998.165 )
- **Internet Connection** – Required to download Maven dependencies. 

## Installing Dependencies

This project uses Maven to manage dependencies. Key dependencies include:
- **Selenium Java**
- **TestNG**
- **Java Faker** (for generating fake data)
- **ExtentReports** (for reporting, if used)
- **Log4j** (for logging)

Maven will download these dependencies automatically when you build the project.

## Building the Project

To build the project, navigate to the project root and run:
```
mvn clean compile
``` 

### Run the Test Cases 

To run the test cases, executed the following command 

``` 
mvn clean test
``` 

### View the Report 

To view the report , navigate to `./Reports/DemoblazeTestReport.html` and open it in a browser 

