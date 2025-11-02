import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.util.*

// Improved color palette for better comfort: softer tones, better contrast, eye-friendly
val PrimaryGreen = Color(0xFF4CAF50) // Soft green for accents
val PrimaryLight = Color(0xFFE8F5E9) // Very light green for backgrounds
val SecondaryBlue = Color(0xFF2196F3) // Soft blue for secondary elements
val NeutralGray = Color(0xFF757575) // Medium gray for text
val LightBackground = Color(0xFFFAFAFA) // Soft off-white background
val DarkText = Color(0xFF212121) // Darker text for readability
val DividerColor = Color(0xFFE0E0E0) // Soft divider

@Composable
fun SmallButton(
    text: String,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = PrimaryGreen,
        contentColor = Color.White
    )
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(32.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
        colors = colors,
        shape = RoundedCornerShape(8.dp) // More rounded for comfort
    ) {
        Text(text, fontSize = 13.sp)
    }
}

@Composable
fun VerticalDivider(modifier: Modifier = Modifier, color: Color = DividerColor) {
    Box(
        modifier = modifier
            .width(1.dp)
            .background(color)
    )
}

enum class PageType(val title: String, val emoji: String) {
    Dashboard("لوحة التحكم", "📊"),
    Products("المنتجات", "📦"),
    Clients("العملاء", "🧍‍♂️"),
    Suppliers("الموردين", "🚚"),
    Employees("الموظفين", "💼"),
    SalesLog("سجل المبيعات", "💰"),
    Inventory("المخزون", "📋"),
    Settings("الإعدادات", "⚙️")
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

    Column(Modifier.fillMaxSize().background(LightBackground)) { // Softer background
        TopBar(tabManager)
        TabBar(tabManager)
        Box(Modifier.fillMaxSize()) {
            val activeTab = tabManager.activeTab
            if (activeTab == null) {
                EmptyState()
            } else {
                PageContent(activeTab)
            }
        }
    }
}

@Composable
fun TopBar(tabManager: TabManager) {
    var showMenu by remember { mutableStateOf(false) }

    Row(
        Modifier.fillMaxWidth()
            .height(56.dp)
            .background(SecondaryBlue) // Changed to softer blue for top bar
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp) // Increased spacing for comfort
    ) {
        // Logo
        Text("🛍️", fontSize = 24.sp) // Slightly larger for visibility
        Text(
            "Open ERP",
            fontSize = 20.sp, // Slightly larger
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.width(24.dp)) // More space
        VerticalDivider(color = Color.White.copy(alpha = 0.3f))

        // Navigation buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { // Increased spacing
            PageType.values().forEach { page ->
                NavigationButton(
                    text = page.title,
                    emoji = page.emoji,
                    onClick = { tabManager.openTab(page) }
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Right side actions
        if (tabManager.tabs.isNotEmpty()) {
            IconButton(onClick = { tabManager.closeAllTabs() }) {
                Icon(
                    Icons.Default.ClearAll,
                    contentDescription = "إغلاق الكل",
                    tint = Color.White
                )
            }
        }

        Box {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "القائمة", tint = Color.White)
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("👤 حساب المستخدم") },
                    onClick = { showMenu = false }
                )
                DropdownMenuItem(
                    text = { Text("⚙️ الإعدادات") },
                    onClick = { showMenu = false }
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("🚪 تسجيل خروج", color = Color(0xFFEF5350)) }, // Softer red
                    onClick = { showMenu = false }
                )
            }
        }
    }
}

@Composable
fun NavigationButton(
    text: String,
    emoji: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Row(
        modifier = Modifier
            .hoverable(interactionSource)
            .background(if (isHovered) PrimaryGreen.copy(alpha = 0.2f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp) // Increased padding for touch comfort
            .height(36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$emoji ", fontSize = 16.sp)
        Text(
            text,
            color = Color.White,
            fontSize = 14.sp, // Slightly larger for readability
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TabBar(tabManager: TabManager) {
    if (tabManager.tabs.isEmpty()) return

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp // Slightly more shadow for depth
    ) {
        Row(
            Modifier.fillMaxWidth()
                .height(48.dp) // Slightly taller for comfort
                .padding(horizontal = 12.dp), // Increased padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Increased spacing
            ) {
                items(tabManager.tabs, key = { it.id }) { tab ->
                    EnhancedTabItem(
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
fun EnhancedTabItem(
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
            .height(36.dp)
            .clickable(onClick = onSelect),
        color = if (isActive) PrimaryGreen else if (isHovered) PrimaryLight else Color(0xFFF5F5F5), // Softer inactive color
        shape = RoundedCornerShape(8.dp) // More rounded
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp), // Increased padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Increased spacing
        ) {
            Text(tab.type.emoji)
            Text(
                tab.title,
                fontSize = 14.sp, // Larger font
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) Color.White else NeutralGray
            )
            IconButton(onClick = onClose, modifier = Modifier.size(18.dp)) {
                Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = if (isActive) Color.White else NeutralGray)
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) { // Increased spacing
            Text("✨ نظام إدارة المحلات والمخزون ✨", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkText)
            Text("اختر صفحة من القائمة العلوية لبدء العمل 🧭", color = NeutralGray, fontSize = 16.sp) // Larger font
        }
    }
}

@Composable
fun PageContent(tab: TabPage) {
    Column(
        Modifier.fillMaxSize().background(Color.White).padding(32.dp), // Increased padding for comfort
        verticalArrangement = Arrangement.spacedBy(24.dp) // Increased spacing
    ) {
        Text("${tab.type.emoji} ${tab.type.title}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SecondaryBlue)
        HorizontalDivider(color = PrimaryLight)
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("🚧 قيد التطوير 🚧", fontSize = 20.sp, color = NeutralGray)
                Text("هذه الصفحة سيتم بناؤها لاحقًا...", fontSize = 16.sp, color = NeutralGray)
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "🛒 Open ERP - نظام إدارة المحلات",
        state = androidx.compose.ui.window.rememberWindowState(width = 1400.dp, height = 850.dp)
    ) {
        TabbedERPApp()
    }
}