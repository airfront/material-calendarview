package com.prolificinteractive.materialcalendarview;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.format.DayFormatter;

public abstract class DayViewAdapter {

    public MaterialCalendarView mcv = null;

    public static DayViewAdapter DEFAULT = new DayViewAdapter() {
        @Override
        public DayViewHolder onCreateViewHolder(Context context, CalendarDay calendarDay) {
            return new DayViewHolder(new DayView(context, calendarDay), calendarDay) {
                @Override
                protected void setTextAppearance(Context context, int textAppearance) {
                    ((DayView) itemView).setTextAppearance(context, textAppearance);
                }

                @Override
                protected void setSelectionColor(int color) {
                    ((DayView) itemView).setSelectionColor(color);
                }

                @Override
                protected void setDayFormatter(DayFormatter formatter) {
                    ((DayView) itemView).setDayFormatter(formatter);
                }

                @Override
                protected void setDayFormatterContentDescription(DayFormatter formatter) {
                    ((DayView) itemView).setDayFormatterContentDescription(formatter);
                }

                @Override
                protected CalendarDay getDate() {
                    return ((DayView) itemView).getDate();
                }

                @Override
                protected void setupSelection(int showOtherDates, boolean inRange, boolean inMonth) {
                    ((DayView) itemView).setupSelection(showOtherDates, inRange, inMonth);
                }

                @Override
                protected void applyFacade(DayViewFacade facade) {
                    ((DayView) itemView).applyFacade(facade);
                }
            };
        }

        @Override
        public void onBindViewHolder(DayViewHolder vh) {
            // DO NOTHING
        }
    };

    public DayViewAdapter() {
    }

    public abstract DayViewHolder onCreateViewHolder(Context context, CalendarDay calendarDay);
    public abstract void onBindViewHolder(DayViewHolder vh);
}
