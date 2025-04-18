// Generated by view binder compiler. Do not edit!
package com.example.hospitalmanagement.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.hospitalmanagement.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ReportCardLayoutBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final TextView cardContent;

  @NonNull
  public final TextView cardTitle;

  @NonNull
  public final CardView reportCard;

  private ReportCardLayoutBinding(@NonNull CardView rootView, @NonNull TextView cardContent,
      @NonNull TextView cardTitle, @NonNull CardView reportCard) {
    this.rootView = rootView;
    this.cardContent = cardContent;
    this.cardTitle = cardTitle;
    this.reportCard = reportCard;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ReportCardLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ReportCardLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.report_card_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ReportCardLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cardContent;
      TextView cardContent = ViewBindings.findChildViewById(rootView, id);
      if (cardContent == null) {
        break missingId;
      }

      id = R.id.cardTitle;
      TextView cardTitle = ViewBindings.findChildViewById(rootView, id);
      if (cardTitle == null) {
        break missingId;
      }

      CardView reportCard = (CardView) rootView;

      return new ReportCardLayoutBinding((CardView) rootView, cardContent, cardTitle, reportCard);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
