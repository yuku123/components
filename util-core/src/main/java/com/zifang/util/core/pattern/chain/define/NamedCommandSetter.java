/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zifang.util.core.pattern.chain.define;

import java.util.Map;

/**
 * Allows adding a command in a {@link org.apache.commons.chain2.Catalog} identified by a name.
 *
 * @param <K> Context key type
 * @param <V> Context value type
 * @param <C> Type of the context associated with this command setter
 * @since 2.0
 * @version $Id$
 */
public interface NamedCommandSetter<K, V, C extends Map<K, V>>
    extends org.apache.commons.chain2.CommandSetter<K, V, C, org.apache.commons.chain2.NameSetter<K, V, C>> {

}
