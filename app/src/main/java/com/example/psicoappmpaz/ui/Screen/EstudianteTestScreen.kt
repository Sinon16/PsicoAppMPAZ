package com.example.psicoappmpaz.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EstudianteTestScreen(
    onFinalizar: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Pregunta 1 de 10",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Cómo te sientes cuando estas en la escuela?",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(modifier = Modifier.fillMaxWidth())  {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                RadioButton(selected = true, onClick = {})
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Me siento feliz y tranquilo")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                RadioButton(selected = false, onClick = { })
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Me siento normal")
            }
        }


        Spacer(modifier = Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                RadioButton(selected = false, onClick = { })
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Me siento triste o deprimido")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button (
            onClick = onFinalizar,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(text = "Finalizar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EstudianteTestPreview() {
    EstudianteTestScreen()
}