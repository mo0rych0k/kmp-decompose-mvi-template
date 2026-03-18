package io.pylyp.coffee.data.mappers

import io.pylyp.coffee.data.network.entity.CoffeeImageND
import io.pylyp.common.core.persistence.entity.CoffeeImageSD
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeDataMapperTest {

    @Test
    fun `storage to domain maps id and url`() {
        val storage = CoffeeImageSD(
            id = 42L,
            url = "https://example.com/coffee.png",
        )

        val domain = storage.toDomain()

        assertEquals(42L, domain.id)
        assertEquals("https://example.com/coffee.png", domain.url)
    }

    @Test
    fun `network to storage maps file to url`() {
        val network = CoffeeImageND(file = "https://example.com/coffee.png")

        val storage = network.toStorage()

        assertEquals("https://example.com/coffee.png", storage.url)
    }
}
