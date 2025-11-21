package com.example.open_erp.presentation.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.el_kotap.ui.theme.AppColors
import com.example.open_erp.presentation.Models.PageType

@Composable
fun AppTopBar(manager: TabManager) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppColors.surface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(42.dp),
                    color = AppColors.dashboard,
                    shape = RoundedCornerShape(11.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Store,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Text(
                    "نظام إدارة الأعمال",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.textPrimary
                )
            }

            VerticalDivider(
                modifier = Modifier.height(28.dp).width(1.dp),
                color = AppColors.divider
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PageType.values().forEach { page ->
                    NavButton(page) { manager.open(page) }
                }
            }

            // زر إغلاق الكل
            if (manager.tabs.isNotEmpty()) {
                TextButton(
                    onClick = { manager.closeAll() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFDC2626)
                    )
                ) {
                    Icon(
                        Icons.Default.ClearAll,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("إغلاق الكل", fontSize = 13.sp)
                }
            }

            // قائمة المستخدم
            Box {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "القائمة",
                        tint = AppColors.dashboard,
                        modifier = Modifier.size(28.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("الملف الشخصي") },
                        onClick = { showMenu = false },
                        leadingIcon = {
                            Icon(Icons.Default.Person, null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("الإعدادات") },
                        onClick = { showMenu = false },
                        leadingIcon = {
                            Icon(Icons.Default.Settings, null)
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("تسجيل خروج", color = Color(0xFFDC2626)) },
                        onClick = { showMenu = false },
                        leadingIcon = {
                            Icon(Icons.Default.Logout, null, tint = Color(0xFFDC2626))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NavButton(page: PageType, onClick: () -> Unit) {
    var hovered by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        color = if (hovered) page.color.copy(0.1f) else Color.Transparent,
        shape = RoundedCornerShape(9.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                page.icon,
                contentDescription = null,
                tint = page.color,
                modifier = Modifier.size(20.dp)
            )
            Text(
                page.titleAr,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = AppColors.textPrimary
            )
        }
    }
}