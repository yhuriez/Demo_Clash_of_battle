package com.example.democlashofbattle.ui.battle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.democlashofbattle.databinding.FragmentBattleBinding
import com.example.democlashofbattle.models.Capability
import com.example.democlashofbattle.ui.battle.engine.getTextForActionResult
import com.example.democlashofbattle.utils.getColor
import com.example.democlashofbattle.utils.getNameId
import com.example.democlashofbattle.utils.loadImage
import java.lang.Integer.max

class BattleFragment : Fragment() {

    companion object {
        const val OPPONENT_ID_ARG = "OPPONENT_ID_ARG"
    }

    private lateinit var viewModel: BattleViewModel

    private var binding: FragmentBattleBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BattleViewModel::class.java)

        val playerId = arguments?.getLong(OPPONENT_ID_ARG)
            ?: throw IllegalStateException("Should have an opponent id")

        viewModel.init(playerId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBattleBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = checkNotNull(binding)

        viewModel.myPlayerInfo.observe(viewLifecycleOwner) {
            loadImage(binding.myPlayerImage, it.imageUrl)
            binding.myPlayerName.text = it.name
        }

        viewModel.opponentInfo.observe(viewLifecycleOwner) {
            loadImage(binding.opponentImage, it.imageUrl)
            binding.opponentName.text = it.name
        }

        viewModel.myPlayerBattleInfo.observe(viewLifecycleOwner) {
            val pv = max(0, it.pv)
            binding.myPlayerLifebar.progress = pv
            binding.myPlayerPv.text = "$pv / 50"

            updateCapabilityButton(binding.capability1Button, it.remainingCapabilities.getOrNull(0))
            updateCapabilityButton(binding.capability2Button, it.remainingCapabilities.getOrNull(1))
            updateCapabilityButton(binding.capability3Button, it.remainingCapabilities.getOrNull(2))
        }

        viewModel.opponentBattleInfo.observe(viewLifecycleOwner) {
            val pv = max(0, it.pv)
            binding.opponentLifebar.progress = pv
            binding.opponentPv.text = "$pv / 50"
        }

        viewModel.rountCount.observe(viewLifecycleOwner) {
            binding.roundCountLabel.isVisible = it != 0
            binding.roundCountLabel.text = "Tour n°$it"
        }

        viewModel.lastPlayerResult.observe(viewLifecycleOwner) { actionResult ->
            // Donne le text à afficher
            binding.roundMyPlayerAction.text = getTextForActionResult(
                requireContext(),
                viewModel.myPlayerInfo.value!!,
                actionResult
            )
            // Donne une couleur au texte s'il s'agit d'une capacité
            actionResult.usedCapability?.let {
                binding.roundMyPlayerAction.setTextColor(it.getColor(requireContext()))
            }
        }

        viewModel.lastOpponentResult.observe(viewLifecycleOwner) { actionResult ->
            // Donne le text à afficher
            binding.roundOpponentAction.text = getTextForActionResult(
                requireContext(),
                viewModel.opponentInfo.value!!,
                actionResult
            )
            // Donne une couleur au texte s'il s'agit d'une capacité
            actionResult.usedCapability?.let {
                binding.roundOpponentAction.setTextColor(it.getColor(requireContext()))
            }
        }

        viewModel.winner.observe(viewLifecycleOwner) {
            binding.winnerLabel.isVisible = it != null
            binding.winnerLabel.text = "$it gagne !"
        }

        binding.simpleAttackButton.setOnClickListener {
            viewModel.attack()
        }
    }

    private fun updateCapabilityButton(button: Button, capability: Capability?) {
        button.isVisible = capability != null

        capability?.let { capa ->
            button.setText(capa.getNameId())
            button.setBackgroundColor(capa.getColor(requireContext()))
            button.setOnClickListener {
                viewModel.attack(capa)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}