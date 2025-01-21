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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddressForm(
    nameInput: String,
    emailInput: String,
    phoneInput: String,
    onUpdate: (apt: String, street: String, zip: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(nameInput) }
    var email by remember { mutableStateOf(emailInput) }
    var phone by remember { mutableStateOf(phoneInput) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Address Form",
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            isError = name.isBlank(),
            label = { Text("Name") },
            modifier = modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
            },
            isError = phone.isBlank(),
            label = { Text("Phone") },
            modifier = modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            isError = email.isBlank(),
            label = { Text("Email") },
            modifier = modifier.fillMaxWidth()
        )

        Button(
            modifier = modifier.fillMaxWidth(),
            enabled = name.isNotEmpty() && phone.isNotBlank() && email.isNotBlank(),
            onClick = {
                onUpdate(name, phone, email)
            }
        ) {
           Text("Update", modifier = modifier)
        }
    }
}