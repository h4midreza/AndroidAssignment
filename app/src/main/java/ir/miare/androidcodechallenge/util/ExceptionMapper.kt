package ir.miare.androidcodechallenge.util

import ir.miare.androidcodechallenge.R
import ir.miare.androidcodechallenge.di.ResourceProvider
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ExceptionMapper {

    fun map(e: Exception, resourceProvider: ResourceProvider, defaultRes: Int): String {
        return when (e) {
            is UnknownHostException -> resourceProvider.getString(R.string.error_no_internet)
            is SocketTimeoutException -> resourceProvider.getString(R.string.error_connection_timeout)
            is IOException -> resourceProvider.getString(R.string.error_network_error)
            else -> resourceProvider.getString(
                defaultRes,
                e.localizedMessage ?: resourceProvider.getString(R.string.error_unknown)
            )
        }
    }
}
