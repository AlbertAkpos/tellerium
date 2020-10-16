package me.alberto.tellerium.screens.login.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import me.alberto.tellerium.App
import me.alberto.tellerium.R
import me.alberto.tellerium.databinding.ActivityLoginBinding
import me.alberto.tellerium.screens.common.base.BaseActivity
import me.alberto.tellerium.screens.dashboard.view.DashboardActivity
import me.alberto.tellerium.screens.login.viewmodel.LoginViewModel
import me.alberto.tellerium.util.extension.hideKeyboard
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        initView()
        setupObservers()
        setupClickListeners()

    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupObservers() {
        viewModel.navigate.observe(this) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

    private fun setupClickListeners() {

        binding.loginBtn.setOnClickListener {
            it.hideKeyboard()
            viewModel.validateCredentials()
        }

        binding.emailText.addTextChangedListener {
            binding.emailInput.isErrorEnabled = false
            binding.emailInput.error = null
        }

        binding.passwordText.addTextChangedListener {
            binding.passwordInput.isErrorEnabled = false
            binding.passwordInput.error = null
        }
    }

}