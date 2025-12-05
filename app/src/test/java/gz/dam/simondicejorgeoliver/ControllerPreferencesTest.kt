package gz.dam.simondicejorgeoliver

import android.content.Context
import android.content.SharedPreferences
import gz.dam.simondicejorgeoliver.Controller.ControllerPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import java.time.temporal.ChronoUnit

/**
 * Los tests unitarios para ControllerPreferences usan Mockito para simular (mockear) las dependencias
 * del framework de Android, como `Context` y `SharedPreferences`.
 * Esto nos permite probar la lógica de la clase de forma aislada, sin necesitar un dispositivo o emulador.
 *
 * Referencias:
 * - Mockito: https://site.mockito.org/
 * - Mockito-Kotlin: https://github.com/mockito/mockito-kotlin
 * - JUnit 4: https://junit.org/junit4/
 */
@RunWith(MockitoJUnitRunner::class) // Runner de Mockito para inicializar los mocks anotados con @Mock.
class ControllerPreferencesTest {

    @Mock // Crea un objeto simulado (mock) de la clase.
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Before // Este método se ejecuta antes de cada test.
    fun setUp() {
        // `when` define el comportamiento que tendrán los mocks cuando se llame a sus métodos.
        // Aquí, configuramos que cuando se pida SharedPreferences, se devuelva nuestro mock.
        `when`(mockContext.getSharedPreferences(anyString(), anyInt()))
            .thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
    }

    @Test
    fun testSetAndGetRecord() {
        // Given
        val testRecordValue = 100
        // Truncamos los nanosegundos para evitar fallos de precisión en la comparación.
        // https://developer.android.com/reference/java/time/LocalDateTime
        val testDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val formattedDate = testDate.format(ControllerPreferences.formatter)

        `when`(mockEditor.putInt(any(), anyInt())).thenReturn(mockEditor)
        `when`(mockEditor.putString(any(), anyString())).thenReturn(mockEditor)

        `when`(mockSharedPreferences.getInt("record", 0)).thenReturn(testRecordValue)
        `when`(mockSharedPreferences.getString("record_fecha", "11/11/2011 11:11:11")).thenReturn(formattedDate)

        // When
        ControllerPreferences.setRecord(testRecordValue, testDate, mockContext)
        val resultRecord = ControllerPreferences.getRecord(mockContext)

        // Then
        // `verify` comprueba que los métodos de los mocks se han llamado con los argumentos esperados.
        verify(mockEditor).putInt("record", testRecordValue)
        verify(mockEditor).putString("record_fecha", formattedDate)
        verify(mockEditor).apply()

        // `assertEquals` comprueba que el resultado es el esperado.
        assertEquals(testRecordValue, resultRecord.recordPun)
        assertEquals(testDate, resultRecord.recordFeha)
    }

    @Test
    fun testGetRecord_independently() {
        // Given
        val expectedRecord = 150
        val expectedDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val formattedDate = expectedDate.format(ControllerPreferences.formatter)

        `when`(mockSharedPreferences.getInt("record", 0)).thenReturn(expectedRecord)
        `when`(mockSharedPreferences.getString("record_fecha", "11/11/2011 11:11:11")).thenReturn(formattedDate)

        // When
        val resultRecord = ControllerPreferences.getRecord(mockContext)

        // Then
        assertEquals(expectedRecord, resultRecord.recordPun)
        assertEquals(expectedDate, resultRecord.recordFeha)
    }

    @Test
    fun testSetRecord_independently() {
        // Given
        val testRecordValue = 200
        val testDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val formattedDate = testDate.format(ControllerPreferences.formatter)
        `when`(mockEditor.putInt(any(), anyInt())).thenReturn(mockEditor)
        `when`(mockEditor.putString(any(), anyString())).thenReturn(mockEditor)

        // When
        ControllerPreferences.setRecord(testRecordValue, testDate, mockContext)

        // Then
        verify(mockEditor).putInt("record", testRecordValue)
        verify(mockEditor).putString("record_fecha", formattedDate)
        verify(mockEditor).apply()
    }
}