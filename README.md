# Simba - Playwright Automation Framework

Simba is Java based clean code playwright automation framework architected using several useful design patterns.

## Software Dependencies

<table>
  <thead align="left">
    <tr border: 2 px;>
      <td><b>Dependency</b></td>
      <td><b>Version</b></td>
      <td><b>Feature</b></td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><b>Playwright</b></td>
      <td><b>1.28.0</b></td>
      <td><b>End to End Automation - Browser & API</b></td>
    </tr>
    <tr>
      <td><b>TestNG</b></td>
      <td><b>7.4.0</b></td>
      <td><b>The Test Runner to execute suite</b></td>
    </tr>
    <tr>
      <td><b>JSON</b></td>
      <td><b>20220924</b></td>
      <td><b>Create & Parse JSON Files for API</b></td>
    </tr>
    <tr>
      <td><b>Apache POI</b></td>
      <td><b>5.2.3</b></td>
      <td><b>Read Excel files for test data</b></td>
    </tr>
    <tr>
      <td><b>Data Faker</b></td>
      <td><b>1.6.0</b></td>
      <td><b>Create runtime fake test data</b></td>
    </tr>
    <tr>
      <td><b>Owner</b></td>
      <td><b>1.0.12</b></td>
      <td><b>Minimize the properties file code</b></td>
    </tr>
    <tr>
      <td><b>Extent Reports</b></td>
      <td><b>3.1.5</b></td>
      <td><b>The HTML reporting library</b></td>
    </tr>
  </tbody>
</table>

## Design Pattern Used

 * <b>Factory Pattern</b> is used to reuse existing browser contexts instead of rebuilding them each time.
 * <b>Bridge pattern</b> is used to switch implementations between UI and API at runtime.
 * <b>Decorator pattern</b> is used to assign extra behaviors like highlight, retry .. to pages & elements at runtime without breaking the code that uses these objects.
 * <b>Chain of Responsibility pattern</b> is used to navigate from a pattern/page to other.
 * <b>Observer pattern</b> is used to switch to different data, browser on failure when retried.

## Framework - How to design your new test?

* <b>Step 1:</b> Use the main source for the framework desaign.
* <b>Step 2:</b> Refer the test source for the sample test code (Salesforce).
* <b>Step 3:</b> Create your own app instance url and update the config.
* <b>Step 4:</b> Build your pages like the sample using the right method and locator.
* <b>Step 5:</b> Use the test data in your tests from faker or API or excel.
* <b>Step 6:</b> Build your tests using the pages
* <b>Step 7:</b> Once ready, run in the debug mode and look at the logs for details

## Amazing Usecases that you should try !

<details><summary>✅  Single framework for both UI 🧭 and API Automation</summary>
<ul>
 </br>
<li>
    &emsp;Single framework</b> designed using bridge pattern to allow conversation between UI and API simultaneously.
</li>
<li>
    &emsp;You can create data using API and use that data to your UI tests to make your tests independent.
</li>
<li>
    &emsp;Your UI tests can execute the test and as part of the assertions, it make sense to validate using API.
</li>
  </br>
  

</ul>
</details>

<details><summary>✅ Fastest test execution 🚀 with configurable speed control</summary>
<ul>
   </br>
   <li>Playwright engine is comparatively (above 30% on average) faster than other UI automation frameworks.
   </li>
   <li>Sometimes it requires a slow down to eliminate the script flakiness with configurable speed included through the listeners.
   </li>
  </br>
  
  ```java
    // Sample code to control your delays
    setSlowMo(ConfigurationManager.configuration().slowMotion());
  ```
  
</ul>
</details>

<details><summary>✅  Debug Faster with Snaps, Videos 🎥 and Tracings with configurable options</summary>
<ul>
 </br>
<li>
    &emsp; Playwright library</b> provides full/partial snaps, videos (webm) and trace viewer that captures network calls.
</li>
<li>
    &emsp; Our framework allows configuration for framework user to either plugin on demand for every run or failures.
</li>
  </br>
  
  ```java
    // Sample code to control your delays
    setRecordVideoDir(Paths.get(folderName));
  ```

</ul>
</details>
<details><summary>✅  Automated logins 💡 to avoid too many login tests</summary>
<ul>
 </br>
<li>
    &emsp;Configurable automated logins</b> can avoid unnecessary login tests through storing the state of the user.
</li>
<li>
    &emsp;The user can either use the existing login storage or decide to login automated through configuration.
</li>
  </br>

  ```java
  # Auto Login
  auto.login = true
  ```
</ul>
</details>
<details><summary>✅  Automated retries 🔁 with different browser and/or data</summary>
<ul>
 </br>
<li>
    &emsp;Configurable retries</b> with different data using the TestNG listener upon failure of the earlier data.
</li>
<li>
     &emsp;Configurable retries</b> with different browser using the TestNG listener upon failure of earlier browser.
</li>
  </br>

  ```java
  # Retry Switch
  retry.data.switch = true
  retry.browser.switch = false
  ```
</ul>
</details>

## Framework - How does it execute tests?

* <b>Step 01:</b> You run your designed testng xml with listeners.
* <b>Step 02:</b> The testng annotations (@Before) initialize the setup.
* <b>Step 03:</b> The playwright script invokes the browser and actions.
* <b>Step 04:</b> Simulatenously, the reporting engine captures the result.
* <b>Step 05:</b> Upon success completion, the videos, reports are published.
* <b>Step 06:</b> Upon failure, the testng retry is invoked with different data/browser.
* <b>Step 07:</b> Step 02 - 06 continues until success or max retries.
* <b>Step 08:</b> Look at the results folder for html results.
* <b>Step 09:</b> Look at the videos for the exact details.
* <b>Step 10:</b> Share the traceviewer results to your developer through defect log.

## 2023 Roadmap !

* <b>Single framework</b> to switch from Playwright to Selenium or vice versa.
* <b>Defect Management</b> tools integration LIKE Jira and users choice.

## License

The project is [MIT](./LICENSE2) licensed.


