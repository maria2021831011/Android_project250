// Generated by view binder compiler. Do not edit!
package com.example.hospitalmanagement.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.hospitalmanagement.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView appLogo;

  @NonNull
  public final Button btnLogin;

  @NonNull
  public final EditText etEmail;

  @NonNull
  public final EditText etPassword;

  @NonNull
  public final ImageView ivTogglePassword;

  @NonNull
  public final TextView tvForgotPassword;

  @NonNull
  public final TextView tvRegister;

  @NonNull
  public final TextView tvTitle;

  private ActivityLoginBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView appLogo,
      @NonNull Button btnLogin, @NonNull EditText etEmail, @NonNull EditText etPassword,
      @NonNull ImageView ivTogglePassword, @NonNull TextView tvForgotPassword,
      @NonNull TextView tvRegister, @NonNull TextView tvTitle) {
    this.rootView = rootView;
    this.appLogo = appLogo;
    this.btnLogin = btnLogin;
    this.etEmail = etEmail;
    this.etPassword = etPassword;
    this.ivTogglePassword = ivTogglePassword;
    this.tvForgotPassword = tvForgotPassword;
    this.tvRegister = tvRegister;
    this.tvTitle = tvTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appLogo;
      ImageView appLogo = ViewBindings.findChildViewById(rootView, id);
      if (appLogo == null) {
        break missingId;
      }

      id = R.id.btnLogin;
      Button btnLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnLogin == null) {
        break missingId;
      }

      id = R.id.etEmail;
      EditText etEmail = ViewBindings.findChildViewById(rootView, id);
      if (etEmail == null) {
        break missingId;
      }

      id = R.id.etPassword;
      EditText etPassword = ViewBindings.findChildViewById(rootView, id);
      if (etPassword == null) {
        break missingId;
      }

      id = R.id.ivTogglePassword;
      ImageView ivTogglePassword = ViewBindings.findChildViewById(rootView, id);
      if (ivTogglePassword == null) {
        break missingId;
      }

      id = R.id.tvForgotPassword;
      TextView tvForgotPassword = ViewBindings.findChildViewById(rootView, id);
      if (tvForgotPassword == null) {
        break missingId;
      }

      id = R.id.tvRegister;
      TextView tvRegister = ViewBindings.findChildViewById(rootView, id);
      if (tvRegister == null) {
        break missingId;
      }

      id = R.id.tvTitle;
      TextView tvTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvTitle == null) {
        break missingId;
      }

      return new ActivityLoginBinding((ConstraintLayout) rootView, appLogo, btnLogin, etEmail,
          etPassword, ivTogglePassword, tvForgotPassword, tvRegister, tvTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
