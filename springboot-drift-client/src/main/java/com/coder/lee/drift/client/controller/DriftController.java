package com.coder.lee.drift.client.controller;


import com.coder.lee.drift.client.service.DriftLogEntry;
import com.coder.lee.drift.client.service.DriftResultCode;
import com.coder.lee.drift.client.service.DriftScribe;
import com.facebook.drift.TException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/5 0:30
 *
 * @author coderLee23
 */
@Log4j2
@Api
@RestController
public class DriftController {

    @Autowired
    private DriftScribe driftScribe;

    @PostMapping("/log")
    public DriftResultCode log( @RequestBody List<DriftLogEntry> messages) {
        try {
            return driftScribe.log(messages);
        } catch (TException e) {
            log.error("TException error", e);
            throw new RuntimeException(e);
        }
    }

}
