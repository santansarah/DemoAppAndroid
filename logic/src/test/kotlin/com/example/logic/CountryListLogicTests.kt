package com.example.logic

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.Continent
import com.example.domainmodels.CountryDetails
import com.example.interfaces.networkLogicApiMocks
import com.example.repositories.repositoriesModule
import io.reactivex.rxjava3.observers.TestObserver
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.concurrent.TimeUnit

@ExtendWith(AutoCloseKoinAfterEachExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountryListLogicTests: KoinTest {
    @BeforeEach
    open fun setup() {
        startKoin {
            loadKoinModules(logicModule + repositoriesModule + networkLogicApiMocks)
        }
    }
    val logic: CountryListLogic by inject()

    @Nested
    @DisplayName("#init")
    inner class GetDetails {
        lateinit var value: CountryDetails
        lateinit var testObserver: TestObserver<List<Continent>>

        @BeforeEach
        fun `setup`() {
            testObserver = logic.continents.test()
        }

        @Test
        fun `then it starts empty`() {
            testObserver.values().last().count().shouldBeEqualTo(0)
        }

        @Nested
        @DisplayName("#reload")
        inner class Reload {
            lateinit var reloadObserver: TestObserver<Void>
            lateinit var value: List<Continent>

            @BeforeEach
            fun setup() {
                reloadObserver = logic.reload().test()
                reloadObserver.awaitDone(5, TimeUnit.SECONDS)

                testObserver.awaitCount(2)
                value = testObserver.values().last()
            }

            @Test
            fun `then it should be valid`() {
                value.count().shouldBeEqualTo(6)
            }
        }
    }
}