package hu.bme.nandiii.gamebrowser.feature.auth.login

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.nandiii.gamebrowser.R
import hu.bme.nandiii.gamebrowser.ui.common.OutlinedTextFieldGeneral
import hu.bme.nandiii.gamebrowser.ui.common.PasswordTextField
import hu.bme.nandiii.gamebrowser.ui.common.SnackbarCommon
import hu.bme.nandiii.gamebrowser.ui.theme.AppTheme
import hu.bme.nandiii.gamebrowser.util.UiEvent
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    viewModel: LoginUserViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)
    LaunchedEffect(uiEvent) {
        Log.d("AUTH", "Launched effect called")
        viewModel.autoLogin()
        when (uiEvent) {
            is UiEvent.Success -> {
                onSuccess()
            }

            is UiEvent.Failure -> {
                Log.d("AUTH", "UiEvent.Failure called")
                scope.launch {
                    Log.d("AUTH", (uiEvent as UiEvent.Failure).message.asString(context))
                    snackbarHostState.showSnackbar(
                        message = (uiEvent as UiEvent.Failure).message.asString(context)
                    )


                }
            }

            null -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarCommon(snackbarHostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 16.dp),

                        )
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer)

            )
        }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues),
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (state.registerState) stringResource(R.string.register) else stringResource(
                            R.string.login
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp)
                            .animateContentSize(),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextFieldGeneral(
                            value = state.email,
                            onValueChange = { viewModel.onEvent(LoginUserEvent.EmailChanged(it)) },
                            modifier = Modifier.padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                            label = stringResource(R.string.email_address),
                            isError = state.emailError
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PasswordTextField(
                            value = state.password,
                            label = stringResource(R.string.password),
                            onValueChange = { viewModel.onEvent(LoginUserEvent.PasswordChanged(it)) },
                            modifier = Modifier.padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                            isVisible = state.passwordVisibility,
                            onVisibilityChanged = { viewModel.onEvent(LoginUserEvent.PasswordVisibilityChanged) },
                            imeAction = if (state.registerState) ImeAction.Next else ImeAction.Done,
                            isError = state.passwordError || state.password.isBlank(),
                            onDone = {
                                viewModel.onEvent(LoginUserEvent.SignIn)
                            }
                        )
                    }
                    AnimatedVisibility(visible = state.registerState, exit = fadeOut()+ shrinkHorizontally()+shrinkVertically()) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            PasswordTextField(
                                value = state.passwordAgain,
                                label = stringResource(R.string.confirm_password),
                                onValueChange = {
                                    viewModel.onEvent(LoginUserEvent.PasswordConfirmChanged(it))
                                },
                                modifier = Modifier.padding(
                                    top = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                                isVisible = state.passwordAgainVisibility,
                                onVisibilityChanged = { viewModel.onEvent(LoginUserEvent.PasswordAgainVisibilityChanged) },
                                onDone = {
                                    viewModel.onEvent(LoginUserEvent.Register)
                                    Log.d(
                                        context.getString(R.string.loginscreen),
                                        context.getString(R.string.login_button_clicked)
                                    )
                                    viewModel.onEvent(LoginUserEvent.Validate)
                                },
                                isError = state.passwordError || state.passwordAgain.isBlank(),
                            )

                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(

                            shape = MaterialTheme.shapes.small,
                            onClick = {
                                //Toast.makeText(context,"Login button pressed",Toast.LENGTH_SHORT).show()
                                if (state.registerState)
                                    viewModel.onEvent(LoginUserEvent.Register)
                                else
                                    viewModel.onEvent(LoginUserEvent.SignIn)
                            },
                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp),


                            ) {
                            Text(
                                modifier = Modifier.animateContentSize(),
                                text = if (state.registerState) stringResource(R.string.register) else stringResource(
                                    R.string.login
                                )
                            )
                        }

                        Text(text = if (state.registerState) stringResource(R.string.already_have_an_account) else stringResource(
                            R.string.dont_have_an_account
                        ), modifier = Modifier
                            .padding(
                                top = 16.dp,
                                end = 16.dp
                            )
                            .clickable {
                                viewModel.onEvent(LoginUserEvent.RegisterStateSwitch)
                            }
                            .animateContentSize()
                        )

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(onSuccess = {})
}