package com.prolificinteractive.materialcalendarview.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewAdapter;
import com.prolificinteractive.materialcalendarview.DayViewHolder;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.showOtherMonths;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.showOutOfRange;

/**
 * Shows off the most basic usage
 */
public class BasicActivity extends AppCompatActivity
    implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

  @BindView(R.id.calendarView)
  MaterialCalendarView widget;

  @BindView(R.id.textView)
  TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic);
    ButterKnife.bind(this);

    widget.setOnDateChangedListener(this);
    widget.setOnDateLongClickListener(this);
    widget.setOnMonthChangedListener(this);

    widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
    widget.setDayViewAdapter(new DayViewAdapter() {
      @Override
      public DayViewHolder createDayView(Context context, CalendarDay calendarDay) {
        return new DayViewHolder(View.inflate(context, R.layout.view_day, null), calendarDay) {
          private TextView label;
          @Override
          protected void onBind(View v, CalendarDay date) {
            label = v.findViewById(R.id.label);
            label.setText(String.format(Locale.US, "%d", calendarDay.getDay()));
          }

          @Override
          protected void setupSelection(int showOtherDates, boolean inRange, boolean inMonth) {
            super.setupSelection(showOtherDates, inRange, inMonth);

            boolean visible = inMonth && inRange;
            itemView.setEnabled(inRange);

            boolean showOtherMonths = showOtherMonths(showOtherDates);
            boolean showOutOfRange = showOutOfRange(showOtherDates) || showOtherMonths;

            if (!inMonth && showOtherMonths) {
              visible = true;
            }

            if (!inRange && showOutOfRange) {
              visible |= inMonth;
            }

            itemView.setAlpha(!inMonth && visible ? .4f : 1f);
            itemView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
          }
        };
      }
    });

    //Setup initial text
    textView.setText("No Selection");
  }

  @Override
  public void onDateSelected(
      @NonNull MaterialCalendarView widget,
      @NonNull CalendarDay date,
      boolean selected) {
    textView.setText(selected ? FORMATTER.format(date.getDate()) : "No Selection");
  }

  @Override
  public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
    final String text = String.format("%s is available", FORMATTER.format(date.getDate()));
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
    //noinspection ConstantConditions
    getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
  }
}
