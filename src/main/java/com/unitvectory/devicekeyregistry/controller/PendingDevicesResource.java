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
import org.springframework.web.bind.annotation.RestController;
import com.unitvectory.devicekeyregistry.mapper.DeviceRecordMapper;
import com.unitvectory.devicekeyregistry.model.DevicesResponse;
import com.unitvectory.devicekeyregistry.service.DeviceService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * The pending devices resource.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@RestController
@AllArgsConstructor
public class PendingDevicesResource {

    private DeviceService deviceService;

    @GetMapping(path = "/v1/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<DevicesResponse> getPendingDevices() {
        return deviceService.getPendingDevices().collectList()
                .map(DeviceRecordMapper.INSTANCE::toDeviceResponseList)
                .map(devices -> DevicesResponse.builder().devices(devices).build());
    }
}
