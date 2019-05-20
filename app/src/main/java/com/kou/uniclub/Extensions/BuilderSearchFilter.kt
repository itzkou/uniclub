package com.kou.uniclub.Extensions

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import com.kou.uniclub.R

class BuilderSearchFilter {
    companion object {
        private fun cards(card:CardView,context: Context)
        {var busic = false
            card.setOnClickListener {
                busic = if (!busic) {
                    card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.orange))
                    true


                } else {
                    card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.darkGray))
                    false

                }

            }}
        fun showDialog(context: Context) {

            val dialogView = LayoutInflater.from(context).inflate(com.kou.uniclub.R.layout.builder_search_filter, null)
            val busi = dialogView.findViewById<CardView>(R.id.busi)
            val learni = dialogView.findViewById<CardView>(R.id.learning)
            val culturi = dialogView.findViewById<CardView>(R.id.culture)
            val sociali = dialogView.findViewById<CardView>(R.id.social)
            val phototi = dialogView.findViewById<CardView>(R.id.photography)
            val techi = dialogView.findViewById<CardView>(R.id.tech)
            val sporti = dialogView.findViewById<CardView>(R.id.sports)
            val desi = dialogView.findViewById<CardView>(R.id.design)
            val gami = dialogView.findViewById<CardView>(R.id.gaming)


            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)

            builder.setView(dialogView)

            cards(busi,context)
            cards(learni,context)
            cards(culturi,context)
            cards(sociali,context)
            cards(phototi,context)
            cards(techi,context)
            cards(sporti,context)
            cards(desi,context)
            cards(gami,context)






            builder.setPositiveButton("confirm") { dialog, which ->

                dialog?.dismiss()
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, which ->
                dialog?.dismiss()
            }
            val dialog = builder.create()



            dialog.show()


        }
    }


}