/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.unitvectory.devicekeyregistry;

import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unitvectory.fileparamunit.ListFileSource;


/**
 * The device key registry test.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeviceKeyRegistryTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebTestClient webTestClient;

    @ParameterizedTest
    @ListFileSource(resources = "/resttest/", fileExtension = ".json", recurse = true)
    public void testIt(String file) throws Exception {

        // Load the file in using the ObjectMapper

        JsonNode node = objectMapper.readTree(new File(file));

        String verb = node.get("verb").asText();
        String path = node.get("path").asText();
        int expectedStatusCode = node.get("expectedStatusCode").asInt();
        JsonNode expectedResponse = node.get("expectedResponse");

        System.out.println("foo");

        if ("GET".equals(verb)) {

            webTestClient.get().uri(path).accept(MediaType.APPLICATION_JSON).exchange()
                    .expectStatus().isEqualTo(expectedStatusCode).expectBody(JsonNode.class)
                    .isEqualTo(expectedResponse);
        } else {
            fail("Unknown verb: " + verb);
        }
    }


}
