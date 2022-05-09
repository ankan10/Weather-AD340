package com.example.weathead340.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat

private val DATE_FORMAT = SimpleDateFormat("MM-dd-yyyy")


class ForecastDetailsViewModelFactory(private val args: ForecastDetailsFragmentArgs): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom((ForecastDetailViewModel::class.java))){
            return ForecastDetailViewModel(args) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}

class ForecastDetailViewModel(args: ForecastDetailsFragmentArgs) : ViewModel() {
    private val _viewState: MutableLiveData<ForecastDetailsViewState> = MutableLiveData()
    val viewState: LiveData<ForecastDetailsViewState> = _viewState
    init {
        _viewState.value = ForecastDetailsViewState(
            temp = args.temp,
            description = args.description,
            date= DATE_FORMAT.format(args.dateText*1000),
            icon = "http://openweathermap.org/img/wn/${args.iconId}@2x.png"
        )
    }
}