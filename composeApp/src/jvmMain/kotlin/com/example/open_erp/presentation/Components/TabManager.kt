package com.example.open_erp.presentation.Components

import androidx.compose.runtime.*
import com.example.open_erp.presentation.Models.AppTab
import com.example.open_erp.presentation.Models.PageType

class TabManager {
    private val _tabs = mutableStateListOf<AppTab>()
    private val _activeId = mutableStateOf<String?>(null)

    val tabs: List<AppTab> get() = _tabs
    val activeTab: AppTab? get() = _tabs.find { it.id == _activeId.value }

    fun open(type: PageType) {
        val existing = _tabs.find { it.type == type }
        if (existing != null) {
            _activeId.value = existing.id
            return
        }

        val newTab = AppTab(type = type)
        _tabs.add(newTab)
        _activeId.value = newTab.id
    }

    fun close(id: String) {
        val idx = _tabs.indexOfFirst { it.id == id }
        if (idx == -1) return

        _tabs.removeAt(idx)

        if (_tabs.isEmpty()) {
            _activeId.value = null
        } else if (_activeId.value == id) {
            _activeId.value = _tabs[idx.coerceAtMost(_tabs.lastIndex)].id
        }
    }

    fun setActive(id: String) {
        if (_tabs.any { it.id == id }) {
            _activeId.value = id
        }
    }

    fun closeAll() {
        _tabs.clear()
        _activeId.value = null
    }
}