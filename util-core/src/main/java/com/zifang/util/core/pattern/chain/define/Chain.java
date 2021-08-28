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
 * <p>A {@link Chain} represents a configured list of
 * {@link org.apache.commons.chain2.Command}s that will be executed in order to perform processing
 * on a specified {@link org.apache.commons.chain2.Context}.  Each included {@link org.apache.commons.chain2.Command} will be
 * executed in turn, until either one of them returns <code>FINISHED</code>,
 * one of the executed {@link org.apache.commons.chain2.Command}s throws an exception,
 * or the end of the chain has been reached.  The {@link Chain} itself will
 * return the return value of the last {@link org.apache.commons.chain2.Command} that was executed
 * (if no exception was thrown), or rethrow the thrown exception.</p>
 *
 * <p>Note that {@link Chain} extends {@link org.apache.commons.chain2.Command}, so that the two can
 * be used interchangeably when a {@link org.apache.commons.chain2.Command} is expected.  This makes it
 * easy to assemble workflows in a hierarchical manner by combining subchains
 * into an overall processing chain.</p>
 *
 * <p>To protect applications from evolution of this interface, specialized
 * implementations of {@link Chain} should generally be created by extending
 * the provided base class {@code org.apache.commons.chain2.impl.ChainBase})
 * rather than directly implementing this interface.</p>
 *
 * <p>{@link Chain} implementations should be designed in a thread-safe
 * manner, suitable for execution on multiple threads simultaneously.  In
 * general, this implies that the state information identifying which
 * {@link org.apache.commons.chain2.Command} is currently being executed should be maintained in a
 * local variable inside the <code>execute()</code> method, rather than
 * in an instance variable.  The {@link org.apache.commons.chain2.Command}s in a {@link Chain} may be
 * configured (via calls to <code>addCommand()</code>) at any time before
 * the <code>execute()</code> method of the {@link Chain} is first called.
 * After that, the configuration of the {@link Chain} is frozen.</p>
 *
 * @param <K> Context key type
 * @param <V> Context value type
 * @param <C> Type of the context associated with this chain
 *
 * @version $Id$
 */
public interface Chain<K, V, C extends Map<K, V>> extends org.apache.commons.chain2.Command<K, V, C> {

    /**
     * <p>Add a {@link org.apache.commons.chain2.Command} to the list of {@link org.apache.commons.chain2.Command}s that will
     * be called in turn when this {@link Chain}'s <code>execute()</code>
     * method is called.  Once <code>execute()</code> has been called
     * at least once, it is no longer possible to add additional
     * {@link org.apache.commons.chain2.Command}s; instead, an exception will be thrown.</p>
     *
     * @param <CMD> the {@link org.apache.commons.chain2.Command} type to be added in the {@link Chain}
     * @param command The {@link org.apache.commons.chain2.Command} to be added
     *
     * @throws IllegalArgumentException if <code>command</code>
     *  is <code>null</code>
     * @throws IllegalStateException if this {@link Chain} has already
     *  been executed at least once, so no further configuration is allowed
     */
    <CMD extends org.apache.commons.chain2.Command<K, V, C>> void addCommand(CMD command);

    /**
     * <p>Execute the processing represented by this {@link Chain} according
     * to the following algorithm.</p>
     * <ul>
     * <li>If there are no configured {@link org.apache.commons.chain2.Command}s in the {@link Chain},
     *     return <code>CONTINUE</code>.</li>
     * <li>Call the <code>execute()</code> method of each {@link org.apache.commons.chain2.Command}
     *     configured on this chain, in the order they were added via calls
     *     to the <code>addCommand()</code> method, until the end of the
     *     configured {@link org.apache.commons.chain2.Command}s is encountered, or until one of
     *     the executed {@link org.apache.commons.chain2.Command}s returns <code>FINISHED</code>
     *     or throws an exception.</li>
     * <li>Walk backwards through the {@link org.apache.commons.chain2.Command}s whose
     *     <code>execute()</code> methods, starting with the last one that
     *     was executed.  If this {@link org.apache.commons.chain2.Command} instance is also a
     *     {@link org.apache.commons.chain2.Filter}, call its <code>postprocess()</code> method,
     *     discarding any exception that is thrown.</li>
     * <li>If the last {@link org.apache.commons.chain2.Command} whose <code>execute()</code> method
     *     was called threw an exception, rethrow that exception.</li>
     * <li>Otherwise, return the value returned by the <code>execute()</code>
     *     method of the last {@link org.apache.commons.chain2.Command} that was executed.  This will be
     *     <code>FINISHED</code> if the last {@link org.apache.commons.chain2.Command} indicated that
     *     processing of this {@link org.apache.commons.chain2.Context} has been completed, or
     *     <code>CONTINUE</code> if none of the called {@link org.apache.commons.chain2.Command}s
     *     returned <code>FINISHED</code>.</li>
     * </ul>
     *
     * @param context The {@link org.apache.commons.chain2.Context} to be processed by this
     *  {@link Chain}
     *
     * @throws IllegalArgumentException if <code>context</code>
     *  is <code>null</code>
     *
     * @return {@link org.apache.commons.chain2.Processing#FINISHED} if the processing of this context
     *  has been completed. Returns {@link org.apache.commons.chain2.Processing#CONTINUE} if the processing
     *  of this context should be delegated to a subsequent command in an 
     *  enclosing chain.
     */
    @Override
    org.apache.commons.chain2.Processing execute(C context);

}
