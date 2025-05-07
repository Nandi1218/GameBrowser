package hu.bme.nandiii.gamebrowser.ui.common

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import hu.bme.nandiii.gamebrowser.R

@ExperimentalMaterial3Api
@Composable
fun PasswordTextField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
    onDone: (KeyboardActionScope.() -> Unit)? = null,
    isVisible: Boolean = true,
    onVisibilityChanged: () -> Unit,
) {
    OutlinedTextField(
        value = value.trim(),
        enabled = enabled,
        label = { Text(text = label) },
        readOnly = readOnly,
        isError = isError,
        modifier = modifier,
        onValueChange = onValueChange,
        shape = MaterialTheme.shapes.medium,
        placeholder = { Text(text = "Password goes here") },
//        trailingIcon = {
//            Icon(
//                imageVector = ImageVector.vectorResource(id = R.drawable.password_visibility),
//                contentDescription = "Password"
//            )
//        },
        trailingIcon = if (isError) {
            {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.error_outline),
                    contentDescription = "Error"
                )
            }
        } else {
            {
                IconButton(onClick = onVisibilityChanged) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.password_visibility),
                        contentDescription = "Change password visibility"
                    )
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = onDone
        ),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}