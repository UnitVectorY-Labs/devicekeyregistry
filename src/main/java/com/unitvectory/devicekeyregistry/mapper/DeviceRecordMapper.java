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
package com.unitvectory.devicekeyregistry.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.unitvectory.devicekeyregistry.model.DeviceRecord;
import com.unitvectory.devicekeyregistry.model.DeviceResponse;

/**
 * The device record mapper.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Mapper
public interface DeviceRecordMapper {

    DeviceRecordMapper INSTANCE = Mappers.getMapper(DeviceRecordMapper.class);

    @Mapping(target = "deviceId", source = "deviceId")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "deviceAlias", source = "deviceAlias")
    @Mapping(target = "keyType", source = "keyType")
    @Mapping(target = "publicKey", source = "publicKey")
    DeviceResponse toDeviceResponse(DeviceRecord record);

    List<DeviceResponse> toDeviceResponseList(List<DeviceRecord> list);
}
