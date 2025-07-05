package com.hook2book.hbsync.Interface


import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.hook2book.hbsync.Model.Categories.CategoriesMain
import com.hook2book.hbsync.Model.InitialVaribales.InitialVariablesMain
import com.hook2book.hbsync.Model.NewTag.ApiResponse
import com.hook2book.hbsync.Model.NewTag.ProductMain
import com.hook2book.hbsync.Model.NewTag.newTag
import com.hook2book.hbsync.Model.ProductTags.ProductTagsMain
import com.hook2book.hbsync.Model.Registration.RegistrationData
import com.hook2book.hbsync.Model.Registration.RegistrationResponse
import com.hook2book.hbsync.Model.SearchProduct.SearchProduct
import com.hook2book.hbsync.Model.SellerData.SellerData
import com.hook2book.hbsync.Model.SellerData.SellerDataResponseStatus
import com.hook2book.hbsync.Model.SingleProduct.ProductMainSingle
import com.hook2book.hbsync.Model.SingleSubOrderDetail.SingleSubOrderDetail
import com.hook2book.hbsync.Model.SubOrderData.SubOrderData
import com.hook2book.hbsync.Model.Token.Token
import com.hook2book.hbsync.Model.UpdatePreStatus.UpdatePreStatus
import com.hook2book.hbsync.Model.productStatusUpdate.productStatusUpdate
import com.hook2book.hbsync.Model.subOrderUpdate.subOrderUpdate
import com.hook2book.hbsync.Model.updateWCProduct.updateWCProduct
import com.hook2book.hbsync.UtilityClass.BaseValues


import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap


interface RetroInterface {

    @GET("/SellerApp/api/v1/searchProduct")
    suspend fun getproductList(@QueryMap data: Map<String, String>): Response<SearchProduct>

    @GET("/wp-json/wc/v3/products/tags")
    suspend fun getTagList(
        @Header("Authorization") authHeader: String, @QueryMap data: Map<String, String>
    ): Response<List<ProductTagsMain>>

    @POST("/SellerApp/api/v1/add_product")
    suspend fun addProductToDB(
        @Header("Authorization") authHeader: String, @Body productMain: ProductMain
    ): Response<ApiResponse>

    @Headers("Content-Type: application/json")
    @POST("/SellerApp/api/v1/add_product")
    suspend fun addProductToDB1(
        @Header("Authorization") authHeader: String, @Body jsonObject: JsonObject
    ): Response<ApiResponse>

    @GET("/wp-json/wc/v3/products/categories")
    suspend fun fetchCategories(
        @Header("Authorization") authHeader: String, @QueryMap data: Map<String, String>
    ): Response<List<CategoriesMain>>

    @GET("/SellerApp/api/v1/getProduct/{idOfProduct}")
    suspend fun fetchSingleProduct(
        @Header("Authorization") authHeader: String, @Path("idOfProduct") id: String
    ): Response<ProductMainSingle>

    @POST("/wp-json/wc/v3/customers")
    suspend fun registerUserAccount(
        @Header("Authorization") authHeader: String, @Body data: RegistrationData
    ): Response<RegistrationResponse>

    @POST("/wp-json/api-bearer-auth/v1/login/")
    suspend fun login(@Body jsonObject: JsonObject): Response<Token>

    @GET("mapp/InitalValue.php/")
    suspend fun fetchInitialValues(): Response<InitialVariablesMain>

    @POST("/SellerApp/api/v1/add_pubSeller")
    suspend fun insertSellerData(@QueryMap data: Map<String, String>): Response<SellerDataResponseStatus>

    @GET("/SellerApp/api/v1/getPubSeller")
    suspend fun getUserData(@QueryMap data: Map<String, String>): Response<SellerData>

    @GET("/SellerApp/api/v1/searchProduct")
    suspend fun fetchProducts(@QueryMap data: Map<String, String>): Response<SearchProduct>


    @Headers("Content-Type: application/json")
    @POST("/SellerApp/api/v1/update_product/{idOfProduct}")
    suspend fun updateProductToDB1(
        @Header("Authorization") authHeader: String,
        @Body jsonObject: JsonObject,
        @Path("idOfProduct") productId: String
    ): Response<ApiResponse>

    @GET("/SellerApp/api/v1/getOrderDetail")
    suspend fun fetchSubOrders(@QueryMap data: Map<String, String>): Response<SubOrderData>

    @PUT("wp-json/wc/v3/products/{idOfProduct}")
    suspend fun updateProductStatusInWC(
        @Header("Authorization") authHeader: String,
        @Path("idOfProduct") productId: String,
        @QueryMap data: Map<String, String>
    ): Response<updateWCProduct>

    @POST("/SellerApp/api/v1/approve/{idOfProduct}")
    suspend fun updateStatusInPre(
        @Path("idOfProduct") productId: String, @QueryMap data: Map<String, String>
    ): Response<UpdatePreStatus>

    @POST("/wp-json/wc/v3/products/tags")
    suspend fun createNewTag(
        @Header("Authorization") authHeader: String, @QueryMap data: Map<String, String>
    ): Response<newTag>

    @GET("/SellerApp/api/v1/getOrderById/{idOfOrder}")
    suspend fun fetchSingleSubOrder(@Path("idOfOrder") id: String): Response<SingleSubOrderDetail>

    @Headers("Content-Type: application/json")
    @PUT("/SellerApp/api/v1/updateOrderStatus/{idOfOrder}")
    suspend fun submitOrderStatus(
        @Path("idOfOrder") id: String, @QueryMap data: Map<String, String>
    ): Response<subOrderUpdate>

    @PUT("/SellerApp/api/v1/updateSubOrderStatus")
    suspend fun submitProductStatus(@Body jsonArray: JsonArray): Response<productStatusUpdate>
}

object RetroService {

    var retroInstance: RetroInterface

    init {
        val okHttpClient = OkHttpClient.Builder().build()
        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder().baseUrl(BaseValues.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build()
        retroInstance = retrofit.create(RetroInterface::class.java)
    }
}