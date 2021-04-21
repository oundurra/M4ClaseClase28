package com.example.m4claseclase28

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity: AppCompatActivity(), android.widget.SearchView.OnQueryTextListener {
    lateinit var rvDogs:RecyclerView
    //lateinit var ivDog:ImageView
    lateinit var imagesPuppies:List<String>
    lateinit var dogsAdapter:DogsAdapter
    lateinit var searchBreed:SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchBreed = findViewById(R.id.searchBreed)
        rvDogs = findViewById(R.id.rvDogs) as RecyclerView
        searchBreed.setOnQueryTextListener(this)
    }

    private fun initCharacter(puppies:DogsResponse) {
        if(puppies.status == "success"){
            imagesPuppies = puppies.images
        }
        dogsAdapter = DogsAdapter(imagesPuppies)
        rvDogs.setHasFixedSize(true)
        rvDogs.layoutManager = LinearLayoutManager(this)
        rvDogs.adapter = dogsAdapter
    }

    private fun searchByName(query: String) {
        doAsync {
            val call = getRetrofit().create(APIService::class.java).getCharacterByName("$query/images").execute()
            val puppies = call.body() as DogsResponse
            uiThread {
                if(puppies.status == "success") {
                    //tvHello.text = "Success"
                    initCharacter(puppies)
                } else{
                    showErrorDialog()
                }
                //hideKeyboard()
            }
        }
    }

    private fun hideKeyboard(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.hideSoftInputFromWindow(viewRoot.windowToken, 0)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://dog.ceo/api/breed/").addConverterFactory(GsonConverterFactory.create()).build()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchByName(query!!.toLowerCase())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // TODO("Not yet implemented")
        return true
    }

    private fun showErrorDialog() {
        alert("Ha ocurrido un error, inténtelo de nuevo.") {
            yesButton { }
        }.show()
    }

}