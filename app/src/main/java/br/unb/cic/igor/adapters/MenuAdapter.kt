package br.unb.cic.igor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import br.unb.cic.igor.R

class MenuAdapter(private val context: Context) : BaseAdapter() {

    val menuOptions: Array<String> = context.resources.getStringArray(R.array.menu_options)

    val images = intArrayOf(
            R.drawable.aventuras_icone,
            R.drawable.livros_icone,
            R.drawable.conta_icone,
            R.drawable.notificacoes_icone,
            R.drawable.configuracoes_icone,
            R.drawable.logout_icone,
            R.drawable.aventuras_icone_selecionado,
            R.drawable.livros_icone_selecionado,
            R.drawable.conta_icone_selecionado,
            R.drawable.notificacoes_icone_selecionado,
            R.drawable.configuracoes_icone_selecionado,
            R.drawable.logout_icone_selecionado)

    override fun getCount(): Int {
        return menuOptions.size
    }

    override fun getItem(position: Int): Any {
        return menuOptions[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row: View
        row = when (convertView) {
            null -> {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                inflater.inflate(R.layout.custom_drawe_row, parent, false)
            }
            else -> convertView
        }
        val title = row.findViewById<TextView>(R.id.title_menu)
        title.text = menuOptions[position]

        val imageView = row.findViewById<ImageView>(R.id.image_menu)
        imageView.setImageResource(images[position])

        return row
    }
}
