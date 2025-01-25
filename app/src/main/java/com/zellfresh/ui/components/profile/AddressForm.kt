package com.zellfresh.ui.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddressForm(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel(),
    ) {
    val isStreetValid = profileViewModel.street.isNotBlank()
    val isZipValid = profileViewModel.zip.all { it.isDigit() } && profileViewModel.zip.length == 6
    val isFormValid = isStreetValid && isZipValid
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Please fill out delivery details",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = profileViewModel.apt,
            onValueChange = { profileViewModel.apt = it },
            label = { Text("Apartment Number (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = profileViewModel.street,
            onValueChange = { profileViewModel.street = it },
            isError = !isStreetValid,
            label = { Text("Street") },
            modifier = Modifier.fillMaxWidth()
        )
        if (!isStreetValid) {
            Text(
                text = "Street cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // ZIP Code field with validation
        OutlinedTextField(
            value = profileViewModel.zip,
            onValueChange = { profileViewModel.zip = it },
            isError = !isZipValid,
            label = { Text("ZIP Code") },
            modifier = Modifier.fillMaxWidth()
        )
        if (!isZipValid) {
            Text(
                text = "ZIP Code must be 5 digits",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = profileViewModel.additionalInfo,
            onValueChange = { profileViewModel.additionalInfo = it },
            label = { Text("Additional Info (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Update button
        Button(
            onClick = {profileViewModel.putAddress()},
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressDetailsFormPreview() {
    AddressForm()
}
