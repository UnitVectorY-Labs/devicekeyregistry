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
package com.unitvectory.devicekeyregistry.repository;

import java.util.HashMap;
import java.util.Map;
import com.unitvectory.devicekeyregistry.mapper.DeviceRecordMapper;
import com.unitvectory.devicekeyregistry.model.DeviceRecord;
import com.unitvectory.devicekeyregistry.model.DeviceStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The device memory repository.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class DeviceMemoryRepository implements DeviceRepository {

    private Map<String, DeviceRecord> records = new HashMap<>();

    public void clear() {
        records.clear();
    }

    @Override
    public <S extends DeviceRecord> Mono<S> save(S record) {
        // Save the record to the memory repository
        @SuppressWarnings("unchecked")
        S recordCopy = (S) DeviceRecordMapper.INSTANCE.clone(record);
        String deviceId = record.getDeviceId();
        records.put(deviceId, recordCopy);
        return Mono.just(recordCopy);
    }

    @Override
    public Mono<DeviceRecord> findById(String id) {
        // Find the record by the device ID
        DeviceRecord record = records.get(id);
        if (record == null) {
            return Mono.empty();
        }

        return Mono.just(DeviceRecordMapper.INSTANCE.clone(record));
    }

    @Override
    public Flux<DeviceRecord> findByDeviceAlias(String deviceAlias) {
        // Loop through all the records and find the ones that match the device alias
        return Flux.fromIterable(records.values())
                .filter(record -> deviceAlias.equals(record.getDeviceAlias()))
                .map(DeviceRecordMapper.INSTANCE::clone);
    }

    @Override
    public Flux<DeviceRecord> findByStatus(DeviceStatus status) {
        // Loop through all the records and find the ones that match the status
        return Flux.fromIterable(records.values())
                .filter(record -> status.equals(record.getStatus()))
                .map(DeviceRecordMapper.INSTANCE::clone);
    }
}
