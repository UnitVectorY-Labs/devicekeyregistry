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
import com.unitvectory.devicekeyregistry.repository.DeviceFirestoreRepository;
import com.unitvectory.devicekeyregistry.service.DeviceService;
import com.unitvectory.devicekeyregistry.service.DeviceServiceImpl;
import com.unitvectory.devicekeyregistry.service.EntropyService;
import com.unitvectory.devicekeyregistry.service.EntropyServiceImpl;
import lombok.AllArgsConstructor;

/**
 * The device key registry configuration.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Profile("!test")
@Configuration
@AllArgsConstructor
public class DeviceKeyRegistryConfig {

    private DeviceFirestoreRepository deviceRepository;

    @Bean
    public DeviceService deviceService(EntropyService entropyService) {
        return new DeviceServiceImpl(deviceRepository, entropyService);
    }

    @Bean
    public EntropyService entropyService() {
        return new EntropyServiceImpl();
    }
}
