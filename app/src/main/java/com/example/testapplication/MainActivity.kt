package com.example.testapplication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.bottom_sheet_rules.*

class MainActivity : AppCompatActivity() {

    private var mapView: MapView? = null
    private lateinit var map: MapboxMap
    private var style: Style? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val rulesAdapter by lazy {
        RuleAdapter(
            listOf(
                Rule("id0", "description0", "status0"),
                Rule("id1", "description1", "status1"),
                Rule("id2", "description2", "status2"),
                Rule("id3", "description3", "status3"),
                Rule("id4", "description4", "status4"),
                Rule("id5", "description5", "status5"),
                Rule("id6", "description6", "status6"),
                Rule("id7", "description7", "status7"),
                Rule("id8", "description8", "status8"),
                Rule("id9", "description9", "status9"),
                Rule("id10", "description10", "status10")
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, MAPBOX_ACCESS_TOKEN)
        setContentView(R.layout.activity_main)

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        mapView = findViewById(R.id.map_view)
        mapView?.apply {
            ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
                bottomSheetBehavior.setExpandedOffset(2 * insets.systemWindowInsetTop)
                insets
            }
            onCreate(savedInstanceState)
            getMapAsync {
                map = it
                map.uiSettings.isRotateGesturesEnabled = false
                map.setStyle(Style.MAPBOX_STREETS) { s ->
                    style = s
                    s.addSource(GeoJsonSource(SOURCE_ID))
                    s.addLayer(createLocationSymbolsLayer())

                    map.locationComponent.apply {
                        activateLocationComponent(
                            LocationComponentActivationOptions.builder(
                                context,
                                s
                            ).build()
                        )
                        isLocationComponentEnabled = true
                        cameraMode = CameraMode.NONE
                        renderMode = RenderMode.NORMAL
                    }

                    showResponse()
                }
            }
        }

        rules_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rulesAdapter
        }
    }

    private fun createLocationSymbolsLayer(): SymbolLayer =
        SymbolLayer(SYMBOLS_LAYER_ID, SOURCE_ID).withProperties(
            PropertyFactory.iconImage(ICON_ID),
            PropertyFactory.textSize(16f),
            PropertyFactory.textField(Expression.get(PROPERTY_NAME)),
            PropertyFactory.iconAnchor(Property.ICON_ANCHOR_BOTTOM),
            PropertyFactory.textAnchor(Property.TEXT_ANCHOR_BOTTOM),
            PropertyFactory.textOffset(arrayOf(0f, -0.7f)),
            PropertyFactory.iconAllowOverlap(true),
            PropertyFactory.iconIgnorePlacement(true),
            PropertyFactory.textIgnorePlacement(true),
            PropertyFactory.textColor(Color.WHITE)
        )

    private fun showResponse() {
        style?.apply {
            getSourceAs<GeoJsonSource>(SOURCE_ID)?.setGeoJson(FEATURES)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 5.0))
        }
    }


    companion object {
        private const val MAPBOX_ACCESS_TOKEN = "MAPBOX_KEY"
        private const val SOURCE_ID = "test.locations"
        private const val SYMBOLS_LAYER_ID = "test.locations.symbols"
        private const val ICON_ID = "marker-15"
        private const val PROPERTY_NAME = "name"
        private const val FEATURES = """
            {"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Point","coordinates":[0.0,0.0]},"properties":{"selected":false,"name":1}},{"type":"Feature","geometry":{"type":"Point","coordinates":[2.0,2.0]},"properties":{"selected":false,"name":2}},{"type":"Feature","geometry":{"type":"Point","coordinates":[-2.0,-2.0]},"properties":{"selected":false,"name":3}}]}
        """
    }
}
