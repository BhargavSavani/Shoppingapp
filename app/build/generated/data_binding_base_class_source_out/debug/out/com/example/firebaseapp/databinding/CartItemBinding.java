// Generated by view binder compiler. Do not edit!
package com.example.firebaseapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.firebaseapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CartItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView ivDecreaseQuantity;

  @NonNull
  public final ImageView ivIncreaseQuantity;

  @NonNull
  public final ImageView ivRemoveFromCart;

  @NonNull
  public final TextView priceTextView;

  @NonNull
  public final ImageView productImageView;

  @NonNull
  public final TextView quantityTextView;

  @NonNull
  public final TextView titleTextView;

  private CartItemBinding(@NonNull LinearLayout rootView, @NonNull ImageView ivDecreaseQuantity,
      @NonNull ImageView ivIncreaseQuantity, @NonNull ImageView ivRemoveFromCart,
      @NonNull TextView priceTextView, @NonNull ImageView productImageView,
      @NonNull TextView quantityTextView, @NonNull TextView titleTextView) {
    this.rootView = rootView;
    this.ivDecreaseQuantity = ivDecreaseQuantity;
    this.ivIncreaseQuantity = ivIncreaseQuantity;
    this.ivRemoveFromCart = ivRemoveFromCart;
    this.priceTextView = priceTextView;
    this.productImageView = productImageView;
    this.quantityTextView = quantityTextView;
    this.titleTextView = titleTextView;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static CartItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CartItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.cart_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CartItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ivDecreaseQuantity;
      ImageView ivDecreaseQuantity = ViewBindings.findChildViewById(rootView, id);
      if (ivDecreaseQuantity == null) {
        break missingId;
      }

      id = R.id.ivIncreaseQuantity;
      ImageView ivIncreaseQuantity = ViewBindings.findChildViewById(rootView, id);
      if (ivIncreaseQuantity == null) {
        break missingId;
      }

      id = R.id.ivRemoveFromCart;
      ImageView ivRemoveFromCart = ViewBindings.findChildViewById(rootView, id);
      if (ivRemoveFromCart == null) {
        break missingId;
      }

      id = R.id.priceTextView;
      TextView priceTextView = ViewBindings.findChildViewById(rootView, id);
      if (priceTextView == null) {
        break missingId;
      }

      id = R.id.productImageView;
      ImageView productImageView = ViewBindings.findChildViewById(rootView, id);
      if (productImageView == null) {
        break missingId;
      }

      id = R.id.quantityTextView;
      TextView quantityTextView = ViewBindings.findChildViewById(rootView, id);
      if (quantityTextView == null) {
        break missingId;
      }

      id = R.id.titleTextView;
      TextView titleTextView = ViewBindings.findChildViewById(rootView, id);
      if (titleTextView == null) {
        break missingId;
      }

      return new CartItemBinding((LinearLayout) rootView, ivDecreaseQuantity, ivIncreaseQuantity,
          ivRemoveFromCart, priceTextView, productImageView, quantityTextView, titleTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
