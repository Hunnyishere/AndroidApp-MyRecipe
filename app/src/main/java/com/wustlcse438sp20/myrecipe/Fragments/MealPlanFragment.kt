package com.wustlcse438sp20.myrecipe.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.wustlcse438sp20.myrecipe.MealAnalysisActivity
import com.wustlcse438sp20.myrecipe.MyApplication

import com.wustlcse438sp20.myrecipe.R
import com.wustlcse438sp20.myrecipe.SmartMealActivity
import kotlinx.android.synthetic.main.example_2_calendar_header.view.*
import kotlinx.android.synthetic.main.fragment_meal_plan.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MealPlanFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MealPlanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MealPlanFragment : Fragment() {
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private var user_email:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        var globalVariable = getActivity()?.getApplicationContext() as MyApplication
        user_email = globalVariable.getEmail()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val daysOfWeek = daysOfWeekFromLocale()
        exTwoCalendar.setup(YearMonth.now().minusMonths(12), YearMonth.now(), daysOfWeek.first())
        smartMeal.setOnClickListener {
            val intent = Intent(this@MealPlanFragment.activity,SmartMealActivity::class.java)
            startActivity(intent)
        }
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay

            val textView = with(view) {
                setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate == day.date) {
                            selectedDate = null
                            exTwoCalendar.notifyDayChanged(day)
                        } else {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            exTwoCalendar.notifyDateChanged(day.date)
                            oldDate?.let { exTwoCalendar.notifyDateChanged(oldDate) }
                        }
//                        menuItem.isVisible = selectedDate != null
                    }
                    val intent = Intent(this@MealPlanFragment.activity,MealAnalysisActivity::class.java)
                    Log.e("MSG","${day.date.yearMonth.month.name.toLowerCase().capitalize()} ${day.date.dayOfMonth},${day.date.year}")
                    intent.putExtra("date","${day.date.yearMonth.month.name.toLowerCase().capitalize()} ${day.date.dayOfMonth},${day.date.year}")
                    intent.putExtra("user_email",user_email)
                    startActivity(intent)
                }
                return@with this as TextView
            }

        }
        exTwoCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.visibility = View.VISIBLE
                    when (day.date) {
                        selectedDate -> {
                            textView.setTextColorRes(R.color.example_2_white)
                            textView.setBackgroundResource(R.drawable.example_2_selected_bg)
                        }
                        today -> {

                            textView.setTextColorRes(R.color.example_2_red)
                            textView.background = null
                        }
                        else -> {
                            textView.setTextColorRes(R.color.example_2_black)
                            textView.background = null
                        }
                    }
                } else {
                    textView.visibility = View.GONE
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exTwoHeaderText
        }
        exTwoCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                @SuppressLint("SetTextI18n") // Concatenation warning for `setText` call.  ${month.year}
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} "
            }
        }
        exTwoCalendar.scrollToDate(today)
    }

    fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }
    internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)
    internal fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))
}
