package com.kou.uniclub.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class ClubsFilter : AppCompatActivity() {
    private var page: String? = null
    private lateinit var autoComplete: android.support.v7.widget.SearchView.SearchAutoComplete


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.kou.uniclub.R.layout.activity_clubs_filter)
       /* val v = searchUnivs.findViewById(android.support.v7.appcompat.R.id.search_plate) as View
        v.setBackgroundColor(resources.getColor(android.R.color.transparent))
        searchUnivs.setOnClickListener {
            searchUnivs.onActionViewExpanded()*/



        /*autoComplete =
           searchUnivs.findViewById(android.support.v7.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        rvUnivs.layoutManager = LinearLayoutManager(this@ClubsFilter, LinearLayout.VERTICAL, false)*/

    }

   /* override fun onStart() {
        super.onStart()
        val service = UniclubApi.create()
        service.getUniversities().enqueue(object : Callback<UniversityResponse> {
            override fun onFailure(call: Call<UniversityResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<UniversityResponse>, response: Response<UniversityResponse>) {
                if (response.isSuccessful) {
                    val x = response.body()!!.pagination.universities
                    val arr = arrayListOf<String>()
                    for (i in 0 until x.size)
                        arr.add(x[i].name)
                    val dataAdapter =
                        ArrayAdapter<String>(
                            this@ClubsFilter,
                            com.kou.uniclub.R.layout.search_autocomplete,
                            com.kou.uniclub.R.id.tvHint,
                            arr
                        )
                    autoComplete.setAdapter(dataAdapter)
                    rvUnivs.adapter = RvUnivsAdapter(x, this@ClubsFilter)


                }
            }

        })

    }*/
}

