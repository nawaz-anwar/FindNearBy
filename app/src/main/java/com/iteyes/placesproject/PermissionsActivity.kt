package com.iteyes.placesproject

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.iteyes.placesproject.R
import android.support.v4.content.ContextCompat
import android.content.pm.PackageManager
import android.content.Intent
import com.iteyes.placesproject.MapActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionDeniedResponse
import android.content.DialogInterface
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest

class PermissionsActivity : AppCompatActivity() {
    private var btnGrant: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)
        if (ContextCompat.checkSelfPermission(
                this@PermissionsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(Intent(this@PermissionsActivity, MapActivity::class.java))
            finish()
            return
        }
        this.btnGrant.setOnClickListener(View.OnClickListener {
            Dexter.withActivity(this@PermissionsActivity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        startActivity(Intent(this@PermissionsActivity, MapActivity::class.java))
                        finish()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        if (response.isPermanentlyDenied) {
                            val builder = AlertDialog.Builder(this@PermissionsActivity)
                            builder.setTitle("Permission Denied")
                                .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("OK") { dialog, which ->
                                    val intent = Intent()
                                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    intent.data = Uri.fromParts("package", packageName, null)
                                }
                                .show()
                        } else {
                            Toast.makeText(
                                this@PermissionsActivity,
                                "Permission Denied",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .check()
        })
    }
}