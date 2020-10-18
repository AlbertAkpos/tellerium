package me.alberto.tellerium.screens.dashboard.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import me.alberto.tellerium.App
import me.alberto.tellerium.R
import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.databinding.ActivityDashboardBinding
import me.alberto.tellerium.screens.common.base.BaseActivity
import me.alberto.tellerium.screens.dashboard.adapter.FarmerAdapter
import me.alberto.tellerium.screens.dashboard.adapter.FarmerAdapter.ItemClickListener
import me.alberto.tellerium.screens.dashboard.viewmodel.DashboardViewModel
import me.alberto.tellerium.screens.newfarmer.view.NewFarmerActivity
import javax.inject.Inject
import kotlin.math.round

class DashboardActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DashboardViewModel> { viewModelFactory }

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        initView()
        getFarmers()
        setupObservers()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.fab.setOnClickListener { goToNewFarmerActivity() }
    }

    private fun getFarmers() {
        viewModel.getFarmers()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.recyclerView.adapter = FarmerAdapter(onItemClicked)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupObservers() {
        viewModel.farmers.observe(this, { farmers ->
            farmers ?: return@observe
            val noOfFarmers = farmers.size
            var noOfFarms = 0
            farmers.map { farmer -> farmer.farms?.let { noOfFarms += it.size } }
            binding.farmers.text = noOfFarmers.toString()
            binding.farmLand.text = noOfFarms.toString()
            calculatePercentage(farmers)
        })

        viewModel.errorMessage.observe(this, {
            if (it.isNotEmpty()) {
                retry(it)
            }
        })
    }

    private fun calculatePercentage(farmers: List<FarmerEntity>) {
        val totalFarmers = farmers.size.toDouble()
        val noOfMen = farmers.filter { it.gender.toLowerCase() == getString(R.string.male) }.size.toDouble()
        val noOfWomen = farmers.filter { it.gender.toLowerCase() == getString(R.string.female) }.size.toDouble()

        var percentageOfMen = if (totalFarmers == 0.0 || noOfMen == 0.0) {
            0
        } else {
            ( noOfMen / totalFarmers ) * 100
        }

        var percentageOfWomen = if (totalFarmers == 0.0 || noOfWomen == 0.0) {
            0
        } else {
            ( noOfWomen/ totalFarmers) * 100
        }

        percentageOfMen = round(percentageOfMen.toFloat()).toInt()
        percentageOfWomen = round(percentageOfWomen.toFloat()).toInt()

        binding.menStat.text = getString(R.string.percent, percentageOfMen.toString())
        binding.womenStat.text = getString(R.string.percent, percentageOfWomen.toString())
    }

    private val onItemClicked = object : ItemClickListener {
        override fun onDelete(farmer: FarmerEntity) {
            showDilaog(
                listener = { viewModel.onDeleteFarmer(farmer) },
                title = getString(R.string.delete),
                message = getString(R.string.delete_confirm, farmer.name),
                has_neg = true,
                negBtnText = null,
                btnText = getString(R.string.ok)
            )
        }

        override fun onNavigate(id: Long) {
        }

        override fun onEdit(farmer: FarmerEntity) {
            goToNewFarmerActivity(farmer)
        }

    }

    private fun retry(message: String) {
        val snackbar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Retry") {
            viewModel.getFarmers()
        }
        snackbar.show()
    }

    private fun goToNewFarmerActivity(extra: FarmerEntity? = null) {
        val intent = Intent(this@DashboardActivity, NewFarmerActivity::class.java)
        intent.putExtra(NewFarmerActivity.EDIT_FARMER, extra)
        startActivity(intent)

    }
}