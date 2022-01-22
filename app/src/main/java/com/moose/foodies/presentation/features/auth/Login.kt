package com.moose.foodies.presentation.features.auth

import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.hilt.navigation.compose.hiltViewModel
import com.moose.foodies.presentation.components.*
import com.moose.foodies.presentation.theme.largeHPadding
import com.moose.foodies.presentation.theme.smallVPadding


@Composable
fun Login(){
    val viewmodel: AuthViewmodel = hiltViewModel()
    val loading by remember { viewmodel.loading }

    val passwordState = remember { TextFieldState(validators = listOf(Required())) }
    val emailState = remember { TextFieldState(validators = listOf(Email(), Required())) }

    val inputModifier = Modifier.fillMaxWidth().largeHPadding()
    val labelModifier = Modifier.fillMaxWidth().largeHPadding().smallVPadding()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Before we get cookin`")
        Text(text = "We need to verify your identity.")

        SmallSpace()

        Text(text = "Email address", modifier = labelModifier, textAlign = Start)
        TextInput(state = emailState, modifier = inputModifier, type = KeyboardType.Email)

        SmallSpace()

        Text(text = "Password", modifier = labelModifier, textAlign = Start)
        PasswordInput(state = passwordState, modifier = inputModifier)

        MediumSpace()

        FilledButton(text = "Login", modifier = inputModifier, loading = loading) {
            emailState.validate()
            passwordState.validate()

            if (!emailState.hasError && !passwordState.hasError && !loading){
                viewmodel.login(emailState.text, passwordState.text)
            }
        }

        SmallSpace()

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = SpaceEvenly) {
            TextButton(text = "Sign up", onClick = { viewmodel.changeScreen(1) })
            TextButton(text = "Forgot password", onClick = { viewmodel.changeScreen(2) })
        }
    }
}