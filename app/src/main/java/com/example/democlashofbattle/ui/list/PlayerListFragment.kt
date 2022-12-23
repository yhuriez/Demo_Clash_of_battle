package com.example.democlashofbattle.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.democlashofbattle.R
import com.example.democlashofbattle.databinding.FragmentPlayerListBinding
import com.example.democlashofbattle.databinding.ViewPlayerInfoBinding
import com.example.democlashofbattle.models.Player
import com.example.democlashofbattle.ui.battle.BattleFragment
import com.example.democlashofbattle.utils.getColor
import com.example.democlashofbattle.utils.getNameId
import com.example.democlashofbattle.utils.getPlayerJob
import com.example.democlashofbattle.utils.loadImage

class PlayerListFragment : Fragment() {

    private lateinit var viewModel: PlayerListViewModel

    private var binding: FragmentPlayerListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlayerListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerListBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.myPlayer.observe(viewLifecycleOwner) { player ->
            binding?.playerInfoView?.let { view ->
                initPlayerView(view, player)
            }
        }

        val adapter = PlayerAdapter {
            goToBattle(it)
        }

        binding?.opponentRecyclerView?.adapter = adapter

        viewModel.players.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initPlayerView(binding: ViewPlayerInfoBinding, item: Player) {
        binding.root.setOnClickListener {
            goToModify()
        }
        loadImage(binding.playerImage, item.imageUrl)
        binding.playerName.text = item.name

        val job = getPlayerJob(item)
        binding.playerJob.setText(job.getNameId())
        binding.playerJob.setTextColor(job.getColor(requireContext()))
    }

    private fun goToModify() {
        findNavController().navigate(R.id.action_playerListFragment_to_editPlayerFragment)
    }

    private fun goToBattle(id: Long) {
        val arguments = bundleOf(BattleFragment.OPPONENT_ID_ARG to id)
        findNavController().navigate(R.id.action_playerListFragment_to_battleFragment, arguments)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}