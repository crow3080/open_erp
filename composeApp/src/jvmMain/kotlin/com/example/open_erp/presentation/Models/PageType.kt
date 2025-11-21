package com.example.open_erp.presentation.Models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.el_kotap.ui.theme.AppColors
import java.util.*

enum class PageType(
    val titleAr: String,
    val icon: ImageVector,
    val color: Color
) {
    DASHBOARD("لوحة التحكم", Icons.Default.Dashboard, AppColors.dashboard),
    PRODUCTS("المنتجات", Icons.Default.Inventory2, AppColors.products),
    CLIENTS("العملاء", Icons.Default.Groups, AppColors.clients),
    SUPPLIERS("الموردين", Icons.Default.LocalShipping, AppColors.suppliers),
    EMPLOYEES("الموظفين", Icons.Default.Badge, AppColors.employees),
    SALES("سجل المبيعات", Icons.Default.Receipt, AppColors.sales),
    INVENTORY("المخزون", Icons.Default.Warehouse, AppColors.inventory),
    SETTINGS("الإعدادات", Icons.Default.Settings, AppColors.settings)
}

data class AppTab(
    val id: String = UUID.randomUUID().toString(),
    val type: PageType,
    val title: String = type.titleAr
)