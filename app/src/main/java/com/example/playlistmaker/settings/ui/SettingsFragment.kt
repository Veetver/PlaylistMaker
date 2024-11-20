package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()


    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.appThemeModeLiveData.observe(viewLifecycleOwner) { mode ->
            binding.themeSwitcher.setOnCheckedChangeListener(null)
            binding.themeSwitcher.isChecked = mode.value
            binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
                viewModel.changeTheme(checked)
            }
        }

        binding.shareAppItemSettings.setOnClickListener { viewModel.shareApp() }

        binding.supportItemSettings.setOnClickListener { viewModel.contactSupport() }

        binding.eulaItemSettings.setOnClickListener { viewModel.openEula() }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}