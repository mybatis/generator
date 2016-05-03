/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package mbg.test.common;

/**
 * @author Jeff Butler
 *
 */
public class MyTime {
    private int hours;
    private int minutes;
    private int seconds;

    /**
     * 
     */
    public MyTime() {
        super();
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean equals(Object arg0) {
        if (arg0 == null) {
            return false;
        }
        
        MyTime other = (MyTime) arg0;
        
        return this.hours == other.hours
            && this.minutes == other.minutes
            && this.seconds == other.seconds;
    }

    public int hashCode() {
        return hours + minutes + seconds;
    }
}
