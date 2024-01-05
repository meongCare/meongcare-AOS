package com.project.meongcare.excreta.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.meongcare.R
import com.project.meongcare.databinding.FragmentExcretaBinding
import com.project.meongcare.excreta.model.entities.Excreta
import com.project.meongcare.excreta.model.entities.Excreta.FECES
import com.project.meongcare.excreta.viewmodel.ExcretaRecordViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class ExcretaFragment : Fragment() {
    private var _excretaBinding: FragmentExcretaBinding? = null
    private val excretaBinding get() = _excretaBinding!!

    private val excretaRecordViewModel: ExcretaRecordViewModel by viewModels()
    private lateinit var excretaAdapter: ExcretaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _excretaBinding = FragmentExcretaBinding.inflate(inflater, container, false)
        return excretaBinding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        excretaAdapter = ExcretaAdapter()
        initExcretaAddButton()
        initExcretaEditButton()
        initExcretaRecordRecyclerView()
        fetchExcretaRecord()
    }

    private fun initExcretaAddButton() {
        binding.textviewExcretaAddbutton.setOnClickListener {
            findNavController().navigate(R.id.action_excretaFragment_to_excretaAddFragment)
        }
    }

    private fun initExcretaEditButton() {
        excretaBinding.textviewExcretaEditbutton.setOnClickListener {
            findNavController().navigate(R.id.action_excretaFragment_to_excretaRecordEditFragment)
        }
    }

    private fun initExcretaRecordRecyclerView() {
        excretaBinding.recyclerviewExcretaRecord.run {
            adapter = excretaAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun fetchExcretaRecord() {
        excretaRecordViewModel.apply {
            val dateTime = LocalDateTime.now().toString().slice(DATE_TIME_START..DATE_TIME_END)
            getExcretaRecord(dateTime)
            excretaRecordGet.observe(viewLifecycleOwner) { response ->
                excretaBinding.apply {
                    if (response.excretaRecords.size == 0) {
                        textviewExcretaEditbutton.visibility = View.GONE
                    }
                    textviewExcretaNumberfeces.text = formatExcretaCount(Excreta.FECES.type, response.fecesCount)
                    textviewExcretaNumberurine.text = formatExcretaCount(Excreta.URINE.type, response.urineCount)
                    excretaAdapter.submitList(response.excretaRecords)
                }
            }
        }
    }

    private fun formatExcretaCount(
        type: String,
        count: Int,
    ) = "$type $count$TIME"

    override fun onDestroyView() {
        super.onDestroyView()
        _excretaBinding = null
    }

    companion object {
        const val DATE_TIME_START = 0
        const val DATE_TIME_END = 18
        const val TIME = "회"
    }
}
