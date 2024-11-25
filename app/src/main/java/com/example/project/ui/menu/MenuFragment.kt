package com.example.project.ui.menu

import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.base.extensions.showToast
import com.example.project.R
import com.example.project.databinding.FragmentMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private lateinit var viewBinding: FragmentMenuBinding
    private val viewModel: MenuViewModel by viewModels()

    private lateinit var projectionResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        projectionResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode != RESULT_OK) {
                requireContext().showToast(R.string.toast_denied_screen_sharing_permission)
            } else {
                viewModel.startScreenCaptureService(
                    resultCode = result.resultCode,
                    data = result.data!!
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.launchButton.setOnClickListener {
            viewModel.onStartPermissions(
                activity = requireActivity() as AppCompatActivity,
                onAllGranted = { showMediaProjectionPermission() }
            )
        }
    }

    private fun showMediaProjectionPermission() {
        ContextCompat.getSystemService(requireContext(), MediaProjectionManager::class.java)?.let { mediaProjectionManager ->
            projectionResultLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
        }
    }
}