package com.newuperapp.uper.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newuperapp.uper.ui.theme.AberColor
import com.newuperapp.uper.ui.theme.AberTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AberTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    useMenuIcon: Boolean = false,
    containerColor: Color = AberColor.White,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = AberTypography.ScreenTitle.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = if (useMenuIcon) Icons.Default.Menu else Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(
                        if (useMenuIcon) com.newuperapp.uper.R.string.nav_menu_content_desc 
                        else com.newuperapp.uper.R.string.nav_back_content_desc
                    ),
                    tint = if (containerColor == AberColor.Yellow) AberColor.Ink else AberColor.Yellow,
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor
        )
    )
}
