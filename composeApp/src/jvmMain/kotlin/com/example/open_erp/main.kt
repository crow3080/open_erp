import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.util.*

// Modern vibrant color palette
val BgGradientStart = Color(0xFFF5F7FA)
val BgGradientEnd = Color(0xFFE8EAF6)
val CardBackground = Color(0xFFFFFFFF)

// Color for each page type
val ColorDashboard = Color(0xFF6366F1)      // Indigo
val ColorProducts = Color(0xFFEC4899)       // Pink
val ColorClients = Color(0xFF10B981)        // Green
val ColorSuppliers = Color(0xFFF59E0B)      // Amber
val ColorEmployees = Color(0xFF8B5CF6)      // Purple
val ColorSalesLog = Color(0xFF06B6D4)       // Cyan
val ColorInventory = Color(0xFFEF4444)      // Red
val ColorSettings = Color(0xFF64748B)       // Slate

@Composable
fun VerticalDivider(modifier: Modifier = Modifier, color: Color = Color.White.copy(alpha = 0.3f)) {
    Box(
        modifier = modifier
            .width(1.dp)
            .height(32.dp)
            .background(color)
    )
}

enum class PageType(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color,
    val emoji: String
) {
    Dashboard("لوحة التحكم", Icons.Default.Dashboard, ColorDashboard, "📊"),
    Products("المنتجات", Icons.Default.Inventory, ColorProducts, "🎁"),
    Clients("العملاء", Icons.Default.People, ColorClients, "👥"),
    Suppliers("الموردين", Icons.Default.LocalShipping, ColorSuppliers, "🚚"),
    Employees("الموظفين", Icons.Default.Badge, ColorEmployees, "👔"),
    SalesLog("سجل المبيعات", Icons.Default.Receipt, ColorSalesLog, "💰"),
    Inventory("المخزون", Icons.Default.Warehouse, ColorInventory, "📦"),
    Settings("الإعدادات", Icons.Default.Settings, ColorSettings, "⚙️")
}

data class TabPage(
    val id: String = UUID.randomUUID().toString(),
    val type: PageType,
    val title: String = type.title,
    val data: Any? = null
)

class TabManager {
    private val _tabs = mutableStateListOf<TabPage>()
    private val _activeTabId = mutableStateOf<String?>(null)

    val tabs: List<TabPage> get() = _tabs
    val activeTab: TabPage? get() = _tabs.find { it.id == _activeTabId.value }

    fun openTab(type: PageType, data: Any? = null) {
        val newTab = TabPage(type = type, data = data)
        _tabs.add(newTab)
        _activeTabId.value = newTab.id
    }

    fun closeTab(tabId: String) {
        val index = _tabs.indexOfFirst { it.id == tabId }
        if (index == -1) return
        _tabs.removeAt(index)
        if (_tabs.isEmpty()) {
            _activeTabId.value = null
        } else if (_activeTabId.value == tabId) {
            val nextIndex = index.coerceAtMost(_tabs.lastIndex)
            _activeTabId.value = _tabs.getOrNull(nextIndex)?.id
        }
    }

    fun setActiveTab(tabId: String) {
        if (_tabs.any { it.id == tabId }) {
            _activeTabId.value = tabId
        }
    }

    fun closeAllTabs() {
        _tabs.clear()
        _activeTabId.value = null
    }
}

@Composable
fun TabbedERPApp() {
    val tabManager = remember { TabManager() }

    Box(
        Modifier.fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BgGradientStart, BgGradientEnd)
                )
            )
    ) {
        Column(Modifier.fillMaxSize()) {
            TopBar(tabManager)
            TabBar(tabManager)
            Box(Modifier.fillMaxSize()) {
                val activeTab = tabManager.activeTab
                if (activeTab == null) {
                    EmptyState(tabManager)
                } else {
                    PageContent(activeTab)
                }
            }
        }
    }
}

