package com.example.weatherapp2023.forecast.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.weatherapp2023.forecast.WeatherForecastVM
import com.example.weatherapp2023.forecast.data.CurrentTempUiModel
import com.example.weatherapp2023.forecast.data.ForecastRowModel
import com.example.weatherapp2023.forecast.data.ForecastUiState
import com.example.weatherapp2023.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class ComposeMainActivity : ComponentActivity() {

    private val viewModel: WeatherForecastVM by viewModels()

    companion object {
        var counter = 0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (counter == 0) {
                    viewModel.getCurrentWeather()
                    counter++
                }
            }
        }
        setContent {
            AppTheme(false) {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Today") }) },
                ) { contentPadding ->
                    Main(viewModel, contentPadding)
                }
            }
        }
    }
}

@Composable
fun Main(viewModel: WeatherForecastVM, paddingValues: PaddingValues) {
//    val forecast by viewModel.uiState.collectAsState()
    val forecast by viewModel.uiState.collectAsStateWithLifecycle()
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "profile") {
//
//    }


    LazyColumn(contentPadding = paddingValues) {
        when (forecast) {
            is ForecastUiState.FullForecastState -> {
                (forecast as ForecastUiState.FullForecastState).forecast.forEach { forecastUiModel ->
                    when (forecastUiModel) {
                        is CurrentTempUiModel -> {
                            item {
                                WeatherNowComposable(forecastUiModel, Modifier)
                            }
                        }
                        is ForecastRowModel -> {
                            item {
                                ForecastRowComposable(forecastUiModel, Modifier.clickable {
                                    Timber.d("Clicked on ${forecastUiModel.date}")

                                })
                            }
                        }
                    }
                }
            }
            else -> {

            }
        }
    }
}

@Composable
fun WeatherNowComposable(currentWeather: CurrentTempUiModel, modifier: Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(16.dp)
    ) {
        Column(
            modifier
                .weight(1f)
        ) {
            Text(text = currentWeather.date, style = MaterialTheme.typography.titleLarge)
            Text(
                text = "High ${currentWeather.tempHigh} Low ${currentWeather.tempLow}",
                style = MaterialTheme.typography.titleSmall
            )
            Text(text = currentWeather.temperature, style = MaterialTheme.typography.displayLarge)
        }
        Column(
            modifier
                .fillMaxHeight()
                .weight(1f)
                .align(Alignment.CenterVertically),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = currentWeather.weatherType.iconRes),
                contentDescription = currentWeather.weatherType.weatherDesc,
                modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = currentWeather.weatherType.weatherDesc,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ForecastRowComposable(model: ForecastRowModel, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        val image: Painter = painterResource(id = model.weatherType.iconRes)
        GlideImage(model = "https://cataas.com/c", contentDescription = "cat", Modifier.size(32.dp)) {
            it.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        }
        Image(
            image, model.weatherType.weatherDesc,
            modifier
                .size(32.dp)
                .padding(end = 4.dp)
        )
        Text(text = model.dayOfWeek, Modifier.weight(1f))
        Text(text = "High ${model.tempHigh}  Low ${model.tempLow}")
    }
}

//@Composable
//fun CurrentWeatherForecastComposable(
//    forecast: List<ForecastUiModel>,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn(modifier = modifier) {
//        forecast.forEach { forecastModel ->
//            when (forecastModel) {
//                is CurrentTempUiModel -> {
//                    item {
//                        ColumnRow(forecastModel.temperature)
//                    }
//                }
//                is ForecastRowModel -> {
//                    item {
//                        CurrentTemperatureRow(forecastModel, modifier)
//                    }
//                }
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme() {
//        Column(verticalArrangement = Arrangement.Top,
//            modifier = Modifier.fillMaxSize()) {
//            Greeting("Android")
//            Text(text = "Temperature", modifier = Modifier.padding(4.dp))
//
//        }
//        Main()
    }
}

/**
 * Only show in preview example
 * if (LocalInspectionMode.current) {
// Show this text in a preview window:
Text("Hello preview user!")
} else {
// Show this text in the app:
Text("Hello $name!")
}

Interactive mode -> allows to run throug hthe UI in a sanbox environment
limitations: No network, file access, context APIs not fully available
 */