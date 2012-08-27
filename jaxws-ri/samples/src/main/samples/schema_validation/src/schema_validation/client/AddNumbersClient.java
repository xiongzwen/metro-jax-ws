/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package schema_validation.client;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import com.sun.xml.ws.developer.SchemaValidationFeature;

/**
 * Schema Validation sample
 *
 * @author Jitendra Kotamraju
 */

public class AddNumbersClient {

    public static void main (String[] args) {
        AddNumbersService service = new AddNumbersService();
        testServerValidationFailure(service);
        testClientValidationFailure(service);
        testClientServerValidationPass(service);
    }

    public static void testServerValidationFailure(AddNumbersService service) {
        AddNumbersPortType port = service.getAddNumbersPort ();
        int number1 = 10001;     // more than 4 digits, so doesn't pass validation
        int number2 = 20;
        try {
            int result = port.addNumbers (number1, number2);
            throw new RuntimeException("Server Schema Validation didn't work");
        } catch(SOAPFaultException se) {
            System.out.println("Success: Server side validation failed as expected");
        }
    }

    private static void testClientValidationFailure(AddNumbersService service) {

        SchemaValidationFeature feature = new SchemaValidationFeature();
        AddNumbersPortType port = service.getAddNumbersPort(feature);
        int number1 = 10001;     // more than 4 digits, so doesn't pass validation
        int number2 = 20;
        try {
            int result = port.addNumbers (number1, number2);
            throw new RuntimeException("Client Schema Validation didn't work");
        } catch(SOAPFaultException se) {
            throw new RuntimeException("Client Schema Validation didn't work");
        } catch(WebServiceException se) {
            System.out.println("Success: Client side validation failed as expected");
        }
    }

    private static void testClientServerValidationPass(AddNumbersService service) {
        SchemaValidationFeature feature = new SchemaValidationFeature();
        AddNumbersPortType port = service.getAddNumbersPort(feature);
        int number1 = 1000;
        int number2 = 20;
        int result = port.addNumbers (number1, number2);
        System.out.println("Success: Both Client and Server validation passed.");
    }

}
