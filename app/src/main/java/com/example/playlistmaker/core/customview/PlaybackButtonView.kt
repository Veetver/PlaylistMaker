package com.example.playlistmaker.core.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val gestureDetector = GestureDetector(
        context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent) = true
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                onActionUpEventListener()
                togglePlayback()
                return true
            }
        }
    )

    private var onActionUpEventListener: () -> Unit = {}

    private var playbackButtonState: PlaybackButtonState = PlaybackButtonState.STATE_PAUSED
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }
    private var playbackDrawable: Drawable? = null
    private var playbackBitmap: Bitmap? = null

    private val playDrawable: Drawable?
    private val playBitmap: Bitmap?
    private val pauseDrawable: Drawable?
    private val pauseBitmap: Bitmap?

    private var imageRect = RectF(0f, 0f, 0f, 0f)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playDrawable = getDrawable(R.styleable.PlaybackButtonView_playIconDrawable)
                playBitmap = playDrawable?.toBitmap()
                pauseDrawable = getDrawable(R.styleable.PlaybackButtonView_pauseIconDrawable)
                pauseBitmap = pauseDrawable?.toBitmap()

                playbackDrawable = playDrawable ?: pauseDrawable
                playbackBitmap = playbackDrawable?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val currentBitmapWidth = playbackDrawable?.intrinsicWidth ?: -1
        val currentBitmapHeight = playbackDrawable?.intrinsicHeight ?: -1

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val contentWidth = when (val widthMode = MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> {
                min(currentBitmapWidth, widthSize)
            }

            MeasureSpec.EXACTLY -> widthSize

            else -> error("Неизвестный режим ширины ($widthMode)")
        }

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val contentHeight = when (val heightMode = MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> {
                min(currentBitmapHeight, heightSize)
            }

            MeasureSpec.EXACTLY -> heightSize

            else -> error("Неизвестный режим высоты ($heightMode)")
        }

        val size = min(contentWidth, contentHeight)

        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        playbackBitmap?.let { bitmap ->
            canvas.drawBitmap(bitmap, null, imageRect, null)
        }
    }

    fun setOnActionUpEventListener(action: () -> Unit) {
        onActionUpEventListener = action
    }

    fun setState(new: PlaybackButtonState) {
        when (new) {
            PlaybackButtonState.STATE_PAUSED -> {
                playbackBitmap = pauseBitmap
                playbackButtonState = PlaybackButtonState.STATE_PLAYING
            }

            PlaybackButtonState.STATE_PLAYING -> {
                playbackBitmap = playBitmap
                playbackButtonState = PlaybackButtonState.STATE_PAUSED
            }
        }
    }

    fun togglePlayback() {
        setState(playbackButtonState)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
}

enum class PlaybackButtonState {
    STATE_PAUSED,
    STATE_PLAYING
}