// Copyright (C) 2009 Mihai Preda

package arity.calculator;

import android.view.View;
import android.view.MotionEvent;
import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import android.util.AttributeSet;
import java.util.Timer;
import java.util.TimerTask;

public class KeyboardView extends View {
    private char[][] keys;
    private int nLine, nCol;
    private Paint downPaint = new Paint();
    private int width, height;
    private Bitmap bitmap;
    private boolean isDown;
    private float downX, downY;
    private int downLine, downCol;
    private float downCW, downCH;
    private Rect rect = new Rect();
    private float cellw, cellh;
    private Calculator calculator;
    private KeyboardView aboveView;
    private boolean isLarge, isBottom;
    private TimerTask timerTask;
    private Timer repeat;

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        downPaint.setAntiAlias(false);
        downPaint.setColor(0xffffffff);
        downPaint.setStyle(Paint.Style.STROKE);
        calculator = (Calculator) context;        
    }
    
    void init(char[][] keys, boolean isLarge, boolean isBottom) {
        this.keys = keys;
        nLine = keys.length;
        nCol = keys[0].length;
        this.isLarge = isLarge;
        this.isBottom = isBottom;
    }

    void setAboveView(KeyboardView aboveView) {
        this.aboveView = aboveView;
    }
    
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        width = w;
        height = isBottom ? h - 5 : h;

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        
        cellw = width / (float) nCol;
        cellh = height / (float) nLine;
        
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(isLarge ? 26 : 22);
        textPaint.setColor(0xffffffff);
        textPaint.setTextAlign(Paint.Align.CENTER);
        final float extraY = isLarge ? 10 : 8;

        Paint textPaintLarge = new Paint();
        textPaintLarge.setAntiAlias(true);
        textPaintLarge.setTextSize(36);
        textPaintLarge.setColor(0xffffffff);
        textPaintLarge.setTextAlign(Paint.Align.CENTER);

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(false);
        for (int line = 0; line < nLine; ++line) {
            final float y1 = getY(line);
            final float y =  y1 + cellh/2 + extraY;
            char[] lineKeys = keys[line];
            for (int col = 0; col < nCol; ++col) {
                final float x1 = getX(col);
                final char c = lineKeys[col];
                if ((col > 0 && c == lineKeys[col-1]) || (line > 0 && c == keys[line-1][col])) {
                    continue;
                }
                float cw = col < nCol-1 && c == lineKeys[col+1] ? cellw+cellw : cellw;
                float ch = line < nLine-1 && c == keys[line+1][col] ? cellh+cellh : cellh;
                final float x = x1 + cw/2;
                final int backColor = (('a' <= c && c <= 'z') 
                                       || c == ' ') ? 0xff404040 :
                    (('0' <= c && c <= '9') || c == '.' || c == Calculator.PI) ? 0xff303030 :
                    (c == 'E' || c == 'C' || c == Calculator.ARROW) ? 0xff306060 : 0xff808080;
                /*
                    (c == '+' || c == '\u2212' || c == '\u00d7' || c == '\u00f7') ? 0xff808080 :
                    0xffb0b0b0;
                */
                linePaint.setColor(backColor);
                canvas.drawRect(x1, y1, x1+cw, y1+ch, linePaint);

                switch (c) {
                case 'E':
                    drawDrawable(canvas, R.drawable.enter, x1, y1, cw, ch);
                    break;

                case 'C':
                    drawDrawable(canvas, R.drawable.delete, x1, y1, cw, ch);
                    break;

                    case Calculator.ARROW:
                        canvas.drawText(lineKeys, col, 1, x, y, textPaintLarge);
                        break;

                default:
                    // textPaint.setColor(('0' <= c && c <= '9') ? 0xffffff00 : 0xffffffff);
                    canvas.drawText(lineKeys, col, 1, x, y, textPaint);
                }
            }
        }

        /*
        linePaint.setStrokeWidth(0);
        linePaint.setColor(0xff000000);
        for (int line = 0; line <= nLine; ++line) {
            final float y = getY(line);
            canvas.drawLine(0, y, width, y, linePaint);
        }
        for (int col = 0; col <= nCol; ++col) {
            final float x = getX(col);
            canvas.drawLine(x, 0, x, height, linePaint);
        }
        */
    }

    private void drawDrawable(Canvas canvas, int id, float x, float y, float cw, float ch) {
        Drawable d = calculator.getResources().getDrawable(id);
        int iw = d.getIntrinsicWidth();
        int ih = d.getIntrinsicHeight();
        int x1 = Math.round(x + (cw - iw)/2.f);
        int y1 = Math.round(y + (ch - ih)/2.f);
        d.setBounds(x1, y1, x1 + iw, y1 + ih);
        d.draw(canvas);
    }

    private float getY(int line) {
        return line * height / (float) nLine;
    }

    private float getX(int col) {
        return col * width / (float) nCol;
    }

    private int getLine(float y) {
        int line = (int) (y * nLine / height);
        if (line < 0) {
            line = 0;
        } else if (line >= nLine) {
            line = nLine - 1;
        }
        return line;
    }
    
    private int getCol(float x) {
        int col = (int) (x * nCol / width);
        if (col < 0) {
            col = 0;
        } else if (col >= nCol) {
            col = nCol - 1;
        }
        return col;
    }

    private void drawDown(Canvas canvas, float x, float y) {
        canvas.drawRect(x, y, x+downCW-.5f, y+downCH-.5f, downPaint);
    }

    protected void onDraw(Canvas canvas) {
        if (isDown) {
            float x1 = getX(downCol);
            float y1 = getY(downLine);
            //if (hasLargeClip(canvas, x1, y1)) {
            canvas.drawBitmap(bitmap, 0, 0, null);
            drawDown(canvas, x1, y1);
        } else {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    private static final float DELTAY = 8;
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
            float y = event.getY();
            if (y < DELTAY && aboveView != null) {
                event.offsetLocation(0, aboveView.getHeight() - DELTAY);
                aboveView.onTouchEvent(event);
            } else {
                isDown = true;
                downY = y >= DELTAY ? y - DELTAY : 0;
                downLine = getLine(downY);
                downCol = getCol(downX);
                downCW = cellw;
                downCH = cellh;
                if (downCol == 5 && downLine >= 2 && isLarge) {  //ENTER height is 2 cells in portrait mode
                    downLine = 2;
                    downCH = cellh + cellh;
                }
                invalidateCell(downLine, downCol);
                char key = keys[downLine][downCol];
                calculator.onKey(key);

                if (key=='C'){
                    repeat = new Timer();
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            calculator.onKey(key); //Repeate backspace if pressed long
                        }
                    };
                    repeat.schedule(timerTask, 500,100);
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (repeat!=null) repeat.cancel();
            if (isDown) {
                isDown = false;
                invalidateCell(downLine, downCol);
            } else if (aboveView != null) {
                aboveView.onTouchEvent(event);
            }
        } else {
            return false;
        }
        return true;
    }   

    private void invalidateCell(int line, int col) {
        float x1 = getX(col);
        float y1 = getY(line);
        int x2 = (int)(x1+downCW+1);
        int y2 = (int)(y1+downCH+1);
        invalidate((int)x1, (int)y1, x2, y2);
        // log("invalidate " + x + ' '  + y + ' ' + ((int)x1) + ' ' + ((int)y1) + ' ' + x2 + ' ' + y2);
    }
}
