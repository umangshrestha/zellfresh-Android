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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ContactDetailsForm(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel(),
) {
    val isNameValid = profileViewModel.name.isNotBlank()
    val isEmailValid = profileViewModel.email.contains("@") && profileViewModel.email.contains(".")
    val isPhoneValid = profileViewModel.phone.all { it.isDigit() } && profileViewModel.phone.length == 10
    val isFormValid = isNameValid && isEmailValid && isPhoneValid

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val (nameField, phoneField, emailField) = FocusRequester.createRefs()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Please fill out your contact details",
            style = MaterialTheme.typography.headlineSmall,
            modifier = modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = profileViewModel.name,
            onValueChange = { profileViewModel.name = it.replace("\n", "") },
            isError = !isNameValid,
            label = { Text("Name") },
            modifier = modifier.fillMaxWidth().focusRequester(nameField)
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

        if (!isNameValid) {
            Text(
                text = "Name cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        OutlinedTextField(
            value = profileViewModel.phone,
            onValueChange = {
                profileViewModel.phone = it.replace("\n", "")
            },
            isError = !isPhoneValid,
            label = { Text("Phone") },
            modifier = modifier.fillMaxWidth().focusRequester(phoneField)
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
            )        )
        if (!isPhoneValid) {
            Text(
                text = "Phone must be 10 digits",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = profileViewModel.email,
            onValueChange = {
                profileViewModel.email = it.replace("\n", "")
            },
            isError = !isEmailValid,
            label = { Text("Email") },
            modifier = modifier.fillMaxWidth().focusRequester(emailField)
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
            )        )
        if (!isEmailValid) {
            Text(
                text = "Enter a valid email address",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            modifier = modifier.fillMaxWidth(),
            enabled = isFormValid,
            onClick = {
                profileViewModel.putUser()
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ) {
           Text("Update", modifier = modifier)
        }
    }
}


@Preview
@Composable
fun ContactDetailsFormPreview() {
    ContactDetailsForm()
}