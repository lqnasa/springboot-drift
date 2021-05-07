package com.coder.lee.drift.server.service;

import com.facebook.drift.TException;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
public class DriftAsyncScribeService implements DriftAsyncScribe {

    /**
     * 线程池
     */
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 10, 60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(200),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    private static final ListeningExecutorService EXECUTOR = MoreExecutors.listeningDecorator(threadPoolExecutor);

    @Override
    public ListenableFuture<DriftResultCode> asyncLog(List<DriftLogEntry> logEntries) throws TException {

        return EXECUTOR.submit(() -> {
            logEntries.forEach(message -> log.info(message.getMessage()));
            return DriftResultCode.OK;
        });
    }

}