@Composable
fun TopBar(tabManager: TabManager) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            Modifier.fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Logo with gradient background
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Store,
                    contentDescription = "Logo",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                "نظام إدارة الأعمال",
                fontSize = 16.sp,
                color = Color(0xFF1E293B),
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(4.dp))
            VerticalDivider(color = Color(0xFFE2E8F0))

            // Navigation buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                PageType.values().forEach { page ->
                    CompactNavigationButton(
                        page = page,
                        onClick = { tabManager.openTab(page) }
                    )
                }
            }

            // Right side actions
            if (tabManager.tabs.isNotEmpty()) {
                Surface(
                    modifier = Modifier.clickable { tabManager.closeAllTabs() },
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ClearAll,
                            contentDescription = "إغلاق الكل",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "إغلاق الكل",
                            color = Color(0xFFEF4444),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Box {
                Surface(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { showMenu = !showMenu },
                    color = Color(0xFFF1F5F9),
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "القائمة",
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF6366F1),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text("حساب المستخدم")
                            }
                        },
                        onClick = { showMenu = false }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text("الإعدادات")
                            }
                        },
                        onClick = { showMenu = false }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(
                                    Icons.Default.Logout,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text("تسجيل خروج", color = Color(0xFFEF4444))
                            }
                        },
                        onClick = { showMenu = false }
                    )
                }
            }
        }
    }
}

@Composable
fun CompactNavigationButton(
    page: PageType,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Surface(
        modifier = Modifier
            .hoverable(interactionSource)
            .clickable(onClick = onClick),
        color = if (isHovered) page.color.copy(alpha = 0.15f) else Color.Transparent,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        page.color.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    page.icon,
                    contentDescription = null,
                    tint = page.color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                page.title,
                fontSize = 12.sp,
                color = Color(0xFF475569),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun TabBar(tabManager: TabManager) {
    if (tabManager.tabs.isEmpty()) return

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            Modifier.fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(tabManager.tabs, key = { it.id }) { tab ->
                    ColorfulTabItem(
                        tab = tab,
                        isActive = tab.id == tabManager.activeTab?.id,
                        onSelect = { tabManager.setActiveTab(tab.id) },
                        onClose = { tabManager.closeTab(tab.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ColorfulTabItem(
    tab: TabPage,
    isActive: Boolean,
    onSelect: () -> Unit,
    onClose: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Surface(
        modifier = Modifier
            .hoverable(interactionSource)
            .height(32.dp)
            .clickable(onClick = onSelect)
            .shadow(
                elevation = if (isActive) 3.dp else 0.dp,
                shape = RoundedCornerShape(8.dp)
            ),
        color = if (isActive) tab.type.color else if (isHovered) tab.type.color.copy(alpha = 0.1f) else Color(0xFFF8FAFC),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                tab.type.icon,
                contentDescription = null,
                tint = if (isActive) Color.White else tab.type.color,
                modifier = Modifier.size(16.dp)
            )
            Text(
                tab.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) Color.White else Color(0xFF1E293B)
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(18.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "إغلاق",
                    tint = if (isActive) Color.White.copy(alpha = 0.9f) else Color(0xFF64748B),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyState(tabManager: TabManager) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(48.dp)
        ) {
            // Large icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Rocket,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "مرحباً بك في نظام إدارة الأعمال! 👋",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    "اختر أحد الأقسام من الأعلى للبدء في العمل",
                    color = Color(0xFF64748B),
                    fontSize = 16.sp
                )
            }

            // Quick access cards
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                listOf(
                    PageType.Dashboard,
                    PageType.Products,
                    PageType.Clients,
                    PageType.SalesLog
                ).forEach { page ->
                    QuickAccessCard(page = page, onClick = { tabManager.openTab(page) })
                }
            }
        }
    }
}

@Composable
fun QuickAccessCard(page: PageType, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Surface(
        modifier = Modifier
            .width(140.dp)
            .height(120.dp)
            .hoverable(interactionSource)
            .clickable(onClick = onClick)
            .shadow(
                elevation = if (isHovered) 12.dp else 4.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        page.color.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    page.icon,
                    contentDescription = null,
                    tint = page.color,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                page.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
            )
        }
    }
}

@Composable
fun PageContent(tab: TabPage) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 3.dp
        ) {
            Column(
                Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header with colored icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                tab.type.color.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            tab.type.icon,
                            contentDescription = null,
                            tint = tab.type.color,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Column {
                        Text(
                            tab.type.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            "Tab ID: ${tab.id.take(8)}",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFE2E8F0))

                // Content area
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    tab.type.color.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Construction,
                                contentDescription = null,
                                tint = tab.type.color,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Text(
                            "قيد التطوير 🚧",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            "هذا القسم سيتم إضافته قريباً",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "نظام إدارة الأعمال - Business Management System",
        state = androidx.compose.ui.window.rememberWindowState(width = 1500.dp, height = 900.dp)
    ) {
        TabbedERPApp()
    }
}