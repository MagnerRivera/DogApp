package com.example.dogapp.fragments.options

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dogapp.adapter.BreedAdapter
import com.example.dogapp.animaionUtils.AnimationUtils
import com.example.dogapp.databinding.FragmentHomeBinding
import com.example.dogapp.retrofit.Breed
import com.example.dogapp.retrofit.BreedListResponse
import com.example.dogapp.retrofit.DogInstance
import com.example.dogapp.retrofit.DogResponse
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val breeds = mutableListOf<Breed>()
    private lateinit var breedAdapter: BreedAdapter
    private lateinit var editTextSearch: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextSearch = binding.editTextSearch

        val imageSearch = binding.imageSearch

        imageSearch.setOnClickListener {
            if (editTextSearch.visibility == View.VISIBLE) {
                AnimationUtils.slideViewUp(editTextSearch)
                editTextSearch.visibility = View.INVISIBLE
            } else {
                AnimationUtils.slideViewDown(editTextSearch)
                editTextSearch.visibility = View.VISIBLE
            }
        }

        breedAdapter = BreedAdapter(breeds) { breedName, imageUrl ->
            shareImage(breedName, imageUrl)
        }
        binding.rvBreeds.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = breedAdapter
        }

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.e(TAG, "beforeTextChanged: $s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterBreeds(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                Log.e(TAG, "afterTextChanged: $s")
            }
        })

        fetchAllBreeds()
    }

    private fun fetchAllBreeds() {
        DogInstance.api.getAllBreeds().enqueue(object : Callback<BreedListResponse> {
            override fun onResponse(
                call: Call<BreedListResponse>,
                response: Response<BreedListResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { breedListResponse ->
                        val breedNames = breedListResponse.breeds.keys
                        for (breedName in breedNames) {
                            fetchBreedImage(breedName)
                        }
                    }
                } else {
                    Log.e(TAG, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BreedListResponse>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }
        })
    }

    private fun fetchBreedImage(breedName: String) {
        DogInstance.api.getBreedImage(breedName).enqueue(object : Callback<DogResponse> {
            override fun onResponse(call: Call<DogResponse>, response: Response<DogResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { dogResponse ->
                        val breed = Breed(breedName, dogResponse.imageUrl)
                        breeds.add(breed)
                        breedAdapter.notifyItemInserted(breeds.size - 1)
                    }
                } else {
                    Log.e(TAG, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DogResponse>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }
        })
    }

    private fun shareImage(breedName: String, imageUrl: String) {
        val shareMessage = "Mira este perrito de raza $breedName esta es la imagen: $imageUrl"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        startActivity(Intent.createChooser(intent, "Share Image Via"))
    }

    private fun filterBreeds(query: String) {
        val filteredBreeds = breeds.filter { it.name.contains(query, ignoreCase = true) }
        breedAdapter.updateBreeds(filteredBreeds)
    }
}