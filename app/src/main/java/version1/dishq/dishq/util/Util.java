package version1.dishq.dishq.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import version1.dishq.dishq.modals.AllergyModal;
import version1.dishq.dishq.modals.FavCuisinesModal;
import version1.dishq.dishq.modals.FoodChoicesModal;
import version1.dishq.dishq.modals.HomeCuisinesModal;
import version1.dishq.dishq.modals.lists.DontEatSelect;
import version1.dishq.dishq.modals.lists.FavCuisineSelect;
import version1.dishq.dishq.modals.lists.HomeCuisineSelect;
import version1.dishq.dishq.server.Response.DeliveryTabResponse;
import version1.dishq.dishq.server.Response.DineoutTabResponse;
import version1.dishq.dishq.server.Response.DishDataInfo;

/**
 * Created by dishq on 13-12-2016.
 * Package name version1.dishq.dishq.
 */

public class Util {

    static String ACCESS_TOKEN = "";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    //Array lists for onBoarding food preferences
    public static ArrayList<AllergyModal> allergyModals = new ArrayList<>();
    public static ArrayList<FoodChoicesModal> foodChoicesModals = new ArrayList<>();
    public static ArrayList<FavCuisinesModal> favCuisinesModals = new ArrayList<>();
    public static ArrayList<HomeCuisinesModal> homeCuisinesModals = new ArrayList<>();
    public static ArrayList<HomeCuisineSelect> homeCuisineSelects = new ArrayList<>();
    public static ArrayList<FavCuisineSelect> favCuisineSelects = new ArrayList<>();
    public static ArrayList<DontEatSelect> dontEatSelects = new ArrayList<>();

    public static ArrayList<DishDataInfo> dishDataModals = new ArrayList<DishDataInfo>();
    public static ArrayList<DineoutTabResponse.DineoutRestInfo> dineoutRestInfos = new ArrayList<>();
    public static ArrayList<DeliveryTabResponse.DeliveryRestInfo> deliveryRestInfos = new ArrayList<>();

    private static int foodChoiceSelected;
    public static Boolean homeCuisineSelected = false;
    public static int favCuisineCount = 0;
    private static int favCuisineTotal = 0;
    private static String latitude = "";
    private static String longitude = "";
    private static int genericDishIdTab = 0;
    private static String defaultTab = "";
    private static String recipeUrl = "";

    public static String getRecipeUrl() {
        return recipeUrl;
    }

    public static void setRecipeUrl(String recipeUrl) {
        Util.recipeUrl = recipeUrl;
    }

    public static String getDefaultTab() {
        return defaultTab;
    }

    public static void setDefaultTab(String defaultTab) {
        Util.defaultTab = defaultTab;
    }

    public static int getGenericDishIdTab() {
        return Util.genericDishIdTab;
    }

    public static void setGenericDishIdTab(int genericDishIdTab) {
        Util.genericDishIdTab = genericDishIdTab;
    }

    @SuppressLint("StaticFieldLeak")
    private static Activity currentAct;

    public static String getLatitude() {
        return Util.latitude;
    }

    public static void setLatitude(String latitude) {
        Util.latitude = latitude;
    }

    public static String getLongitude() {
        return Util.longitude;
    }

    public static void setLongitude(String longitude) {
        Util.longitude = longitude;
    }

    public static Typeface opensanslight = Typeface.createFromAsset(DishqApplication.getContext().getAssets(),
            "opensanslight.ttf"),
            opensansregular = Typeface.createFromAsset(DishqApplication.getContext().getAssets(),
                    "opensansregular.ttf"),
            opensanssemibold = Typeface.createFromAsset(DishqApplication.getContext().getAssets(),
                    "opensanssemibold.ttf");

    public static int getFavCuisinetotal() {
        return Util.favCuisineTotal;
    }

    public static void setFavCuisinetotal(int favCuisineTotal) {
        Util.favCuisineTotal = favCuisineTotal;
    }

    public static int getFoodChoiceSelected() {
        return Util.foodChoiceSelected;
    }

    public static void setFoodChoiceSelected(int foodChoiceSelected) {
        Util.foodChoiceSelected = foodChoiceSelected;
    }

    public static boolean checkAndShowNetworkPopup(final Activity activity) {
        currentAct = activity;
        if (!isOnline(false)) {
            AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("No Internet Detected")
                    .setMessage("Please try again when you're online. ")
                    .setCancelable(false)
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory( Intent.CATEGORY_HOME );
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(homeIntent);
                        }
                    })
                    .create();
            dialog.show();

            TextView message = (TextView) dialog.findViewById(android.R.id.message);
            assert message != null;

            return true;
        }
        return false;
    }

    private static boolean isOnline(boolean showToast) {
        ConnectivityManager conMgr = (ConnectivityManager) DishqApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            if (showToast)
                Toast.makeText(DishqApplication.getContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static void showAlert(String title, String message, final Activity activity) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.GET_ACCOUNTS},
                                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    }
                });


        android.app.AlertDialog alert = builder.create();
        alert.show();
        TextView message1 = (TextView) alert.findViewById(android.R.id.message);
        assert message != null;
        message1.setLineSpacing(0, 1.5f);

    }

}
