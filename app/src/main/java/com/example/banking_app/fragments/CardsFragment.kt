package com.example.banking_app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.banking_app.R
import com.example.banking_app.activity.GenerateCardActivity

class CardsFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_cards, container, false);
        val btn_gen_card = view.findViewById(R.id.btnGenCard) as Button
        btn_gen_card.setOnClickListener {
            val intent = Intent(this.activity, GenerateCardActivity::class.java)
            val iban = "12312AA2313"
            intent.putExtra("extra_iban", iban)
            startActivity(intent)
        }
        return view;
    }

}