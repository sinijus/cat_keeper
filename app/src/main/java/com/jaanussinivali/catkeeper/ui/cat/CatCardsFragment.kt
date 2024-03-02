package com.jaanussinivali.catkeeper.ui.cat


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import com.jaanussinivali.catkeeper.data.entity.Cat
import com.jaanussinivali.catkeeper.data.entity.General
import com.jaanussinivali.catkeeper.data.entity.Insurance
import com.jaanussinivali.catkeeper.data.entity.Medical
import com.jaanussinivali.catkeeper.data.entity.Weight
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
import kotlin.math.roundToInt


class CatCardsFragment : Fragment() {
    private var fragmentTitle: String? = null
    private var fragmentNumber: Int = 0
    private lateinit var binding: FragmentCatCardsBinding
    private val catDao: CatDao by lazy { CatKeeperDatabase.getDatabase(requireContext()).getCatDao() }
    private lateinit var cat: Cat
    private lateinit var general: General
    private lateinit var medical: Medical
    private lateinit var insurance: Insurance
    private lateinit var weights: List<Weight>


//    private val selectImageIntent by lazy {
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            binding.imageViewMainPic.setImageURI(uri)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentTitle = it.getString(ARG_TITLE)
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
            cat = catDao.getCat(fragmentNumber)
            general = catDao.getGeneral(fragmentNumber)
            medical = catDao.getMedical(fragmentNumber)
            insurance = catDao.getInsurance(fragmentNumber)
            weights = catDao.getWeights(fragmentNumber)

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
                setAndDisplayWeightChart()
            }
        }
    }

    private fun setAndDisplayWeightChart() {
        val anyChartView = binding.anyChartView
        val cartesian = AnyChart.line()
        cartesian.title("Weight")
//        cartesian.animation(true)
        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true)
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.yAxis(0).title("kg")
        cartesian.xAxis(0).labels().padding(0.0)
        val seriesData: MutableList<DataEntry> = ArrayList()
        for (weight in weights) {
//            if (weight.catId == null || weight.date == null || weight.value == null) {
////                seriesData.add(ValueDataEntry("", 0))
//                binding.cardViewChart.visibility = View.GONE
//                break
//            } else
                seriesData.add(ValueDataEntry(weight.date, weight.value))
//            Toast(context, weight.value.toString(), Toast.LENGTH_LONG).show()

        }
//        seriesData.add(ValueDataEntry("12.12.2023", 3.0))
//        seriesData.add(ValueDataEntry("9.01.2024", 3.1))
//        seriesData.add(ValueDataEntry("5.02.2024", 3.0))
//        seriesData.add(ValueDataEntry("2.03.2024", 2.9))

        val series1: Line = cartesian.line(seriesData)
        series1.name(cat.name)
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(0.0)
            .offsetY(0.0)
        cartesian.legend().fontColor("#111111")
        anyChartView.setBackgroundColor("#FFFFFF")
        anyChartView.setChart(cartesian)
    }

    private fun setOnClickListeners() {
        binding.cardViewImage.setOnClickListener { showEditDialogNameAndPicture() }
        binding.imageViewGeneralEditIcon.setOnClickListener {
            showEditDialog(GENERAL, OFFICIAL_NAME, BIRTH_DATE, AGE, BIRTH_PLACE)
        }
        binding.imageViewMedicalEditIcon.setOnClickListener {
            showEditDialog(MEDICAL, DOCTOR_VISIT, WORM_MEDICINE, VACCINATION, "")
        }
        binding.imageViewInsuranceEditIcon.setOnClickListener {
            showEditDialog(INSURANCE, INS_NAME, INS_PHONE, INS_END_DATE, INS_SUM)
        }
        binding.imageViewWeightChartEditIcon.setOnClickListener {
//            showEditDialogWeightChart()
        }
    }

    private fun showEditDialogWeightChart() {
        TODO("Not yet implemented")
    }

    private fun showEditDialogNameAndPicture() {
        val dialogBinding = DialogEditImageAndNameBinding.inflate(requireActivity().layoutInflater)
        dialogBinding.textInputNameLayout.hint = "Cat name (on tab view)"
        thread {
            requireActivity().runOnUiThread { dialogBinding.textInputNameValue.setText(cat.name) }
        }

        dialogBinding.buttonInsertGalleryImage.setOnClickListener {
//            selectImageIntent.launch("image/*")
        }

        MaterialAlertDialogBuilder(requireContext()).setTitle("Update name and image").setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                thread {
                    catDao.update(
                        Cat(id = fragmentNumber, name = dialogBinding.textInputNameValue.text.toString())
                    )
                }
                displayFields()
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.show()
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
                    general = catDao.getGeneral(fragmentNumber)
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
                    medical = catDao.getMedical(fragmentNumber)
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
                    insurance = catDao.getInsurance(fragmentNumber)
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
//                val general = General(
//                    officialName = "",
//                    birthDate = "",
//                    age = 2,
//                    birthPlace = ""
//                )
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.show()
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
//
//
//private class CustomDataEntry internal constructor(x: String?, value: Number?) :
//    ValueDataEntry(x, value) {
//}


