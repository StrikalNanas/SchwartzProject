package com.example.project.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.base.extensions.showToast
import com.example.project.R
import com.example.project.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var viewBinding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.apply {
            authButton.setOnClickListener {
                val key = keyInput.text.toString().trim()
                viewModel.checkValidKey(key)
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loginState.collect { state ->
                        Log.d("MyLog", "Login state: $state")
                        when(state) {
                            is LoginState.Idle -> handleIdle()
                            is LoginState.Loading -> handleLoading()
                            is LoginState.Success -> handleSuccess()
                            is LoginState.KeyNotFound -> handleKeyNotFound()
                            is LoginState.NoInternet -> handleNoInternet()
                        }
                    }
                }
            }
        }
    }

    private fun handleIdle() {
        viewBinding.authButton.isEnabled = true
    }

    private fun handleLoading() {
        viewBinding.authButton.isEnabled = false
    }

    private fun handleSuccess() {
        requireContext().showToast(R.string.toast_login_success)
        findNavController().navigate(R.id.fragment_login_to_menu)
    }

    private fun handleKeyNotFound() {
        requireContext().showToast(R.string.toast_login_not_found_key)
        viewModel.resetToIdle()
    }

    private fun handleNoInternet() {
        requireContext().showToast(R.string.toast_login_no_internet)
        viewModel.resetToIdle()
    }
}