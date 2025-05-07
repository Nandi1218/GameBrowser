package hu.bme.nandiii.gamebrowser.ui.common

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import hu.bme.nandiii.gamebrowser.R

@Composable
fun OutlinedTextFieldGeneral(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    readOnly: Boolean = false,
    isError: Boolean = false,
    onDone: (KeyboardActionScope.() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value.trim(),
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        onValueChange = onValueChange,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        label = { Text(text = label) },
        placeholder = { Text(text = "example@email.com") },
        trailingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.email_at),
                contentDescription = "Email"
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(onDone = onDone),

        )
}