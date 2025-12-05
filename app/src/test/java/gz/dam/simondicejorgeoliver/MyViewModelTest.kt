package gz.dam.simondicejorgeoliver

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Tests para MyViewModel. Esta clase de test utiliza una combinación de herramientas:
 * - Robolectric: para poder instanciar un `Application` y usar `SharedPreferences` en un test local.
 * - kotlinx-coroutines-test: para probar la lógica que utiliza coroutines, como `viewModelScope`.
 * - JUnit 4: como framework base para estructurar los tests.
 *
 * Referencias:
 * - Probar coroutines en Kotlin: https://developer.android.com/kotlin/coroutines/test
 * - Robolectric: http://robolectric.org/
 * - AndroidX Test (ApplicationProvider): https://developer.android.com/reference/androidx/test/core/app/ApplicationProvider
 */
@ExperimentalCoroutinesApi // Habilita el uso de las APIs de test de coroutines.
@RunWith(RobolectricTestRunner::class) // Usa el runner de Robolectric para simular un entorno Android.
class MyViewModelTest {

    private lateinit var viewModel: MyViewModel

    // Usamos UnconfinedTestDispatcher para ejecutar las coroutines de forma síncrona en los tests,
    // lo que simplifica las aserciones.
    // https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // `Dispatchers.setMain` reemplaza el dispatcher principal por nuestro dispatcher de test.
        // Es crucial para probar ViewModels que usan `viewModelScope`.
        Dispatchers.setMain(testDispatcher)

        // `ApplicationProvider` nos da un contexto de aplicación válido en el entorno de Robolectric.
        val application = ApplicationProvider.getApplicationContext<Application>()

        // Limpiamos las SharedPreferences y los datos del juego antes de cada test
        // para asegurar que los tests son independientes entre sí.
        val prefs = application.getSharedPreferences("preferencias_app", 0)
        prefs.edit().clear().commit()
        Datos.numero.clear()

        viewModel = MyViewModel(application)
    }

    @After
    fun tearDown() {
        // `Dispatchers.resetMain()` restaura el dispatcher principal a su estado original.
        Dispatchers.resetMain()
    }

    // `runTest` es un constructor de coroutines para tests. Asegura que las coroutines
    // que se lancen dentro se completen antes de que el test termine.
    @Test
    fun `numeroRandom debe cambiar el estado a GENERANDO`() = runTest {
        // Cuando
        viewModel.numeroRandom()

        // Entonces
        assertEquals(1, Datos.numero.size)
    }

    @Test
    fun `derrota debe reiniciar el estado del juego`() = runTest {
        // Dado
        viewModel.puntuacion.value = 5
        viewModel.ronda.value = 2
        Datos.numero.add(1)

        // Cuando
        viewModel.derrota()

        // Entonces
        assertEquals(0, viewModel.puntuacion.value)
        assertEquals(1, viewModel.ronda.value)
        assertEquals(Estados.INICIO, viewModel.estadoActual.value)
        assertTrue(Datos.numero.isEmpty())
    }

    @Test
    fun `correcionOpcionElegida debe devolver true si la opción es correcta`() = runTest {
        // Dado
        Datos.numero.add(2) // La secuencia correcta es [2]
        viewModel.posicion = 0
        viewModel.puntuacion.value = 0

        // Cuando
        val result = viewModel.correcionOpcionElegida(2)

        // Entonces
        assertTrue(result)
        assertEquals(0, viewModel.posicion)
        assertEquals(1, viewModel.puntuacion.value)
    }

    @Test
    fun `correcionOpcionElegida debe devolver false y provocar derrota si la opción es incorrecta`() = runTest {
        // Dado
        Datos.numero.add(2) // La secuencia correcta es [2]
        viewModel.posicion = 0
        viewModel.puntuacion.value = 5

        // Cuando
        val result = viewModel.correcionOpcionElegida(3) // Opción incorrecta

        // Entonces
        assertFalse(result)
        // La derrota debería reiniciar el estado
        assertEquals(0, viewModel.puntuacion.value)
        assertEquals(Estados.INICIO, viewModel.estadoActual.value)
    }

    @Test
    fun `derrota con nuevo récord debe actualizar el récord`() = runTest {
        // Dado
        // El récord inicial es 0
        assertEquals(0, viewModel.record.value)
        viewModel.puntuacion.value = 10 // Nueva puntuación más alta

        // Cuando
        viewModel.derrota()

        // Entonces
        assertEquals(10, viewModel.record.value)

        // Verificamos que se ha guardado creando un nuevo ViewModel
        val newViewModel = MyViewModel(ApplicationProvider.getApplicationContext())
        assertEquals(10, newViewModel.record.value)

        // Y comprobamos que la fecha no es la de por defecto
        val defaultDate = "11/11/2011 11:11:11"
        assertFalse(newViewModel.recordData.value == defaultDate)
    }

    @Test
    fun `cambiarRonda debe incrementar la ronda y generar un nuevo número`() = runTest {
        // Dado
        viewModel.ronda.value = 1
        Datos.numero.clear()
        viewModel.posicion = 0

        // Cuando
        viewModel.cambiarRonda()

        // Entonces
        assertEquals(2, viewModel.ronda.value)
        assertEquals(0, viewModel.posicion) // La posición se reinicia
        assertEquals(1, Datos.numero.size) // Se añade un número a la secuencia
    }
}