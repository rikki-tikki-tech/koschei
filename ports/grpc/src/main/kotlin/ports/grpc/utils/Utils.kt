package ports.grpc.utils

import com.google.type.Date
import java.time.LocalDate

fun convertGoogleDateToLocalDate(date: Date): LocalDate {
    return LocalDate.of(date.year, date.month, date.day)
}

fun convertLocalDateToGoogleDate(localDate: LocalDate): Date {
    return Date.newBuilder()
        .setYear(localDate.year)
        .setMonth(localDate.monthValue)
        .setDay(localDate.dayOfMonth)
        .build()
}
