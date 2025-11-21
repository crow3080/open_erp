package com.example.open_erp.presentation.Components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.el_kotap.ui.theme.AppColors
import com.example.open_erp.presentation.Models.AppTab
import com.example.open_erp.presentation.Models.PageType

// ==========================================
// 1. RESPONSIVE TOP BAR SECTION
// ==========================================

@Composable
fun AppTopBar(manager: TabManager) {
    // BoxWithConstraints allows us to change layout based on the immediate width available
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().background(AppColors.surface)
    ) {
        val width = maxWidth
        // Breakpoints for responsiveness
        val isCompact = width < 850.dp      // Show Hamburger menu
        val isUltraWide = width > 1150.dp   // Show full text labels

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppColors.surface,
            shadowElevation = 4.dp,
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // --- LEFT: Logo ---
                LogoSection(showTitle = width > 450.dp)

                // --- CENTER: Navigation ---
                Box(
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompact) {
                        CompactNavDropdown(manager)
                    } else {
                        DesktopNavRow(manager)
                    }
                }

                // --- RIGHT: Actions (Close All, Profile) ---
                ActionsSection(manager, showLabels = isUltraWide)
            }
        }
    }
}

@Composable
private fun LogoSection(showTitle: Boolean) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            color = AppColors.dashboard,
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        if (showTitle) {
            Text(
                text = "نظام إدارة الأعمال",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.textPrimary
            )
        }
    }
}

@Composable
private fun DesktopNavRow(manager: TabManager) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        VerticalDivider(
            modifier = Modifier.height(24.dp).width(1.dp),
            color = AppColors.divider
        )
        Spacer(Modifier.width(12.dp))

        PageType.values().forEach { page ->
            NavButton(
                page = page,
                isActive = manager.activeTab?.type == page,
                onClick = { manager.open(page) }
            )
        }
    }
}

@Composable
private fun CompactNavDropdown(manager: TabManager) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Surface(
            modifier = Modifier
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = true },
            color = AppColors.hoverBg.copy(alpha = 0.5f),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Menu, "Menu", tint = AppColors.textPrimary, modifier = Modifier.size(18.dp))
                Text("الأقسام", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = AppColors.textPrimary)
                Icon(Icons.Default.ArrowDropDown, null, tint = AppColors.textSecondary, modifier = Modifier.size(18.dp))
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(220.dp).background(AppColors.surface)
        ) {
            PageType.values().forEach { page ->
                DropdownMenuItem(
                    text = { Text(page.titleAr, fontWeight = FontWeight.Medium) },
                    leadingIcon = { Icon(page.icon, null, tint = page.color) },
                    onClick = {
                        expanded = false
                        manager.open(page)
                    }
                )
            }
        }
    }
}

@Composable
private fun ActionsSection(manager: TabManager, showLabels: Boolean) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (manager.tabs.isNotEmpty()) {
            HoverableActionButton(
                onClick = { manager.closeAll() },
                icon = Icons.Default.ClearAll,
                label = if (showLabels) "إغلاق الكل" else null,
                color = Color(0xFFDC2626)
            )
        }

        VerticalDivider(modifier = Modifier.height(24.dp).padding(horizontal = 4.dp), color = AppColors.divider)
        ProfileMenuDropdown(showLabels)
    }
}

@Composable
private fun ProfileMenuDropdown(showLabel: Boolean) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = AppColors.dashboard.copy(alpha = 0.1f),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, null, tint = AppColors.dashboard, modifier = Modifier.size(18.dp))
                }
            }

            if (showLabel) {
                Column {
                    Text("المسؤول", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppColors.textPrimary)
                    Text("Admin", fontSize = 11.sp, color = AppColors.textSecondary)
                }
            }
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("الملف الشخصي") },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.Badge, null) }
            )
            DropdownMenuItem(
                text = { Text("الإعدادات") },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.Settings, null) }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("تسجيل خروج", color = Color(0xFFDC2626)) },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.Logout, null, tint = Color(0xFFDC2626)) }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavButton(page: PageType, isActive: Boolean, onClick: () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }
    val backgroundColor = when {
        isActive -> page.color.copy(alpha = 0.15f)
        isHovered -> page.color.copy(alpha = 0.08f)
        else -> Color.Transparent
    }

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
            .onPointerEvent(PointerEventType.Exit) { isHovered = false }
            .clickable(onClick = onClick)
            .animateContentSize(),
        color = backgroundColor,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                tint = if (isActive || isHovered) page.color else AppColors.textSecondary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = page.titleAr,
                fontSize = 13.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                color = if (isActive) page.color else AppColors.textPrimary
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HoverableActionButton(onClick: () -> Unit, icon: ImageVector, label: String?, color: Color) {
    var isHovered by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
            .onPointerEvent(PointerEventType.Exit) { isHovered = false }
            .clickable(onClick = onClick),
        color = if (isHovered) color.copy(alpha = 0.1f) else Color.Transparent,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null, tint = if(isHovered) color else AppColors.textSecondary, modifier = Modifier.size(18.dp))
            if (label != null) {
                Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = if(isHovered) color else AppColors.textSecondary)
            }
        }
    }
}

