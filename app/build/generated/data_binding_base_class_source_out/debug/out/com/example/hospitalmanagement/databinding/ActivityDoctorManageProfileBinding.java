// Generated by view binder compiler. Do not edit!
package com.example.hospitalmanagement.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.hospitalmanagement.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDoctorManageProfileBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final EditText emailField;

  @NonNull
  public final EditText nameField;

  @NonNull
  public final EditText phoneField;

  @NonNull
  public final Button saveButton;

  @NonNull
  public final Toolbar toolbar;

  private ActivityDoctorManageProfileBinding(@NonNull ConstraintLayout rootView,
      @NonNull EditText emailField, @NonNull EditText nameField, @NonNull EditText phoneField,
      @NonNull Button saveButton, @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.emailField = emailField;
    this.nameField = nameField;
    this.phoneField = phoneField;
    this.saveButton = saveButton;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDoctorManageProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDoctorManageProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_doctor_manage_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDoctorManageProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.emailField;
      EditText emailField = ViewBindings.findChildViewById(rootView, id);
      if (emailField == null) {
        break missingId;
      }

      id = R.id.nameField;
      EditText nameField = ViewBindings.findChildViewById(rootView, id);
      if (nameField == null) {
        break missingId;
      }

      id = R.id.phoneField;
      EditText phoneField = ViewBindings.findChildViewById(rootView, id);
      if (phoneField == null) {
        break missingId;
      }

      id = R.id.saveButton;
      Button saveButton = ViewBindings.findChildViewById(rootView, id);
      if (saveButton == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityDoctorManageProfileBinding((ConstraintLayout) rootView, emailField,
          nameField, phoneField, saveButton, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
