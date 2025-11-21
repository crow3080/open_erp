package com.example.open_erp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.util.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.el_kotap.ui.theme.AppColors
import com.example.open_erp.presentation.Components.AppTopBar
import com.example.open_erp.presentation.Components.TabContent
import com.example.open_erp.presentation.Components.TabManager
import com.example.open_erp.presentation.Components.TabsRow
import com.example.open_erp.presentation.Components.WelcomeScreen


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "نظام إدارة الأعمال - Business Management System",
        state = rememberWindowState(width = 1200.dp, height = 800.dp)
    ) {
        MainApp()
    }
}

@Composable
fun MainApp() {
    val manager = remember { TabManager() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background)
    ) {
        AppTopBar(manager)
        if (manager.tabs.isNotEmpty()) {
            TabsRow(manager)
        }
        Box(Modifier.fillMaxSize()) {
            when (val tab = manager.activeTab) {
                null -> WelcomeScreen(manager)
                else -> TabContent(tab)
            }
        }
    }
}