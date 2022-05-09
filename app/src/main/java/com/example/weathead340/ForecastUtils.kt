package com.example.weathead340

import android.content.Context
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.weathead340.api.Temp

fun formatTempForDisplay(temp: Float, tempDisplaySetting: TempDisplaySetting): String{
    return when(tempDisplaySetting) {
        TempDisplaySetting.Fahrenheit -> String.format("%.2f F°", temp)
        TempDisplaySetting.Celsius -> {
            val temp= (temp-32f)*(5f/9f)
            String.format("%.2f C°", temp)
        }
    }
}

fun showTempDisplaySettingDialog(context: Context , tempDisplaySettingManager: TempDisplaySettingManager) {
    val dialogBuilder  = AlertDialog.Builder(context)
    dialogBuilder
        .setTitle("Choose Display Units")
        .setMessage("Choose which temperature unit to choose to use for temperature display")
        .setPositiveButton("°F") { _, _ ->
            tempDisplaySettingManager.updateSetting(TempDisplaySetting.Fahrenheit)
        }
        .setNeutralButton("°C") { _, _ ->
            tempDisplaySettingManager.updateSetting(TempDisplaySetting.Celsius)
        }
        .setOnDismissListener {
            Toast.makeText(context, "Setting will take action on app restart", Toast.LENGTH_SHORT).show()
        }
    dialogBuilder.show()
}

