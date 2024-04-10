import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class IngredientSpinnerAdapter(context: Context, data: MutableList<Pair<Int, String>> = mutableListOf()) : ArrayAdapter<Pair<Int, String>>(context, android.R.layout.simple_spinner_item, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val currentItem = getItem(position)
        val text = "${currentItem?.first}. ${currentItem?.second}"
        (view as TextView).text = text
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val currentItem = getItem(position)
        val text = "${currentItem?.first}. ${currentItem?.second}"
        (view as TextView).text = text
        return view
    }
}
