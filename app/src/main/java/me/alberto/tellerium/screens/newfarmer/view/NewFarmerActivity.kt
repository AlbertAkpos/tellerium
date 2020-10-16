package me.alberto.tellerium.screens.newfarmer.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.theartofdev.edmodo.cropper.CropImage
import me.alberto.tellerium.App
import me.alberto.tellerium.R
import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.databinding.ActivityNewFarmerBinding
import me.alberto.tellerium.screens.common.base.BaseActivity
import me.alberto.tellerium.screens.common.dialogs.DateModal
import me.alberto.tellerium.screens.newfarmer.viewmodel.NewFarmerViewModel
import me.alberto.tellerium.util.DomainDateFormater
import me.alberto.tellerium.util.extension.setGender
import javax.inject.Inject

class NewFarmerActivity : BaseActivity(), DateModal.DatePickerListener {

    private lateinit var binding: ActivityNewFarmerBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var domainDateFormater: DomainDateFormater

    private val viewModel by viewModels<NewFarmerViewModel> { factory }

    private val farmerToEdit by lazy {
        intent?.getParcelableExtra<FarmerEntity>(EDIT_FARMER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        initView()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.addProfileImg.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).start(this)
        }

        binding.dob.setOnClickListener {
            val datePicker = DateModal.newInstance(this)
            datePicker.show(supportFragmentManager, datePicker.javaClass.name)
        }
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_farmer)
        binding.genderSelect.setGender()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.editFarmer(farmerToEdit)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            val imageUrl = CropImage.getActivityResult(data).uri.toString()
            viewModel.setImage(imageUrl)
        }
    }

    override fun onDatePicked(year: Int, month: Int, dayOfMonth: Int) {
        val date = domainDateFormater.getDate(year, month, dayOfMonth)
        val dateAsString = domainDateFormater.formatDateAsString("MM/dd/yyyy", date)
        viewModel.setDate(dateAsString)
    }

    companion object {
        const val EDIT_FARMER = "edit_farmer"
    }
}