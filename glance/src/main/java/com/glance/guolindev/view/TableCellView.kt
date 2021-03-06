/*
 * Copyright (C)  guolin, Glance Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glance.guolindev.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.glance.guolindev.R
import com.glance.guolindev.extension.dp
import com.glance.guolindev.logic.model.Row

/**
 * Custom view to represent a table cell. Draw the border(only left border is needed) for the cell when necessary.
 *
 * @author guolin
 * @since 2020/9/27
 */
class TableCellView(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {

    /**
     * Use this paint to draw border of table.
     */
    private val borderPaint = Paint()

    /**
     * Keep the column index of current table row.
     */
    var columnIndex = -1

    /**
     * Keep the row data of current table row.
     */
    var row: Row? = null
        set(value) {
            field = value
            if (columnIndex != -1) { // This means we always need to set columnIndex before we set row.
                text = value?.data?.get(columnIndex)
            }
        }

    init {
        borderPaint.color = ContextCompat.getColor(context, R.color.glance_library_table_border)
        borderPaint.strokeWidth = 1f.dp
        // Register the long click listener for each table cell.
        setOnLongClickListener {
            // When user long click a table cell, we copy the content of the cell to clipboard.
            if (!TextUtils.isEmpty(text)) {
                val clipboardManager = getContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText(getContext().getString(R.string.glance_library_clipboard_content_copy_label), text)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(getContext(), R.string.glance_library_content_copied_to_clipboard, Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (columnIndex != -1 && columnIndex != 0) {
            // We don't draw the left border if it's first cell.
            canvas.drawLine(0f, 0f, 0f, height.toFloat(), borderPaint)
        }
        super.onDraw(canvas)
    }

}