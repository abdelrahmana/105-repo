package com.urcloset.smartangle.api



import com.skydoves.sandwich.ApiResponse
import com.urcloset.smartangle.data.model.agreement_terms.AgreementResponseTerms
import com.urcloset.smartangle.model.*
import com.urcloset.smartangle.model.project21.*
import com.urcloset.smartangle.model.project_105.*
import io.reactivex.Observable
import okhttp3.*
import retrofit2.http.*


interface AppApi {

    @GET("country/{id}/get")
    fun getCountryWithCity(@Path("id")id:String):Observable<CountryWithCity>


    @GET("country/all")
    fun getCountry():Observable<CountryModel>



    @POST("country/all")
    fun checkEmailOrPhone(@Body map: Map<String, String>):Observable<CheckPhoneOrEmailModel>


    @Multipart
    @POST("auth/new_register")
    fun signUp(@Part image:MultipartBody.Part, @QueryMap map:Map<String,String>):
            Observable<LoginResponseModel>


    @POST("auth/code/email/verify_code")
    fun checkEmailOtp(@Body map: Map<String, String>):Observable<CheckOtpEmailModel>

    fun CheckEmailOtp(@Body map: Map<String, String>):Observable<CheckOtpEmailModel>
//    @GET("home")
//    fun getUsers():Observable<UsersModel>
   @GET("home")
   fun getUsers(@QueryMap map: Map<String, String>):Observable<NearbyUsersModel>

    @GET("auth/profile/visitor/log")
    fun getVisitors(@Query("page") page:String):Observable<VisitorModel>
    @GET("general_text")
    fun getGeneralText(@Query("key_text") generalText:String):Observable<PrivacyModel>
    @GET("public/profile/info")
    fun getUserProfile(@Query("user_id") user_id:Int):Observable<UserProfileModel>
    @GET("auth/profile/info")
    fun getPersonalInformation():Observable<PersonalUserInfoModel>
    @POST("auth/profile/linkAccount")
    fun linkAccount(@Body map: Map<String, String>):Observable<LinkAccountModel>
    @POST("auth/profile/unlinkAccount")
    fun unLinkAccount(@Body map: Map<String, String>):Observable<UnLinkAccountModel>
    @GET("categoires")
    fun getCategories(@Query("with_paginate") paginate:String):Observable<CategoryModel>
    @GET("condtions")
    fun getConditions(@Query("with_paginate") paginate:String):Observable<ConditionModel>
    @GET("public/general/sentence")
    fun getGeneralSentence(@Query("key") key:String):Observable<SentenceModel>
    @GET("colors")
    fun getColors(@Query("with_paginate") paginate:String):Observable<ColorModel>
    @GET("sizes")
    fun getSizes(@Query("with_paginate") paginate:String):Observable<SizeModel>
    @GET("public/product/all")
    fun getProducts(@Query("with_paginate") paginate:String,@Query("owner_id") owner_id:String):Observable<ProductModel>
    @Multipart
    @POST("auth/product/store")
    fun createProduct(
         @Part image:ArrayList<MultipartBody.Part>,
         @QueryMap params: Map<String, String>,
         @Part("sizes[]") sizes:ArrayList<Int>,
         @Part("colors[]") colors:ArrayList<Int>

    ):Observable<CreateProductModel>
    @Multipart
    @POST("auth/product/update")
    fun updateProduct(
        @Part image:ArrayList<MultipartBody.Part>,
        @QueryMap params: Map<String, String>,
        @Part("sizes[]") sizes:ArrayList<Int>,
        @Part("colors[]") colors:ArrayList<Int>,
        @Part("item_media_to_delete[]") media_to_delete:ArrayList<Int>


    ):Observable<CreateProductModel>

    @GET("public/product/grouped/all/")
    fun getProductsByState(@Query("owner_id") owner_id:String):Observable<ProductStateModel>
    @GET("product/show/")
    fun getProductDetails(@QueryMap map: Map<String, String>):Observable<ProductDetailsModel>
    @POST("auth/product/report/store")
    fun reportProduct(@Body map: Map<String, String>):Observable<BasicModel>
    @POST("auth/favorite")
    fun saveProduct(@Body map: Map<String, String>):Observable<BasicModel>
    @POST("auth/favorite")
    fun saveUser(@Body map: Map<String, String>):Observable<BasicModel>
    @POST("auth/product/change/status")
    fun changeProductStatus(@Body map: Map<String, String>):Observable<BasicModel>
    @POST("auth/notification/profile")
    fun toggleNotification(@Body map: Map<String, String>):Observable<BasicModel>
   // @GET("auth/product/publish_status/get")
   @GET("auth/product/myProducts/published")
   suspend fun getPublicationStatus():ApiResponse<ProductItemResponse>
 @GET("auth/product/myProducts/selled")
 suspend fun getSelledProducts():ApiResponse<ProductItemResponse>
 @GET("auth/product/myProducts/rejected")
 suspend fun getRejected():ApiResponse<ProductItemResponse>
    @GET("auth/product/myProducts/unPaid")
    suspend fun getUnPaidProducts():ApiResponse<ProductItemResponse>
    @POST("auth/product/financialSettlement")
    suspend fun postPaymentAfterMyFatoraPay(@Body hashMap: HashMap<String, Any>?)
    :ApiResponse<ResponseBody>

