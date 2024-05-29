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

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import com.unitvectory.devicekeyregistry.repository.DeviceMemoryRepository;
import com.unitvectory.devicekeyregistry.repository.DeviceRepository;

/**
 * The device service implementation test.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class DeviceServiceImplTest {

    @Test
    public void registerDeviceTest() {
        DeviceRepository deviceRepository = new DeviceMemoryRepository();
        EntropyService entropyService = new EntropyServiceTestImpl();
        DeviceServiceImpl deviceService = new DeviceServiceImpl(deviceRepository, entropyService);

        // Testing null pointer validation
        assertThrows(IllegalArgumentException.class, () -> {
            deviceService.registerDevice(null).block();
        });
    }

    @Test
    public void setDeviceStatusTest() {
        DeviceRepository deviceRepository = new DeviceMemoryRepository();
        EntropyService entropyService = new EntropyServiceTestImpl();
        DeviceServiceImpl deviceService = new DeviceServiceImpl(deviceRepository, entropyService);

        // Testing null pointer validation
        assertThrows(IllegalArgumentException.class, () -> {
            deviceService.setDeviceStatus(null, null).block();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            deviceService.setDeviceStatus("foo", null).block();
        });
    }

    @Test
    public void getDeviceTest() {
        DeviceRepository deviceRepository = new DeviceMemoryRepository();
        EntropyService entropyService = new EntropyServiceTestImpl();
        DeviceServiceImpl deviceService = new DeviceServiceImpl(deviceRepository, entropyService);

        // Testing null pointer validation
        assertThrows(IllegalArgumentException.class, () -> {
            deviceService.getDevice(null).block();
        });
    }

    @Test
    public void getDeviceAliasesTest() {
        DeviceRepository deviceRepository = new DeviceMemoryRepository();
        EntropyService entropyService = new EntropyServiceTestImpl();
        DeviceServiceImpl deviceService = new DeviceServiceImpl(deviceRepository, entropyService);

        // Testing null pointer validation
        assertThrows(IllegalArgumentException.class, () -> {
            deviceService.getDeviceAliases(null).next().block();
        });
    }
}
