package app.zilvinas.todoslist.database

import androidx.room.TypeConverter
import java.util.*

class Converter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun toDate(value: Long): Date {
            return Date(value*1000L)
        }

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date): Long {
            return date.time
        }
    }
}