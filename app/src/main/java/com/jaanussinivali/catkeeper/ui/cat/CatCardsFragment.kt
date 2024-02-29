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
import com.jaanussinivali.catkeeper.data.entity.General
import com.jaanussinivali.catkeeper.data.entity.Insurance
import com.jaanussinivali.catkeeper.data.entity.Medical
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
            fragmentNumber = it.getInt((ARG_POS)) + 1
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
//            val cat: Cat = catDao.getCat(fragmentNumber)
            val general = catDao.getGeneral(fragmentNumber)
            val medical = catDao.getMedical(fragmentNumber)
            val insurance = catDao.getInsurance(fragmentNumber)

            requireActivity().runOnUiThread {
                binding.textViewOfficialNameValue.text = general.officialName
                binding.textViewBirthDateValue.text = general.birthDate
                binding.textViewAgeValue.text = general.age.toString()
                binding.textViewBirthPlaceValue.text = general.birthPlace

                binding.textViewLastDoctorVisitDate.text = medical.lastDoctorVisit
                binding.textViewLastWormMedicineDate.text = medical.lastWormMedicine
                binding.textViewLastVaccinationDate.text = medical.lastVaccination

                binding.textViewInsuranceNameValue.text = insurance.company
                binding.textViewInsurancePhoneNumber.text = insurance.phone
                binding.textViewInsuranceValidUntilDate.text = insurance.validUntil
                binding.textViewInsuranceSumValue.text = insurance.sum.toString()
            }
        }
    }

    private fun setOnClickListeners() {
        binding.cardViewImage.setOnClickListener { showEditDialogNameAndPicture() }
        binding.cardViewGeneral.setOnClickListener {
            showEditDialog(
                GENERAL,
                OFFICIAL_NAME,
                BIRTH_DATE,
                AGE,
                BIRTH_PLACE
            )
        }
        binding.cardViewMedical.setOnClickListener {
            showEditDialog(
                MEDICAL,
                DOCTOR_VISIT,
                WORM_MEDICINE,
                VACCINATION,
                ""
            )
        }
        binding.cardViewInsurance.setOnClickListener {
            showEditDialog(
                INSURANCE,
                INS_NAME,
                INS_PHONE,
                INS_END_DATE,
                INS_SUM
            )
        }
    }

    private fun showEditDialogNameAndPicture() {
        val dialogBinding = DialogEditImageAndNameBinding.inflate(requireActivity().layoutInflater)
        dialogBinding.textInputNameLayout.hint = "Cat name (on tab view)"
        thread {
            val cat: Cat = catDao.getCat(fragmentNumber)
            requireActivity().runOnUiThread {
                dialogBinding.textInputNameValue.setText(cat?.name)
            }
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Update name and image")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                thread {
                    catDao.update(
                        Cat(
                            id = fragmentNumber,
                            name = dialogBinding.textInputNameValue.text.toString()
                        )
                    )
                }
                displayFields()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun showEditDialog(
        cardName: String,
        hintOne: String,
        hintTwo: String,
        hintThree: String,
        hintFour: String?
    ) {
        val dialogBinding = DialogEditCatCardBinding.inflate(requireActivity().layoutInflater)
        dialogBinding.textInputOneLayout.hint = hintOne
        dialogBinding.textInputTwoLayout.hint = hintTwo
        dialogBinding.textInputThreeLayout.hint = hintThree
        dialogBinding.textInputFourLayout.hint = hintFour
        when (cardName) {
            GENERAL -> {
                thread {
                    val general = catDao.getGeneral(fragmentNumber)
                    requireActivity().runOnUiThread {
                        dialogBinding.textInputOneValue.setText(general.officialName)
                        dialogBinding.textInputTwoValue.setText(general.birthDate)
                        dialogBinding.textInputThreeValue.setText(general.age.toString())
                        dialogBinding.textInputFourValue.setText(general.birthPlace)
                    }
                }
            }

            MEDICAL -> {
                thread {
                    val medical = catDao.getMedical(fragmentNumber)
                    requireActivity().runOnUiThread {
                        dialogBinding.textInputOneValue.setText(medical.lastDoctorVisit)
                        dialogBinding.textInputTwoValue.setText(medical.lastWormMedicine)
                        dialogBinding.textInputThreeValue.setText(medical.lastVaccination)
                    }
                }
            }

            INSURANCE -> {
                thread {
                    val insurance = catDao.getInsurance(fragmentNumber)
                    requireActivity().runOnUiThread {
                        dialogBinding.textInputOneValue.setText(insurance.company)
                        dialogBinding.textInputTwoValue.setText(insurance.phone)
                        dialogBinding.textInputThreeValue.setText(insurance.validUntil)
                        dialogBinding.textInputFourValue.setText(insurance.sum.toString())
                    }
                }
            }
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Update $cardName")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                when (cardName) {
                    GENERAL -> {
                        thread {
                            catDao.update(
                                General(
                                    id = fragmentNumber,
                                    catId = fragmentNumber,
                                    officialName = dialogBinding.textInputOneValue.text.toString(),
                                    birthDate = dialogBinding.textInputTwoValue.text.toString(),
                                    age = dialogBinding.textInputThreeValue.text.toString().toInt(),
                                    birthPlace = dialogBinding.textInputFourValue.text.toString()
                                )
                            )
                        }
                        displayFields()
                    }

                    MEDICAL -> {
                        thread {
                            catDao.update(
                                Medical(
                                    id = fragmentNumber,
                                    catId = fragmentNumber,
                                    lastDoctorVisit = dialogBinding.textInputOneValue.text.toString(),
                                    lastWormMedicine = dialogBinding.textInputTwoValue.text.toString(),
                                    lastVaccination = dialogBinding.textInputThreeValue.text.toString()
                                )
                            )
                        }
                        displayFields()
                    }

                    INSURANCE -> {
                        thread {
                            catDao.update(
                                Insurance(
                                    id = fragmentNumber,
                                    catId = fragmentNumber,
                                    company = dialogBinding.textInputOneValue.text.toString(),
                                    phone = dialogBinding.textInputTwoValue.text.toString(),
                                    validUntil = dialogBinding.textInputThreeValue.text.toString(),
                                    sum = dialogBinding.textInputFourValue.text.toString().toInt()
                                )
                            )
                        }
                        displayFields()
                    }
                }
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