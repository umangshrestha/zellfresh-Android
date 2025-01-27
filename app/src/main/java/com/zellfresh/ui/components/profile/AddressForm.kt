package com.zellfresh.ui.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.input.ImeAction

@Composable
fun AddressForm(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel(),
    ) {
    val isStreetValid = profileViewModel.street.isNotBlank()
    val isZipValid = profileViewModel.zip.all { it.isDigit() } && profileViewModel.zip.length == 6
    val isFormValid = isStreetValid && isZipValid

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val (aptField, streetField, zipCodeField, additionalInfoField) = FocusRequester.createRefs()


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Please fill out delivery details",
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = profileViewModel.apt,
            onValueChange = { profileViewModel.apt = it.replace("\n", "")},
            label = { Text("Apartment Number (Optional)") },
            modifier = modifier.fillMaxWidth().focusRequester(aptField)
                .onKeyEvent { event ->
                    if (event.key == Key.Enter) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = profileViewModel.street,
            onValueChange = { profileViewModel.street = it.replace("\n", "") },
            isError = !isStreetValid,
            label = { Text("Street") },
            modifier = modifier.fillMaxWidth().focusRequester(streetField)
                .onKeyEvent { event ->
                    if (event.key == Key.Enter) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        if (!isStreetValid) {
            Text(
                text = "Street cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = profileViewModel.zip,
            onValueChange = { profileViewModel.zip = it.replace("\n", "") },
            isError = !isZipValid,
            label = { Text("ZIP Code") },
            modifier = modifier.fillMaxWidth().focusRequester(zipCodeField)
                .onKeyEvent { event ->
                    if (event.key == Key.Enter) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
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
            onValueChange = { profileViewModel.additionalInfo = it.replace("\n", "") },
            label = { Text("Additional Info (Optional)") },
            modifier = modifier.fillMaxWidth().focusRequester(additionalInfoField)
        )

        Button(
            onClick = {
                profileViewModel.putAddress()
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            enabled = isFormValid,
            modifier = modifier.fillMaxWidth()
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
