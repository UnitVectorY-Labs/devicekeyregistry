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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.unitvectory.devicekeyregistry.mapper.DeviceRecordMapper;
import com.unitvectory.devicekeyregistry.model.ActivateRequest;
import com.unitvectory.devicekeyregistry.model.DeviceResponse;
import com.unitvectory.devicekeyregistry.service.DeviceService;
import com.unitvectory.jsonschema4springboot.ValidateJsonSchema;
import com.unitvectory.jsonschema4springboot.ValidateJsonSchemaVersion;
import lombok.AllArgsConstructor;

/**
 * The device alias resource.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@RestController
@AllArgsConstructor
public class DeviceResource {

    private DeviceService deviceService;

    @GetMapping("/v1/device/{deviceId}")
    public DeviceResponse getDevice(@RequestParam("deviceId") String deviceId) {
        return DeviceRecordMapper.INSTANCE.toDeviceResponse(deviceService.getDevice(deviceId));
    }

    @PutMapping("/v1/device/{deviceId}/activate")
    public DeviceResponse activateDevice(@RequestParam("deviceAlias") String deviceId,
            @ValidateJsonSchema(version = ValidateJsonSchemaVersion.V7,
                    schemaPath = "classpath:schema/activateDevice.json") ActivateRequest request) {
        return DeviceRecordMapper.INSTANCE.toDeviceResponse(deviceService.activateDevice(deviceId));
    }

    @PutMapping("/v1/device/{deviceId}/deactivate")
    public DeviceResponse deactivateDevice(@RequestParam("deviceAlias") String deviceId,
            @ValidateJsonSchema(version = ValidateJsonSchemaVersion.V7,
                    schemaPath = "classpath:schema/deactivateDevice.json") ActivateRequest request) {
        return DeviceRecordMapper.INSTANCE
                .toDeviceResponse(deviceService.deactivateDevice(deviceId));
    }
}
