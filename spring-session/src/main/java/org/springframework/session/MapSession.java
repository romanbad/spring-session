/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.session;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * <p>
 * A {@link Session} implementation that is backed by a {@link java.util.Map}. The defaults for the properties are:
 * </p>
 * <ul>
 *     <li>id - a secure random generated id</li>
 *     <li>creationTime - the moment the {@link MapSession} was instantiated</li>
 *     <li>lastAccessedTime - the moment the {@link MapSession} was instantiated</li>
 *     <li>maxInactiveInterval - 30 minutes</li>
 * </ul>
 *
 * <p>
 * This implementation has no synchronization, so it is best to use the copy constructor when working on multiple threads.
 * </p>
 *
 * @author Rob Winch
 */
public final class MapSession implements Session {
    private String id = UUID.randomUUID().toString();
    private Map<String, Object> sessionAttrs = new HashMap<String, Object>();
    private long creationTime = System.currentTimeMillis();
    private long lastAccessedTime = creationTime;

    /**
     * Defaults to 30 minutes
     */
    private int maxInactiveInterval = 1800;

    /**
     * Creates a new instance
     */
    public MapSession() {
    }

    /**
     * Creates a new instance from the provided {@link Session}
     *
     * @param session the {@link Session} to initialize this {@link Session} with. Cannot be null.
     */
    public MapSession(Session session) {
        Assert.notNull(session, "session cannot be null");
        this.id = session.getId();
        this.sessionAttrs = new HashMap<String, Object>(session.getAttributeNames().size());
        for (String attrName : session.getAttributeNames()) {
            Object attrValue = session.getAttribute(attrName);
            this.sessionAttrs.put(attrName, attrValue);
        }
        this.lastAccessedTime = session.getLastAccessedTime();
        this.creationTime = session.getCreationTime();
        this.maxInactiveInterval = session.getMaxInactiveInterval();
    }

    @Override
    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public Object getAttribute(String attributeName) {
        return sessionAttrs.get(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {
        return sessionAttrs.keySet();
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            removeAttribute(attributeName);
        } else {
            sessionAttrs.put(attributeName, attributeValue);
        }
    }

    @Override
    public void removeAttribute(String attributeName) {
        sessionAttrs.remove(attributeName);
    }

    /**
     * Sets the time that this {@link Session} was created in milliseconds since midnight of 1/1/1970 GMT. The default is when the {@link Session} was instantiated.
     * @param creationTime the time that this {@link Session} was created in milliseconds since midnight of 1/1/1970 GMT.
     */
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * Sets the identifier for this {@link Session}. The id should be a secure random generated value to prevent malicious users from guessing this value.   The default is a secure random generated identifier.
     *
     * @param id the identifier for this session.
     */
    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        return obj instanceof Session && id.equals(((Session) obj).getId());
    }

    public int hashCode() {
        return id.hashCode();
    }
}