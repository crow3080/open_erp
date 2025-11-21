package com.example.open_erp.presentation.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.el_kotap.ui.theme.AppColors
import com.example.open_erp.presentation.Models.AppTab
import com.example.open_erp.presentation.Models.PageType



@Composable
fun TabChip(
    tab: AppTab,
    active: Boolean,
    onClick: () -> Unit,
    onClose: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(34.dp)
            .clickable(onClick = onClick)
            .shadow(if (active) 2.dp else 0.dp, RoundedCornerShape(9.dp)),
        color = if (active) tab.type.color else AppColors.hoverBg,
        shape = RoundedCornerShape(9.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                tab.type.icon,
                contentDescription = null,
                tint = if (active) Color(255, 255, 255, 255) else tab.type.color,
                modifier = Modifier.size(16.dp)
            )
            Text(
                tab.title,
                fontSize = 13.sp,
                fontWeight = if (active) FontWeight.SemiBold else FontWeight.Medium,
                color = if (active) Color(255, 255, 255, 255) else AppColors.textPrimary
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "إغلاق",
                    tint = if (active) Color(255, 255, 255, 255).copy(0.85f) else AppColors.textSecondary,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}



@Composable
fun QuickCard(page: PageType, onClick: () -> Unit) {
    var hovered by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .width(150.dp)
            .height(130.dp)
            .clickable(onClick = onClick)
            .shadow(if (hovered) 10.dp else 3.dp, RoundedCornerShape(18.dp)),
        color = AppColors.surface,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically)
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                color = page.color.copy(0.13f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        page.icon,
                        contentDescription = null,
                        tint = page.color,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Text(
                page.titleAr,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.textPrimary
            )
        }
    }
}

@Composable
fun TabContent(tab: AppTab) {
    Box(Modifier.fillMaxSize().padding(24.dp)) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppColors.surface,
            shape = RoundedCornerShape(18.dp),
            shadowElevation = 2.dp
        ) {
            when (tab.type) {
                PageType.DASHBOARD -> DashboardPage()
                PageType.PRODUCTS -> ProductsPage()
                PageType.CLIENTS -> ClientsPage()
                PageType.SUPPLIERS -> SuppliersPage()
                PageType.EMPLOYEES -> EmployeesPage()
                PageType.SALES -> SalesPage()
                PageType.INVENTORY -> InventoryPage()
                PageType.SETTINGS -> SettingsPage()
            }
        }
    }
}




@Composable
fun ProductsPage() {
    PlaceholderPage("المنتجات", Icons.Default.Inventory2, AppColors.products)
}

@Composable
fun ClientsPage() {
    PlaceholderPage("العملاء", Icons.Default.Groups, AppColors.clients)
}

@Composable
fun SuppliersPage() {
    PlaceholderPage("الموردين", Icons.Default.LocalShipping, AppColors.suppliers)
}

@Composable
fun EmployeesPage() {
    PlaceholderPage("الموظفين", Icons.Default.Badge, AppColors.employees)
}

@Composable
fun SalesPage() {
    PlaceholderPage("سجل المبيعات", Icons.Default.Receipt, AppColors.sales)
}

@Composable
fun InventoryPage() {
    PlaceholderPage("المخزون", Icons.Default.Warehouse, AppColors.inventory)
}

@Composable
fun SettingsPage() {
    PlaceholderPage("الإعدادات", Icons.Default.Settings, AppColors.settings)
}

@Composable
fun PlaceholderPage(title: String, icon: ImageVector, color: Color) {
    Column(Modifier.fillMaxSize().padding(28.dp)) {
        PageHeader(title, icon, color)

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Surface(
                    modifier = Modifier.size(70.dp),
                    color = color.copy(0.12f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Construction,
                            null,
                            tint = color,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                Text(
                    "قيد التطوير",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.textPrimary
                )
                Text(
                    "سيتم إضافة هذا القسم قريباً",
                    fontSize = 14.sp,
                    color = AppColors.textSecondary
                )
            }
        }
    }
}

@Composable
fun PageHeader(title: String, icon: ImageVector, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            color = color.copy(0.13f),
            shape = RoundedCornerShape(13.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
            }
        }
        Text(
            title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.textPrimary
        )
    }
}


@Composable
fun TabsRow(manager: TabManager) {
    // نفس الكود الأصلي تقريبًا بدون تغيير كبير (هو بالفعل قابل للتمرير)
    // فقط قللنا التباعد قليلاً للشاشات الصغيرة إذا أردت
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppColors.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            manager.tabs.forEach { tab ->
                TabChip(
                    tab = tab,
                    active = tab.id == manager.activeTab?.id,
                    onClick = { manager.setActive(tab.id) },
                    onClose = { manager.close(tab.id) }
                )
            }
        }
    }
}

// باقي الملف الثاني مع تحسينات إضافية للشاشات الصغيرة

@Composable
fun WelcomeScreen(manager: TabManager) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isSmall = maxWidth < 900.dp

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(if (isSmall) 30.dp else 40.dp),
                modifier = Modifier.padding(if (isSmall) 40.dp else 60.dp)
            ) {
                Surface(
                    modifier = Modifier.size(if (isSmall) 100.dp else 130.dp),
                    color = AppColors.dashboard.copy(0.12f),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.RocketLaunch,
                            contentDescription = null,
                            tint = AppColors.dashboard,
                            modifier = Modifier.size(if (isSmall) 50.dp else 70.dp)
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "أهلاً بك! 👋",
                        fontSize = if (isSmall) 28.sp else 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.textPrimary
                    )
                    Text(
                        "اختر قسم من الأعلى للبدء",
                        fontSize = if (isSmall) 15.sp else 17.sp,
                        color = AppColors.textSecondary
                    )
                }

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    listOf(
                        PageType.DASHBOARD,
                        PageType.PRODUCTS,
                        PageType.CLIENTS,
                        PageType.SALES
                    ).forEach { page ->
                        QuickCard(page) { manager.open(page) }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardPage() {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isCompact = maxWidth < 1000.dp

        Column(Modifier.fillMaxSize().padding(28.dp)) {
            PageHeader("لوحة التحكم", Icons.Default.Dashboard, AppColors.dashboard)

            Spacer(Modifier.height(24.dp))

            if (isCompact) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    StatCard("إجمالي المبيعات", "125,430 ج.م", Icons.Default.TrendingUp, AppColors.sales, Modifier.fillMaxWidth())
                    StatCard("عدد العملاء", "248", Icons.Default.Groups, AppColors.clients, Modifier.fillMaxWidth())
                    StatCard("المنتجات", "1,432", Icons.Default.Inventory2, AppColors.products, Modifier.fillMaxWidth())
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    StatCard("إجمالي المبيعات", "125,430 ج.م", Icons.Default.TrendingUp, AppColors.sales, Modifier.weight(1f))
                    StatCard("عدد العملاء", "248", Icons.Default.Groups, AppColors.clients, Modifier.weight(1f))
                    StatCard("المنتجات", "1,432", Icons.Default.Inventory2, AppColors.products, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(120.dp),
        color = color.copy(0.08f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(54.dp),
                color = color.copy(0.15f),
                shape = RoundedCornerShape(13.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(30.dp))
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    title,
                    fontSize = 13.sp,
                    color = AppColors.textSecondary,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.textPrimary
                )
            }
        }
    }
}

