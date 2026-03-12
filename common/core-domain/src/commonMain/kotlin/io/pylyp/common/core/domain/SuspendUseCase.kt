package io.pylyp.common.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public abstract class SuspendUseCase<in P> {

    protected abstract suspend fun execute(parameters: P)

    public operator fun invoke(parameters: P): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            execute(parameters)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error", e))
        }
    }
}