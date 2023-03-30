package com.goormthon.mang9rme.jihun.presentation.ui.main.view

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.goormthon.mang9rme.databinding.MapTestBinding
import com.goormthon.mang9rme.jihun.presentation.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.daum.android.map.MapViewEventListener
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@AndroidEntryPoint
class MapDialogFragment : DialogFragment() {
    private lateinit var binding : MapTestBinding
    private val viewModel : MainViewModel by activityViewModels()

    private val mapView by lazy {
        MapView(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = MapTestBinding.inflate(inflater, container, false)

        initMap()


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val windowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.8).toInt()
        params?.height = (deviceWidth * 0.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }


    private fun initMap() {
        binding.mapView.addView(mapView)

        mapView.setMapViewEventListener(object : MapViewEventListener {
            override fun onLoadMapView() {
                Log.d("----", "onLoadMapView: LOADED")
            }

        })


        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(viewModel.markerLocation.value!!.getLat(), viewModel.markerLocation.value!!.getLng()), true)
        mapView.setZoomLevel(7, true)

        setMarker()
    }

    private fun setMarker() {
        val marker = MapPOIItem()
        marker.itemName = "Marker"
        marker.tag = 0
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(viewModel.markerLocation.value!!.getLat(), viewModel.markerLocation.value!!.getLng())
        marker.markerType = MapPOIItem.MarkerType.YellowPin
        mapView.addPOIItem(marker)
    }

    private fun Pair<Double, Double>.getLat() = this.first
    private fun Pair<Double, Double>.getLng() = this.second
}