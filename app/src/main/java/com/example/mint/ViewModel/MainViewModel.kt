package com.example.mint.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mint.Model.CategoryModel
import com.example.mint.Model.MejoresNegociosModel
import com.example.mint.Model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainViewModel:ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _category = MutableLiveData<MutableList<CategoryModel>>()
    private val _mejoresNegocios = MutableLiveData<MutableList<MejoresNegociosModel>>()

    val banners: LiveData<List<SliderModel>> = _banner
    val category: LiveData<MutableList<CategoryModel>> = _category
    val mejoresNegocios: LiveData<MutableList<MejoresNegociosModel>> = _mejoresNegocios

    fun loadBanners(){
        val Ref=firebaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists= mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null){
                        lists.add(list)
                    }
                    _banner.value = lists
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun loadCategory(){
        val Ref = firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CategoryModel>()

                for(childSnaphot in snapshot.children){
                    val list = childSnaphot.getValue(CategoryModel::class.java)
                    if(list != null){
                        lists.add(list)
                    }
                }
                _category.value = lists
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun loadMejoresNegocios(){
        val Ref = firebaseDatabase.getReference("Items")
        Ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<MejoresNegociosModel>()

                for(childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(MejoresNegociosModel::class.java)
                    if(list != null){
                        lists.add(list)
                    }
                }
                _mejoresNegocios.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}