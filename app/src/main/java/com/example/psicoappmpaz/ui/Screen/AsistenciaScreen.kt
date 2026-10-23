package com.example.psicoappmpaz.ui.screen
import android.R
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AsistenciaScreen(
    onVolver: () -> Unit = {}
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Registro de asistencia",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Curso: 4° Basico A",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                Checkbox(
                    checked = true,
                    onCheckedChange = { }
                )
                Spacer(modifier = Modifier.width(16.dp))

                    Text(text = "Estudiante EST-001",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                Checkbox(
                    checked = true,
                    onCheckedChange = { }
                )
                Spacer(modifier = Modifier.width(16.dp))

                Text(text = "Estudiante EST-002",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                Checkbox(
                    checked = true,
                    onCheckedChange = { }
                )
                Spacer(modifier = Modifier.width(16.dp))

                Text(text = "Estudiante EST-003",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Guardar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AsistenciaPreview() {
    AsistenciaScreen()
}