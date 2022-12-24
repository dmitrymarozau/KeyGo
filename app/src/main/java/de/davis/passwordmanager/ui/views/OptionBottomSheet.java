package de.davis.passwordmanager.ui.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import de.davis.passwordmanager.R;
import de.davis.passwordmanager.databinding.MoreBottomSheetContentBinding;
import de.davis.passwordmanager.dialog.DeleteDialog;
import de.davis.passwordmanager.manager.ActivityResultManager;
import de.davis.passwordmanager.security.element.SecureElement;
import de.davis.passwordmanager.ui.ViewFragment;

public class OptionBottomSheet extends BottomSheetDialog {

    private MoreBottomSheetContentBinding binding;

    private final SecureElement element;

    public OptionBottomSheet(Context context, SecureElement element) {
        super(context);
        this.element = element;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MoreBottomSheetContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setText(element == null ? getContext().getString(R.string.options) : element.getTitle());

        binding.edit.setOnClickListener(v -> {
            ActivityResultManager.getOrCreateManager(ViewFragment.class, null).launchEdit(element, getContext());
            dismiss();
        });

        if(element == null)
            binding.edit.setVisibility(View.GONE);

        binding.delete.setOnClickListener(v -> {
            new DeleteDialog(getContext()).show(null, element);
            dismiss();
        });

    }
}
