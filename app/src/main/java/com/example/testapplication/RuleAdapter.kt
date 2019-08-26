package com.example.testapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.*

class RuleAdapter(
    private val items: List<Rule>
) : RecyclerView.Adapter<RuleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.updateView(items[position])

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun updateView(rule: Rule) {
            rule_id.text = rule.ruleId
            rule_description.text = rule.ruleDescription
            rule_status.text = rule.status
        }
    }
}