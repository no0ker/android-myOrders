package com.example.rush0714.myapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.text.SimpleDateFormat;
import java.util.List;

import NetUtils.Orders.Order;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;


public class MapActivity extends ActionBarActivity {

    private static SimpleDateFormat SHORT_TIME_FORMAT = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final MapView mMapView = (MapView) findViewById(R.id.map);
        MapController mMapController = mMapView.getMapController();

        mMapController.setPositionAnimationTo(new GeoPoint(51.534684, 46.019465));
        mMapController.setZoomCurrent(13);

        OverlayManager overlayManager = mMapController.getOverlayManager();

        Overlay overlay = new Overlay(mMapController);

        for (Order iOrder : DataStorage.getOrders()) {
            if(Order.closedColor.equals(iOrder.getColor())){
                continue;
            }
            Resources res = getResources();
            NetUtils.Maps.GeoPoint geoPoint = iOrder.getGeoPoint();
            OverlayItem overlayItem = new OverlayItem(
                new GeoPoint(geoPoint.getLng(), geoPoint.getLat()),
                res.getDrawable(R.drawable.ic_gps_not_fixed_black_48dp)
            );
            BalloonItem balloonItem = new BalloonItem(
                getApplicationContext(),
                new GeoPoint(geoPoint.getLng(), geoPoint.getLat())
            );
            balloonItem.setText(SHORT_TIME_FORMAT.format(iOrder.getTime()) + "  " + iOrder.getAddress());
            overlayItem.setBalloonItem(balloonItem);
            overlay.addOverlayItem(overlayItem);
        }
        overlayManager.addOverlay(overlay);

        List<OverlayItem> list = overlay.getOverlayItems();
        double maxLat, minLat, maxLon, minLon;
        maxLat = maxLon = Double.MIN_VALUE;
        minLat = minLon = Double.MAX_VALUE;
        for (int i = 0; i < overlay.getOverlayItems().size(); i++){
            GeoPoint geoPoint = list.get(i).getGeoPoint();
            double lat = geoPoint.getLat();
            double lon = geoPoint.getLon();

            maxLat = Math.max(lat, maxLat);
            minLat = Math.min(lat, minLat);
            maxLon = Math.max(lon, maxLon);
            minLon = Math.min(lon, minLon);
        }
        mMapController.setZoomToSpan(maxLat - minLat, maxLon - minLon);
        mMapController.setPositionAnimationTo(new GeoPoint((maxLat + minLat) / 2, (maxLon + minLon) / 2));
    }

}
