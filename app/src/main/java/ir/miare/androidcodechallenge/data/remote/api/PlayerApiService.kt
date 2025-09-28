package ir.miare.androidcodechallenge.data.remote.api

import ir.logicbase.mockfit.Mock
import ir.miare.androidcodechallenge.data.model.FakeData
import retrofit2.http.GET

interface PlayerApiService {
    @Mock("data.json")
    @GET("list")
    suspend fun getData(): List<FakeData>
}
