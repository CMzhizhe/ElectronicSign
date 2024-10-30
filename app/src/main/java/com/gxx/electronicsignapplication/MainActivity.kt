package com.gxx.electronicsignapplication

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import com.gxx.electronicsignlibrary.ElectronicSign

class MainActivity:Activity() {
    var size = 8.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val electronView = findViewById<ElectronicSign>(R.id.electron_view)

        findViewById<Button>(R.id.bt_che).setOnClickListener {
            electronView.undo()
        }

        findViewById<Button>(R.id.bt_restore).setOnClickListener {
            electronView.redo()
        }

        findViewById<Button>(R.id.bt_clean).setOnClickListener {
            electronView.clean()
        }

        findViewById<Button>(R.id.bt_size_add).setOnClickListener {
            size = size + 5.0f
            electronView.setFontSize(size)
        }

    }
}