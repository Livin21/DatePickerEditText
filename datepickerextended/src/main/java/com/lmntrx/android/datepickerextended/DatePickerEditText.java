package com.lmntrx.android.datepickerextended;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lmntrx.livin.library.droidawesome.DroidAwesomeImageView;

import java.util.Calendar;

/***
 * Created by livin on 26/12/16.
 */

public class DatePickerEditText extends LinearLayout {
    DroidAwesomeImageView calendarImage;
    EditText dateInput;

    Calendar currDate;


    //attrs
    String fontAwesomeText;
    String hint;
    int textColor = 0;
    int fontAwesomeIconColor = 0;
    int underlineColor = 0;
    float marginBetweenTextAndIcon = 0f;
    float fontAwesomeIconSize = 0f;
    float textSize = 0f;
    boolean showUnderline = true;
    Drawable pickerDrawable;


    public DatePickerEditText(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.date_picker_edit_text, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.lmntrx.livin.library.droidawesome.R.styleable.DroidAwesomeTextDrawable,defStyleAttr,defStyleRes);
        try {
            fontAwesomeText = typedArray.getString(R.styleable.DatePickerEditText_setFontAwesomeIcon);
            hint = typedArray.getString(R.styleable.DatePickerEditText_hint);
            textColor = typedArray.getColor(R.styleable.DatePickerEditText_textColor, ContextCompat.getColor(getContext(),android.R.color.black));
            fontAwesomeIconColor = typedArray.getColor(R.styleable.DatePickerEditText_fontAwesomeIconColor, ContextCompat.getColor(getContext(),R.color.unSelectedItemColor));
            pickerDrawable = typedArray.getDrawable(R.styleable.DatePickerEditText_setPickerIcon);
            underlineColor = typedArray.getColor(R.styleable.DatePickerEditText_underlineColor,ContextCompat.getColor(getContext(),R.color.unSelectedItemColor));
            marginBetweenTextAndIcon = typedArray.getDimension(R.styleable.DatePickerEditText_setMarginBetweenTextAndIcon,getResources().getDimension(R.dimen.default_gap));
            fontAwesomeIconSize = typedArray.getDimension(R.styleable.DatePickerEditText_fontAwesomeIconSize,getResources().getDimension(R.dimen.default_font_awesome_icon_size));
            textSize = typedArray.getDimension(R.styleable.DatePickerEditText_textSize,getResources().getDimension(R.dimen.default_text_size));
            showUnderline = typedArray.getBoolean(R.styleable.DatePickerEditText_showUnderline,true);
            Log.d("Test",
                    fontAwesomeText + " " + hint + " " + textColor + " " + fontAwesomeIconColor + " " + pickerDrawable + " " +
                    underlineColor + " " +marginBetweenTextAndIcon + " " +fontAwesomeIconSize + " " +textSize + " " +showUnderline
            );
        }finally {
            typedArray.recycle();
        }
    }

    public Calendar getDate() {
        String currDateStr = dateInput.getText().toString();
        try {
            currDate = Calendar.getInstance();
            currDate.set(
                    Integer.parseInt(currDateStr.split("/")[2]),
                    Integer.parseInt(currDateStr.split("/")[1]) - 1,
                    Integer.parseInt(currDateStr.split("/")[0]));
        }catch (Exception ignored){}
        return currDate;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dateInput = (EditText) findViewById(R.id.dateInputEditText);
        calendarImage = (DroidAwesomeImageView) findViewById(R.id.calendarIcon);
        View view = findViewById(R.id.view);

        view.setVisibility(showUnderline?VISIBLE:GONE);
        if (pickerDrawable != null){
            calendarImage.setImageDrawable(pickerDrawable);
        }else {
            if (fontAwesomeText!=null){
                calendarImage.setIcon(fontAwesomeText,fontAwesomeIconSize,fontAwesomeIconColor);
            }
            if (textColor > 0){
                try {
                    dateInput.setTextColor(ContextCompat.getColor(getContext(),textColor));
                }catch (Exception ignored){}
            }
            if (textSize > 0f){
                dateInput.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
            }
            if (marginBetweenTextAndIcon > 0f){
                int leftPX = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        marginBetweenTextAndIcon,
                        getContext().getResources().getDisplayMetrics()
                );
                int topPX = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        5f,
                        getContext().getResources().getDisplayMetrics()
                );
                ViewGroup.MarginLayoutParams marginLayoutParams = (MarginLayoutParams) calendarImage.getLayoutParams();
                marginLayoutParams.setMargins(leftPX,topPX,0,0);
            }
            if (hint != null){
                dateInput.setHint(hint);
            }
            if (underlineColor > 0){
                try {
                    DrawableCompat.setTint(view.getBackground(), ContextCompat.getColor(getContext(), underlineColor));
                }catch (Exception ignored){}
            }
        }


        calendarImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                try {
                    String dateEntered = dateInput.getText().toString();
                    if (!dateEntered.isEmpty()){
                        String[] dateArgs = dateEntered.split("/");
                        cal.set(
                                Integer.parseInt(dateArgs[2]),
                                Integer.parseInt(dateArgs[1])-1,
                                Integer.parseInt(dateArgs[0])
                        );
                    }
                }catch (Exception ignored){}
                DatePickerDialog dialog =
                        new DatePickerDialog(getContext(),
                                listener,
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)
                        );
                dialog.show();
            }
        });
        dateInput.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String dateFormat = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");
                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + dateFormat.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dateInput.setText(current);
                    dateInput.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            String dayStr = day + "", monthStr = (month + 1) + "";
            if (day < 10){
                dayStr = "0" + dayStr;
            }
            if (month < 10){
                monthStr = "0" + monthStr;
            }
            dateInput.setText(dayStr + monthStr + year);
        }
    };

    public DatePickerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public DatePickerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DatePickerEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr,defStyleRes);
    }
}
