package com.kou.uniclub.Extensions

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvUnivsAdapter
import com.kou.uniclub.Model.University.UniversityResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BuilderUnivs: DialogFragment() {
    private var page: String? = null

    companion object {
        val  TAG = "BuilderUnivs"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.activity_clubs_filter, container, false)
        val rvUnivs=v.findViewById<RecyclerView>(com.kou.uniclub.R.id.rvUnivs)

        rvUnivs.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)


        getUnivs(rvUnivs,activity!!)




        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentAlertDialog)
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = 444
        dialog.window!!.setLayout(width, height)
    }






    fun getUnivs(rv: RecyclerView, context: Context) {
        val service = UniclubApi.create()
        service.getUniversities().enqueue(object : Callback<UniversityResponse> {
            override fun onFailure(call: Call<UniversityResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<UniversityResponse>, response: Response<UniversityResponse>) {
                if (response.isSuccessful&&isAdded) {
                    page = response.body()!!.pagination.nextPageUrl
                    val adapter = RvUnivsAdapter(response.body()!!.pagination.universities, context)
                    rv.adapter = adapter
                    //Pagination
                    adapter.setOnBottomReachedListener(object : OnBottomReachedListener {
                        override fun onBottomReached(position: Int) {
                            if (page != null)
                                getMoreItems(adapter, context)
                        }

                    })
                }
            }

        })
    }

    fun getMoreItems(adapter: RvUnivsAdapter, context: Context) {
        val service = UniclubApi.create()
        if (page != null)
            service.paginateUnivs(page!!).enqueue(object : Callback<UniversityResponse> {
                override fun onFailure(call: Call<UniversityResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<UniversityResponse>, response: Response<UniversityResponse>) {

                    if (response.isSuccessful&&isAdded) {
                        if (page != null) {
                            adapter.addData(response.body()!!.pagination.universities)
                            page = response.body()!!.pagination.nextPageUrl
                            if (page == null)
                                Toasty.custom(
                                    context,
                                    "Load more",
                                    com.kou.uniclub.R.drawable.ic_error_outline_white_24dp,
                                    com.kou.uniclub.R.color.black,
                                    Toasty.LENGTH_SHORT,
                                    false,
                                    true
                                ).show()


                        }
                    }
                }

            })
    }
}