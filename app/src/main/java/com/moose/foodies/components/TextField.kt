package com.moose.foodies.components

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moose.foodies.theme.secondaryDark
import com.moose.foodies.theme.secondaryVariant
import com.moose.foodies.theme.variantDark
import java.util.regex.Pattern
import androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors as Colors

open class TextFieldState (val validators: List<Validators>){

    var text: String by mutableStateOf("")
    var message: String by mutableStateOf("")
    var hasError: Boolean by mutableStateOf(false)

    fun showError(error: String){
        hasError = true
        message = error
    }

    fun hideError(){
        message = ""
        hasError = false
    }

    fun clear() = "".also { text = it }

    fun validate() {
        for (validator in validators){
            when (validator){
                is Email -> if (!email()) showError(validator.message)
                is Required -> if (!required()) showError(validator.message)
                is Regex -> if (!regex(validator.regex)) showError("value does not match required regex")
                is Max -> if (!max(validator.limit)) showError("value cannot be more than ${validator.limit}")
                is Min -> if (!min(validator.limit)) showError("value cannot be less than ${validator.limit}")
            }
        }
    }

    private fun required(): Boolean = text.isNotEmpty()

    private fun max(limit: Double): Boolean = text.toDouble() > limit

    private fun min(limit: Double): Boolean = text.toDouble() < limit

    private fun email(): Boolean = Patterns.EMAIL_ADDRESS.matcher(text).matches()

    private fun regex(regex: String): Boolean = Pattern.compile(regex).matcher(text).matches()
}

@Composable
fun OutlinedInput(
    label: String,
    type: KeyboardType,
    hide: Boolean = false,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    onChanged: (String) -> Unit = {}
){
    val color = secondaryVariant.copy(alpha = .2f)
    var hidden by remember { mutableStateOf(hide) }

    val icon = if (hidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    val transformation = if (hidden)  PasswordVisualTransformation() else VisualTransformation.None

    val colors = Colors(
        backgroundColor = color,
        trailingIconColor = color,
        errorBorderColor = Color.Transparent,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        cursorColor = MaterialTheme.colors.onPrimary,
        focusedLabelColor = MaterialTheme.colors.onPrimary,
        unfocusedLabelColor =  MaterialTheme.colors.onPrimary,
    )

    Column(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
        Text(label, modifier = Modifier.padding(5.dp))
        TextField(
            colors = colors,
            value = state.text,
            isError = state.hasError,
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            visualTransformation = transformation,
            textStyle = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = type),
            onValueChange = {
                onChanged(it)
                state.text = it
                state.hideError()
            },
            trailingIcon = {
                when {
                    state.hasError -> Icon(imageVector = Icons.Filled.Error, contentDescription = "Error")
                    type == KeyboardType.Password -> {
                        IconButton(onClick = { hidden = !hidden }) {
                            Icon(imageVector  = icon, "visibility")
                        }
                    }
                }
            },
        )
        if (state.hasError) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Text(state.message, color = MaterialTheme.colors.error)
            }
        }
    }
}