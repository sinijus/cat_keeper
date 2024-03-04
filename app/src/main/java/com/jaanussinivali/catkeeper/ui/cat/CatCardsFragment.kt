package com.jaanussinivali.catkeeper.ui.cat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.core.cartesian.series.Line
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jaanussinivali.catkeeper.data.CatDao
import com.jaanussinivali.catkeeper.data.CatKeeperDatabase
import com.jaanussinivali.catkeeper.data.entity.General
import com.jaanussinivali.catkeeper.data.entity.Insurance
import com.jaanussinivali.catkeeper.data.entity.Medical
import com.jaanussinivali.catkeeper.databinding.DialogEditCatCardBinding
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
    private var fragmentNumber: Int = 0
    private lateinit var binding: FragmentCatCardsBinding
    private val catDao: CatDao by lazy { CatKeeperDatabase.getDatabase(requireContext()).getCatDao() }

    fun getFragmentNumber(): Int {
        return fragmentNumber
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentNumber = it.getInt((ARG_POS)) + 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
            val general = catDao.getGeneral(fragmentNumber)
            val medical = catDao.getMedical(fragmentNumber)
            val insurance = catDao.getInsurance(fragmentNumber)

            requireActivity().runOnUiThread {
                binding.textViewOfficialNameValue.text = general.officialName
                binding.textViewBirthDateValue.text = general.birthDate
                binding.textViewAgeValue.text = general.age
                binding.textViewBirthPlaceValue.text = general.birthPlace

                binding.textViewLastDoctorVisitDate.text = medical.lastDoctorVisit
                binding.textViewLastWormMedicineDate.text = medical.lastWormMedicine
                binding.textViewLastVaccinationDate.text = medical.lastVaccination

                binding.textViewInsuranceNameValue.text = insurance.company
                binding.textViewInsurancePhoneNumber.text = insurance.phone
                binding.textViewInsuranceValidUntilDate.text = insurance.validUntil
                binding.textViewInsuranceSumValue.text = insurance.sum
            }
        }
    }

    private fun setOnClickListeners() {
        thread {
            val insurance = catDao.getInsurance(fragmentNumber)
            requireActivity().runOnUiThread {
                binding.textViewInsurancePhoneNumber.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel: ${insurance.phone}")
                    }
                    startActivity(intent)
                }
            }
        }

        binding.imageViewGeneralEditIcon.setOnClickListener {
            showEditDialog(GENERAL, OFFICIAL_NAME, BIRTH_DATE, AGE, BIRTH_PLACE)
        }
        binding.imageViewMedicalEditIcon.setOnClickListener {
            showEditDialog(MEDICAL, DOCTOR_VISIT, WORM_MEDICINE, VACCINATION, "")
        }
        binding.imageViewInsuranceEditIcon.setOnClickListener {
            showEditDialog(INSURANCE, INS_NAME, INS_PHONE, INS_END_DATE, INS_SUM)
        }
    }

    private fun showEditDialog(
        cardName: String, hintOne: String, hintTwo: String, hintThree: String, hintFour: String?
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
                        dialogBinding.textInputFourLayout.visibility = View.GONE
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
        MaterialAlertDialogBuilder(requireContext()).setTitle("Update $cardName").setView(dialogBinding.root)
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
                                    age = dialogBinding.textInputThreeValue.text.toString(),
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
                                    sum = dialogBinding.textInputFourValue.text.toString()
                                )
                            )
                        }
                        displayFields()
                    }
                }
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.show()
    }

    companion object {
        private const val ARG_POS = "position"
        fun newInstance(position: Int): CatCardsFragment {
            val fragment = CatCardsFragment()
            val args = Bundle()
            args.putInt(ARG_POS, position)
            fragment.arguments = args
            return fragment
        }
    }
}



