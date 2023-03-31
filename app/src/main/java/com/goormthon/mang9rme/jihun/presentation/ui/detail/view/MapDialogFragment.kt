package com.goormthon.mang9rme.jihun.presentation.ui.detail.view

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.goormthon.mang9rme.databinding.MapTestBinding
import com.goormthon.mang9rme.jihun.presentation.ui.detail.viewmodel.DetailViewModel
import com.goormthon.mang9rme.kimbsu.common.customview.LoadingProgressDialog
import com.goormthon.mang9rme.kimbsu.common.util.ConvertUtil
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@AndroidEntryPoint
class MapDialogFragment(private val mapView : MapView) : DialogFragment() {
    private lateinit var binding: MapTestBinding
    private val viewModel: DetailViewModel by activityViewModels()

    private val loadingDialog by lazy {
        LoadingProgressDialog(requireContext())
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
        val windowManager =
            requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        // val deviceHeight = size.y
        params?.width = (deviceWidth - ConvertUtil.dpToPx(requireActivity(), 20))
        params?.height = (deviceWidth - ConvertUtil.dpToPx(requireActivity(), 20))
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }


    private fun initMap() {
        binding.mapView.addView(mapView)
        loadingDialog.show()

        mapView.setMapViewEventListener(object : MapView.MapViewEventListener {
            override fun onMapViewInitialized(p0: MapView?) {
                loadingDialog.dismiss()
            }

            override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
            }

            override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
            }

            override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
            }

            override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
            }

            override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
            }

            override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
            }

            override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
            }

            override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
            }

        })
        mapView.setMapCenterPoint(
            MapPoint.mapPointWithGeoCoord(
//                viewModel.stoneData.value!!.lat.toDouble(),
//                viewModel.stoneData.value!!.lng.toDouble()
                33.458528,126.94225
            ), true
        )
        mapView.setZoomLevel(3, false)
        setMarker()
    }

    private fun setMarker() {
        val marker = MapPOIItem()
        marker.itemName = "Marker"
        marker.tag = 0
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(
//            viewModel.stoneData.value!!.lat.toDouble(),
//            viewModel.stoneData.value!!.lng.toDouble()
            33.458528,126.94225
        )
        marker.markerType = MapPOIItem.MarkerType.YellowPin
        mapView.addPOIItem(marker)
    }

    override fun onDestroy() {
        super.onDestroy()
        (mapView.parent as ViewGroup).removeView(mapView)
    }
}