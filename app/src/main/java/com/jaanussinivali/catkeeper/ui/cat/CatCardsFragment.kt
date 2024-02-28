package com.jaanussinivali.catkeeper.ui.cat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jaanussinivali.catkeeper.data.CatDao
import com.jaanussinivali.catkeeper.data.CatKeeperDatabase
import com.jaanussinivali.catkeeper.data.entity.Cat
import com.jaanussinivali.catkeeper.data.entity.CatWithWeights
import com.jaanussinivali.catkeeper.databinding.DialogEditCatCardBinding
import com.jaanussinivali.catkeeper.databinding.DialogEditImageAndNameBinding
import com.jaanussinivali.catkeeper.databinding.FragmentCatCardsBinding
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.AGE
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.BIRTH_DATE
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.BIRTH_PLACE
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.DOCTOR_VISIT
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.GENERAL
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.INSURANCE
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.INS_END_DATE
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.INS_NAME
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.INS_PHONE
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.INS_SUM
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.MEDICAL
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.OFFICIAL_NAME
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.VACCINATION
import com.jaanussinivali.catkeeper.ui.cat.CatFragmentConstants.WORM_MEDICINE
import kotlin.concurrent.thread

class CatCardsFragment : Fragment() {
    private var fragmentTitle: String? = null
    private var fragmentNumber: Int = 0
    private lateinit var binding: FragmentCatCardsBinding
    private val catDao: CatDao by lazy {
        CatKeeperDatabase.getDatabase(requireContext()).getCatDao()
    }

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
        displayFields()
        setOnClickListeners()
    }

    private fun displayFields() {
        thread {
            val catWithWeights: CatWithWeights? = fragmentNumber.let { catDao.getCat(it) }
            requireActivity().runOnUiThread {
                when (catWithWeights) {
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
                        binding.textViewOfficialNameValue.text =
                            catWithWeights.cat.general?.officialName
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

    private fun setOnClickListeners() {
        binding.cardViewImage.setOnClickListener { showEditDialogNameAndPicture() }
        binding.cardViewGeneral.setOnClickListener { showEditDialog(GENERAL, OFFICIAL_NAME, BIRTH_DATE, AGE, BIRTH_PLACE ) }
        binding.cardViewMedical.setOnClickListener { showEditDialog(MEDICAL, DOCTOR_VISIT, WORM_MEDICINE, VACCINATION, "") }
        binding.cardViewInsurance.setOnClickListener { showEditDialog(INSURANCE, INS_NAME, INS_PHONE, INS_END_DATE, INS_SUM) }
    }

    private fun showEditDialogNameAndPicture() {
        val dialogBinding = DialogEditImageAndNameBinding.inflate(requireActivity().layoutInflater)
        dialogBinding.textInputNameLayout.hint = "Cat name (on tab view)"
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Update name and image")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun showEditDialog(cardName: String, hintOne: String, hintTwo: String, hintThree: String, hintFour: String?) {
        val dialogBinding = DialogEditCatCardBinding.inflate(requireActivity().layoutInflater)
        dialogBinding.textInputOneLayout.hint = hintOne
        dialogBinding.textInputTwoLayout.hint = hintTwo
        dialogBinding.textInputThreeLayout.hint = hintThree
        dialogBinding.textInputFourLayout.hint = hintFour
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Update $cardName")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
//                val general = General(
//                    officialName = "",
//                    birthDate = "",
//                    age = 2,
//                    birthPlace = ""
//                )
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_POS = "position"
        fun newInstance(title: String, position: Int): CatCardsFragment {
            val fragment = CatCardsFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putInt(ARG_POS, position)
            fragment.arguments = args
            return fragment
        }
    }
}