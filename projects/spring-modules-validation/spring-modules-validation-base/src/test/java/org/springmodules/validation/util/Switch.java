/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.validation.util;

/**
 * Represents a switch that can be truned on and off.
 * <p/>
 * This switch can be used by test cases that require overriding protected methods to verify that indeed
 * the methods are called.
 *
 * @author Uri Boness
 */
public class Switch {

    private boolean on = false;

    /**
     * Returns whether the switch is on or off.
     *
     * @return Whether the switch is on or off.
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Turns on the switch.
     */
    public void turnOn() {
        on = true;
    }

    /**
     * Turns off the switch.
     */
    public void turnOff() {
        on = false;
    }

}
