/*
Copyright 2014 Julian Exenberger

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/
package org.yadi.core;

/**
 * Created by julian3 on 2014/05/03.
 */
public class AnObject {

    Runnable wired;
    Runnable other;

    public void setWired(Runnable wired) {
        this.wired = wired;
    }

    public Runnable getWired() {
        return wired;
    }

    public Runnable getOther() {
        return other;
    }

    public void setOther(Runnable other) {
        this.other = other;
    }
}
