// Generated by view binder compiler. Do not edit!
package com.example.firebaseapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.firebaseapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityProductDetailBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final Button btnAddToCart;

  @NonNull
  public final ImageView ivsimliar1;

  @NonNull
  public final ImageView ivsimliar2;

  @NonNull
  public final ImageView ivsimliar3;

  @NonNull
  public final TextView productDetailDescriptionTextView;

  @NonNull
  public final ImageView productDetailImageView;

  @NonNull
  public final TextView productDetailPriceTextView;

  @NonNull
  public final RatingBar productDetailRatingBar;

  @NonNull
  public final TextView productDetailTitleTextView;

  private ActivityProductDetailBinding(@NonNull ScrollView rootView, @NonNull Button btnAddToCart,
      @NonNull ImageView ivsimliar1, @NonNull ImageView ivsimliar2, @NonNull ImageView ivsimliar3,
      @NonNull TextView productDetailDescriptionTextView, @NonNull ImageView productDetailImageView,
      @NonNull TextView productDetailPriceTextView, @NonNull RatingBar productDetailRatingBar,
      @NonNull TextView productDetailTitleTextView) {
    this.rootView = rootView;
    this.btnAddToCart = btnAddToCart;
    this.ivsimliar1 = ivsimliar1;
    this.ivsimliar2 = ivsimliar2;
    this.ivsimliar3 = ivsimliar3;
    this.productDetailDescriptionTextView = productDetailDescriptionTextView;
    this.productDetailImageView = productDetailImageView;
    this.productDetailPriceTextView = productDetailPriceTextView;
    this.productDetailRatingBar = productDetailRatingBar;
    this.productDetailTitleTextView = productDetailTitleTextView;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityProductDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityProductDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_product_detail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityProductDetailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnAddToCart;
      Button btnAddToCart = ViewBindings.findChildViewById(rootView, id);
      if (btnAddToCart == null) {
        break missingId;
      }

      id = R.id.ivsimliar1;
      ImageView ivsimliar1 = ViewBindings.findChildViewById(rootView, id);
      if (ivsimliar1 == null) {
        break missingId;
      }

      id = R.id.ivsimliar2;
      ImageView ivsimliar2 = ViewBindings.findChildViewById(rootView, id);
      if (ivsimliar2 == null) {
        break missingId;
      }

      id = R.id.ivsimliar3;
      ImageView ivsimliar3 = ViewBindings.findChildViewById(rootView, id);
      if (ivsimliar3 == null) {
        break missingId;
      }

      id = R.id.productDetailDescriptionTextView;
      TextView productDetailDescriptionTextView = ViewBindings.findChildViewById(rootView, id);
      if (productDetailDescriptionTextView == null) {
        break missingId;
      }

      id = R.id.productDetailImageView;
      ImageView productDetailImageView = ViewBindings.findChildViewById(rootView, id);
      if (productDetailImageView == null) {
        break missingId;
      }

      id = R.id.productDetailPriceTextView;
      TextView productDetailPriceTextView = ViewBindings.findChildViewById(rootView, id);
      if (productDetailPriceTextView == null) {
        break missingId;
      }

      id = R.id.productDetailRatingBar;
      RatingBar productDetailRatingBar = ViewBindings.findChildViewById(rootView, id);
      if (productDetailRatingBar == null) {
        break missingId;
      }

      id = R.id.productDetailTitleTextView;
      TextView productDetailTitleTextView = ViewBindings.findChildViewById(rootView, id);
      if (productDetailTitleTextView == null) {
        break missingId;
      }

      return new ActivityProductDetailBinding((ScrollView) rootView, btnAddToCart, ivsimliar1,
          ivsimliar2, ivsimliar3, productDetailDescriptionTextView, productDetailImageView,
          productDetailPriceTextView, productDetailRatingBar, productDetailTitleTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
