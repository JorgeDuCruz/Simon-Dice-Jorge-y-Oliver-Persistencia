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

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MyViewModelTest {

    private lateinit var viewModel: MyViewModel
    // Usamos UnconfinedTestDispatcher para ejecutar las coroutines de forma síncrona en los tests
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // Establecemos el dispatcher principal para los tests
        Dispatchers.setMain(testDispatcher)
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
        // Restauramos el dispatcher principal
        Dispatchers.resetMain()
    }

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