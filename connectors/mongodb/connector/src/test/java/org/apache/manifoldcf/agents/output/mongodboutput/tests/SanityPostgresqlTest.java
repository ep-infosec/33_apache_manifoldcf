/* $Id: SanityPostgresqlTest.java 1800083 2017-06-27 19:55:21Z piergiorgio $ */

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.manifoldcf.agents.output.mongodboutput.tests;

import org.junit.Test;

/** This is a very basic sanity check */
public class SanityPostgresqlTest extends BasePostgresql {

    @Test
    public void sanityCheck()
            throws Exception {
        // If we get this far, it must mean that the setup was successful, which is all that I'm shooting for in this test.
    }


}
