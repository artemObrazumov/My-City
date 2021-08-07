package com.artem_obrazumov.mycity.ui.instructions.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.ui.instructions.models.InstructionSlide
import com.artem_obrazumov.mycity.ui.instructions.models.InstructionsScript
import com.bumptech.glide.Glide

class InstructionsPagerAdapter(
    private var script: InstructionsScript,
    private val context: Context
) : PagerAdapter() {

    override fun getCount(): Int = script.slides.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = (view == `object`)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(
            R.layout.instruction_card,
            container, false)

        displayInstructionSlide(view, script.slides[position])
        container.addView(view, position)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    private fun displayInstructionSlide(view: View, slide: InstructionSlide) {
        view.findViewById<TextView>(R.id.title).text = slide.title
        view.findViewById<TextView>(R.id.description).text = slide.description

        try {
            Glide.with(context).load(slide.photoURL)
                .into(view.findViewById(R.id.image))
        } catch (ignored: Exception) {}
    }
}