    @POST("auth/contact_us/message/send")
    fun sendMessage(@Body map: Map<String, String>):Observable<SendMessageModel>
    @GET("auth/contact_us/chat/get")
    fun getChat(@Query("page") page:String):Observable<ChatModel>
    @GET("auth/notification/list")
    fun getNotifications(@Query("page") page:String):Observable<NotificationModel>
    @POST("auth/profile/removeVoiceIdentity")
    fun deleteVoiceIdentity(@Body map: Map<String, String>,@Query("lang") lang:String):Observable<UserProfileModel>
    @POST("auth/rating/new")
    fun rate(@Body map: Map<String, String>):Observable<RateModel>
    @FormUrlEncoded
    @POST("auth/notification/read")
    fun readNotification(@Field("ids[]") ids:ArrayList<String>):Observable<BasicModel>
    @POST("auth/data/check")
    fun checkPhone(@Body map:Map<String,String>,@QueryMap lang: Map<String, String>):Observable<CheckEmailPhoneModel>
    @GET("home/posts/")
    fun getPosts(@QueryMap map: Map<String, String>):Observable<PostsModel>
    @GET("product/commission/get")
    fun getCommission(@Query("price") price:String):Observable<CommissionModel>








    @POST("auth/send/code/email")
    fun sendCodeToEmail(@Body map:Map<String,String>):Observable<ResponseBody>


    @GET("condtions?with_paginate=yes")
    fun getCondition():Observable<ConditionBookMarkModel>


    @GET("auth/favorite/get/list/type")
    fun getBookMark(@QueryMap map: Map<String, String>):Observable<BookmarkMV3>


    @GET("categoires?with_paginate=yes")
    fun getCategoryWithPaginate():Observable<CategoryModelV2>




    @POST("auth/favorite")
    fun addDeleteBookMark(@Body map: Map<String, String>):Observable<ResponseBody>



    @GET("sizes")
    fun getSizesSearch(@Query("with_paginate") paginate:String):Observable<SizeModelHassan>

    @GET("colors")
    fun getColorsSearch(@Query("with_paginate") paginate:String):Observable<ColorModelHassan>


    @GET("public/products/search")
    fun searchProduct(@QueryMap map: Map<String, String>):Observable<SearchProductV2Model>


    @POST("auth/logout")
    fun logout():Observable<ResponseBody>


    @Multipart
    @POST("auth/profile/update/me")
    fun updateProfileWithImage(@Part image:MultipartBody.Part, @QueryMap map:Map<String,String>):
            Observable<LoginResponseModel>



    @POST("auth/profile/update/me")
    fun updateProfile( @Body map:Map<String,String>):
            Observable<LoginResponseModel>

    @POST("auth/new_register/social")
    fun loginSoical( @Body map:Map<String,String>):
            Observable<LoginResponseModel>


    @POST("import/contact/list")
    fun sendContact(@Body item:ContactModelList):Observable<ResponseBody>


    @GET("public/providers/search")
    fun searchProvider(@QueryMap map: Map<String, String>):Observable<ProivderSeachModel>

    @POST("contact/list/getName")
    fun getCard(@Body map: Map<String, String>):Observable<CardResultModel>


    @POST("auth/new_register")
    fun signUpWithoutImage(@Body map:Map<String,String>):
            Observable<LoginResponseModel>

    //--------------------------------------------------------//


    @POST("payment/confirm")
    fun savePaymentStatus(@Body map: Map<String, String>):Observable<ResponseBody>

    @POST("payment/get/code")
    fun getCodeCheckout(@Body map: Map<String, String>):Observable<CheckoutIDCodeResposne>








    @GET("account/{accountId}")
    fun getAccountById(@Path("accountId")id:String):Observable<LoginResponseModel>

    @POST("auth/login")
    fun loginNormal(@Body map:Map<String,String>):Observable<LoginResponseModel>


    @POST("social-login")
    fun loginSocial(@Body map:Map<String,String>):Observable<LoginResponseModel>

    @POST("auth/token/set")
    fun saveDevicesToken(@Body map: Map<String, String>):Observable<ResponseBody>






    @POST("profile/edit/phone")
    fun editPhone(@Body map:Map<String,String>):Observable<MessageModel>




    @POST("auth/profile/change/password")
    fun editPassword(@Body map:Map<String,String>):Observable<ResponseBody>

    @POST("forget-password")
    fun fogotPassword(@Body map: Map<String, String>):Observable<MessageModel>









    @Multipart
    @POST("auth/profile/uploadVoiceIdentity")
    fun sendSound(@Part image:MultipartBody.Part):
            Observable<ResponseBody>




    @POST("auth/resetPassword")
    fun resetPass(@Body map:Map<String,String>):Observable<ResponseBody>


    @POST("auth/profile/changeLang")
    fun changeLang(@Body map: Map<String, String>):Observable<ResponseBody>

 @GET("globallinks")
 fun getSocialLoginData(@QueryMap hashMap: HashMap<String,Any>):Observable<GlobalLink>

 @GET("general_text")
suspend fun getAgreementText(@Query("key_text")keyText: String = "terms_and_conditions"):ApiResponse<AgreementResponseTerms>
 @POST("auth/user/agreeOrNot")
 suspend fun postAgreements(@Body hashMap: HashMap<String, Int>):ApiResponse<ResponseBody>

}
