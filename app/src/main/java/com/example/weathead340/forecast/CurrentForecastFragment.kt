package com.example.weathead340.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.weathead340.*
import com.example.weathead340.api.CurrentWeather
import com.example.weathead340.api.DailyForecast
import com.example.weathead340.api.WeeklyForecast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CurrentForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        // error may rise
        val zipcode = arguments?.getString(KEY_ZIPCODE)?:""

        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)

        val tempText: TextView = view.findViewById(R.id.tempText)
        val locationName: TextView = view.findViewById(R.id.locationName)
        val weatherIcon= view.findViewById<ImageView>(R.id.weatherIcon)
        val emptyText = view.findViewById<TextView>(R.id.emptyText)
        val progressBar= view.findViewById<ProgressBar>(R.id.progressBar)
//         live data observe
        val currentWeatherObserver=  Observer<CurrentWeather> { weather ->
            emptyText.visibility=View.GONE
            progressBar.visibility=View.GONE
            locationName.visibility= View.VISIBLE
            tempText.visibility= View.VISIBLE
            weatherIcon.visibility= View.VISIBLE

            locationName.text = weather.name
            tempText.text = formatTempForDisplay(weather.forecast.temp, tempDisplaySettingManager.getTempDisplaySetting())
            val iconId= weather.weather[0].icon
            weatherIcon.load("http://openweathermap.org/img/wn/${iconId}@2x.png")
        }
        forecastRepository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)

        // floating button
        val locationEntryButton: FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when(savedLocation){
                is Location.Zipcode -> {
                    emptyText.visibility=View.GONE
                    progressBar.visibility= View.VISIBLE
                    forecastRepository.loadCurrentForecast(savedLocation.zipcode)
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return view
    }

    private fun showLocationEntry(){
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragment3ToLocationEntryFragment2()
        findNavController()
            .navigate(action)

    }


    companion object{
        const val KEY_ZIPCODE = "key_zipcode"

        fun newInstance(zipcode: String): CurrentForecastFragment{
            val fragment = CurrentForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }
}