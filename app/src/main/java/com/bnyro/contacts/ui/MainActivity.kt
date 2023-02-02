package com.bnyro.contacts.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bnyro.contacts.ui.screens.ContactsScreen
import com.bnyro.contacts.ui.theme.ConnectYouTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConnectYouTheme {
                ContactsScreen(shouldShowEditor(), getInitialContactId())
            }
        }
    }

    private fun shouldShowEditor(): Boolean {
        return when (intent?.action) {
            Intent.ACTION_INSERT -> true
            else -> intent?.getStringExtra("action") == "create"
        }
    }

    private fun getInitialContactId(): Long? {
        return when (intent?.action) {
            Intent.ACTION_EDIT, Intent.ACTION_VIEW -> intent?.data?.lastPathSegment?.toLongOrNull()
            else -> null
        }
    }
}
