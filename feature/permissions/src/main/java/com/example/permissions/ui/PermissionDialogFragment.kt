package com.example.permissions.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.permissions.EXTRA_RESULT_KEY_PERMISSION_STATE
import com.example.permissions.FRAGMENT_RESULT_KEY_PERMISSION_STATE
import com.example.permissions.databinding.DialogPermissionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class PermissionDialogFragment : DialogFragment() {

    private lateinit var viewBinding: DialogPermissionBinding
    private val viewModel: DialogPermissionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initResultLauncherIfNeeded(this) { isGranted ->
            if (isGranted) dismiss()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.permissionDialogUiState.collect(::updateDialogUiState) }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewBinding = DialogPermissionBinding.inflate(layoutInflater).apply {
            buttonRequestPermission.setOnClickListener {
                viewModel.startRequestIfNeeded(requireContext())
            }
            buttonDenyPermission.setOnClickListener {
                dismiss()
            }
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setView(viewBinding.root)
            .create()
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.shouldBeDismissedOnResume(requireContext())) {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setFragmentResult(
            requestKey = FRAGMENT_RESULT_KEY_PERMISSION_STATE,
            result = bundleOf(EXTRA_RESULT_KEY_PERMISSION_STATE to viewModel.isPermissionGranted(requireContext()))
        )
    }

    private fun updateDialogUiState(state: PermissionDialogUiState?) {
        state ?: return

        viewBinding.titlePermission.setText(state.titleRes)
        viewBinding.descPermission.setText(state.descRes)
    }
}