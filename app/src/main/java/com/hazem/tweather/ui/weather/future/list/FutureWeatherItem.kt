package com.hazem.tweather.ui.weather.future.list

import com.hazem.tweather.R
import com.hazem.tweather.data.db.unitlocalized.future.list.MetricSimpleFutureWeatherEntry
import com.hazem.tweather.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.hazem.tweather.internal.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_weather.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureWeatherItem(
    val weatherEntry: UnitSpecificSimpleFutureWeatherEntry
) : Item() {


    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_condition.text = weatherEntry.conditionText
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }


    override fun getLayout() = R.layout.item_future_weather

    private fun ViewHolder.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        textView_date.text = weatherEntry.date.format(dtFormatter)
    }

    private fun ViewHolder.updateTemperature() {
        val unitAbbreviation = if (weatherEntry is MetricSimpleFutureWeatherEntry)
            this.itemView.context.getString(R.string.celsius) else this.itemView.context.getString(R.string.fahrenheit)

        textView_temperature.text =
            this.itemView.context.getString(R.string.temperature, weatherEntry.avgTemperature.toString(), unitAbbreviation)
    }

    private fun ViewHolder.updateConditionImage() {
        GlideApp.with(this.containerView)
            .load("https:" + weatherEntry.conditionIconUrl)
            .into(imageView_condition_icon)
    }
}