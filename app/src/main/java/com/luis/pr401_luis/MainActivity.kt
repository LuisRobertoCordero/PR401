@file:OptIn(ExperimentalMaterial3Api::class)

package com.luis.pr401_luis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CuadernoApp()
                }
            }
        }
    }

@Composable
fun CuadernoApp() {
    var nota by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf(doubleArrayOf()) }

    var posicionBorrar by remember { mutableStateOf("") }
    var mediaClase by remember { mutableStateOf("") }

    var notaMasAltaPosicion by remember { mutableStateOf("") }
    var notaMasAlta by remember { mutableStateOf("") }

    var mensaje by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = nota,
            onValueChange = { nota = it },
            label = { Text("Introduce la nota (formato double)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (nota.isNotEmpty()) {
                        notas = notas + nota.toDouble()

                        mensaje = "Nota $nota añadida correctamente."
                        nota = ""
                    } else {
                        mensaje = "Ingrese una nota válida."
                    }
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                if (notas.isNotEmpty()) {
                    var maxNota = Double.MIN_VALUE
                    var posicion = -1

                    for ((index, nota) in notas.withIndex()) {
                        if (nota > maxNota) {
                            maxNota = nota
                            posicion = index
                        }
                    }

                    if (posicion != -1) {
                        notaMasAlta = maxNota.toString()
                        notaMasAltaPosicion = "Posición: $posicion"
                        mensaje = "Nota más alta: $notaMasAlta en la posición $notaMasAltaPosicion."
                    } else {
                        mensaje = "Error al obtener la posición de la nota más alta."
                    }
                } else {
                    mensaje = "El cuaderno está vacío."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Sacar nota más alta")
        }

        Button(
            onClick = {
                if (notas.isNotEmpty()) {
                    if (notas.size >= 2) {
                        val notaMinima = notas.minOrNull()!!
                        val notaMaxima = notas.maxOrNull()!!

                        val notasFiltradas =
                            notas.filter { it != notaMinima && it != notaMaxima }.toDoubleArray()

                        val sumaNotas = notasFiltradas.sum()
                        val media = sumaNotas / notasFiltradas.size.toDouble()
                        mediaClase = "${round(media * 100) / 100}"
                        mensaje = "Media de la clase (sin la nota más alta y más baja): $mediaClase"
                    } else {
                        mensaje = "No hay suficientes notas para calcular la media."
                    }
                } else {
                    mensaje = "El cuaderno está vacío."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Calcular media de la clase")
        }

        TextField(
            value = posicionBorrar.toString(),
            onValueChange = { posicionBorrar = it },
            label = { Text("Ingrese la posición de la nota a borrar (nota 1 = posicion 0)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (posicionBorrar.isNotEmpty()) {
                        val pos = posicionBorrar.toIntOrNull()
                        if (pos != null && pos in 0 until notas.size) {
                            notas = notas.filterIndexed { index, _ -> index != pos }.toDoubleArray()
                            mensaje = "Nota en la posición $posicionBorrar ha sido borrada correctamente."
                        } else {
                            mensaje = "Posición inválida."
                        }
                    } else {
                        mensaje = "Ingrese una posición válida."
                    }
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                if (posicionBorrar.isNotEmpty()) {
                    val pos = posicionBorrar.toInt()
                    if (pos in 0 until notas.size) {
                        notas = notas.filterIndexed { index, _ -> index != pos }.toDoubleArray()
                        mensaje = "La nota que esta en la posicion $posicionBorrar ha sido borrada correctamente."
                        posicionBorrar = ""
                    } else {
                        mensaje = "Posición inválida."
                    }
                } else {
                    mensaje = "Ingrese una posición válida."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Borrar nota en posición")
        }
        Button(
            onClick = {
                notas = doubleArrayOf()
                mensaje = "Todas las notas han sido borradas."
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Borrar todas las notas")
        }

        Text(mensaje)
    }
    Button(
        onClick = { showDialog = true },
        modifier = Modifier
            .wrapContentSize()
            .padding(bottom = 8.dp)
    ) {
        Text("Ver Notas")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Notas") },
            text = {
                Column {
                    notas.forEachIndexed { index, value ->
                        Text("Nota ${index + 1}: $value")
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CuadernoAppPreview() {

        CuadernoApp()
}
