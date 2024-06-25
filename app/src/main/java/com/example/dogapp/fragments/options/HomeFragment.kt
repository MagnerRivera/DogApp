package com.example.dogapp.fragments.options

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Response

private const val TAG = "HomeFragment"
private const val POLL_INTERVAL = 3000L

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val breeds = mutableListOf<Breed>()
    private lateinit var breedAdapter: BreedAdapter
    private lateinit var editTextSearch: EditText
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var handler: Handler
    private lateinit var connectivityCheckRunnable: Runnable

    private lateinit var rvBreeds: RecyclerView
    private lateinit var searchLayout: LinearLayout
    private lateinit var imageEmpty: ImageView
    private lateinit var emptyPhotos: TextView

    private var isConnected = false
    private var isFirstConnection = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvBreeds = binding.rvBreeds
        searchLayout = binding.searchLayout
        imageEmpty = binding.imageEmpty
        emptyPhotos = binding.emptyPhotos

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

        breedAdapter = BreedAdapter(breeds,
            onShareClick = { breedName, imageUrl ->
                shareImage(breedName, imageUrl)
            },
            onItemClick = { breedName, imageUrl ->
                navigateToDetailBreed(breedName, imageUrl)
            }
        )
        rvBreeds.apply {
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

        handler = Handler(Looper.getMainLooper())
        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityCheckRunnable = object : Runnable {
            override fun run() {
                val isConnectedNow = isNetworkAvailable()
                if (isConnectedNow && !isConnected) {
                    Log.d(TAG, "Conexión a Internet disponible")
                    rvBreeds.visibility = View.VISIBLE
                    searchLayout.visibility = View.VISIBLE
                    imageEmpty.visibility = View.INVISIBLE
                    emptyPhotos.visibility = View.INVISIBLE

                    if (isFirstConnection) {
                        showToast()
                        isFirstConnection = false
                    }

                    fetchAllBreeds()

                } else if (!isConnectedNow && isConnected) {
                    Log.d(TAG, "No hay conexión a Internet")
                    rvBreeds.visibility = View.INVISIBLE
                    searchLayout.visibility = View.INVISIBLE
                    imageEmpty.visibility = View.VISIBLE
                    emptyPhotos.visibility = View.VISIBLE
                }
                isConnected = isConnectedNow
                handler.postDelayed(this, POLL_INTERVAL)
            }
        }

        handler.post(connectivityCheckRunnable)

        isConnected = isNetworkAvailable()
        if (isConnected) {
            fetchAllBreeds()
        } else {
            rvBreeds.visibility = View.INVISIBLE
            searchLayout.visibility = View.INVISIBLE
            imageEmpty.visibility = View.VISIBLE
            emptyPhotos.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(connectivityCheckRunnable)
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
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

    private fun navigateToDetailBreed(breedName: String, imageUrl: String) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToFragmentDetailBreed(breedName, imageUrl)
        findNavController().navigate(action)
    }

    private fun showToast() {
        Toast.makeText(requireContext(), "Vuelta a la red", Toast.LENGTH_SHORT).show()
    }
}