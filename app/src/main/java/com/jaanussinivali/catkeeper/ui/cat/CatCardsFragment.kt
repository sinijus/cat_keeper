package com.jaanussinivali.catkeeper.ui.cat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jaanussinivali.catkeeper.data.CatDao
import com.jaanussinivali.catkeeper.data.CatKeeperDatabase
import com.jaanussinivali.catkeeper.data.entity.Cat
import com.jaanussinivali.catkeeper.databinding.FragmentCatCardsBinding
import kotlin.concurrent.thread

class CatCardsFragment : Fragment() {

    private lateinit var binding: FragmentCatCardsBinding
    private val catDao: CatDao by lazy { CatKeeperDatabase.getDatabase(requireContext()).getCatDao() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatCardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCatCardsFragment()
    }

    private fun fetchCatCardsFragment() {
        thread {
            val cats = catDao.getAllCats()
            requireActivity().runOnUiThread {
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
                binding.textViewOfficialNameValue.text = "Trying to get cat name here"
            }
        }
    }

    private fun updateCat(cat: Cat) {
        thread {
            catDao.updateCat(cat)
        }
    }



}