package ir.miare.androidcodechallenge.di

interface ResourceProvider {
    fun getString(resId: Int, vararg args: Any): String
}
