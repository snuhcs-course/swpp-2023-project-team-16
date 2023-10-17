package com.example.shattle.ui.lostnfound

import ItemAdapter
import android.content.ClipData.Item
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shattle.databinding.FragmentLostnfoundBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LostnfoundFragment : Fragment() {

    private var _binding: FragmentLostnfoundBinding? = null

    private lateinit var recyclerView: RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLostnfoundBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = ItemAdapter(lostItems)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val lostItems = listOf(
        LostItem(1, "item 1", "description 1"),
        LostItem(2, "item 2", "description 2"),
        LostItem(3, "item 3", "description 3"),
        LostItem(4, "item 4", "description 4"),
        LostItem(5, "item 5", "description 5"),
        LostItem(6, "item 6", "description 6"),

    )

    data class LostItem(
        val id: Int,
        val name: String,
        val description: String,
    )
}