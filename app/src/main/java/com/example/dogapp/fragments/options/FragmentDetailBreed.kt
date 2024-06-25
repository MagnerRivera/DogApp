package com.example.dogapp.fragments.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dogapp.databinding.FragmentDetailBreedBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentDetailBreed : Fragment() {
    private lateinit var binding: FragmentDetailBreedBinding
    private val args: FragmentDetailBreedArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBreedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameBreed.text = args.breedName
        Picasso.get()
            .load(args.imageUrl)
            .into(binding.imageDetail)

        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}