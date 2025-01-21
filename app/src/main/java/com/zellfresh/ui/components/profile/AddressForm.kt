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
    aptInput: String,
    streetInput: String,
    zipInput: String,
    additionalInfoInput: String,
    onUpdate: (String?, String, String,String) -> Unit,
    modifier: Modifier = Modifier
) {
    var apt by remember { mutableStateOf(aptInput) }
    var street by remember { mutableStateOf(streetInput) }
    var zip by remember { mutableStateOf(zipInput) }
    var additionalInfo by remember { mutableStateOf(additionalInfoInput) }

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
            value = apt,
            onValueChange = { apt = it },
            label = { Text("Apartment Number") },
            modifier = modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = street,
            onValueChange = {
                street = it
            },
            isError = street.isBlank(),
            label = { Text("Street") },
            modifier = modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = zip,
            onValueChange = {
                zip = it
            },
            isError = zip.isBlank(),
            label = { Text("ZIP Code") },
            modifier = modifier.fillMaxWidth()
        )

        OutlinedTextField(value = additionalInfo, onValueChange = {
            additionalInfo = it
        }, label = { Text("Additional Info") }, modifier = modifier.fillMaxWidth())

        Button(
            modifier = modifier.fillMaxWidth(),
            enabled = zip.isNotBlank() && street.isNotBlank(),
            onClick = {
                onUpdate(apt, street, zip, additionalInfoInput)
            }
        ) {
           Text("Update", modifier = modifier)
        }
    }
}