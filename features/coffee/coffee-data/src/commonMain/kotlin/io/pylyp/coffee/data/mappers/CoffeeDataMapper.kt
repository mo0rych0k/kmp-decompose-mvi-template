package io.pylyp.coffee.data.mappers

import io.pylyp.coffee.data.network.entity.CoffeeImageND
import io.pylyp.coffee.domain.entity.CoffeeImageDD
import io.pylyp.common.core.persistence.entity.CoffeeImageSD


internal fun CoffeeImageSD.toDomain(): CoffeeImageDD {
    return CoffeeImageDD(
        id = id,
        url = url
    )
}

internal fun CoffeeImageND.toStorage(): CoffeeImageSD {
    return CoffeeImageSD(
        url = file
    )
}