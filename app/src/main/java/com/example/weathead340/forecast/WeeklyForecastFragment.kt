package com.example.weathead340.forecast

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathead340.*
import com.example.weathead340.api.DailyForecast
import com.example.weathead340.api.WeeklyForecast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WeeklyForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)

        val zipcode = arguments?.getString(KEY_ZIPCODE) ?: ""

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

//      recycler view
        val dailyForecastList: RecyclerView = view.findViewById(R.id.forecastList)
        dailyForecastList.layoutManager = LinearLayoutManager(requireContext())

        val emptyText = view.findViewById<TextView>(R.id.emptyTextWeekly)
        val progressBar= view.findViewById<ProgressBar>(R.id.progressBar2)

        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager) {
            showForecastDetails(it)
        }
        dailyForecastList.adapter = dailyForecastAdapter


//      live data observe
        val weeklyForecastObserver=  Observer<WeeklyForecast> { weeklyForecast ->
            emptyText.visibility= View.GONE
            progressBar.visibility= View.GONE
            dailyForecastList.visibility= View.VISIBLE
            dailyForecastAdapter.submitList(weeklyForecast.daily)
        }
        forecastRepository.weeklyForecast.observe(viewLifecycleOwner, weeklyForecastObserver)


        // floating button
        val locationEntryButton: FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener {
            showLocationEntry()
        }


        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when(savedLocation){
                is Location.Zipcode -> {
                    progressBar.visibility= View.VISIBLE
                    emptyText.visibility=View.GONE
                    forecastRepository.loadWeeklyForecast(savedLocation.zipcode)
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return view
    }

    private fun showLocationEntry(){
        val action= WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController()
            .navigate(action)
    }


    private fun showForecastDetails(forecast: DailyForecast){
        val temp= forecast.temp.max
        val description = forecast.weather[0].description
        val date = forecast.date
        val icon= forecast.weather[0].icon
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(temp, description, date, icon)
        findNavController().navigate(action)
    }

    companion object{
        const val KEY_ZIPCODE = "key_zipcode"

        fun newInstance(zipcode: String): WeeklyForecastFragment{
            val fragment = WeeklyForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }
}