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

import java.util.function.Consumer;

/**
 * Created by julian3 on 2014/05/03.
 */
public class DefaultContainer extends BasicContainer {

    private Consumer<Container> builder;
    private Log log;

    private DefaultContainer(Consumer<Container> builder) {
        super();
        this.builder = builder;
    }

    public static Container create(Consumer<Container> builder) {
        DefaultContainer defaultContainer = new DefaultContainer(builder);
        defaultContainer.init();
        return defaultContainer;
    }

    @Override
    public void build() {
       this.builder.accept(this);
    }




}
