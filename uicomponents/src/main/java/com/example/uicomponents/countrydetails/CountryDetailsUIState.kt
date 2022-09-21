package com.example.uicomponents.countrydetails

import com.example.domainmodels.CountryDetails
import com.example.viewmodels.CountryDetailsViewModel

data class CountryDetailsUIState(
    val details: CountryDetails,
    val viewModelState: CountryDetailsViewModel.State,
)
