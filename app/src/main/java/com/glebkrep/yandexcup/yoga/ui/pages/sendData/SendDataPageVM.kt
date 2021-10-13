package com.glebkrep.yandexcup.yoga.ui.pages.sendData

import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.glebkrep.yandexcup.yoga.data.BreathingItem
import com.glebkrep.yandexcup.yoga.data.repository.BreathingItemRepository
import com.glebkrep.yandexcup.yoga.data.repository.BreathingRoomDatabase
import com.glebkrep.yandexcup.yoga.features.reportBuilder.ReportBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SendDataPageVM(application: Application) : AndroidViewModel(application) {
    private var breathingItemRepository: BreathingItemRepository? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            breathingItemRepository = BreathingItemRepository(
                BreathingRoomDatabase.getDatabase(getApplication()).breathingItemDao()
            )
        }
    }

    fun sendEmail(email: String, activity: Activity) {
        val repositorySnapshot = breathingItemRepository ?: return
        val job = viewModelScope.launch(Dispatchers.Default) {
            repositorySnapshot.getAllBreathingItems()
                .collect {
                    sendEmailWithData(email = email, it, activity)
                }
        }
    }

    private fun sendEmailWithData(
        email: String,
        data: List<BreathingItem>,
        activity: Activity
    ) {
        if (data.isEmpty()) {
            com.glebkrep.yandexcup.yoga.utils.Debug.log("No records...")
        } else {
            val subject = "Breathing Report"
            val message = ReportBuilder(data).generateMessage()

            val selectorIntent = Intent(Intent.ACTION_SENDTO)
            selectorIntent.data = Uri.parse(
                "mailto:"
//                    + Uri.encode(email) + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(message) +
//                    "&to="+Uri.encode(email)
            )

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            emailIntent.selector = selectorIntent
            try {
                activity.startActivity(Intent.createChooser(emailIntent, "Send email..."))
            } catch (ex: ActivityNotFoundException) {
                com.glebkrep.yandexcup.yoga.utils.Debug.log("No records...")
            }
        }
    }
}
