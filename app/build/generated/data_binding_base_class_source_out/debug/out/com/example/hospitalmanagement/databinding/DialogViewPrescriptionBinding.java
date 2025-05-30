// Generated by view binder compiler. Do not edit!
package com.example.hospitalmanagement.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.hospitalmanagement.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogViewPrescriptionBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView dosageTextView;

  @NonNull
  public final TextView medicineTextView;

  private DialogViewPrescriptionBinding(@NonNull LinearLayout rootView,
      @NonNull TextView dosageTextView, @NonNull TextView medicineTextView) {
    this.rootView = rootView;
    this.dosageTextView = dosageTextView;
    this.medicineTextView = medicineTextView;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogViewPrescriptionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogViewPrescriptionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_view_prescription, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogViewPrescriptionBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.dosageTextView;
      TextView dosageTextView = ViewBindings.findChildViewById(rootView, id);
      if (dosageTextView == null) {
        break missingId;
      }

      id = R.id.medicineTextView;
      TextView medicineTextView = ViewBindings.findChildViewById(rootView, id);
      if (medicineTextView == null) {
        break missingId;
      }

      return new DialogViewPrescriptionBinding((LinearLayout) rootView, dosageTextView,
          medicineTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
