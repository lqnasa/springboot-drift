/*
 * Copyright (C) 2012 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.coder.lee.drift.client.service;

import com.facebook.drift.annotations.ThriftField;
import com.facebook.drift.annotations.ThriftStruct;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

@ThriftStruct
public final class DriftLogEntry {

    private String category;
    private String message;

    @ThriftField(name = "category")
    public void setCategory(String category) {
        this.category = category;
    }

    @ThriftField(name = "message")
    public void setMessage(String message) {
        this.message = message;
    }

    @ThriftField(1)
    public String getCategory() {
        return category;
    }

    @ThriftField(2)
    public String getMessage() {
        return message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DriftLogEntry that = (DriftLogEntry) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, message);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("category", category)
                .add("message", message)
                .toString();
    }
}
