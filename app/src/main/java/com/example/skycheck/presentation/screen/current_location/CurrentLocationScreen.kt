package com.example.skycheck.presentation.screen.current_location

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.skycheck.presentation.component.current_location.CurrentLocationBottomBar
import com.example.skycheck.presentation.component.current_location.CurrentLocationPage
import com.example.skycheck.presentation.route.Locations
import com.example.skycheck.presentation.theme.ColorBackground
import com.example.skycheck.utils.LoadingBox
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrentLocationScreen(
    navController: NavController,
    viewModel: CurrentLocationViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0) { uiState.userLocations.size }

//    LaunchedEffect(Unit) {
//        viewModel.onEvent(CurrentLocationUiEvent.OnGetUserLocations)
//    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ColorBackground,
        bottomBar = {
            CurrentLocationBottomBar(
                pagesSize = uiState.userLocations.size,
                selectedPage = pagerState.currentPage,
                isLoadingLocations = uiState.isLoadingLocations,
                onLocationsClick = {
                    navController.navigate(Locations)
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoadingLocations) {
            LoadingBox()
        } else {
            if (uiState.locationsForecasts.isNotEmpty()) {
                HorizontalPager(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    state = pagerState
                ) {
                    val currentLocation = uiState.userLocations[pagerState.currentPage]
                    CurrentLocationPage(
                        location = currentLocation,
                        forecastData = uiState.locationsForecasts[currentLocation.id]
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun CurrentLocationScreenPreview() {
    CurrentLocationScreen(navController = rememberNavController())
}