/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ezbilllite;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String EZBILL_SERVICE = "0000f00d-1212-efde-1523-785fef13d123";
    public static String POWER = "00000010-1212-efde-1523-785fef13d123";

    static {
        // Sample Services.
        attributes.put("00000000-1212-efde-1523-785fef13d123", "Power Selection: Click to Select");
        attributes.put("00000001-1212-efde-1523-785fef13d123", "Sensor Status");
        // Sample Characteristics.
        attributes.put(EZBILL_SERVICE, "EZbill service");
        attributes.put("00000010-1212-efde-1523-785fef13d123", "Power Value");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
