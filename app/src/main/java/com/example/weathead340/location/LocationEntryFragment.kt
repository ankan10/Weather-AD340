package com.example.weathead340.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.weathead340.Location
import com.example.weathead340.LocationRepository
import com.example.weathead340.R

class LocationEntryFragment : Fragment() {

    private lateinit var locationRepository: LocationRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_location_entry, container, false)
        var pincode: EditText = view.findViewById(R.id.editPinCode)
        var submitButton: Button = view.findViewById(R.id.buttonSubmit)
        locationRepository = LocationRepository(requireContext())



        // OnSubmit
        submitButton.setOnClickListener {
            val zipCode: String = pincode.text.toString().trim()
            if (zipCode.length != 5) {
                Toast.makeText(view.context, "Not a valid Pin code", Toast.LENGTH_SHORT).show()
            } else {
                locationRepository.saveLocation(Location.Zipcode(zipCode))
                findNavController().navigateUp()
            }
        }

        return view
    }
}