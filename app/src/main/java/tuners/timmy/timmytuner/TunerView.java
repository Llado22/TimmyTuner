package tuners.timmy.timmytuner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TunerView extends View {

    private float selected = 0;
    Paint background_paintbrush;
    Paint lines_paintbrush;
    Paint onCalibrated_paintbrush_background;
    Paint white_paintbrush_text;
    private Canvas canvas;
    Paint cursor_paintbrush;
    private float pitch=0;
    Paint cursorCorrect_paintbrush;



    public TunerView(Context context) {
        super(context);
        //setBackgroundResource(R.drawable)
    }

    public TunerView (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TunerView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        //do stuff
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth()/2;
        float height = getHeight();
        float width = getWidth();

        paintbrush();
        initialize(canvas);

        float dif = (this.selected-this.pitch);
        float ndif= dif/80*width;
        if(Math.abs(dif)<=5){
            cursor_paintbrush.setColor(Color.GREEN);
        }else{
            cursor_paintbrush.setColor(Color.RED);
        }
        //canvas.drawText( String.valueOf(dif), centerX , 100,white_paintbrush_text);
        if(pitch>32.70){
            canvas.drawLine(centerX - ndif, 0, centerX - ndif, height, cursor_paintbrush);
        }

        invalidate();

    }



    private void initialize(Canvas canvas) {
        float centerX = getWidth()/2;
        float height = getHeight();
        float width = getWidth();

        canvas.drawPaint(background_paintbrush);
        //linea central
        canvas.drawLine(centerX, 0, centerX, height, lines_paintbrush);
        //segones
        canvas.drawText("-10",centerX-(centerX/4)-40, height/8-20, white_paintbrush_text);
        canvas.drawText("+10",centerX+(centerX/4)-40, height/8-20, white_paintbrush_text);
        canvas.drawLine(centerX-(centerX/4), height/8, centerX-(centerX/4), height-(height/8), lines_paintbrush);
        canvas.drawLine(centerX+(centerX/4), height/8, centerX+(centerX/4), height-(height/8), lines_paintbrush);
        //terceres
        canvas.drawText("-20",centerX/2-40, height/4-20, white_paintbrush_text);
        canvas.drawText("+20",centerX+(centerX/2)-40, height/4-20, white_paintbrush_text);
        canvas.drawLine(centerX/2, height/4, centerX/2, height-(height/4), lines_paintbrush);
        canvas.drawLine(centerX+(centerX/2), height/4, centerX+(centerX/2), height-(height/4), lines_paintbrush);
        //cuartes
        canvas.drawText("-30",centerX/4-40, height/3-20, white_paintbrush_text);
        canvas.drawText("+30",width-(centerX/4)-40, height/3-20, white_paintbrush_text);
        canvas.drawLine(centerX/4, height/3, centerX/4, height-(height/3), lines_paintbrush);
        canvas.drawLine(width-(centerX/4), height/3, width-(centerX/4), height-(height/3), lines_paintbrush);
    }

    private void paintbrush() {
        cursor_paintbrush = new Paint();
        cursor_paintbrush.setColor(Color.RED);
        cursor_paintbrush.setStyle(Paint.Style.FILL_AND_STROKE);
        cursor_paintbrush.setStrokeWidth(25);

        onCalibrated_paintbrush_background = new Paint();
        onCalibrated_paintbrush_background.setColor(Color.GREEN);
        onCalibrated_paintbrush_background.setStyle(Paint.Style.FILL_AND_STROKE);
        //onCalibrated_paintbrush_background.setStrokeWidth(3);

        cursorCorrect_paintbrush = new Paint();
        cursorCorrect_paintbrush.setColor(Color.GREEN);
        cursorCorrect_paintbrush.setStyle(Paint.Style.FILL_AND_STROKE);
        cursorCorrect_paintbrush.setStrokeWidth(25);

        white_paintbrush_text = new Paint();
        white_paintbrush_text.setColor(Color.WHITE);
        white_paintbrush_text.setStyle(Paint.Style.FILL_AND_STROKE);
        white_paintbrush_text.setTextSize(40);
        background_paintbrush = new Paint();
        background_paintbrush.setColor(Color.GRAY);
        lines_paintbrush = new Paint();
        lines_paintbrush.setColor(Color.BLACK);
        lines_paintbrush.setStyle(Paint.Style.FILL_AND_STROKE);
        lines_paintbrush.setStrokeWidth(5);
    }

    //gets and sets
    public void setPitch (float pitch){
        this.pitch = pitch;
    }

    public float getSelected() {
        return selected;
    }

    public void setSelected(float selected) {
        this.selected = selected;
    }
}
