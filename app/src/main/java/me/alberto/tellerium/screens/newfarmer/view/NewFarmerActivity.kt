package me.alberto.tellerium.screens.newfarmer.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import me.alberto.tellerium.App
import me.alberto.tellerium.R
import me.alberto.tellerium.data.local.db.Farm
import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.databinding.ActivityNewFarmerBinding
import me.alberto.tellerium.screens.common.base.BaseActivity
import me.alberto.tellerium.screens.common.dialogs.DateModal
import me.alberto.tellerium.screens.map.view.MapsActivity
import me.alberto.tellerium.screens.newfarmer.adapter.FarmAdapter
import me.alberto.tellerium.screens.newfarmer.viewmodel.NewFarmerViewModel
import me.alberto.tellerium.util.DomainDateFormater
import permissions.dispatcher.*
import javax.inject.Inject

const val MAP_RC = 101

@RuntimePermissions
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
        initToolbar()
        setObservers()
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

        binding.addFarm.setOnClickListener {
            canGetLocationWithPermissionCheck()
        }
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_farmer)
        binding.farmsContainer.adapter = FarmAdapter(farmItemListener)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.editFarmer(farmerToEdit)
    }

    private val farmItemListener = object : FarmAdapter.ItemClickListener{
        override fun onDelete(farm: Farm) {
            viewModel.deleteFarm(farm)
        }

        override fun onNavigate(farm: Farm) {
           val intent = Intent(this@NewFarmerActivity, MapsActivity::class.java)
            intent.putExtra(MapsActivity.FARM, farm)
            startActivity(intent)
        }
    }

    private fun initToolbar() {

        binding.rootToolbar.toolbar.title =
            if (farmerToEdit != null) getString(R.string.edit) else getString(
                R.string.create
            )


        binding.rootToolbar.toolbar.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_arrow_left)

        binding.rootToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    private fun setObservers() {

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

        if (requestCode == MAP_RC && data != null) {

            val farm = data.getParcelableExtra<Farm>(MapsActivity.FARM)
            farm?.let { viewModel.setFarmList(farm) }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onDatePicked(year: Int, month: Int, dayOfMonth: Int) {
        val date = domainDateFormater.getDate(year, month, dayOfMonth)
        val dateAsString = domainDateFormater.formatDateAsString("MM/dd/yyyy", date)
        viewModel.setDate(dateAsString)
    }

    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun canGetLocation() {
        startActivityForResult(Intent(this, MapsActivity::class.java), MAP_RC)
    }

    @OnShowRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationnale(request: PermissionRequest) {
        showPermissionsRationale(request)
    }

    @OnPermissionDenied(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationPermissionDenied() {
        showMessage(binding.root, getString(R.string.permission_denied_rationale))
    }

    @OnNeverAskAgain(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun onNeverAskForLocationPermissionAgain() {
        val snackbar = Snackbar.make(
            binding.root,
            getString(R.string.needs_location_to_work),
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("Enabled") {
            canGetLocationWithPermissionCheck()
        }
    }


    companion object {
        const val EDIT_FARMER = "edit_farmer"
    }
}