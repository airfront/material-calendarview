package com.prolificinteractive.materialcalendarview.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
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

  class CustomDayViewHolder extends DayViewHolder {
    private final TextView label;
    private final TextView event1, event2, event3;

    public CustomDayViewHolder(Context context, CalendarDay date) {
      super(View.inflate(context, R.layout.view_day, null), date);
      label = itemView.findViewById(R.id.label);
      event1 = itemView.findViewById(R.id.event1);
      event2 = itemView.findViewById(R.id.event2);
      event3 = itemView.findViewById(R.id.event3);
    }

    public void bind() {
      label.setText(String.format(Locale.US, "%d", getDate().getDay()));
      final long c = System.currentTimeMillis();
      event1.setText(String.format(Locale.US, "%d", c % 100000 / 1000));
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
  }

  class CustomDayViewAdapter extends DayViewAdapter {
    @Override
    public DayViewHolder onCreateViewHolder(Context context, CalendarDay calendarDay) {
      return new CustomDayViewHolder(context, calendarDay);
    }

    @Override
    public void onBindViewHolder(DayViewHolder vh) {
      ((CustomDayViewHolder) vh).bind();
    }
  }

  private CustomDayViewAdapter adapter = new CustomDayViewAdapter();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic);
    ButterKnife.bind(this);

    widget.setOnDateChangedListener(this);
    widget.setOnDateLongClickListener(this);
    widget.setOnMonthChangedListener(this);

    widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
    widget.setDayViewAdapter(adapter);

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

  class UpdateTimer extends CountDownTimer {

    public UpdateTimer(long t) {
      super(t, t);
      start();
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
      updateCalendar();
      timer = new UpdateTimer(1000);
    }
  }

  private UpdateTimer timer = null;

  @Override
  protected void onResume() {
    super.onResume();
    timer = new UpdateTimer(1000);
  }

  @Override
  protected void onPause() {
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
    super.onPause();
  }

  void updateCalendar() {
    widget.refreshDayViews();
  }
}
