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
package com.unitvectory.devicekeyregistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.unitvectory.devicekeyregistry.repository.DeviceMemoryRepository;
import com.unitvectory.devicekeyregistry.service.DeviceService;
import com.unitvectory.devicekeyregistry.service.DeviceServiceImpl;
import com.unitvectory.devicekeyregistry.service.EntropyService;
import com.unitvectory.devicekeyregistry.service.EntropyServiceTestImpl;

/**
 * The device key registry test configuration.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Profile("test")
@Configuration
public class DeviceKeyRegistryTestConfig {

    @Bean
    public DeviceMemoryRepository deviceMemoryRepository() {
        return new DeviceMemoryRepository();
    }

    @Bean
    public DeviceService deviceService(DeviceMemoryRepository deviceMemoryRepository,
            EntropyService entropyService) {
        return new DeviceServiceImpl(deviceMemoryRepository, entropyService);
    }

    @Bean
    public EntropyService entropyService() {
        return new EntropyServiceTestImpl();
    }
}
