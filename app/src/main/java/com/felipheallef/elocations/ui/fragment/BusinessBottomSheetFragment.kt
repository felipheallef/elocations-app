package com.felipheallef.elocations.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.felipheallef.elocations.R
import com.felipheallef.elocations.data.model.Business
import com.felipheallef.elocations.databinding.BottomsheetBusinessDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BusinessBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var business: Business
    private lateinit var binding: BottomsheetBusinessDetailsBinding

    override fun getTheme(): Int {
        return R.style.Theme_ELocations_BottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottomsheet_business_details, container, false)
        binding = BottomsheetBusinessDetailsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBusinessInfo()
    }

    private fun setupBusinessInfo() {

        binding.tvBusinessName.text = business.name
        binding.tvBusinessDescription.text = business.description

    }

    companion object {

        fun getInstance(business: Business): BusinessBottomSheetFragment{
            val fragment = BusinessBottomSheetFragment()
            fragment.business = business
            return fragment
        }

    }
}