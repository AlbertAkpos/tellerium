package me.alberto.tellerium.screens.dashboard.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import me.alberto.tellerium.App
import me.alberto.tellerium.R
import me.alberto.tellerium.databinding.ActivityDashboardBinding
import me.alberto.tellerium.screens.dashboard.adapter.FarmerAdapter
import me.alberto.tellerium.screens.dashboard.viewmodel.DashboardViewModel
import javax.inject.Inject

class DashboardActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DashboardViewModel> { viewModelFactory }

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        initView()
        getFarmers()
    }

    private fun getFarmers() {
        viewModel.getFarmers()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.recyclerView.adapter = FarmerAdapter()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}