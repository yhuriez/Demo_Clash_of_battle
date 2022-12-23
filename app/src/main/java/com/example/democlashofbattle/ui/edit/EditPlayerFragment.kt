package com.example.democlashofbattle.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.democlashofbattle.databinding.FragmentEditPlayerBinding
import com.example.democlashofbattle.databinding.IncludeCapabilityViewBinding
import com.example.democlashofbattle.models.Capability
import com.example.democlashofbattle.ui.capabilities.SelectCapabilityActivity
import com.example.democlashofbattle.utils.getColor
import com.example.democlashofbattle.utils.getNameId
import com.example.democlashofbattle.utils.loadImage
import kotlinx.coroutines.launch

class EditPlayerFragment : Fragment() {

    private lateinit var viewModel: EditPlayerViewModel

    private var binding: FragmentEditPlayerBinding? = null

    private val selectCapabilityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.let { intent ->
            val pair = SelectCapabilityActivity.extractResultData(intent)
            // pair.first = index de votre capacité (1,2,3)
            // pair.second = capability sélectionné par votre utilisateur
            viewModel.updateCapability(pair.first, pair.second)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditPlayerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPlayerBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = checkNotNull(binding)

        viewModel.myPlayer.observe(viewLifecycleOwner) {
            binding.playerName.setText(it.name)
            binding.playerImageUrl.setText(it.imageUrl)
            loadImage(binding.playerImage, it.imageUrl)

            configureCapabilityView(binding.firstCapabilityView, it.capability1, 0)
            configureCapabilityView(binding.secondCapabilityView, it.capability2, 1)
            configureCapabilityView(binding.thirdCapabilityView, it.capability3, 2)
        }

        binding.playerValidateButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.validate(
                    binding.playerName.text.toString(),
                    binding.playerImageUrl.text.toString()
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun configureCapabilityView(
        binding: IncludeCapabilityViewBinding,
        capability: Capability?,
        index: Int
    ) {
        binding.capabilityLabel.text = "Compétence ${index + 1}"
        binding.capabilityValue.isVisible = capability != null

        capability?.let {
            binding.capabilityValue.setText(capability.getNameId())
            binding.capabilityValue.setTextColor(capability.getColor(requireContext()))
        }

        binding.capabilityModifyButton.setOnClickListener {
            selectCapabilityLauncher.launch(
                SelectCapabilityActivity.newIntent(
                    requireContext(),
                    index
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}