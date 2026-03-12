package io.pylyp.utils.logging

import io.pylyp.common.core.di.IsolatedKoinContext

public object Logger {

    private val loggerImpl: LoggerInterface by IsolatedKoinContext.koin().inject()

    public fun d(message: () -> String) {
        loggerImpl.d(message)
    }

    public fun e(message: () -> String) {
        loggerImpl.e(message)
    }

    public fun e(throwable: Throwable) {
        loggerImpl.e(throwable)
    }

    public fun e(throwable: Throwable, message: String) {
        loggerImpl.e(throwable, message)
    }
}