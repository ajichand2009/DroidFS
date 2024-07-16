package sushi.hardcore.droidfs.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sushi.hardcore.droidfs.R

abstract class SelectableAdapter<T>(private val onSelectionChanged: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectedItems: MutableSet<Int> = HashSet()

    protected abstract fun getItems(): List<T>

    override fun getItemCount(): Int {
        return getItems().size
    }

    protected open fun toggleSelection(position: Int): Boolean {
        val isSelected = if (selectedItems.contains(position)) {
            selectedItems.remove(position)
            false
        } else {
            selectedItems.add(position)
            true
        }
        onSelectionChanged(selectedItems.size)
        return isSelected
    }

    protected open fun onItemClick(position: Int): Boolean {
        if (selectedItems.isNotEmpty()) {
            return toggleSelection(position)
        }
        return false
    }

    protected open fun onItemLongClick(position: Int): Boolean {
        return toggleSelection(position)
    }

    protected open fun isSelectable(position: Int): Boolean {
        return true
    }

    fun unselect(position: Int) {
        selectedItems.remove(position)
        onSelectionChanged(selectedItems.size)
        notifyItemChanged(position)
    }

    fun selectAll() {
        for (i in getItems().indices) {
            if (!selectedItems.contains(i) && isSelectable(i)) {
                selectedItems.add(i)
                notifyItemChanged(i)
            }
        }
        onSelectionChanged(selectedItems.size)
    }

    fun unSelectAll(notifyChange: Boolean) {
        if (notifyChange) {
            val whatWasSelected = selectedItems
            selectedItems = HashSet()
            whatWasSelected.forEach {
                notifyItemChanged(it)
            }
        } else {
            selectedItems.clear()
        }
        onSelectionChanged(selectedItems.size)
    }

    private fun setBackground(rootView: View, isSelected: Boolean) {
        rootView.setBackgroundResource(if (isSelected) R.color.itemSelected else 0)
    }

    protected fun setSelectable(element: View, rootView: View, position: Int) {
        element.setOnClickListener {
            setBackground(rootView, onItemClick(position))
        }
        element.setOnLongClickListener {
            setBackground(rootView, onItemLongClick(position))
            true
        }
        setBackground(rootView, selectedItems.contains(position))
    }
}