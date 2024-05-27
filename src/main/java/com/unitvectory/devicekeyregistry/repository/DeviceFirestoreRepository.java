package com.unitvectory.devicekeyregistry.repository;

import org.springframework.stereotype.Repository;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.unitvectory.devicekeyregistry.model.DeviceRecord;
import com.unitvectory.devicekeyregistry.model.DeviceStatus;
import reactor.core.publisher.Flux;

@Repository
public interface DeviceFirestoreRepository
        extends DeviceRepository, FirestoreReactiveRepository<DeviceRecord> {

    @Override
    Flux<DeviceRecord> findByDeviceAlias(String deviceAlias);

    @Override
    Flux<DeviceRecord> findByStatus(DeviceStatus status);
}
