package com.jaanussinivali.catkeeper.ui.cat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.jaanussinivali.catkeeper.data.CatDao
import com.jaanussinivali.catkeeper.data.CatKeeperDatabase
import com.jaanussinivali.catkeeper.data.entity.Cat
import com.jaanussinivali.catkeeper.databinding.FragmentCatCardsBinding
import kotlin.concurrent.thread

class CatCardsFragment : Fragment() {
    private var fragmentTitle:String? = null
    private var fragmentNumber:Int? = null
    private lateinit var binding: FragmentCatCardsBinding
    private val catDao: CatDao by lazy { CatKeeperDatabase.getDatabase(requireContext()).getCatDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentTitle = it.getString(ARG_TITLE)
            fragmentNumber = it.getInt((ARG_POS))
        }
    }
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
            val cat = fragmentNumber?.let { catDao.getCat(it) }
            requireActivity().runOnUiThread {
                when(cat){
                   null -> {
                       binding.textViewOfficialNameValue.text = "name value"
                       binding.textViewBirthDateValue.text = "__/__/____"
                       binding.textViewAgeValue.text = "age value"
                       binding.textViewBirthPlaceValue.text = "place value"
                       binding.textViewLastDoctorVisitDate.text = "__/__/____"
                       binding.textViewLastWormMedicineDate.text = "__/__/____"
                       binding.textViewLastVaccinationDate.text = "__/__/____"

                       binding.textViewInsuranceNameValue.text = "Trying to get cat name here"
                       binding.textViewInsurancePhoneNumber.text = "Trying to get cat name here"
                       binding.textViewInsuranceValidUntilDate.text = "__/__/____"
                       binding.textViewInsuranceSumValue.text = "1000€"
                   }
                   else -> {
                       binding.textViewOfficialNameValue.text = cat.cat.general?.officialName
                       binding.textViewBirthDateValue.text = ""
                       binding.textViewAgeValue.text = ""
                       binding.textViewBirthPlaceValue.text = ""
                       binding.textViewLastDoctorVisitDate.text = "__/__/____"
                       binding.textViewLastWormMedicineDate.text = "__/__/____"
                       binding.textViewLastVaccinationDate.text = "__/__/____"

                       binding.textViewInsuranceNameValue.text = ""
                       binding.textViewInsurancePhoneNumber.text = ""
                       binding.textViewInsuranceValidUntilDate.text = "__/__/____"
                       binding.textViewInsuranceSumValue.text = ""
                   }
                }
            }
        }
    }
    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_POS = "position"
        fun newInstance(title:String, position:Int): CatCardsFragment {
            val fragment = CatCardsFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putInt(ARG_POS, position)
            fragment.arguments = args
            return fragment
        }
    }
}