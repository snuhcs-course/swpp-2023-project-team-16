package com.example.shattle.ui.lostnfound

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shattle.databinding.FragmentLostnfoundBinding

class LostnfoundFragment : Fragment() {

    private var _binding: FragmentLostnfoundBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lostnfoundViewModel =
            ViewModelProvider(this).get(LostnfoundViewModel::class.java)

        _binding = FragmentLostnfoundBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textLostnfound
        lostnfoundViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}