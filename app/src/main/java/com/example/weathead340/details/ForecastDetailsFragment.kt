package com.example.weathead340.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.weathead340.*
import com.example.weathead340.databinding.FragmentForecastDetailsBinding
import java.text.SimpleDateFormat


private val DATE_FORMAT = SimpleDateFormat("MM-dd-yyyy")


class ForecastDetailsFragment : Fragment() {



    private val args: ForecastDetailsFragmentArgs by navArgs()
    private var _binding: FragmentForecastDetailsBinding?= null
    // This property only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory
    private val viewModel: ForecastDetailViewModel by viewModels(
        factoryProducer = {
            viewModelFactory
        }
    )

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        viewModelFactory =  ForecastDetailsViewModelFactory(args)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewStateObserver = Observer<ForecastDetailsViewState> { viewState ->
            // update the UI
            binding.tempText.text= formatTempForDisplay(viewState.temp, tempDisplaySettingManager.getTempDisplaySetting())
            binding.descripionText.text= viewState.description
            binding.dateText.text = viewState.date
            binding.forecastIcon.load((viewState.icon))
        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}