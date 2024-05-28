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
import java.util.Set;
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
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.ValidationMessage;
import com.unitvectory.devicekeyregistry.repository.DeviceMemoryRepository;
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

    @Autowired
    private DeviceMemoryRepository deviceMemoryRepository;

    private final JsonSchema schema;

    public DeviceKeyRegistryTest() {
        super();

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(VersionFlag.V7);
        this.schema = factory.getSchema(SchemaLocation.of("classpath:testschema.json"));

    }

    @ParameterizedTest
    @ListFileSource(resources = "/resttest/", fileExtension = ".json", recurse = true)
    public void testIt(String file) throws Exception {

        // Clear the device memory repository before each test
        this.deviceMemoryRepository.clear();

        // Load the file in using the ObjectMapper
        JsonNode node = objectMapper.readTree(new File(file));

        Set<ValidationMessage> schemaValidationMessages = this.schema.validate(node);
        if (schemaValidationMessages.size() > 0) {
            // Build a string with all of the failure messages
            StringBuilder sb = new StringBuilder();
            for (ValidationMessage message : schemaValidationMessages) {
                sb.append(message.getMessage());
                sb.append("\n");
            }

            fail("Test schema validation failed: " + sb.toString());
        }

        // Get JsonNode from "requests" array in node and loop through each request
        JsonNode requests = node.get("requests");
        for (JsonNode request : requests) {


            // Pull out the fields from the test case file
            String verb = request.get("verb").asText();
            String path = request.get("path").asText();
            int expectedStatusCode = request.get("expectedStatusCode").asInt();
            JsonNode expectedResponse = request.get("expectedResponse");

            String requestBody = null;
            if (request.has("requestBody")) {
                requestBody = objectMapper.writeValueAsString(request.get("requestBody"));
            }

            // Perform the test

            if ("GET".equals(verb)) {
                // GET
                webTestClient.get().uri(path).accept(MediaType.APPLICATION_JSON).exchange()
                        .expectStatus().isEqualTo(expectedStatusCode).expectBody(JsonNode.class)
                        .isEqualTo(expectedResponse);
            } else if ("POST".equals(verb)) {
                // POST
                webTestClient.post().uri(path).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange()// .expectStatus().isEqualTo(expectedStatusCode)
                        .expectBody(JsonNode.class).isEqualTo(expectedResponse);
            } else if ("PUT".equals(verb)) {
                // PUT
                webTestClient.put().uri(path).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange()
                        .expectStatus().isEqualTo(expectedStatusCode).expectBody(JsonNode.class)
                        .isEqualTo(expectedResponse);
            } else {
                fail("Unknown verb: " + verb);
            }
        }
    }
}
