package com.prolificinteractive.materialcalendarview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Checkable;

import com.prolificinteractive.materialcalendarview.format.DayFormatter;

abstract public class DayViewHolder implements Checkable {

    protected final View itemView;
    protected final CalendarDay day;

    public DayViewHolder(@NonNull View v, @NonNull CalendarDay calendarDay) {
        itemView = v;
        day = calendarDay;
        onBind(v, calendarDay);
    }

    protected void onBind(@NonNull View v, @NonNull CalendarDay date) {}

    protected void setTextAppearance(Context context, @StyleRes int textAppearance) {}

    protected void setOnClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }

    protected void setClickable(boolean clickable) {
        itemView.setClickable(clickable);
    }

    protected void setSelectionColor(int color) {}
    protected void setDayFormatter(DayFormatter formatter) {}
    protected void setDayFormatterContentDescription(DayFormatter formatter) {}

    protected CalendarDay getDate() {
        return day;
    }

    protected void setupSelection(@MaterialCalendarView.ShowOtherDates int showOtherDates,
            boolean inRange, boolean inMonth) {}

    protected void applyFacade(DayViewFacade facade) {}

    @Override
    public void setChecked(boolean checked) {
        if (itemView instanceof Checkable) {
            ((Checkable) itemView).setChecked(checked);
        }
    }

    @Override
    public boolean isChecked() {
        return (itemView instanceof Checkable) && ((Checkable) itemView).isChecked();
    }

    @Override
    public void toggle() {
        if (itemView instanceof Checkable) {
            ((Checkable) itemView).toggle();
        }
    }
}
