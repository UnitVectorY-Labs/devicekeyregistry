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
package com.unitvectory.devicekeyregistry.service;

import org.springframework.stereotype.Service;
import com.unitvectory.devicekeyregistry.exception.RecordNotFoundException;
import com.unitvectory.devicekeyregistry.model.DeviceRecord;
import com.unitvectory.devicekeyregistry.model.DeviceStatus;
import com.unitvectory.devicekeyregistry.model.KeyType;
import com.unitvectory.devicekeyregistry.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The device service.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@AllArgsConstructor
@Service
public class DeviceService {

    private DeviceRepository deviceRepository;

    private EntropyService entropyService;

    public Mono<DeviceRecord> registerDevice(String deviceAlias, KeyType keyType,
            String publicKey) {
        return Mono.just(DeviceRecord.builder().deviceId(entropyService.newDeviceId())
                .status(DeviceStatus.PENDING).deviceAlias(deviceAlias).keyType(keyType)
                .publicKey(publicKey).build()).flatMap(deviceRepository::save);
    }

    public Mono<DeviceRecord> activateDevice(String deviceId) {
        return getDevice(deviceId)
                .switchIfEmpty(Mono.error(
                        new RecordNotFoundException("Device with ID " + deviceId + " not found")))
                .flatMap(device -> {
                    device.setStatus(DeviceStatus.ACTIVE);
                    return deviceRepository.save(device);
                });
    }

    public Mono<DeviceRecord> deactivateDevice(String deviceId) {
        return getDevice(deviceId)
                .switchIfEmpty(Mono.error(
                        new RecordNotFoundException("Device with ID " + deviceId + " not found")))
                .flatMap(device -> {
                    device.setStatus(DeviceStatus.INACTIVE);
                    return deviceRepository.save(device);
                });
    }

    public Mono<DeviceRecord> getDevice(String deviceId) {
        if (deviceId == null) {
            return Mono.error(new IllegalArgumentException("Device ID cannot be null"));
        }

        return this.deviceRepository.findById(deviceId)
                .switchIfEmpty(Mono.error(new RecordNotFoundException("Device record not found")))
                .onErrorMap(RecordNotFoundException.class, e -> e);
    }

    public Flux<DeviceRecord> getDeviceAliases(String deviceAlias) {
        if (deviceAlias == null) {
            return Flux.error(new IllegalArgumentException("Device alias cannot be null"));
        }

        return this.deviceRepository.findByDeviceAlias(deviceAlias);
    }

    public Flux<DeviceRecord> getPendingDevices() {
        return deviceRepository.findByStatus(DeviceStatus.PENDING);
    }
}