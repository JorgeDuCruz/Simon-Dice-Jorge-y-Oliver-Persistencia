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

@RunWith(MockitoJUnitRunner::class)
class ControllerPreferencesTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Before
    fun setUp() {
        `when`(mockContext.getSharedPreferences(anyString(), anyInt()))
            .thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
    }

    @Test
    fun testSetAndGetRecord() {
        // Given
        val testRecordValue = 100
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
        verify(mockEditor).putInt("record", testRecordValue)
        verify(mockEditor).putString("record_fecha", formattedDate)
        verify(mockEditor).apply()

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