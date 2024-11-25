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
                        when(state) {
                            is LoginState.Idle -> {
                                viewBinding.authButton.isEnabled = true
                            }
                            is LoginState.Loading -> {
                                viewBinding.authButton.isEnabled = false
                            }
                            is LoginState.Success -> {
                                requireContext().showToast(R.string.toast_login_success)
                                findNavController().navigate(R.id.fragment_login_to_menu)
                            }
                            is LoginState.KeyNotFound -> {
                                requireContext().showToast(R.string.toast_login_not_found_key)
                                viewModel.resetToIdle()
                            }
                            is LoginState.NoInternet -> {
                                requireContext().showToast(R.string.toast_login_no_internet)
                                viewModel.resetToIdle()
                            }
                        }
                    }
                }
            }
        }
    }
}