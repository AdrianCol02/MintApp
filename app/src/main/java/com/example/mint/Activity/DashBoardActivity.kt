package com.example.mint.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mint.Adapter.CategoryAdapter
import com.example.mint.Adapter.MejoresNegociosAdapter
import com.example.mint.Adapter.SliderAdapter
import com.example.mint.Model.SliderModel
import com.example.mint.ViewModel.MainViewModel
import com.example.mint.databinding.ActivityDashBoardBinding

class DashBoardActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityDashBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDashBoardBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initBanners()
        initCategories()
        initMejoresNegocios()

    }

    private fun initMejoresNegocios() {
        binding.progressBarMejoresNegocios.visibility = View.VISIBLE
        viewModel.mejoresNegocios.observe(this, Observer {
            binding.viewMejoresNegocios.layoutManager = GridLayoutManager(this, 2)
            binding.viewMejoresNegocios.adapter = MejoresNegociosAdapter(it)
            binding.progressBarMejoresNegocios.visibility = View.GONE
        })
        viewModel.loadMejoresNegocios()
    }

    private fun initCategories() {
        binding.progressBarCategorias.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.viewCategorias.layoutManager = LinearLayoutManager(this@DashBoardActivity,
                LinearLayoutManager.HORIZONTAL, false)
            binding.viewCategorias.adapter = CategoryAdapter(it)
            binding.progressBarCategorias.visibility = View.GONE

        })
        viewModel.loadCategory()

    }

    private fun initBanners() {
        binding.progressBarNegocios.visibility= View.VISIBLE
        viewModel.banners.observe(this, Observer {
            banners(it)
            binding.progressBarNegocios.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    private fun banners(images:List<SliderModel>){
        binding.viewPagerSlider.adapter=SliderAdapter(images,binding.viewPagerSlider)
        binding.viewPagerSlider.clipToPadding=false
        binding.viewPagerSlider.clipChildren=false
        binding.viewPagerSlider.offscreenPageLimit=3
        binding.viewPagerSlider.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer=CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPagerSlider.setPageTransformer(compositePageTransformer)
        if (images.size>1){
            binding.dotIndicator.visibility=View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewPagerSlider)
        }
    }
}