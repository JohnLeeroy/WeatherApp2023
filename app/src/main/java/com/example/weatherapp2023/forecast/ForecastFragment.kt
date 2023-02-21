package com.example.weatherapp2023.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp2023.databinding.FragmentForecastBinding
import com.example.weatherapp2023.forecast.adapter.DailyForecastAdapter
import com.example.weatherapp2023.forecast.data.ForecastRowModel
import com.example.weatherapp2023.forecast.data.ForecastUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ForecastFragment : Fragment() {

    private var _forecastBinding: FragmentForecastBinding? = null

    private val viewModel: WeatherForecastVM by viewModels()
    private val adapter = DailyForecastAdapter {
        onWeatherRowClicked(it)
    }

    private val binding get() = _forecastBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _forecastBinding = FragmentForecastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _forecastBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         viewModel.getCurrentWeather()
        binding.forecastRecycler.adapter = adapter
        binding.forecastRecycler.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.loadingIndicator.visibility = View.GONE
                    when(it) {
                        is ForecastUiState.EmptyForecastState -> {
                        }
                        is ForecastUiState.FullForecastState -> {
//                            binding.temperatureLabel.text = String.format("Current Temp: %.0f", it.temperature)
                            adapter.submitList(it.forecast)
                        }
                        is ForecastUiState.ForecastErrorState -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        is ForecastUiState.LoadingForecastState -> {
                            binding.loadingIndicator.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun onWeatherRowClicked(weatherRow: ForecastRowModel) {
        Timber.d(weatherRow.toString())
        val navController = findNavController()
        val action = ForecastFragmentDirections.actionForecastFragment2ToForecastDetailFragment(weatherRow)
        navController.navigate(action)
    }
}

