package tuners.timmy.timmytuner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TunerView extends View {

    private float selected;
    Paint paint = new Paint();
    Paint background_paintbrush;
    Paint lines_paintbrush;
    Paint black_paintbrush_fill;
    Paint black_paintbrush_stroke;
    Paint white_paintbrush_text;
    private Canvas canvas;

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
        float centerY = getHeight()/2;
        float centerX = getWidth()/2;
        float height = getHeight();


        paintbrush();
        initialize(canvas);


        //Rect rectangle = new Rect();
        //rectangle.set(0,0,getWidth(),getHeight());
        //canvas.drawRect(rectangle, black_paintbrush_fill);
        //canvas.drawText(String.valueOf(), getWidth()/2, getHeight()/2, white_paintbrush_text);


    }

    private void initialize(Canvas canvas) {
        float centerX = getWidth()/2;
        float height = getHeight();
        float width = getWidth();

        //pintar background
        canvas.drawPaint(background_paintbrush);
        //linea central
        canvas.drawLine(centerX, 0, centerX, height, lines_paintbrush);
        //segones
        canvas.drawLine(centerX-(centerX/4), height/8, centerX-(centerX/4), height-(height/8), lines_paintbrush);
        canvas.drawLine(centerX+(centerX/4), height/8, centerX+(centerX/4), height-(height/8), lines_paintbrush);
        //terceres
        canvas.drawLine(centerX/2, height/4, centerX/2, height-(height/4), lines_paintbrush);
        canvas.drawLine(centerX+(centerX/2), height/4, centerX+(centerX/2), height-(height/4), lines_paintbrush);
        //cuartes
        canvas.drawLine(centerX/4, height/3, centerX/4, height-(height/3), lines_paintbrush);
        canvas.drawLine(width-(centerX/4), height/3, width-(centerX/4), height-(height/3), lines_paintbrush);
    }

    private void paintbrush() {
        black_paintbrush_fill = new Paint();
        black_paintbrush_fill.setColor(Color.BLACK);
        black_paintbrush_fill.setStyle(Paint.Style.FILL);
        black_paintbrush_stroke = new Paint();
        black_paintbrush_stroke.setColor(Color.BLACK);
        black_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        black_paintbrush_stroke.setStrokeWidth(3);
        white_paintbrush_text = new Paint();
        white_paintbrush_text.setColor(Color.WHITE);
        white_paintbrush_text.setStyle(Paint.Style.FILL_AND_STROKE);
        background_paintbrush = new Paint();
        background_paintbrush.setColor(Color.GRAY);
        lines_paintbrush = new Paint();
        lines_paintbrush.setColor(Color.BLACK);
        lines_paintbrush.setStyle(Paint.Style.FILL_AND_STROKE);
        lines_paintbrush.setStrokeWidth(5);
    }


    //gets and sets
    public float getSelected() {
        return selected;
    }

    public void setSelected(float selected) {
        this.selected = selected;
    }
}
