// Generated by view binder compiler. Do not edit!
package com.example.hospitalmanagement.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.hospitalmanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAdminDashboard2Binding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final BottomNavigationView bottomNavigation;

  @NonNull
  public final Button logoutBtn;

  @NonNull
  public final Button makeReportBtn;

  @NonNull
  public final Button monthlyReportBtn;

  @NonNull
  public final ImageView profileImage;

  @NonNull
  public final LinearLayout reportsContainer;

  @NonNull
  public final ScrollView reportsScrollView;

  @NonNull
  public final TextView todayApprovedCount;

  @NonNull
  public final TextView todayRejectedCount;

  @NonNull
  public final TextView todayTotalCount;

  @NonNull
  public final Button toggleReportBtn;

  @NonNull
  public final TextView welcomeText;

  private ActivityAdminDashboard2Binding(@NonNull LinearLayout rootView,
      @NonNull BottomNavigationView bottomNavigation, @NonNull Button logoutBtn,
      @NonNull Button makeReportBtn, @NonNull Button monthlyReportBtn,
      @NonNull ImageView profileImage, @NonNull LinearLayout reportsContainer,
      @NonNull ScrollView reportsScrollView, @NonNull TextView todayApprovedCount,
      @NonNull TextView todayRejectedCount, @NonNull TextView todayTotalCount,
      @NonNull Button toggleReportBtn, @NonNull TextView welcomeText) {
    this.rootView = rootView;
    this.bottomNavigation = bottomNavigation;
    this.logoutBtn = logoutBtn;
    this.makeReportBtn = makeReportBtn;
    this.monthlyReportBtn = monthlyReportBtn;
    this.profileImage = profileImage;
    this.reportsContainer = reportsContainer;
    this.reportsScrollView = reportsScrollView;
    this.todayApprovedCount = todayApprovedCount;
    this.todayRejectedCount = todayRejectedCount;
    this.todayTotalCount = todayTotalCount;
    this.toggleReportBtn = toggleReportBtn;
    this.welcomeText = welcomeText;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAdminDashboard2Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAdminDashboard2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_admin_dashboard2, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAdminDashboard2Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bottomNavigation;
      BottomNavigationView bottomNavigation = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavigation == null) {
        break missingId;
      }

      id = R.id.logoutBtn;
      Button logoutBtn = ViewBindings.findChildViewById(rootView, id);
      if (logoutBtn == null) {
        break missingId;
      }

      id = R.id.makeReportBtn;
      Button makeReportBtn = ViewBindings.findChildViewById(rootView, id);
      if (makeReportBtn == null) {
        break missingId;
      }

      id = R.id.monthlyReportBtn;
      Button monthlyReportBtn = ViewBindings.findChildViewById(rootView, id);
      if (monthlyReportBtn == null) {
        break missingId;
      }

      id = R.id.profileImage;
      ImageView profileImage = ViewBindings.findChildViewById(rootView, id);
      if (profileImage == null) {
        break missingId;
      }

      id = R.id.reportsContainer;
      LinearLayout reportsContainer = ViewBindings.findChildViewById(rootView, id);
      if (reportsContainer == null) {
        break missingId;
      }

      id = R.id.reportsScrollView;
      ScrollView reportsScrollView = ViewBindings.findChildViewById(rootView, id);
      if (reportsScrollView == null) {
        break missingId;
      }

      id = R.id.todayApprovedCount;
      TextView todayApprovedCount = ViewBindings.findChildViewById(rootView, id);
      if (todayApprovedCount == null) {
        break missingId;
      }

      id = R.id.todayRejectedCount;
      TextView todayRejectedCount = ViewBindings.findChildViewById(rootView, id);
      if (todayRejectedCount == null) {
        break missingId;
      }

      id = R.id.todayTotalCount;
      TextView todayTotalCount = ViewBindings.findChildViewById(rootView, id);
      if (todayTotalCount == null) {
        break missingId;
      }

      id = R.id.toggleReportBtn;
      Button toggleReportBtn = ViewBindings.findChildViewById(rootView, id);
      if (toggleReportBtn == null) {
        break missingId;
      }

      id = R.id.welcomeText;
      TextView welcomeText = ViewBindings.findChildViewById(rootView, id);
      if (welcomeText == null) {
        break missingId;
      }

      return new ActivityAdminDashboard2Binding((LinearLayout) rootView, bottomNavigation,
          logoutBtn, makeReportBtn, monthlyReportBtn, profileImage, reportsContainer,
          reportsScrollView, todayApprovedCount, todayRejectedCount, todayTotalCount,
          toggleReportBtn, welcomeText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
