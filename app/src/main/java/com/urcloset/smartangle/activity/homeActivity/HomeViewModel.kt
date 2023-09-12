package com.urcloset.smartangle.activity.homeActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.urcloset.smartangle.model.CategoryModel
import com.urcloset.smartangle.model.NearbyUsersModel
import com.urcloset.smartangle.model.PostsModel

class HomeViewModel : ViewModel() {

    private val _savePosts = MutableLiveData<CategoryModel?>()
     val savePosts : LiveData<CategoryModel?> =_savePosts
    private val _postMutable = MutableLiveData<PostsModel?>()
    val postLiveData : LiveData<PostsModel?> =_postMutable
    private val _saveUsers = MutableLiveData<ArrayList<NearbyUsersModel.Data.User>?>()
    val saveUsers : LiveData<ArrayList<NearbyUsersModel.Data.User>?> =_saveUsers

   private var _loadPreviousNavBottom = MutableLiveData<Int?>()
     var loadPreviousNavBottom :LiveData<Int?> =_loadPreviousNavBottom
    fun setPreviousNavBottom (id:Int?) // id of what item need to change
    {
        this._loadPreviousNavBottom.value = id

    }
}