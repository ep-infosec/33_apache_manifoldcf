/* $Id$ */

/**
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.manifoldcf.crawler.connectors.wiki.tests;

import org.apache.manifoldcf.core.interfaces.*;
import org.apache.manifoldcf.agents.interfaces.*;
import org.apache.manifoldcf.crawler.interfaces.*;
import org.apache.manifoldcf.crawler.system.ManifoldCF;

import java.io.*;
import java.util.*;
import org.junit.*;

import org.apache.manifoldcf.core.tests.SeleniumTester;

/** Basic UI navigation tests */
public class NavigationHSQLDBUI extends BaseUIHSQLDB
{

  protected static Map<String,String> initialCheckResources;
  protected static Map<String,String> initialListResources;
  protected static Map<String,String> initialTimestampQueryResources;
  protected static Map<String,String> initialURLQueryResources;
  protected static Map<String,String> initialDocInfoQueryResources;
  protected static final String namespaceResource = "get_namespaces.xml";

  static
  {
    initialCheckResources = new HashMap<String,String>();
    initialCheckResources.put("","list_one.xml");
    
    initialListResources = new HashMap<String,String>();
    
    initialTimestampQueryResources = new HashMap<String,String>();
    
    initialURLQueryResources = new HashMap<String,String>();
    
    initialDocInfoQueryResources = new HashMap<String,String>();
  }

  @Test
  public void createConnectionsAndJob()
    throws Exception
  {
    // Initialize the mock service
    wikiService.setResources(initialCheckResources,
      initialListResources,
      initialTimestampQueryResources,
      initialURLQueryResources,
      initialDocInfoQueryResources,
      namespaceResource);
    
    testerInstance.start(SeleniumTester.BrowserType.CHROME, "en-US", "http://localhost:8346/mcf-crawler-ui/index.jsp");

    //Login
    testerInstance.waitForElementWithName("loginform");
    testerInstance.setValue("userID","admin");
    testerInstance.setValue("password","admin");
    testerInstance.clickButton("Login");
    testerInstance.verifyHeader("Welcome to Apache ManifoldCF???");
    testerInstance.navigateTo("List output connections");
    testerInstance.clickButton("Add a new output connection");

    // Fill in a name
    testerInstance.waitForElementWithName("connname");
    testerInstance.setValue("connname","MyOutputConnection");

    //Goto to Type tab
    testerInstance.clickTab("Type");

    // Select a type
    testerInstance.waitForElementWithName("classname");
    testerInstance.selectValue("classname","org.apache.manifoldcf.agents.tests.TestingOutputConnector");
    testerInstance.clickButton("Continue");

    // Now save the connection.
    testerInstance.clickButton("Save");
    testerInstance.verifyThereIsNoError();

    // Define a repository connection via the UI
    testerInstance.navigateTo("List repository connections");
    testerInstance.clickButton("Add new connection");

    testerInstance.waitForElementWithName("connname");
    testerInstance.setValue("connname","MyRepositoryConnection");

    // Select a type
    testerInstance.clickTab("Type");
    testerInstance.selectValue("classname","org.apache.manifoldcf.crawler.connectors.wiki.WikiConnector");
    testerInstance.clickButton("Continue");

    // Visit the Throttling tab
    testerInstance.clickTab("Throttling");

    // Visit the rest of the tabs - Email first
    testerInstance.clickTab("Email");
    testerInstance.setValue("email", "foo@bar.com");
    
    // Visit the Server tab
    testerInstance.clickTab("Server");
    testerInstance.setValue("servername", "localhost");
    testerInstance.setValue("serverport", "8089");

    // Save
    testerInstance.clickButton("Save");
    testerInstance.verifyThereIsNoError();
    // Look for "connection working"
    final String pageSource = testerInstance.getRenderedSource();
    if (pageSource.indexOf("Connection working") == -1)
    {
      throw new Exception("Connection working message not there");
    }
    //window = testerInstance.findWindow(null);
    //window.checkMatch(testerInstance.createStringDescription("Connection working"));

    // Create a job
    testerInstance.navigateTo("List jobs");
    //Add a job
    testerInstance.clickButton("Add a new job");
    testerInstance.waitForElementWithName("description");
    //Fill in a name
    testerInstance.setValue("description","MyJob");
    testerInstance.clickTab("Connection");

    // Select the connections
    testerInstance.selectValue("output_connectionname","MyOutputConnection");
    testerInstance.selectValue("output_precedent","-1");
    testerInstance.clickButton("Add output",true);
    testerInstance.waitForElementWithName("connectionname");
    testerInstance.selectValue("connectionname","MyRepositoryConnection");
    
    testerInstance.clickButton("Continue");

    // Visit all the tabs.  Scheduling tab first
    testerInstance.clickTab("Scheduling");
    testerInstance.selectValue("dayofweek","0");
    testerInstance.selectValue("hourofday","1");
    testerInstance.selectValue("minutesofhour","30");
    testerInstance.selectValue("monthofyear","11");
    testerInstance.selectValue("dayofmonth","none");
    testerInstance.setValue("duration","120");
    testerInstance.clickButton("Add Scheduled Time",true);
    testerInstance.waitForElementWithName("editjob");

    // The Namespace and Titles tab
    testerInstance.clickTab("Namespace and Titles");
    testerInstance.clickButton("Add", true);

    // Go back to the Name tab
    testerInstance.clickTab("Name");
    
    // Save the job
    testerInstance.clickButton("Save");
    testerInstance.verifyThereIsNoError();
    
    testerInstance.waitForPresenceById("job");
    String jobID = testerInstance.getAttributeValueById("job","jobid");

    //Navigate to List Jobs
    testerInstance.navigateTo("List jobs");
    testerInstance.waitForElementWithName("listjobs");

    //Delete the job
    testerInstance.clickButtonByTitle("Delete job " + jobID);
    testerInstance.acceptAlert();
    testerInstance.verifyThereIsNoError();

    //Wait for the job to go away
    testerInstance.waitForJobDeleteEN(jobID, 120);

    // Delete the repository connection
    testerInstance.navigateTo("List repository connections");
    testerInstance.clickButtonByTitle("Delete MyRepositoryConnection");
    testerInstance.acceptAlert();

    // Delete the output connection
    testerInstance.navigateTo("List output connections");
    testerInstance.clickButtonByTitle("Delete MyOutputConnection");
    testerInstance.acceptAlert();

  }
  
}
