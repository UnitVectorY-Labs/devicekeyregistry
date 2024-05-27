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
package com.unitvectory.devicekeyregistry.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import com.unitvectory.devicekeyregistry.mapper.DeviceRecordMapper;
import com.unitvectory.devicekeyregistry.model.ActivateRequest;
import com.unitvectory.devicekeyregistry.model.DeactivateRequest;
import com.unitvectory.devicekeyregistry.model.DeviceResponse;
import com.unitvectory.devicekeyregistry.service.DeviceService;
import com.unitvectory.jsonschema4springboot.ValidateJsonSchema;
import com.unitvectory.jsonschema4springboot.ValidateJsonSchemaVersion;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * The device alias resource.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@RestController
@AllArgsConstructor
public class DeviceResource {

        private final DeviceRecordMapper deviceRecordMapper = DeviceRecordMapper.INSTANCE;

        private DeviceService deviceService;

        @GetMapping(path = "/v1/device/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public Mono<DeviceResponse> getDevice(@PathVariable("deviceId") String deviceId) {
                return deviceService.getDevice(deviceId).map(deviceRecordMapper::toDeviceResponse);
        }

        @PutMapping(path = "/v1/device/{deviceId}/activate",
                        consumes = MediaType.APPLICATION_JSON_VALUE,
                        produces = MediaType.APPLICATION_JSON_VALUE)
        public Mono<DeviceResponse> activateDevice(@PathVariable("deviceId") String deviceId,
                        @ValidateJsonSchema(version = ValidateJsonSchemaVersion.V7,
                                        schemaPath = "classpath:schema/activateDevice.json") ActivateRequest request) {
                return deviceService.activateDevice(deviceId, request)
                                .map(deviceRecordMapper::toDeviceResponse);
        }

        @PutMapping(path = "/v1/device/{deviceId}/deactivate",
                        consumes = MediaType.APPLICATION_JSON_VALUE,
                        produces = MediaType.APPLICATION_JSON_VALUE)
        public Mono<DeviceResponse> deactivateDevice(@PathVariable("deviceId") String deviceId,
                        @ValidateJsonSchema(version = ValidateJsonSchemaVersion.V7,
                                        schemaPath = "classpath:schema/deactivateDevice.json") DeactivateRequest request) {
                return deviceService.deactivateDevice(deviceId, request)
                                .map(deviceRecordMapper::toDeviceResponse);
        }
}
