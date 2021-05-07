/*
 * Copyright (C) 2013 Facebook, Inc.
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
package com.coder.lee.drift.server.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/6 12:53
 *
 * @author coderLee23
 */
@Log4j2
@Service
public class DriftScribeService implements DriftScribe {

    private final List<DriftLogEntry> messages = new ArrayList<>();

    public List<DriftLogEntry> getMessages() {
        return messages;
    }

    @Override
    public DriftResultCode log(List<DriftLogEntry> messages) {
        this.messages.addAll(messages);
        log.info(getMessages());
        return DriftResultCode.OK;
    }
}
